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

	private final Controller _controller;
	private final Chart _chart;
	private final SpaceGroupSelectionPanel _selectionPanel;
	private final SpaceGroupViewSettingsPanel _viewSettingsPanel;
	private final SpaceGroupSettingsPanel _settingPanel;
    private final SpaceGroupViewMouseController _mouseController;

	private Coord3d _globalCenter;
	private final Point _originPoint;
    private volatile boolean _showSpacing = false;
    private volatile float _currentSpacing = Min_Spacing_Factor;
	private final List<Point> _chartVertices = new LinkedList<Point>();
	private final List<Polygon> _chartFaces = new LinkedList<Polygon>();

    private final Map<Mesh, Coord3d> _meshCentroids = new HashMap<Mesh, Coord3d>();
    private final Map<Mesh, Boolean> _meshVisibility = new HashMap<Mesh, Boolean>();
    private final Map<Mesh, List<Polygon>> _meshPolygons = new HashMap<Mesh, List<Polygon>>();
    private final Map<Mesh, List<Point>> _meshVertices = new HashMap<Mesh, List<Point>>();

    // color providers
    private ColorProvider _currentColorProvider;
    private final ColorProvider _monoChromaticColors;
    private final ColorProvider _chromaticColors;

	private final ResourceBundle bundle = ResourceBundle.getBundle("Messages");
	
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
		this._selectionPanel = new SpaceGroupSelectionPanel(this._controller);
		this.add(this._selectionPanel, BorderLayout.PAGE_START);
		
        this._settingPanel = new SpaceGroupSettingsPanel(this._controller);
        this.add(this._settingPanel, BorderLayout.PAGE_END);
        
        this._viewSettingsPanel = new SpaceGroupViewSettingsPanel(this._controller);
        this.add(this._viewSettingsPanel, BorderLayout.LINE_END);

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
		// remove polygons
		for (Polygon poly : this._chartFaces) {
			this._chart.removeDrawable(poly, false);
		}
		
		// remove vertices
		for(Point vertice : this._chartVertices) {
			this._chart.removeDrawable(vertice, false);
		}
		
		this._chartFaces.clear();
		this._chartVertices.clear();
		this._meshCentroids.clear();
        this._meshVisibility.clear();
        this._meshPolygons.clear();
        this._meshVertices.clear();
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
        Iterator<Mesh> iter = this._meshVertices.keySet().iterator();
        while(iter.hasNext()) {
			Mesh m = iter.next();
            Coord3d centroid = this._meshCentroids.get(m);
            List<Polygon> polygons = this._meshPolygons.get(m);
            List<Point> vertices = this._meshVertices.get(m);

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

        Iterator<Mesh> iter = this._meshVertices.keySet().iterator();
        while(iter.hasNext()) {
            Mesh m = iter.next();
            List<Point> vertices = this._meshVertices.get(m);
            Coord3d polyCenter = Coord3d.ORIGIN;

            for(Point vertex : vertices) {
                Coord3d coord = vertex.xyz;
                // count total number of vertices
                totalVect = totalVect.add(coord);
                polyCenter = polyCenter.add(coord);
            }
            // calculate center of polygon
            this._meshCentroids.put(m, polyCenter.div(vertices.size()));
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
            this._meshVisibility.put(m, true);
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
                this._chartFaces.add(nPoly);
                this._mouseController.getPickingSupport().registerPickableObject(nPoly, m);
            }
            this._meshPolygons.put(m, polygons);

            // create vertices
            for (com.Softwareprojekt.interfaces.Vector3D vertice : m.getVertices()) {
                Point point = new Point(ConvertHelper.convertVector3dTojzyCoord3d(vertice), Vertex_Color, Vertex_Size);
                point.setDisplayed(showVertices);
                this._chartVertices.add(point);
                drawables.add(point);
                vertices.add(point);
            }
            this._meshVertices.put(m, vertices);
        }

		this.calculateMeshCenter();
		this.calculateMeshPosition();

        // update the origin point
        this._originPoint.xyz = ConvertHelper.convertVector3dTojzyCoord3d(this._controller.getOriginPoint());

		this._chart.getScene().add(drawables);
		this._viewSettingsPanel.invalidateView();
		this._settingPanel.invalidateView();
		this._selectionPanel.invalidateView();
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

        Iterator<Mesh> iter = this._meshVertices.keySet().iterator();
        while(iter.hasNext()) {
            Mesh m = iter.next();
            if (this._meshVisibility.get(m)) {
                List<Polygon> polygons = this._meshPolygons.get(m);
                List<Point> vertices = this._meshVertices.get(m);

                // update polygon visibility
                for (Polygon poly : polygons) {
                    poly.setWireframeDisplayed(showWireframe);
                    poly.setFaceDisplayed(showFaces);
                    poly.setColor(this.getColorProivder().getColor(m, poly));
                }

                // update vertices visibility
                for(Point vertex : vertices) {
                    vertex.setDisplayed(showVertices);
                }
            }
        }

		this._showSpacing = this._controller.getViewOption(Controller.ViewOptions.ShowSpacing);
		this.calculateMeshPosition();
		
		this._viewSettingsPanel.invalidateViewOptions();
		this._settingPanel.invalidateViewOptions();
		this._selectionPanel.invalidateViewOptions();
	}

    private void setMeshDisplayed(Mesh mesh, boolean visible) {
        final List<Polygon> polygons = this._meshPolygons.get(mesh);
        final List<Point> vertices = this._meshVertices.get(mesh);

        for(Polygon poly : polygons) {
            poly.setDisplayed(visible);
            poly.setWireframeDisplayed(visible);
        }

        for (Point point : vertices) {
            point.setDisplayed(visible);
        }
        this._meshVisibility.put(mesh, visible);
    }

    @Override
    public void objectPicked(List<? extends Object> objects, PickingSupport pickingSupport) {
        if (System.currentTimeMillis() - this._mouseController.getPickingSupport().getLastPickTime() >= Pick_Timeout) {
            double minDistance = Double.MAX_VALUE;
            Mesh selectedMesh = null;

            for (int i = 0; i < objects.size(); i++) {
                if (objects.get(i) instanceof Mesh) {
                    Mesh mesh = (Mesh)objects.get(i);
                    if (this._meshVisibility.get(mesh)) {
                        Coord3d centroid = this._meshCentroids.get(mesh);
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
