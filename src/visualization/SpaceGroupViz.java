package visualization;

import java.util.*;

import javax.media.nativewindow.util.Dimension;

import quickhull3d.Point3d;
import quickhull3d.QuickHull3D;

import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.events.IViewPointChangedListener;
import org.jzy3d.events.ViewPointChangedEvent;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Rectangle;
import org.jzy3d.maths.algorithms.interpolation.algorithms.Spline3D;
import org.jzy3d.plot3d.builder.delaunay.JDTConverter;
import org.jzy3d.plot3d.primitives.*;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.tooltips.Tooltip;

import il.ac.idc.jdt.*;

public class SpaceGroupViz extends AbstractAnalysis {

   public static void main(String[] args) throws Exception {
        AnalysisLauncher.open(new SpaceGroupViz());
    }
	
	@Override
	public void init() throws Exception {
		// randomize points
		Point3d[] rndPts = new Point3d[20];
		Random rnd = new Random();
		for (int i = 0; i < 20; i++) {
			rndPts[i] = new Point3d(rnd.nextFloat() * 200, rnd.nextFloat() * 200, rnd.nextFloat() * 200);
		}
	
		chart = AWTChartComponentFactory.chart(Quality.Nicest, getCanvasType());
		// listener to click
		/*chart.getView().addViewPointChangedListener(new IViewPointChangedListener() {
			@Override
			public void viewPointChanged(ViewPointChangedEvent e) {
				System.out.println("sdfdsf");
			}
		});*/
		
		
		// add vertices
		for(Point3d pt : rndPts){
			chart.getScene().add(
				new Point(new Coord3d(pt.x, pt.y, pt.z), new Color(255,100,100), 20)
			);
		}
		
		final QuickHull3D hull = new QuickHull3D(rndPts);
		
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
						
			final LineStrip line = new LineStrip(list);
			line.setWidth(3);
			line.setWireframeColor(Color.BLUE);
			chart.getScene().add(line);
			
			poly.setColor(new Color(135, 206, 235, 70));
			chart.getScene().add(poly);
		 }
		

		
	
	}
	
}
