package com.Softwareprojekt.visualization;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

import com.Softwareprojekt.Utilities.ConvertHelper;

import com.Softwareprojekt.interfaces.*;
import com.Softwareprojekt.interfaces.View;
import org.jzy3d.bridge.awt.FrameAWT;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.chart.factories.IChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Rectangle;
import org.jzy3d.picking.IObjectPickedListener;
import org.jzy3d.picking.PickingSupport;
import org.jzy3d.plot3d.primitives.*;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.pickable.PickablePolygon;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.ordering.AbstractOrderingStrategy;
import org.jzy3d.plot3d.rendering.view.modes.ViewBoundMode;
import org.jzy3d.plot3d.text.DrawableTextWrapper;
import org.jzy3d.plot3d.text.align.Halign;
import org.jzy3d.plot3d.text.align.Valign;
import org.jzy3d.plot3d.text.drawable.DrawableTextBitmap;

import javax.swing.*;

public class SpaceGroupView extends FrameAWT implements View, IObjectPickedListener {

    // interna
	protected final Controller _controller;
    protected final Chart _chart;
    protected final View[] _subViewControls;
    protected final SpaceGroupViewChartController _chartController;
    protected final ResourceBundle bundle = ResourceBundle.getBundle("Messages");

    protected Coord3d _globalCenter;
    protected final Point _originPoint;
    protected volatile boolean _showSpacing = false;
    protected volatile float _currentSpacing = Min_Spacing_Factor;
    protected final Map<Mesh, MeshInformation> _meshes = new HashMap<Mesh, MeshInformation>();

    // color providers
    protected ColorProvider _currentColorProvider;
    protected final ColorProvider _monoChromaticColors;
    protected final ColorProvider _chromaticColors;
    protected final ColorProvider _faceColors;

    // internal structure to store meta data of mesh
    protected class MeshInformation {

        public final Mesh Mesh;
        public Coord3d Centroid;
        public boolean Visible;
        public final DrawableTextWrapper Label;
        public final List<Polygon> Polygons;
        public final List<Point> Vertices;

        public MeshInformation(Mesh mesh, List<Polygon> polygons, List<Point> vertices, DrawableTextWrapper label) {
            this(mesh, Coord3d.ORIGIN, true, polygons, vertices, label);
        }

        public MeshInformation(Mesh mesh, Coord3d centroid, boolean visible, List<Polygon> polygons, List<Point> vertices, DrawableTextWrapper label) {
            this.Mesh = mesh;
            this.Centroid = centroid;
            this.Visible = visible;
            this.Label = label;
            this.Polygons = polygons;
            this.Vertices = vertices;

            this.Label.setHalign(Halign.CENTER);
            this.Label.setValign(Valign.TOP);
        }

    }

    public static final String Label_Format = "[ V: %s, F: %s ]";
	public static final float Origin_Point_Size = 15f;	
	public static final float Min_Spacing_Factor = 1f;
	public static final float Max_Spacing_Factor = 3f;
	public static final float Wireframe_Width = 1.5f;
	public static final float Vertex_Size = 5f;
	public static final Color Vertex_Color = new Color(255,100,100);
	public static final Color Wireframe_Color = Color.WHITE;
	public static final Color Faces_Color = new Color(135, 206, 235, 170);
	public static final Color Foreground_Color = Color.WHITE;
    public static final Color Label_Color = Color.BLACK;
	public static final Color Viewport_Background = new Color(105, 105, 105);
    public static final Color Grid_Color = new Color(192, 192, 192);
	public static final Rectangle Default_Size = new Rectangle(1024, 768);
    public static final Dimension Min_Size = new Dimension(500, 450);

