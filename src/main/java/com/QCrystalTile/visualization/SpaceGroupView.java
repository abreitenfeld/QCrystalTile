package com.QCrystalTile.visualization;

import com.QCrystalTile.internationalshortsymbol.ID;
import com.QCrystalTile.utilities.MeshHelper;
import com.QCrystalTile.utilities.QHullException;
import com.QCrystalTile.common.UserPreferences;
import com.QCrystalTile.interfaces.*;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.chart.factories.IChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Rectangle;
import org.jzy3d.picking.IObjectPickedListener;
import org.jzy3d.picking.PickingSupport;
import org.jzy3d.plot3d.primitives.AbstractDrawable;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.pickable.PickablePolygon;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.ordering.AbstractOrderingStrategy;
import org.jzy3d.plot3d.text.DrawableTextWrapper;
import org.jzy3d.plot3d.text.align.Halign;
import org.jzy3d.plot3d.text.align.Valign;
import org.jzy3d.plot3d.text.drawable.DrawableTextBitmap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutionException;

public class SpaceGroupView extends JFrame implements View, IObjectPickedListener {

    // interna
    protected final Controller<ID> _controller;
    protected final Chart _chart;
    protected final View[] _subViewControls;
    protected  final SpaceGroupViewStatus _status;
    protected final SpaceGroupViewChartController _chartController;
    protected static final ResourceBundle bundle = ResourceBundle.getBundle("Messages");

    protected Coord3d _globalCenter;
    protected final Point _originPoint;
    protected volatile boolean _showSpacing = false;
    protected volatile float _currentSpacing = Min_Spacing_Factor;
    protected float _currentMaxSpacing = 3f;
    protected final Map<Mesh, MeshInformation> _meshes = new HashMap<Mesh, MeshInformation>();
    protected boolean _workerRunning = false;
    protected volatile  CalculationWorker _currentWorker;
    // color providers
    protected ColorProvider _currentColorProvider;
    protected final HashMap<Controller.ColorScheme, ColorProvider> _colorProviders = new HashMap<Controller.ColorScheme, ColorProvider>();

    // internal structure to store meta data of mesh
    protected class MeshInformation {

        public final Mesh Mesh;
        public Coord3d Centroid;
        public boolean Visible;
        public final DrawableTextWrapper Label;
        public final List<Polygon> Polygons;
        public final List<Point> Vertices;

        public MeshInformation(Mesh mesh, List<Polygon> polygons, List<Point> vertices) {
            this(mesh, polygons, vertices, null);
        }

        public MeshInformation(Mesh mesh, List<Polygon> polygons, List<Point> vertices, DrawableTextWrapper label) {
            this(mesh, true, polygons, vertices, label);
        }

        public MeshInformation(Mesh mesh, boolean visible, List<Polygon> polygons, List<Point> vertices, DrawableTextWrapper label) {
            this.Mesh = mesh;
            this.Centroid = MeshHelper.convertVector3dTojzyCoord3d(mesh.getCentroid());
            this.Visible = visible;
            this.Label = label;
            this.Polygons = polygons;
            this.Vertices = vertices;
            if (this.Label != null) {
                this.Label.setHalign(Halign.CENTER);
                this.Label.setValign(Valign.TOP);
            }
        }

    }

    public static final String Label_Format = "[ V: %s, F: %s ]";
	public static final float Origin_Point_Size = 15f;	
	public static final float Min_Spacing_Factor = 1f;
	public static final float Wireframe_Width = 1.5f;
	public static final float Vertex_Size = 5f;
	public static final Color Vertex_Color = new Color(255,100,100);
	public static final Color Wireframe_Color = Color.WHITE;
	public static final Color Faces_Color = new Color(135, 206, 235, 170);
	public static final Color Foreground_Color = Color.WHITE;
    public static final Color Label_Color = Color.WHITE;
	public static final Color Viewport_Background = new Color(105, 105, 105);
    public static final Color Grid_Color = new Color(192, 192, 192);
	public static final Rectangle Default_Size = new Rectangle(1280, 1000);
    public static final Dimension Min_Size = new Dimension(800, 600);
    public static final String Frame_Title = "QCrystalTile";
    private final static  String ID_RegExpPattern = "\\((.+?)\\)";
    private final static String ID_ReplacePattern = "<sub>$1</sub>";

