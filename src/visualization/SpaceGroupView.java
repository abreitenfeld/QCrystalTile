package visualization;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Panel;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

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
import org.jzy3d.plot3d.primitives.AbstractDrawable;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.pickable.PickablePoint;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import file.ObjectFileFormatReader;

import interfaces.Controller;
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
	
	private enum DemoOFF {
		spiral,
		voro_01,
		voro_02
	}
	
	@Override
	public Model getModel() {
		return null;
	}

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
        this.add(this._settingPanel, BorderLayout.LINE_START);
        
        this._toolPanel = new SpaceGroupToolPanel(this._controller);
        this.add(this._toolPanel, BorderLayout.LINE_END);
        
		// set up default mouse controller
		ICameraMouseController mouse = ChartLauncher.configureControllers(_chart, "", true, true);
        ChartLauncher.instructions();
		super.initialize(_chart, Default_Size, "SpaceGroup Visualizer");
	}
	
	private void clearScene() {
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
		
		final InputStream inFile = SpaceGroupView.class.getResourceAsStream("/resources/" + DemoOFF.spiral + ".off");
		final ObjectFileFormatReader offReader = new ObjectFileFormatReader(inFile);
		final List<Polygon> polys = offReader.getPolygons();
		final List<Coord3d> vertices = offReader.getVertices();
		final List<AbstractDrawable> drawables = new LinkedList<AbstractDrawable>();

		this.clearScene();
		
		// add polygons
		for (Polygon poly : polys) {
			poly.setWireframeColor(Wireframe_Color);
			poly.setColor(Faces_Color);
			this._chartFaces.add(poly);
			drawables.add(poly);
		 }
	
		// add vertices
		for (Coord3d coord : vertices){
			Point vertice = new Point(coord, new Color(255,100,100), Sphere_Radius);
			vertice.setDisplayed(showVertices);
			this._chartVertices.add(vertice);
			drawables.add(vertice);
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