	/**
	 * Constructor of view.
	 * @param controller
	 */
	SpaceGroupView(Controller controller) {
		super();

		this._controller = controller;
        this.setExtendedState(this.getExtendedState() | Frame.MAXIMIZED_BOTH);
		this.setLayout(new BorderLayout());
        this.setMinimumSize(Min_Size);
		this.setForeground(org.jzy3d.colors.ColorAWT.toAWT(Foreground_Color));
		this._showSpacing = _controller.getViewOption(Controller.ViewOptions.ShowSpacing);
		this._currentSpacing = this._showSpacing ? Max_Spacing_Factor : Min_Spacing_Factor;
        // window listener
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { System.exit(0); }
        });

        final Quality quality = Quality.Nicest;
		this._chart = AWTChartComponentFactory.chart(quality, IChartComponentFactory.Toolkit.awt);
		this._chart.getView().setBackgroundColor(Viewport_Background);
		this._chart.getView().setSquared(true);
        this._chart.getAxeLayout().setMainColor(Grid_Color);
        //this._chart.getView().setBoundManual(BoundingBox3d.newBoundsAtOrigin());
        //this._chart.getView().setBoundMode(ViewBoundMode.MANUAL);

        // set ordering strategy for view
        final AbstractOrderingStrategy strategy = new TextFirstOrderingStrategy(this._chart.getView());
        this._chart.getScene().getGraph().setStrategy(strategy);
        // setup key and mouse controller
        this._chartController = new SpaceGroupViewChartController(this._chart);
        this._chartController.getPickingSupport().addObjectPickedListener(this);
        this.createControllerMenuItems();

		// create moveable point
		this._originPoint = new Point(new Coord3d(), Color.BLUE, Origin_Point_Size);
		this._originPoint.setDisplayed(true);
		this._chart.addDrawable(this._originPoint);

        // setup color providers
        this._monoChromaticColors = new MonochromaticColorProvider(Faces_Color);
        this._chromaticColors = new ChromaticColorProvider();
        this._faceColors =new FaceColorProvider();
        this._currentColorProvider = this._monoChromaticColors;

		// add components
        final SpaceGroupSelectionPanel selectionPanel = new SpaceGroupSelectionPanel(this._controller);
		this.add(selectionPanel, BorderLayout.PAGE_START);

        final SpaceGroupSettingsPanel settingPanel = new SpaceGroupSettingsPanel(this._controller);
        this.add(settingPanel, BorderLayout.PAGE_END);
        
        final SpaceGroupViewSettingsPanel viewSettingsPanel = new SpaceGroupViewSettingsPanel(this._controller);
        this.add(viewSettingsPanel, BorderLayout.LINE_END);

        this._subViewControls = new View[] {selectionPanel, settingPanel, viewSettingsPanel};

		super.initialize(_chart, Default_Size, "SpaceGroup Visualizer");

		// timer for tweening the current spacing value
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			final float StepWith = 0.3f;
			final float MinScale = 0.2f;
			
			@Override
			public void run() {
				if (_showSpacing && _currentSpacing < Max_Spacing_Factor) {
					// increase spacing if spacing is turned on
					float spacing = Math.max((float)(Math.log10(_currentSpacing) / Math.log10(Max_Spacing_Factor)), MinScale) * StepWith;
					setSpacing(Math.min(_currentSpacing + spacing, Max_Spacing_Factor));
				}
				else if (!_showSpacing && _currentSpacing > Min_Spacing_Factor) {
					// decrease  the spacing if spacing is turned off
					float spacing = Math.max((float)(Math.log10(_currentSpacing) / Math.log10(Max_Spacing_Factor)), MinScale) * StepWith;
					setSpacing(Math.max(_currentSpacing - spacing, Min_Spacing_Factor));
				}
			}
		}, 0, 30);
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
                final boolean showBox = !_controller.getViewOption(Controller.ViewOptions.showAxeBox);
                _controller.setViewOption(Controller.ViewOptions.showAxeBox, showBox);
            }
        });

        // crate face tinting menu items
        final JMenu mnuFaceTinting = new JMenu(bundle.getString("faces"));
        final JMenuItem miMonochromColors = new JMenuItem(bundle.getString("monochromaticColors"));
        miMonochromColors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setColorProvider(_monoChromaticColors);
            }
        });

        final JMenuItem miChromaticColors = new JMenuItem(bundle.getString("chromaticColors"));
        miChromaticColors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setColorProvider(_chromaticColors);
            }
        });
        final JMenuItem miFaceColors = new JMenuItem(bundle.getString("faceColors"));
        miFaceColors.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setColorProvider(_faceColors);
            }
        });
        mnuFaceTinting.add(miMonochromColors);
        mnuFaceTinting.add(miChromaticColors);
        mnuFaceTinting.add(miFaceColors);

        mnu.add(mnuFaceTinting);
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
            // update face color
            Iterator<Mesh> iter = this._meshes.keySet().iterator();
            while(iter.hasNext()) {
                Mesh m = iter.next();
                MeshInformation info = this._meshes.get(m);
                for(Polygon poly : info.Polygons) {
                    poly.setColor(this._currentColorProvider.getColor(m, poly));
                }
            }
        }
    }

	/**
	 * Clears the entire scene.
	 */
	protected void clearScene() {
        Iterator<Mesh> iter = this._meshes.keySet().iterator();
        while (iter.hasNext()) {
            Mesh m = iter.next();
            MeshInformation info = this._meshes.get(m);

            // remove label
            this._chart.removeDrawable(info.Label, false);

            // remove polygons
            for (Polygon poly : info.Polygons) {
                this._chart.removeDrawable(poly, false);
            }

            // remove vertices
            for(Point vertex : info.Vertices) {
                this._chart.removeDrawable(vertex, false);
            }
        }
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
                    Point point = ConvertHelper.convertVector3dTojzyPoint(vertex);
                    // update vertex position from polygon
                    polygons.get(c).get(i).xyz = point.xyz.sub(centroid).add(originVertex);
                    vertices.get(i).xyz = point.xyz;
                }
            }
            // update position of vertex points
            for(int i = 0; i < m.getVertices().size(); i++) {
                Vector3D vertex = m.getVertices().get(i);
                Point point = ConvertHelper.convertVector3dTojzyPoint(vertex);
                // update vertex position from polygon
                vertices.get(i).xyz = point.xyz.sub(centroid).add(originVertex);
            }
            info.Label.setPosition(originVertex);
		}
	}
	
	/**
	 * Calculates the centroid for every mesh inside the scene.
	 */
	private void calculateMeshCenter() {
        int totalVertexCount = 0;
        Coord3d totalVect = Coord3d.ORIGIN;

        Iterator<Mesh> iter = this._meshes.keySet().iterator();
        while(iter.hasNext()) {
            Mesh m = iter.next();
            MeshInformation info = this._meshes.get(m);
            Coord3d polyCenter = Coord3d.ORIGIN;

            for(Point vertex : info.Vertices) {
                Coord3d coord = vertex.xyz;
                // count total number of vertices
                totalVect = totalVect.add(coord);
                polyCenter = polyCenter.add(coord);
            }
            // calculate center of polygon
            info.Centroid = polyCenter.div(info.Vertices.size());
            //this._chart.addDrawable(new DrawableTextBitmap("test", info.Centroid.getXY(), Color.RED));
            totalVertexCount += info.Vertices.size();
        }
        // calculate center of box
        this._globalCenter = totalVect.div(totalVertexCount);
	}
	
	@Override
	public void invalidateView() {
		final boolean showVertices =  this._controller.getVisualizationStep() == Controller.VisualizationSteps.ScatterPlot
                || this._controller.getViewOption(Controller.ViewOptions.ShowVertices);
		final boolean showFaces = this._controller.getViewOption(Controller.ViewOptions.ShowFaces);
		final boolean showWireframe = this._controller.getViewOption(Controller.ViewOptions.ShowWireframe);
        final boolean showLabel = this._controller.getVisualizationStep() != Controller.VisualizationSteps.ScatterPlot
                && this._controller.getViewOption(Controller.ViewOptions.ShowLabeledMeshes);

        this.clearScene();
        this.invalidateViewOptions();

        final List<AbstractDrawable> drawables = new LinkedList<AbstractDrawable>();
	    final List<Mesh> meshes = this._controller.calculateMesh();

	    for (Mesh m : meshes) {
            List<Polygon> polygons = new LinkedList<Polygon>();
            List<Point> vertices = new LinkedList<Point>();

            // create polygons
            for (com.Softwareprojekt.interfaces.Polygon poly : m.getFaces()) {
                PickablePolygon nPoly = ConvertHelper.convertPolygonToPickablePolygon(poly);
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
            for (com.Softwareprojekt.interfaces.Vector3D vertex : m.getVertices()) {
                Point point = new Point(ConvertHelper.convertVector3dTojzyCoord3d(vertex), Vertex_Color, Vertex_Size);
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

		this.calculateMeshCenter();
		this.calculateMeshPosition();

        // update the origin point
        this._originPoint.xyz = ConvertHelper.convertVector3dTojzyCoord3d(this._controller.getOriginPoint());

		this._chart.getScene().getGraph().add(drawables, true);

        // invalidate view of sub controls
        for(View view : this._subViewControls) {
            view.invalidateView();
        }
	}
	
	@Override
	public void invalidateViewOptions() {
		final boolean showVertices = this._controller.getVisualizationStep() == Controller.VisualizationSteps.ScatterPlot
                || this._controller.getViewOption(Controller.ViewOptions.ShowVertices);
		final boolean showFaces = this._controller.getViewOption(Controller.ViewOptions.ShowFaces);
		final boolean showWireframe = this._controller.getViewOption(Controller.ViewOptions.ShowWireframe);
        final boolean showLabel = this._controller.getVisualizationStep() != Controller.VisualizationSteps.ScatterPlot
                && this._controller.getViewOption(Controller.ViewOptions.ShowLabeledMeshes);

        Iterator<Mesh> iter = this._meshes.keySet().iterator();
        while(iter.hasNext()) {
            Mesh m = iter.next();
            MeshInformation info = this._meshes.get(m);
            info.Label.setDisplayed(showLabel && info.Visible);
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
        this._chart.getView().setAxeBoxDisplayed(this._controller.getViewOption(Controller.ViewOptions.showAxeBox));

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