    protected class CalculationWorker extends  SwingWorker<List<Mesh>, Object> {

        @Override
        protected List<Mesh> doInBackground() throws Exception {
            return _controller.calculateMesh();
        }

        @Override
        protected void done() {
            if (_currentWorker == this) {
                _status.enableLoadingIndicator(false);
                // free worker
                _workerRunning = false;
                _currentWorker = null;
                try {
                    updateView(this.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    if (e.getCause() instanceof QHullException) {
                        if (handleQHullNotFound()) {
                            invalidateView();
                        }
                    }
                }
            }
        }
    }

	/**
	 * Constructor of view.
	 * @param controller
	 */
	SpaceGroupView(Controller<ID> controller) {
		super();

		this._controller = controller;
        this.initializeLookAndFeel();

        this.setTitle(Frame_Title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setForeground(org.jzy3d.colors.ColorAWT.toAWT(Foreground_Color));
        this.setBackground(java.awt.Color.gray);
		this.getContentPane().setLayout(new BorderLayout());
        this.setMinimumSize(Min_Size);

		this._showSpacing = _controller.getViewOption(Controller.ViewOptions.ShowSpacing);
		this._currentSpacing = this._showSpacing ? _currentMaxSpacing : Min_Spacing_Factor;
        // window listener
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });

        final Quality quality = Quality.Nicest;
		this._chart = AWTChartComponentFactory.chart(quality, IChartComponentFactory.Toolkit.newt);
		this._chart.getView().setBackgroundColor(Viewport_Background);
		this._chart.getView().setSquared(true);
        this._chart.getAxeLayout().setMainColor(Grid_Color);

        // set ordering strategy for view
        final AbstractOrderingStrategy strategy = new TextFirstOrderingStrategy(this._chart.getView());
        this._chart.getScene().getGraph().setStrategy(strategy);
        // setup key and mouse controller
        this._chartController = new SpaceGroupViewChartController(this._chart);
        this._chartController.getPickingSupport().addObjectPickedListener(this);
        this.createControllerMenuItems();

		// create moveable point
		this._originPoint = new Point(new Coord3d(), Color.BLUE, Origin_Point_Size);

        // setup color providers
        this._colorProviders.put(Controller.ColorScheme.Monochromatic, new MonochromaticColorProvider(Faces_Color));
        this._colorProviders.put(Controller.ColorScheme.ChromaticCells, new ChromaticCellsColorProvider());
        this._colorProviders.put(Controller.ColorScheme.ChromaticFaces, new ChromaticFacesColorProvider());
        this._currentColorProvider = this._colorProviders.get(_controller.getColorScheme());

		// add components
        final SpaceGroupSelectionPanel selectionPanel = new SpaceGroupSelectionPanel(this._controller);
		this.getContentPane().add(selectionPanel, BorderLayout.PAGE_START);

        final SpaceGroupViewSettingsPanel viewSettingsPanel = new SpaceGroupViewSettingsPanel(this._controller);
        this.getContentPane().add(viewSettingsPanel, BorderLayout.LINE_END);

        this.getContentPane().add((Component)_chart.getCanvas(), BorderLayout.CENTER);

        this._status = new SpaceGroupViewStatus(controller);
        this.getContentPane().add(this._status, BorderLayout.PAGE_END);

        this.setPreferredSize(new Dimension(Default_Size.width, Default_Size.height));
        this.pack();

        this._subViewControls = new View[] {selectionPanel, viewSettingsPanel, this._status};

		// timer for tweening the current spacing value
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			final float MinScale = 0.2f;
            final float StepWith = 0.3f;

