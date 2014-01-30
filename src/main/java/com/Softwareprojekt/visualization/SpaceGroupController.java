package com.Softwareprojekt.visualization;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import com.Softwareprojekt.Utilities.ImmutableMesh;

import com.Softwareprojekt.Utilities.*;
import com.Softwareprojekt.interfaces.*;

public class SpaceGroupController implements Controller {

	private final Model _model;
	private final View _view;
	private final EnumSet<ViewOptions> _options = EnumSet.of(ViewOptions.ShowVertices, ViewOptions.ShowWireframe, ViewOptions.ShowFaces);
	private VisualizationSteps _step = VisualizationSteps.VoronoiTesselation;

	/**
	 * Factory method to create controller.
	 * @return
	 */
	public static Controller createController() {
		final SpaceGroupModel model = new SpaceGroupModel();
     	return new SpaceGroupController(model);
	}
	
	/**
	 * Constructor of space group controller.
	 * @param model
	 */
	private SpaceGroupController(Model model) {
		this._model = model;
		this._view = new SpaceGroupView(this);
		this._view.invalidateView();
		this._view.invalidateViewOptions();
	}
	
	@Override
	public void setVisualizationStep(VisualizationSteps step) {
		if (step != this._step) {
			this._step = step;
			this._view.invalidateView();
		}
	}

	@Override
	public VisualizationSteps getVisualizationStep() { return this._step; }
	
	@Override
	public Vector3D getOriginPoint() {
		return this._model.getPoint();
	}

	@Override
	public void setOriginPoint(Vector3D point) {
		this._model.setPoint(point);
		this._view.invalidateView();
	}
	
	/**
	 * Returns true if the specified view option is turned on otherwise false..
	 */
	public boolean getViewOption(ViewOptions option) { return this._options.contains(option); }
	
	/**
	 * Enables or disables the specified view option.
	 */
	public void setViewOption(ViewOptions option, boolean value) {
		if (value)
			this._options.add(option);
		else
			this._options.remove(option);
		this._view.invalidateViewOptions();
	}
	
	@Override
	public Model getModel() { return this._model; }

	@Override
	public View getView() { return this._view; }
	
	/**
	 * Calculates the mesh according visualization step.
	 * @param
	 * @return
	 */
	@Override
	public List<Mesh> calculateMesh() {
		List mesh = new LinkedList();
        Mesh qMesh;
		// generate points
		PointList p = new PointList();
		p.gen_randomPoints(20);

        //p.add(this.getOriginPoint());
		

		// iterate over transformation set
		/*Iterator<Transformation> iter = this._model.getSpaceGroup().getTransformations().iterator();
		while(iter.hasNext()) {
			Transformation transform = iter.next();
			p.add(transform.apply(this._model.getPoint()));
		}*/
		
		// trigger qhull wrapper according current viz step
		switch (this.getVisualizationStep()) {
            case ScatterPlot:
                mesh.add(ConvertHelper.convertPointListToMesh(p));
                break;
		    case ConvexHull:
                mesh.add(QConvex.call(p));
		    	break;
		    case DelaunayTriangulation:
                qMesh = QDelaunay.call(p);
                for (Polygon poly : qMesh.getFaces()) {
                    PointList cellPoints = new PointList();
                    cellPoints.addAll(poly.getVertices());
                    mesh.add(QConvex.call(cellPoints));
                }
		    	break;
		    case VoronoiTesselation:
                qMesh = QVoronoi.call(p);
                qMesh = removeVerticeFromMesh(qMesh.getVertices().get(0), qMesh);
                for (Polygon poly : qMesh.getFaces()) {
                    PointList cellPoints = new PointList();
                    cellPoints.addAll(poly.getVertices());
                    mesh.add(QConvex.call(cellPoints));
                }
		    	break;
	    }
		
		return mesh;
	}
	
	/**
	 * Removes the specified vertex from mesh.
	 * @param point
	 * @param mesh
	 */
	private static Mesh removeVerticeFromMesh(Vector3D point, Mesh mesh) {
		List<Vector3D> vertices = new LinkedList<Vector3D>(mesh.getVertices());
		List<Polygon> polys = new LinkedList<Polygon>();
		
		if (vertices.remove(point)) {
			// find polys containing the specified point
			for (Polygon poly : mesh.getFaces()) {
				if (!poly.getVertices().contains(point)) {
					polys.add(poly);
				}
			}		
		}

		return new ImmutableMesh(vertices, polys);
	}
}
