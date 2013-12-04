package interfaces;

import java.util.EnumSet;

public interface Controller {
	Model getModel();
	View getView();
	
	public void setViewOption(ViewOptions option, boolean value);
	public boolean getViewOption(ViewOptions option);
	
	public enum ViewOptions { ShowVertices, ShowWireframe, ShowFaces }
}