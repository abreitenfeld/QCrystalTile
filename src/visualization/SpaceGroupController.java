package visualization;

import java.util.EnumSet;
import javax.swing.UIManager;
import Utilities.*;
import interfaces.*;

public class SpaceGroupController implements Controller {

	private final Model _model;
	private final View _view;
	private final EnumSet<ViewOptions> _options = EnumSet.of(ViewOptions.ShowVertices, ViewOptions.ShowWireframe, ViewOptions.ShowFaces);
	private Vector3D _originPoint = new Vector3D(new double[] {0, 0, 0});;
	private VisualizationSteps _step = VisualizationSteps.ConvexHull;
	
	/**
	 * Constructor of space group controller.
	 * @param model
	 */
	public SpaceGroupController(Model model) {
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
		return this._originPoint;
	}

	@Override
	public void setOriginPoint(Vector3D point) {
		this._originPoint = point;
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
	public Model getModel() { return null; }

	@Override
	public View getView() { return this._view; }
	
	/**
	 * Calculates the mesh according visualization step.
	 * @param p
	 * @return
	 */
	@Override
	public Mesh calculateMesh() {
		QMesh mesh = null;
		
		// generate points
		PointList p = new PointList();
		p.gen_randomPoints(10);
		/*int pCount = 5;
		float w = 5;
		for (int i = 0; i < pCount; i++) {
			for (int c = 0; c < pCount; c++) {
				for (int z = 0; z < pCount; z++) {
					p.add(new Vector3D(new double[] {i * w, c  * w, z * w}));
				}
			}	
		}*/
		
		// trigger qhull wrapper according current viz step
		switch (this.getVisualizationStep()) {
		    case ConvexHull:
		    	mesh = QConvex.call(p);
		    	break;
		    case DelaunayTriangulation:
		    	mesh = QDelaunay.call(p);
		    	break;
		    case VoronoiTesselation:
		    	mesh = QVoronoi.call(p);
		    	break;
	    }
		
		return mesh;
	}
	
	public static void main(String[] args) throws Exception {
		//UIManager.setLookAndFeel("javax.swing.plaf.synth.SynthLookAndFeel");
     	final Controller controller = new SpaceGroupController(null);
     	controller.getView().show();
    }
	
}