			@Override
			public void run() {
				if (_showSpacing) {
                    if (_currentSpacing < _currentMaxSpacing) {
                        // increase spacing if spacing is turned on
                        float spacing = Math.max((float)(Math.log10(_currentSpacing) / Math.log10(_currentMaxSpacing)), MinScale) * StepWith;
                        setSpacing(Math.min(_currentSpacing + spacing, _currentMaxSpacing));
                    }
                    else if (_currentSpacing > _currentMaxSpacing) {
                        // decrease  the spacing if spacing is turned off
                        float spacing = Math.max((float)(Math.log10(_currentMaxSpacing) / Math.log10(_currentSpacing)), MinScale) * StepWith;
                        setSpacing(Math.max(_currentSpacing - spacing, _currentMaxSpacing));
                    }
				}
				else if (!_showSpacing && _currentSpacing > Min_Spacing_Factor) {
					// decrease  the spacing if spacing is turned off
					float spacing = Math.max((float)(Math.log10(_currentSpacing) / Math.log10(_currentMaxSpacing)), MinScale) * StepWith;
					setSpacing(Math.max(_currentSpacing - spacing, Min_Spacing_Factor));
				}
			}
		}, 0, 30);
	}

    private void initializeLookAndFeel() {
        String os = System.getProperty("os.name");
        try {
            if (os.toLowerCase().contains("windows")) {
                //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            }
            else {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        }
        catch (Exception e) { }
    }

    /**
     * Guides the user to solve missing qhull binaries exception. Returns true if the problem should be solved.
     * @return
     */
    private boolean handleQHullNotFound() {
        final UserPreferences prefs = new UserPreferences();
        final String os = System.getProperty("os.name");
        if (os.toLowerCase().contains("windows")) {
            // inform user
            if (JOptionPane.showConfirmDialog(null,
                    bundle.getString("qhullNotFound") + "\n" + bundle.getString("qhullLocateBinaries"), bundle.getString("qhullNotFound"),
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                // open dialog to specify root folder
                final JFileChooser dir = new JFileChooser();
                dir.setCurrentDirectory(new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()));
                dir.setDialogTitle(bundle.getString("qhullSpecifyRootFolder"));
                dir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (dir.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    // store path in user prefs
                    prefs.setQHullRootPath(dir.getSelectedFile().getPath() + File.separator);
                    return true; // problem solved
                }
            }
        }
        else {
            JOptionPane.showMessageDialog(null, bundle.getString("qhullNotFound"),
                bundle.getString("qhullNotFound") + " " + bundle.getString("qhullInstall"), JOptionPane.WARNING_MESSAGE);
        }
        return false; // problem not solved
    }

    private void createControllerMenuItems() {
        final JPopupMenu mnu = this._chartController.getPopupMenu();

        final JMenuItem miShowAll = new JMenuItem(bundle.getString("showAll"));
        miShowAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Iterator<Mesh> iter = _meshes.keySet().iterator();
                while(iter.hasNext()) {
                    Mesh m = iter.next();
                    MeshInformation info = _meshes.get(m);
                    if (!info.Visible) {
                        setMeshDisplayed(m, true);
                    }
                }
            }
        });

        final JMenuItem miToggleBox = new JMenuItem(bundle.getString("toogleBox"));
        miToggleBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final boolean showBox = !_controller.getViewOption(Controller.ViewOptions.ShowAxeBox);
                _controller.setViewOption(Controller.ViewOptions.ShowAxeBox, showBox);
            }
        });

        // crate face tinting menu items
        final JMenu mnuFaceTinting = new JMenu(bundle.getString("colors"));
        final JMenuItem miMonochromaticColors = new JMenuItem(bundle.getString("monochromaticColors"));
        miMonochromaticColors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _controller.setColorScheme(Controller.ColorScheme.Monochromatic);
            }
        });

        final JMenuItem miChromaticCellsColors = new JMenuItem(bundle.getString("chromaticCellColors"));
        miChromaticCellsColors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _controller.setColorScheme(Controller.ColorScheme.ChromaticCells);
            }
        });
        final JMenuItem miChromaticFacesColors = new JMenuItem(bundle.getString("chromaticFaceColors"));
        miChromaticFacesColors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _controller.setColorScheme(Controller.ColorScheme.ChromaticFaces);
            }
        });
        mnuFaceTinting.add(miMonochromaticColors);
        mnuFaceTinting.add(miChromaticCellsColors);
        mnuFaceTinting.add(miChromaticFacesColors);

        // crate spacing menu items
        final JMenu mnuCellSpacing = new JMenu(bundle.getString("cellSpacing"));
        for (int i = 1; i <= 8; i++) {
            final float spacingValue = 1f + (float)i *0.5f;
            final JMenuItem miSpacing = new JMenuItem(spacingValue + "x");
            miSpacing.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    _currentMaxSpacing = spacingValue;
                    if (!_controller.getViewOption(Controller.ViewOptions.ShowSpacing)) {
                        _controller.setViewOption(Controller.ViewOptions.ShowSpacing, true);
                    }
                }
            });
            mnuCellSpacing.add(miSpacing);
        }
        mnu.add(mnuFaceTinting);
        mnu.add(mnuCellSpacing);
        mnu.addSeparator();
        mnu.add(miToggleBox);
        mnu.add(miShowAll);
    }

    protected ColorProvider getColorProvider() {
        return this._currentColorProvider;
    }

    protected void setColorProvider(ColorProvider colorProvider) {
        if (this._currentColorProvider != colorProvider) {
            this._currentColorProvider = colorProvider;
            this._currentColorProvider.reset();
        }
    }

    private static String formatSpaceGroupID(ID id) {
        return id.stringRepr().replaceAll(ID_RegExpPattern, ID_ReplacePattern);
    }

	/**
	 * Clears the entire scene.
	 */
	protected void clearScene() {
        this._chart.getScene().getGraph().getAll().clear();
	    this._meshes.clear();
        this._chartController.getPickingSupport().clear();
	}
	
	/**
	 * Sets the current spacing between polygons.
	 * @param spacing
	 */
	private synchronized void setSpacing(float spacing) {
		this._currentSpacing = spacing;
		this.calculateMeshPosition();
	}
	
	/**
	 * Calculates the position of the meshes according current spacing value.
	 */
	private void calculateMeshPosition() {
        Iterator<Mesh> iter = this._meshes.keySet().iterator();
        while(iter.hasNext()) {
			Mesh m = iter.next();
            MeshInformation info = this._meshes.get(m);
            Coord3d centroid = info.Centroid;
            List<Polygon> polygons = info.Polygons;
            List<Point> vertices = info.Vertices;
            Coord3d originVertex =  centroid.sub(this._globalCenter).mul(this._currentSpacing).add(this._globalCenter);

		    // update position of polygons
            for (int c = 0; c < m.getFaces().size(); c++) {
                // update position of each vertex
                for(int i = 0; i < m.getFaces().get(c).getVertices().size(); i++) {
                    Vector3D vertex = m.getFaces().get(c).getVertices().get(i);
                    Point point = MeshHelper.convertVector3dTojzyPoint(vertex);
                    // update vertex position from polygon
                    polygons.get(c).get(i).xyz = point.xyz.sub(centroid).add(originVertex);
                    vertices.get(i).xyz = point.xyz;
                }
            }
            // update position of vertex points
            for(int i = 0; i < m.getVertices().size(); i++) {
                Vector3D vertex = m.getVertices().get(i);
                Point point = MeshHelper.convertVector3dTojzyPoint(vertex);
                // update vertex position from polygon
                vertices.get(i).xyz = point.xyz.sub(centroid).add(originVertex);
            }
            if (info.Label != null) {
                info.Label.setPosition(originVertex);
            }
		}
	}
	
	/**
	 * Calculates the centroid for every mesh inside the scene.
	 */
	private void calculateCentroids() {
        this._globalCenter = Coord3d.ORIGIN;

        Iterator<Mesh> iter = this._meshes.keySet().iterator();
        while(iter.hasNext()) {
            Mesh m = iter.next();
            MeshInformation info = this._meshes.get(m);
            this._globalCenter  = this._globalCenter .add(info.Centroid);
        }
        // calculate center of box
        this._globalCenter = this._globalCenter .div(this._meshes.size());
	}
	
	@Override
	public void invalidateView() {
        if (!this._workerRunning) {
            this._status.enableLoadingIndicator(true);
            this._workerRunning = true;
            this._currentWorker = new CalculationWorker();
            this._currentWorker.execute();
        }
	}

    private synchronized void updateView(List<Mesh> meshes) {
        final List<AbstractDrawable> drawables = new LinkedList<AbstractDrawable>();
        final boolean showVertices =  this._controller.getVisualization() == Controller.Visualization.ScatterPlot
                || this._controller.getViewOption(Controller.ViewOptions.ShowVertices);
        final boolean showFaces = this._controller.getViewOption(Controller.ViewOptions.ShowFaces);
        final boolean showWireframe = this._controller.getViewOption(Controller.ViewOptions.ShowWireframe);
        final boolean showLabel = this._controller.getVisualization() != Controller.Visualization.ScatterPlot
                && this._controller.getViewOption(Controller.ViewOptions.ShowLabeledMeshes);

        this.clearScene();
        this.invalidateViewOptions();

        for (Mesh m : meshes) {
            List<Polygon> polygons = new LinkedList<Polygon>();
            List<Point> vertices = new LinkedList<Point>();

            // create polygons
            for (com.QCrystalTile.interfaces.Polygon poly : m.getFaces()) {
                PickablePolygon nPoly = MeshHelper.convertPolygonToPickablePolygon(poly);
                nPoly.setWireframeColor(Wireframe_Color);
                nPoly.setWireframeWidth(Wireframe_Width);
                nPoly.setWireframeDisplayed(showWireframe);
                nPoly.setFaceDisplayed(showFaces);
                nPoly.setColor(this.getColorProvider().getColor(m, nPoly));
                polygons.add(nPoly);
                drawables.add(nPoly);
                this._chartController.getPickingSupport().registerPickableObject(nPoly, m);
            }

            // create vertices
            for (com.QCrystalTile.interfaces.Vector3D vertex : m.getVertices()) {
                Point point = new Point(MeshHelper.convertVector3dTojzyCoord3d(vertex), Vertex_Color, Vertex_Size);
                point.setDisplayed(showVertices);
                drawables.add(point);
                vertices.add(point);
            }

            // create label
            String labelString = String.format(Label_Format, m.getVertices().size(), m.getFaces().size());
            DrawableTextBitmap label = new DrawableTextBitmap(labelString, Coord3d.ORIGIN, Label_Color);
            label.setDisplayed(showLabel);
            drawables.add(label);

            this._meshes.put(m, new MeshInformation(m, polygons, vertices, label));
        }

        // show origin point drawable in scatter plot only
        if (this._controller.getVisualization() == Controller.Visualization.ScatterPlot) {
            this._chart.addDrawable(this._originPoint);
            // update the origin point
            this._originPoint.xyz = MeshHelper.convertVector3dTojzyCoord3d(this._controller.getModel().getPoint());
        }

        this.calculateCentroids();
        this.calculateMeshPosition();

        this._chart.getScene().getGraph().add(drawables, false);
        this._chart.getView().updateBoundsForceUpdate(true);

        // find unit cell
        Mesh unitCell = null;
        double minDistance = Integer.MAX_VALUE;
        final Vector3D globalCentroid = MeshHelper.convertCoord3DToVector3D(this._globalCenter);
        for (Mesh m : meshes) {
            double distance = MeshHelper.magnitude(m.getCentroid(), globalCentroid);
            if (distance < minDistance) {
                unitCell = m;
                minDistance = distance;
            }
        }
        // update status panel
        int verticesCount = unitCell != null ? unitCell.getVertices().size() : 0;
        int neighbourCount = unitCell != null ? unitCell.getFaces().size() : 0;
        this._status.setStatusCaption(String.format(bundle.getString("statusFormat")
                , formatSpaceGroupID(this._controller.getSpaceGroupID())
                , meshes.size(), verticesCount, neighbourCount));

        // invalidate view of sub controls
        for(View view : this._subViewControls) {
            view.invalidateView();
        }
    }

	@Override
	public void invalidateViewOptions() {
		final boolean showVertices = this._controller.getVisualization() == Controller.Visualization.ScatterPlot
                || this._controller.getViewOption(Controller.ViewOptions.ShowVertices);
		final boolean showFaces = this._controller.getViewOption(Controller.ViewOptions.ShowFaces);
		final boolean showWireframe = this._controller.getViewOption(Controller.ViewOptions.ShowWireframe);
        final boolean showLabel = this._controller.getVisualization() != Controller.Visualization.ScatterPlot
                && this._controller.getViewOption(Controller.ViewOptions.ShowLabeledMeshes);

        // set color provider
        if (this._colorProviders.containsKey(this._controller.getColorScheme())) {
            this.setColorProvider(this._colorProviders.get(this._controller.getColorScheme()));
        }

        Iterator<Mesh> iter = this._meshes.keySet().iterator();
        while(iter.hasNext()) {
            Mesh m = iter.next();
            MeshInformation info = this._meshes.get(m);

            if (info.Label != null) {
                info.Label.setDisplayed(showLabel && info.Visible);
            }
            // update polygon visibility
            for(Polygon poly : info.Polygons) {
                poly.setWireframeDisplayed(showWireframe && info.Visible);
                poly.setFaceDisplayed(showFaces && info.Visible);
                poly.setColor(this.getColorProvider().getColor(m, poly));
            }

            // update vertices visibility
            for(Point vertex : info.Vertices) {
                vertex.setDisplayed(showVertices && info.Visible);
            }
        }

		this._showSpacing = this._controller.getViewOption(Controller.ViewOptions.ShowSpacing);
		this.calculateMeshPosition();
        this._chart.getView().setAxeBoxDisplayed(this._controller.getViewOption(Controller.ViewOptions.ShowAxeBox));

        // invalidate view options of sub controls
        for(View view : this._subViewControls) {
		    view.invalidateViewOptions();
        }
	}

    private void setMeshDisplayed(Mesh mesh, boolean visible) {
        final boolean showVertices = this._controller.getViewOption(Controller.ViewOptions.ShowVertices);
        final boolean showFaces = this._controller.getViewOption(Controller.ViewOptions.ShowFaces);
        final boolean showWireframe = this._controller.getViewOption(Controller.ViewOptions.ShowWireframe);
        final boolean showLabel = this._controller.getViewOption(Controller.ViewOptions.ShowLabeledMeshes);

        final MeshInformation info = this._meshes.get(mesh);
        info.Visible = visible;
        info.Label.setDisplayed(visible && showLabel);

        for(Polygon poly : info.Polygons) {
            poly.setDisplayed(visible);
            poly.setWireframeDisplayed(visible && showWireframe);
            poly.setFaceDisplayed(visible && showFaces);
        }

        for(Point point : info.Vertices) {
            point.setDisplayed(visible && showVertices);
        }
    }

    @Override
    public void objectPicked(List<? extends Object> objects, PickingSupport pickingSupport) {
        double minDistance = Double.MAX_VALUE;
        Mesh selectedMesh = null;

        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) instanceof Mesh) {
                Mesh mesh = (Mesh)objects.get(i);
                MeshInformation info = this._meshes.get(mesh);
                if (info.Visible) {
                    Coord3d centroid = info.Centroid;
                    double distance = this._chart.getView().getCamera().getDistance(centroid);
                    if (distance < minDistance) {
                        minDistance = distance;
                        selectedMesh = mesh;
                    }
                }
            }
        }

        // make selected mesh invisible
        if (selectedMesh != null) {
            this.setMeshDisplayed(selectedMesh, false);
        }
    }
}
