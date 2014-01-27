package com.Softwareprojekt.visualization;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

import com.Softwareprojekt.Utilities.ConvertHelper;

import com.Softwareprojekt.Utilities.ExtendedPickingSupport;
import com.Softwareprojekt.interfaces.*;
import com.Softwareprojekt.interfaces.View;
import org.jzy3d.bridge.awt.FrameAWT;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.chart.controllers.mouse.camera.ICameraMouseController;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.chart.factories.IChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.IntegerCoord2d;
import org.jzy3d.maths.Rectangle;
import org.jzy3d.picking.IObjectPickedListener;
import org.jzy3d.picking.PickingSupport;
import org.jzy3d.plot3d.primitives.*;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.pickable.PickablePolygon;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.scene.Graph;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.swing.*;

public class SpaceGroupView extends FrameAWT implements View, IObjectPickedListener {

	protected final Controller _controller;
    protected final Chart _chart;
    protected final View[] _subViewControls;
    protected final SpaceGroupViewMouseController _mouseController;
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

    protected class MeshInformation {

        public final Mesh Mesh;
        public Coord3d Centroid;
        public boolean Visible;
        public final List<Polygon> Polygons;
        public final List<Point> Vertices;

        public MeshInformation(Mesh mesh, List<Polygon> polygons, List<Point> vertices) {
            this(mesh, Coord3d.ORIGIN, true, polygons, vertices);
        }

        public MeshInformation(Mesh mesh, Coord3d centroid, boolean visible, List<Polygon> polygons, List<Point> vertices) {
            this.Mesh = mesh;
            this.Centroid = centroid;
            this.Visible = visible;
            this.Polygons = polygons;
            this.Vertices = vertices;
        }

    }

	public static final float Origin_Point_Size = 15f;	
	public static final float Min_Spacing_Factor = 1f;
	public static final float Max_Spacing_Factor = 3f;
	public static final float Wireframe_Width = 1.5f;
	public static final float Vertex_Size = 5f;
	public static final Color Vertex_Color = new Color(255,100,100);
	public static final Color Wireframe_Color = Color.WHITE;
	public static final Color Faces_Color = new Color(135, 206, 235, 170);
	public static final Color Foreground_Color = Color.WHITE;
	public static final Color Viewport_Background = new Color(105, 105, 105);
	public static final Rectangle Default_Size = new Rectangle(1024, 768);
	public static final long Pick_Timeout = 1;
	/**
	 * Constructor of view.
	 * @param controller
	 */
	SpaceGroupView(Controller controller) {
		super();
		this._controller = controller;
        this.setExtendedState(this.getExtendedState() | Frame.MAXIMIZED_BOTH);
		this.setLayout(new BorderLayout());
		this.setForeground(org.jzy3d.colors.ColorAWT.toAWT(Foreground_Color));
		this._showSpacing = _controller.getViewOption(Controller.ViewOptions.ShowSpacing);
		this._currentSpacing = this._showSpacing ? Max_Spacing_Factor : Min_Spacing_Factor;
        // stop application listener
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

		this._chart = AWTChartComponentFactory.chart(Quality.Nicest, IChartComponentFactory.Toolkit.awt);
		this._chart.getView().setBackgroundColor(Viewport_Background);
		this._chart.getView().setSquared(true);
        // set up default mouse controller
        this._mouseController = new SpaceGroupViewMouseController(this._chart);
        this._mouseController.getPickingSupport().addObjectPickedListener(this);
        this._chart.addKeyController();
        this._chart.addScreenshotKeyController();
        //this._chart.getView().getCamera().

		// create moveable point
		this._originPoint = new Point(new Coord3d(), Color.BLUE, Origin_Point_Size);
		this._originPoint.setDisplayed(true);
		this._chart.addDrawable(this._originPoint);

        // setup color providers
        this._monoChromaticColors = new MonochromaticColorProvider(Faces_Color);
        this._chromaticColors = new ChromaticColorProvider();
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

    private ColorProvider getColorProivder() {
        return this._currentColorProvider;
    }

    private void setColorProvider(ColorProvider colorProvider) {
        this._currentColorProvider = colorProvider;
    }

	/**
	 * Clears the entire scene.
	 */
	protected void clearScene() {
        Iterator<Mesh> iter = this._meshes.keySet().iterator();
        while (iter.hasNext()) {
            Mesh m = iter.next();
            MeshInformation info = this._meshes.get(m);
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
        this._mouseController.getPickingSupport().clear();
	}
	
	/**
	 * Sets the current spacing between polygons.
	 * @param spacing
	 */
	private synchronized void setSpacing(float spacing) {
		this._currentSpacing = spacing;
		this.calculateMeshPosition();
		this._chart.getView().updateBounds();
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

		    // update position of polygons
            for (int c = 0; c < m.getFaces().size(); c++) {
                // update position of each vertex
                for(int i = 0; i < m.getFaces().get(c).getVertices().size(); i++) {
                    Vector3D vertex = m.getFaces().get(c).getVertices().get(i);
                    Point point = ConvertHelper.convertVector3dTojzyPoint(vertex);
                    Coord3d originVect =  centroid.sub(this._globalCenter).mul(this._currentSpacing).add(this._globalCenter);
                    // update vertex position from polygon
                    polygons.get(c).get(i).xyz = point.xyz.sub(centroid).add(originVect);
                    vertices.get(i).xyz = point.xyz;
                }
            }
            // update position of vertex points
            for(int i = 0; i < m.getVertices().size(); i++) {
                Vector3D vertex = m.getVertices().get(i);
                Point point = ConvertHelper.convertVector3dTojzyPoint(vertex);
                Coord3d originVect =  centroid.sub(this._globalCenter).mul(this._currentSpacing).add(this._globalCenter);
                // update vertex position from polygon
                vertices.get(i).xyz = point.xyz.sub(centroid).add(originVect);
            }
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
            List<Point> vertices = info.Vertices;
            Coord3d polyCenter = Coord3d.ORIGIN;

            for(Point vertex : vertices) {
                Coord3d coord = vertex.xyz;
                // count total number of vertices
                totalVect = totalVect.add(coord);
                polyCenter = polyCenter.add(coord);
            }
            // calculate center of polygon
            info.Centroid = polyCenter.div(vertices.size());
            totalVertexCount += vertices.size();
        }
        // calculate center of box
        this._globalCenter = totalVect.div(totalVertexCount);
	}
	
	@Override
	public void invalidateView() {
		final boolean showVertices =  this._controller.getVisualizationStep() == Controller.VisualizationSteps.ScatterPlot ||
                this._controller.getViewOption(Controller.ViewOptions.ShowVertices);
		final boolean showFaces = this._controller.getViewOption(Controller.ViewOptions.ShowFaces);
		final boolean showWireframe = this._controller.getViewOption(Controller.ViewOptions.ShowWireframe);

        this.clearScene();

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
                nPoly.setColor(this.getColorProivder().getColor(m, nPoly));
                polygons.add(nPoly);
                drawables.add(nPoly);
                this._mouseController.getPickingSupport().registerPickableObject(nPoly, m);
            }

            // create vertices
            for (com.Softwareprojekt.interfaces.Vector3D vertice : m.getVertices()) {
                Point point = new Point(ConvertHelper.convertVector3dTojzyCoord3d(vertice), Vertex_Color, Vertex_Size);
                point.setDisplayed(showVertices);
                drawables.add(point);
                vertices.add(point);
            }

            this._meshes.put(m, new MeshInformation(m, polygons, vertices));
        }

