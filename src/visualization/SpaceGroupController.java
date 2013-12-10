package visualization;

import java.util.EnumSet;

import javax.swing.UIManager;

import Utilities.PointList;
import Utilities.QConvex;
import Utilities.QDelaunay;
import Utilities.QMesh;
import Utilities.QVoronoi;

import interfaces.*;

public class SpaceGroupController implements Controller {

	private final Model _model;
	private final View _view;
	private final EnumSet<ViewOptions> _options = EnumSet.of(ViewOptions.ShowVertices, ViewOptions.ShowWireframe, ViewOptions.ShowFaces);
	private Vector3D _originPoint = null;
	private VisualizationSteps _step = VisualizationSteps.ConvexHull;
	private final PointList p;
	
	/**
	 * Constructor.
	 * @param model
	 */
	public SpaceGroupController(Model model) {
		this._model = model;
		this._view = new SpaceGroupView(this);
		
		this._view.invalidateView();
		
		this.p = new PointList();
	    this.p.gen_randomPoints(20);
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
		return this._originPoint;
	}

	@Override
	public void setOriginPoint(Vector3D point) {
		this._originPoint = point;
		}
	
	public boolean getViewOption(ViewOptions option) { return this._options.contains(option); }
	
	public void setViewOption(ViewOptions option, boolean value) {
		if (value)
			this._options.add(option);
		else
			this._options.remove(option);
		this._view.invalidateViewOptions();
	}
	
	@Override
	public Model getModel() {
		return null;
	}

	@Override
	public View getView() { return this._view; }
	
	public static void main(String[] args) throws Exception {
		//UIManager.setLookAndFeel("javax.swing.plaf.synth.SynthLookAndFeel");
     	Controller controller = new SpaceGroupController(null);
     	controller.getView().show();
    }

	/**
	 * Calculates the mesh according visualization step.
	 * @param p
	 * @return
	 */
	public Mesh calculateMesh() {
		String[] qargs={" "};
		QMesh mesh = null;

		switch (this.getVisualizationStep()) {
		    case ConvexHull:
		    	mesh = QConvex.call(p, qargs);
		    	break;
		    case DelaunayTriangulation:
		    	mesh = QDelaunay.call(p,qargs);
		    	break;
		    case VoronoiTesselation:
		    	mesh = QVoronoi.call(p,qargs);
		    	break;
	    }
		return mesh;
	}
	
}
