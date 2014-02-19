package com.Softwareprojekt.interfaces;

import java.util.List;

public interface Controller<I extends SpaceGroupID> {

    Model getModel();
	View getView();

	Vector3D getOriginPoint();
	void setOriginPoint(Vector3D point);

    void setSpaceGroup(SpaceGroup spaceGroup);
    void setSpaceGroup(I id);
    SpaceGroup getSpaceGroup();
    I getSpaceGroupID();

	void setViewOption(ViewOptions option, boolean value);
	boolean getViewOption(ViewOptions option);

	void setVisualizationStep(VisualizationSteps step);
	VisualizationSteps getVisualizationStep();

	List<Mesh> calculateMesh();
	
	public enum ViewOptions {
        ShowVertices, ShowWireframe, ShowFaces, ShowSpacing,
        ShowAxeBox, ShowLabeledMeshes, ShowUnifiedCells
    };
	public enum VisualizationSteps { ScatterPlot, ConvexHull, DelaunayTriangulation, VoronoiTesselation };
}
