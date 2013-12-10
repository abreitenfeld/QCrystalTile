package visualization;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Panel;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

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

import util.ConvertHelper;

import Utilities.*;

import interfaces.Controller;
import interfaces.Mesh;
import interfaces.Model;
import interfaces.View;

public class SpaceGroupView extends FrameAWT implements View {

	private final Controller _controller;
	private final Chart _chart;
	private final SpaceGroupToolPanel _toolPanel;
	private final SpaceGroupSettingsPanel _settingPanel;
	
	private final List<Point> _chartVertices = new LinkedList<Point>();
	private final List<Polygon> _chartFaces = new LinkedList<Polygon>();
	
	private final ResourceBundle bundle = ResourceBundle.getBundle("resources.Messages");
	
	private static final Color Wireframe_Color = Color.GRAY;
	private static final float Sphere_Radius = 5f;
	private static final Color Faces_Color = new Color(135, 206, 235, 150);
	private static final Rectangle Default_Size = new Rectangle(1024, 768);
		
	SpaceGroupView(Controller controller) {
		super();
		
		this._controller = controller;
		this.setLayout(new BorderLayout());
		
		_chart = AWTChartComponentFactory.chart(Quality.Nicest, IChartComponentFactory.Toolkit.awt);
        
		// add movable point
		/*final PickablePoint pivot = new PickablePoint(new Coord3d(0.1, 0.1, 0.1), Color.BLUE, 10);
		pivot.setDisplayed(true);
		pivot.setPickingId(1);*/
		 
		// add components
        this._settingPanel = new SpaceGroupSettingsPanel(this._controller);
        this.add(this._settingPanel, BorderLayout.PAGE_END);
        
        this._toolPanel = new SpaceGroupToolPanel(this._controller);
        this.add(this._toolPanel, BorderLayout.LINE_END);
        
		// set up default mouse controller
		ICameraMouseController mouse = ChartLauncher.configureControllers(_chart, "", true, true);
        ChartLauncher.instructions();
		super.initialize(_chart, Default_Size, "SpaceGroup Visualizer");
	}
	
	@Override
	public Model getModel() { return null; }
	
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
	}
	
	@Override
	public void invalidateView() {
		boolean showVertices = this._controller.getViewOption(Controller.ViewOptions.ShowVertices);
		boolean showFaces = this._controller.getViewOption(Controller.ViewOptions.ShowFaces);
		boolean showWireframe = this._controller.getViewOption(Controller.ViewOptions.ShowWireframe);
		
	    final Mesh m = this._controller.calculateMesh();
	    
	    final List<interfaces.Polygon> polys = m.getFaces();
		final List<interfaces.Vector3D> vertices = m.getVertices();
		final List<AbstractDrawable> drawables = new LinkedList<AbstractDrawable>();
		
		this.clearScene();

		// add polygons
		for (interfaces.Polygon poly : polys) {
			Polygon nPoly = ConvertHelper.convertPolygonToJzyPolygon(poly);
			nPoly.setWireframeColor(Wireframe_Color);
			nPoly.setColor(Faces_Color);
			nPoly.setWireframeDisplayed(showWireframe);
			nPoly.setFaceDisplayed(showFaces);
			this._chartFaces.add(nPoly);
			drawables.add(nPoly);
		 }
	
		// add vertices
		for (interfaces.Vector3D vertice : vertices){
			Coord3d coord = ConvertHelper.convertVector3dTojzyCoord3d(vertice);
			Point point = new Point(coord, new Color(255,100,100), Sphere_Radius);
			point.setDisplayed(showVertices);
			this._chartVertices.add(point);
			drawables.add(point);
		}
	
		this._chart.getScene().add(drawables);
	}

	@Override
	public void invalidateViewOptions() {
		boolean showVertices = this._controller.getViewOption(Controller.ViewOptions.ShowVertices);
		boolean showFaces = this._controller.getViewOption(Controller.ViewOptions.ShowFaces);
		boolean showWireframe = this._controller.getViewOption(Controller.ViewOptions.ShowWireframe);
		
		// update polygon visibility
		for (Polygon poly : this._chartFaces) {
			poly.setWireframeDisplayed(showWireframe);
			poly.setFaceDisplayed(showFaces);
		}
		
		// update vertices visibility
		for(Point vertice : this._chartVertices) {
			vertice.setDisplayed(showVertices);
		}
		
	}
	
}
