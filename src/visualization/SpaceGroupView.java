package visualization;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Panel;
import java.io.InputStream;
import java.util.List;

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
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.pickable.PickablePoint;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import file.ObjectFileFormatReader;

import interfaces.Model;
import interfaces.View;

public class SpaceGroupView extends FrameAWT implements View {

	private final Chart _chart;
	private final SpaceGroupToolPanel _toolPanel;
	
	private static final float Sphere_Radius = 5f;
	private static final Color Faces_Color = new Color(135, 206, 235, 150);
	private static final Rectangle DEFAULT_SIZE = new Rectangle(1024, 768);
	
	private enum DemoOFF {
		spiral,
		voro_01,
		voro_02
	}
	
	public static void main(String[] args) throws Exception {
     	SpaceGroupView view = new SpaceGroupView();
     	view.setVisible(true);
    }
	
	@Override
	public Model getModel() {
		return null;
	}

	public SpaceGroupView() {
		super();
		this.setLayout(new BorderLayout());

		_chart = AWTChartComponentFactory.chart(Quality.Nicest, IChartComponentFactory.Toolkit.awt);
        
		// add movable point
		/*final PickablePoint pivot = new PickablePoint(new Coord3d(0.1, 0.1, 0.1), Color.BLUE, 10);
		pivot.setDisplayed(true);
		pivot.setPickingId(1);
		 */
		
		// add components
        this._toolPanel = new SpaceGroupToolPanel();
        this.add(this._toolPanel, BorderLayout.LINE_START);
		
		final InputStream inFile = SpaceGroupViz.class.getResourceAsStream("/resources/" + DemoOFF.voro_02 + ".off");
		final ObjectFileFormatReader offReader = new ObjectFileFormatReader(inFile);
		final List<Polygon> polys = offReader.getPolygons();
		final List<Coord3d> vertices = offReader.getVertices();
		
		// add polygons to chart
		for (Polygon poly : polys) {
			poly.setWireframeColor(Color.GRAY);
			poly.setColor(Faces_Color);
			_chart.getScene().add(poly);
		 }
	
		// add vertices
		for(Coord3d coord : vertices){
			Point vertPt = new Point(coord, new Color(255,100,100), Sphere_Radius);
			_chart.getScene().add(vertPt);
		}
		
		// set up default mouse controller
		ICameraMouseController mouse = ChartLauncher.configureControllers(_chart, "", true, true);
        ChartLauncher.instructions();
		super.initialize(_chart, DEFAULT_SIZE, "SpaceGroup Visualizer");
	}
	
}
