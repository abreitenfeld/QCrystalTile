package interfaces;

import java.util.EnumSet;

public interface Controller {
	Model getModel();
	View getView();

	public Vector3D getOriginPoint();
	public void setOriginPoint(Vector3D point);	

	public void setViewOption(ViewOptions option, boolean value);
	public boolean getViewOption(ViewOptions option);
	
	public enum ViewOptions { ShowVertices, ShowWireframe, ShowFaces }

}
