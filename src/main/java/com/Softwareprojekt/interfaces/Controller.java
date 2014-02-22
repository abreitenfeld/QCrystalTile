package com.Softwareprojekt.interfaces;

import com.Softwareprojekt.Utilities.QHullException;

import java.util.List;

public interface Controller<I extends SpaceGroupID> {

    Model getModel();
	View getView();

    void configure(I id, Vector3D origin, Vector3D space);
    void configure(Vector3D origin, Vector3D space);

    I getSpaceGroupID();

	void setViewOption(ViewOptions option, boolean value);
	boolean getViewOption(ViewOptions option);

	void setVisualization(Visualization step);
	Visualization getVisualization();

	List<Mesh> calculateMesh() throws QHullException;
	
	public enum ViewOptions {
        ShowVertices, ShowWireframe, ShowFaces, ShowSpacing,
        ShowAxeBox, ShowLabeledMeshes, ShowUnifiedCells
    };
	public enum Visualization {
        ScatterPlot, ConvexHull
        , DelaunayTriangulation, VoronoiTesselation
    };
}
