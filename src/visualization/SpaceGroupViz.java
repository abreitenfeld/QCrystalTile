package visualization;

import java.io.*;
import java.net.URL;
import java.util.*;

import javax.media.nativewindow.util.Dimension;

import quickhull3d.*;

import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.events.IViewPointChangedListener;
import org.jzy3d.events.ViewPointChangedEvent;
import org.jzy3d.maths.*;
import org.jzy3d.maths.algorithms.interpolation.algorithms.Spline3D;
import org.jzy3d.plot3d.builder.delaunay.JDTConverter;
import org.jzy3d.plot3d.primitives.*;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.pickable.PickablePoint;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.lights.Light;
import org.jzy3d.plot3d.rendering.lights.LightSet;
import org.jzy3d.plot3d.rendering.tooltips.Tooltip;
import org.jzy3d.plot3d.rendering.view.modes.ViewPositionMode;

import file.ObjectFileFormatReader;

import il.ac.idc.jdt.*;

public class SpaceGroupViz extends AbstractAnalysis {

	private static final float Sphere_Radius = 5f;
	private static final Color Faces_Color = new Color(135, 206, 235, 150);
	
	private enum DemoOFF {
		spiral, voro_01, voro_02, voro_03
	}
	
	public static void main(String[] args) throws Exception {
        AnalysisLauncher.open(new SpaceGroupViz());
    }
	
	@Override
	public void init() throws Exception {
		
		chart = AWTChartComponentFactory.chart(Quality.Nicest, getCanvasType());

		//chart.setAxeDisplayed(false);
		//chart.setViewMode(ViewPositionMode.PROFILE);
		/*LightSet lightSet = new LightSet();
		Light light = new Light();
		light.setPosition(new Coord3d(10, 10, 0));
		light.setRepresentationDisplayed(true);
		light.setDiffuseColor(Color.RED);
		lightSet.add(light);
		this.getChart().getScene().setLightSet(lightSet);*/
		// listener to click
		/*chart.getView().addViewPointChangedListener(new IViewPointChangedListener() {
			@Override
			public void viewPointChanged(ViewPointChangedEvent e) {
				System.out.println("sdfdsf");
			}
		});*/
	
		final InputStream inFile = SpaceGroupViz.class.getResourceAsStream("/resources/" + DemoOFF.voro_02 + ".off");
		final ObjectFileFormatReader offReader = new ObjectFileFormatReader(inFile);
		final List<Polygon> polys = offReader.getPolygons();
		final List<Coord3d> vertices = offReader.getVertices();
		
		// add polygons to chart
		for (Polygon poly : polys) {
			poly.setWireframeColor(Color.GRAY);
			poly.setColor(Faces_Color);
			chart.getScene().add(poly);
		 }
	
		// add vertices
		for(Coord3d coord : vertices){
			Point vertPt = new Point(coord, new Color(255,100,100), Sphere_Radius);
			chart.getScene().add(vertPt);
		}
		
		/*final QuickHull3D hull = new QuickHull3D(rndPts);
		List<il.ac.idc.jdt.Point> jdtPts = new LinkedList<il.ac.idc.jdt.Point>();
		int[][] faceIndices = hull.getFaces(QuickHull3D.POINT_RELATIVE + QuickHull3D.CLOCKWISE);
		// iterate over faces
		for (int i = 0; i < faceIndices.length; i++) {
			final Polygon poly = new Polygon();
			List<Coord3d> list = new LinkedList<Coord3d>();
			
			for (int k = 0; k < faceIndices[i].length; k++) {
				int index = faceIndices[i][k];
				list.add(new Coord3d(rndPts[index].x, rndPts[index].y, rndPts[index].z));
				poly.add(new Point(list.get(list.size() - 1)));
				jdtPts.add(JDTConverter.toJdtPoint(list.get(list.size() - 1)));
			}
									
			poly.setColor(new Color(135, 206, 235, 70));
			chart.getScene().add(poly);
		 }
		*/
	}
	
}
