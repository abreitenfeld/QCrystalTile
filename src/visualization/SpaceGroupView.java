package visualization;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.*;
import java.util.Timer;

import javax.swing.*;

import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.bridge.swing.FrameSwing;
import org.jzy3d.bridge.awt.FrameAWT;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.chart.Settings;
import org.jzy3d.chart.controllers.mouse.camera.ICameraMouseController;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.chart.factories.IChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.events.IViewPointChangedListener;
import org.jzy3d.events.ViewPointChangedEvent;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Rectangle;
import org.jzy3d.plot3d.primitives.*;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.view.modes.CameraMode;


import Utilities.*;

import interfaces.Controller;
import interfaces.Mesh;
import interfaces.Model;
import interfaces.Vector3D;
import interfaces.View;

public class SpaceGroupView extends FrameAWT implements View {

	private final Controller _controller;
	private final Chart _chart;
	private final SpaceGroupSelectionPanel _selectionPanel;
	private final SpaceGroupViewSettingsPanel _viewSettingsPanel;
	private final SpaceGroupSettingsPanel _settingPanel;
	
	private Coord3d _globalCenter;
	private final Point _originPoint;
	private final List<Point> _chartVertices = new LinkedList<Point>();
	private final List<Polygon> _chartFaces = new LinkedList<Polygon>();
	private final HashMap<interfaces.Polygon, Polygon> _polyToJzyPoly = new HashMap<interfaces.Polygon, Polygon>();
	private final HashMap<interfaces.Vector3D, Point> _vectToJzyPoint = new HashMap<interfaces.Vector3D, Point>();
	private final HashMap<interfaces.Polygon, Coord3d> _polyToCenter = new HashMap<interfaces.Polygon, Coord3d>();
	private final HashMap<interfaces.Polygon, List<Point>> _polyToJzyPoint = new HashMap<interfaces.Polygon, List<Point>>();
	private volatile boolean _showSpacing = false;
	private volatile float _currentSpacing = Min_Spacing_Factor;
	
	private final ResourceBundle bundle = ResourceBundle.getBundle("resources.Messages");
	
	public static final float Origin_Point_Size = 15f;	
	public static final float Min_Spacing_Factor = 1f;
	public static final float Max_Spacing_Factor = 2f;
	public static final float Wireframe_Width = 2f;
	public static final float Vertice_Size = 5f;
	public static final Color Vertice_Color = new Color(255,100,100);
	public static final Color Wireframe_Color = Color.WHITE;
	public static final Color Faces_Color = new Color(135, 206, 235, 150);
	public static final Color Foregrond_Color = Color.WHITE;
	public static final Color Viewport_Background = Color.GRAY;	
	public static final Rectangle Default_Size = new Rectangle(1024, 768);
	
	/**
	 * Constructor of view.
	 * @param controller
	 */
	SpaceGroupView(Controller controller) {
		super();
		this._controller = controller;
		
		this.setLayout(new BorderLayout());
		this.setForeground(org.jzy3d.colors.ColorAWT.toAWT(Foregrond_Color));
		this._showSpacing = _controller.getViewOption(Controller.ViewOptions.ShowSpacing);
		this._currentSpacing = this._showSpacing ? Max_Spacing_Factor : Min_Spacing_Factor;
		
		this._chart = AWTChartComponentFactory.chart(Quality.Nicest, IChartComponentFactory.Toolkit.awt);
		this._chart.getView().setBackgroundColor(Viewport_Background);
		this._chart.getView().setSquared(false);
		
			
		// create moveable point
		this._originPoint = new Point(new Coord3d(), Color.BLUE, Origin_Point_Size);
		this._originPoint.setDisplayed(true);
		this._chart.addDrawable(this._originPoint);
		//this._chart.getView().setViewPoint(new Coord3d(0,0,0));
		//this._chart.setAxeDisplayed(false);
		
		
		// add components
		this._selectionPanel = new SpaceGroupSelectionPanel(this._controller);
		this.add(this._selectionPanel, BorderLayout.PAGE_START);
		
        this._settingPanel = new SpaceGroupSettingsPanel(this._controller);
        this.add(this._settingPanel, BorderLayout.PAGE_END);
        
        this._viewSettingsPanel = new SpaceGroupViewSettingsPanel(this._controller);
        this.add(this._viewSettingsPanel, BorderLayout.LINE_END);
        
		// set up default mouse controller
		ICameraMouseController mouse = ChartLauncher.configureControllers(_chart, "", true, true);
        ChartLauncher.instructions();
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
		
		//this._chart.getView().setCameraMode(CameraMode.PERSPECTIVE);
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
		this._polyToCenter.clear();
		this._polyToJzyPoly.clear();
		this._vectToJzyPoint.clear();
		this._polyToJzyPoint.clear();
	}
	
	/**
	 * Sets the current spacing between polygons.
	 * @param spacing
	 */
	private synchronized void setSpacing(float spacing) {
		this._currentSpacing = spacing;
		this.calculatePolygonPosition();
		this._chart.getView().updateBounds();
	}
	
	/**
	 * Calculates the position of polygons according current spacing value.
	 */
	private void calculatePolygonPosition() {
		Iterator<interfaces.Polygon> iter = this._polyToJzyPoly.keySet().iterator();
		while (iter.hasNext()) {
			interfaces.Polygon poly = iter.next();
			if (this._polyToCenter.containsKey(poly)) {
				Polygon jzyPoly = this._polyToJzyPoly.get(poly);
				Coord3d center = this._polyToCenter.get(poly);
				List<Point> drawableVertices = this._polyToJzyPoint.get(poly);
				
				// update position of each vertex
				for(int i = 0; i < poly.getVertices().size(); i++) {
					interfaces.Vector3D vertice = poly.getVertices().get(i);
					Coord3d jzyPoint = ConvertHelper.convertVector3dTojzyCoord3d(vertice);
					Coord3d originVect =  center.sub(this._globalCenter).mul(this._currentSpacing).add(this._globalCenter);
					// update vertex position from polygon
					jzyPoly.get(i).xyz = jzyPoint.sub(center).add(originVect);
					drawableVertices.get(i).xyz = jzyPoly.get(i).xyz;
				}
			}
		}
	}
	
