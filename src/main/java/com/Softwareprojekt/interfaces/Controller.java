package com.Softwareprojekt.interfaces;

import java.util.EnumSet;
import java.util.List;

public interface Controller {
	Model getModel();
	View getView();

	Vector3D getOriginPoint();
	void setOriginPoint(Vector3D point);	
	void setViewOption(ViewOptions option, boolean value);
	boolean getViewOption(ViewOptions option);
	void setVisualizationStep(VisualizationSteps step);
	VisualizationSteps getVisualizationStep();
	List<Mesh> calculateMesh();
	
	public enum ViewOptions { ShowVertices, ShowWireframe, ShowFaces, ShowSpacing, showAxeBox };
	public enum VisualizationSteps { ScatterPlot, ConvexHull, DelaunayTriangulation, VoronoiTesselation };
}