		this.calculateMeshCenter();
		this.calculateMeshPosition();

        // update the origin point
        this._originPoint.xyz = ConvertHelper.convertVector3dTojzyCoord3d(this._controller.getOriginPoint());

		this._chart.getScene().add(drawables);

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
		final boolean showChromaticFaces = this._controller.getViewOption(Controller.ViewOptions.ShowChromaticFaces);

        if (showChromaticFaces) {
            this.setColorProvider(this._chromaticColors);
        }
        else {
            this.setColorProvider(this._monoChromaticColors);
        }

        Iterator<Mesh> iter = this._meshes.keySet().iterator();
        while(iter.hasNext()) {
            Mesh m = iter.next();
            MeshInformation info = this._meshes.get(m);

            // update polygon visibility
            for (Polygon poly : info.Polygons) {
                poly.setWireframeDisplayed(showWireframe && info.Visible);
                poly.setFaceDisplayed(showFaces && info.Visible);
                poly.setColor(this.getColorProivder().getColor(m, poly));
            }

            // update vertices visibility
            for(Point vertex : info.Vertices) {
                vertex.setDisplayed(showVertices && info.Visible);
            }
        }

		this._showSpacing = this._controller.getViewOption(Controller.ViewOptions.ShowSpacing);
		this.calculateMeshPosition();

        // invalidate view options of sub controls
        for(View view : this._subViewControls) {
		    view.invalidateViewOptions();
        }
	}

    private void setMeshDisplayed(Mesh mesh, boolean visible) {
        final MeshInformation info = this._meshes.get(mesh);
        final boolean showVertices = this._controller.getViewOption(Controller.ViewOptions.ShowVertices);
        final boolean showFaces = this._controller.getViewOption(Controller.ViewOptions.ShowFaces);
        final boolean showWireframe = this._controller.getViewOption(Controller.ViewOptions.ShowWireframe);
        final List<Polygon> polygons = info.Polygons;
        final List<Point> vertices = info.Vertices;

        for(Polygon poly : polygons) {
            poly.setDisplayed(visible && showFaces);
            poly.setWireframeDisplayed(visible && showWireframe);
        }

        for (Point point : vertices) {
            point.setDisplayed(visible);
        }
        info.Visible = visible && showVertices;
    }

    @Override
    public void objectPicked(List<? extends Object> objects, PickingSupport pickingSupport) {
        if (System.currentTimeMillis() - this._mouseController.getPickingSupport().getLastPickTime() >= Pick_Timeout) {
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
}
