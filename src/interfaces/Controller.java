package interfaces;

import java.util.EnumSet;

public interface Controller {
	Model getModel();
	View getView();

	Vector3D getOriginPoint();
	void setOriginPoint(Vector3D point);	
	void setViewOption(ViewOptions option, boolean value);
	boolean getViewOption(ViewOptions option);
	void setVisualizationStep(VisualizationSteps step);
	VisualizationSteps getVisualizationStep();
	Mesh calculateMesh();
	
	public enum ViewOptions { ShowVertices, ShowWireframe, ShowFaces, ShowSpacing, ShowChromaticFaces };
	public enum VisualizationSteps { ConvexHull, DelaunayTriangulation, VoronoiTesselation };
}