	/**
	 * Calculates the centroid for every polygons inside the scene.
	 * @param polys
	 */
	private void calculatePolygonCenter(List<interfaces.Polygon> polys) {
		int totalVerticeCount = 0;
		Coord3d totalVect = Coord3d.ORIGIN;

		for(interfaces.Polygon poly : polys) {
			int polyCount = poly.getVertices().size();
			Coord3d polyCenter = Coord3d.ORIGIN;
			
			for (Vector3D vect : poly.getVertices()) {
				Coord3d jzyVect = ConvertHelper.convertVector3dTojzyCoord3d(vect);
				// count total number of vertices
				totalVerticeCount++;
				totalVect = totalVect.add(jzyVect);
				polyCenter = polyCenter.add(jzyVect);
			}
			// calculate center of polygon
			this._polyToCenter.put(poly, polyCenter.div(poly.getVertices().size()));
		}
		// calculate center of box
		this._globalCenter = totalVect.div(totalVerticeCount);
	}
	
	@Override
	public void invalidateView() {
		boolean showVertices = this._controller.getViewOption(Controller.ViewOptions.ShowVertices);
		boolean showFaces = this._controller.getViewOption(Controller.ViewOptions.ShowFaces);
		boolean showWireframe = this._controller.getViewOption(Controller.ViewOptions.ShowWireframe);
		boolean showChromaticFaces = this._controller.getViewOption(Controller.ViewOptions.ShowChromaticFaces);;
				
	    final Mesh m = this._controller.calculateMesh();
	    final List<interfaces.Polygon> polys = m.getFaces();
		final List<interfaces.Vector3D> vertices = m.getVertices();
		final List<AbstractDrawable> drawables = new LinkedList<AbstractDrawable>();
		
		this.clearScene();
		// update the origin point
		this._originPoint.xyz = ConvertHelper.convertVector3dTojzyCoord3d(this._controller.getOriginPoint());

		// add polygons
		for (interfaces.Polygon poly : polys) {
			Polygon nPoly = ConvertHelper.convertPolygonToJzyPolygon(poly);
			nPoly.setWireframeColor(Wireframe_Color);
			nPoly.setWireframeWidth(Wireframe_Width);
			// set poly color
			if (showChromaticFaces) {
				Color faceColor = Color.random();
				faceColor.a = Faces_Color.a;
				nPoly.setColor(faceColor);
			}
			else {
				nPoly.setColor(Faces_Color);
			}
			nPoly.setWireframeDisplayed(showWireframe);
			nPoly.setFaceDisplayed(showFaces);
			
			this._chartFaces.add(nPoly);
			this._polyToJzyPoly.put(poly, nPoly);
			drawables.add(nPoly);
			
			// add vertices
			this._polyToJzyPoint.put(poly, new ArrayList<Point>(poly.getVertices().size()));
			for (interfaces.Vector3D vertice : poly.getVertices()) {
				Point point = new Point(ConvertHelper.convertVector3dTojzyCoord3d(vertice), Vertice_Color, Vertice_Size);
				point.setDisplayed(showVertices);
				this._chartVertices.add(point);
				this._polyToJzyPoint.get(poly).add(point);
				drawables.add(point);
			}
		 }
	
		// add vertices
		/*for (interfaces.Vector3D vertice : vertices){
			Coord3d coord = ConvertHelper.convertVector3dTojzyCoord3d(vertice);
			Point point = new Point(coord, new Color(255,100,100), Vertice_Size);
			point.setDisplayed(showVertices);
			this._vectToJzyPoint.put(vertice, point);
			this._chartVertices.add(point);
			drawables.add(point);
		}*/
	
		this.calculatePolygonCenter(polys);
		this.calculatePolygonPosition();
		
		this._chart.getScene().add(drawables);
		this._viewSettingsPanel.invalidateView();
		this._settingPanel.invalidateView();
		this._selectionPanel.invalidateView();
	}
	
	@Override
	public void invalidateViewOptions() {
		boolean showVertices = this._controller.getViewOption(Controller.ViewOptions.ShowVertices);
		boolean showFaces = this._controller.getViewOption(Controller.ViewOptions.ShowFaces);
		boolean showWireframe = this._controller.getViewOption(Controller.ViewOptions.ShowWireframe);
		boolean showChromaticFaces = this._controller.getViewOption(Controller.ViewOptions.ShowChromaticFaces);
		
		// update polygon visibility
		for (Polygon poly : this._chartFaces) {
			poly.setWireframeDisplayed(showWireframe);
			poly.setFaceDisplayed(showFaces);
			// set poly color
			if (showChromaticFaces) {
				Color faceColor = Color.random();
				faceColor.a = Faces_Color.a;
				poly.setColor(faceColor);
			}
			else {
				poly.setColor(Faces_Color);
			}
		}
		
		// update vertices visibility
		for(Point vertice : this._chartVertices) {
			vertice.setDisplayed(showVertices);
		}

		this._showSpacing = this._controller.getViewOption(Controller.ViewOptions.ShowSpacing);
		this.calculatePolygonPosition();
		
		this._viewSettingsPanel.invalidateViewOptions();
		this._settingPanel.invalidateViewOptions();
		this._selectionPanel.invalidateViewOptions();
	}
	
}
