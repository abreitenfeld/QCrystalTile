package visualization;

import java.util.EnumSet;
import interfaces.*;

public class SpaceGroupController implements Controller {

	private final Model _model;
	private final View _view;
	private final EnumSet<ViewOptions> _options = EnumSet.of(ViewOptions.ShowVertices, ViewOptions.ShowWireframe, ViewOptions.ShowFaces);
	
	/**
	 * Constructor.
	 * @param model
	 */
	public SpaceGroupController(Model model) {
		this._model = model;
		this._view = new SpaceGroupView(this);
		
		this._view.invalidateView();
	}

	public boolean getViewOption(ViewOptions option) {
		return this._options.contains(option);
	}
	
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
	public View getView() {
		return this._view;
	}
	
	public static void main(String[] args) throws Exception {
     	Controller controller = new SpaceGroupController(null);
     	controller.getView().show();
    }

}
