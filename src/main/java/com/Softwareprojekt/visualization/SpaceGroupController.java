package com.Softwareprojekt.visualization;

import com.Softwareprojekt.InternationalShortSymbol.ID;
import com.Softwareprojekt.InternationalShortSymbol.SpaceGroupFactoryImpl;
import com.Softwareprojekt.Utilities.*;
import com.Softwareprojekt.common.UserPreferences;
import com.Softwareprojekt.interfaces.*;
import quickhull3d.Point3d;
import quickhull3d.QuickHull3D;

import java.util.*;

public class SpaceGroupController implements Controller<ID> {

    protected final Model _model;
    protected final View _view;
    protected  ID _currentSpaceGroupID;
    protected final EnumSet<ViewOptions> _options;
    protected Visualization _visualization = Visualization.VoronoiTesselation;
    protected final UserPreferences _prefs = new UserPreferences();

	/**
	 * Factory method to create controller.
	 * @return
	 */
	public static Controller<ID> createController() {
		final SpaceGroupModel model = new SpaceGroupModel();
     	return new SpaceGroupController(model);
	}
	
	/**
	 * Constructor of space group controller.
	 * @param model
	 */
	private SpaceGroupController(Model model) {
		this._model = model;
        // restore settings from user preferences
        this._visualization = this._prefs.getVisualization();
        this._options = this._prefs.getViewOptions();
        this._model.setPoint(this._prefs.getOriginPoint());
        this._model.setSpace(this._prefs.getSpace());
        // instantiate space group
        try {
            final SpaceGroupFactory<ID> factory = new SpaceGroupFactoryImpl();
            this._currentSpaceGroupID = this._prefs.getSpaceGroupID();
            this._model.setSpaceGroup(factory.createSpaceGroup(this._currentSpaceGroupID));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
		this._view = new SpaceGroupView(this);
		this._view.invalidateView();
	}
	
	@Override
	public void setVisualization(Visualization viz) {
		if (viz != this._visualization) {
			this._visualization = viz;
			this._view.invalidateView();
            this._prefs.setVisualization(viz);
		}
	}

	@Override
	public Visualization getVisualization() { return this._visualization; }

    @Override
    public ID getSpaceGroupID() {
        return this._currentSpaceGroupID;
    }

    /**
	 * Returns true if the specified view option is turned on otherwise false..
	 */
	public boolean getViewOption(ViewOptions option) { return this._options.contains(option); }
	
	/**
	 * Enables or disables the specified view option.
	 */
	public void setViewOption(ViewOptions option, boolean value) {
		// update enum set
        if (value)
			this._options.add(option);
		else
			this._options.remove(option);

        final boolean invalidateView;
        switch (option) {
            case ShowUnifiedCells: invalidateView = true; break;
            default: invalidateView = false;
        }

        // raise invalidation
        if (invalidateView) {
            this._view.invalidateView();
        }
        else {
            this._view.invalidateViewOptions();
        }
        this._prefs.setViewOption(this._options);
	}
	
	@Override
	public Model getModel() { return this._model; }

	@Override
	public View getView() { return this._view; }

    @Override
    public void configure(Vector3D origin, Vector3D space) {
        configure(this._currentSpaceGroupID, origin, space);
    }

    @Override
    public void configure(ID id, Vector3D origin, Vector3D space) {
        this._model.setPoint(origin);
        this._prefs.setOriginPoint(origin);

        this._model.setSpace(space);
        this._prefs.setSpace(space);

        if (!id.stringRepr().equals(this._currentSpaceGroupID.stringRepr())) {
            try {
                final SpaceGroupFactory<ID> factory = new SpaceGroupFactoryImpl();
                this._model.setSpaceGroup(factory.createSpaceGroup(id));
                this._currentSpaceGroupID = id;
                this._prefs.setSpaceGroupID(id);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        this._view.invalidateView();
    }

    /**
	 * Calculates the mesh according visualization step.
	 * @param
	 * @return
	 */
	@Override
	public List<Mesh> calculateMesh() throws QHullException {
		List<Mesh> meshes = new LinkedList<Mesh>();
        Mesh mesh;
		PointList p = this._model.getCalculatedPoints();

		// trigger qhull wrapper according current viz step
        switch (this.getVisualization()) {
            case ScatterPlot:
                meshes.add(MeshHelper.convertPointListToMesh(p));
                break;
            case ConvexHull:
                meshes.add(QConvex.call(p));
                break;
            case DelaunayTriangulation:
                mesh = QDelaunay.call(p);
                for (Polygon poly : mesh.getFaces()) {
                    PointList cellPoints = new PointList();
                    cellPoints.addAll(poly.getVertices());
                    meshes.add(QConvex.call(cellPoints));
                }
                break;
            case VoronoiTesselation:
                mesh = QVoronoi.call(p);
                if (!mesh.getVertices().isEmpty()) {
                    mesh = removeVertexFromMesh(mesh.getVertices().get(0), mesh);
                    for (Polygon poly : mesh.getFaces()) {
                        PointList cellPoints = new PointList();
                        cellPoints.addAll(poly.getVertices());
                        Mesh convexHull = createConvexHull(cellPoints);
                        if (!convexHull.getVertices().isEmpty()) {
                            meshes.add(convexHull);
                        }
                    }
                }
                meshes = filterAbnormalMeshes(meshes);
                break;
        }

		return meshes;
	}

    private static Mesh createConvexHull(List<Vector3D> vertices) {
        final QuickHull3D hull = new QuickHull3D();
        Point3d[] points = new Point3d[vertices.size()];

        for (int i = 0; i < points.length; i++) {
            points[i] = MeshHelper.convertVector3DToQPoint3d(vertices.get(i));
        }

        try {
            hull.build(points);
        }
        catch (IllegalArgumentException e) { }

        return MeshHelper.convertQuickHullToMesh(hull);
    }

	/**
	 * Removes the specified vertex from mesh.
	 * @param point
	 * @param mesh
	 */
	private static Mesh removeVertexFromMesh(Vector3D point, Mesh mesh) {
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

    private static List<Mesh> filterAbnormalMeshes(List<Mesh> meshes) {
        final List<Mesh> whiteList = new LinkedList<Mesh>();
        // calculate the center over all meshes
        org.la4j.vector.Vector globalCentroid = new Vector3D(new double[] {0, 0, 0});
        for (Mesh m : meshes) {
            globalCentroid = globalCentroid.add(m.getCentroid());
        }
        globalCentroid = globalCentroid.divide(meshes.size());

        // find the cell with the closest distance to center
        Mesh unitCell = null;
        double minDistance = Integer.MAX_VALUE;
        for (Mesh m : meshes) {
            double distance = MeshHelper.magnitude(m.getCentroid(), globalCentroid);
            if (distance < minDistance) {
                unitCell = m;
                minDistance = distance;
            }
        }

        // filter for cells equal to unit cell
        if (unitCell != null) {
            for (Mesh m : meshes) {
                if (MeshHelper.approximateEquality(m, unitCell)) {
                    whiteList.add(m);
                }
            }
        }
        return whiteList;
    }

    private static Mesh filterForMajorityCell(Mesh mesh){

        Map< Integer, LinkedList<Polygon>> vertCount = new HashMap<Integer,LinkedList<Polygon>>();
        Integer verts = 0;

        for(Polygon cell : mesh.getFaces()){
            verts = cell.getVertices().size();
            if (vertCount.containsKey(verts)) {
                vertCount.get(verts).add(cell);
            }else {
                LinkedList<Polygon> sameCell= new LinkedList<Polygon>();
                sameCell.add(cell);
                vertCount.put(verts,sameCell);
            }
        }
       // Find most common Cell
        Integer max = 0;
        Integer  cellCount;
        LinkedList<Polygon> mayorityGroup = new LinkedList<Polygon>();

        for (Map.Entry<Integer, LinkedList<Polygon>> entry : vertCount.entrySet()){
            cellCount = entry.getValue().size();
           if (cellCount.compareTo(max) > 0){
               max = cellCount;
               mayorityGroup = entry.getValue();
           }
        }
        return new ImmutableMesh(mesh.getVertices() ,mayorityGroup);
    }

    private static List<Mesh> filterByVolume(List<Mesh> mesh_list){
        Map< Double, LinkedList<Mesh>> volume_sort= new HashMap<Double ,LinkedList<Mesh>>();
        QMesh  current_mesh = null;
        Double volume = null;

        for (Mesh x : mesh_list){
            current_mesh = (QMesh) x;
            volume = current_mesh.getVolume();
            if (volume_sort.containsKey(volume)) {
                volume_sort.get(volume).add(x);
            }else {
                LinkedList<Mesh> sameCell = new LinkedList<Mesh>();
                sameCell.add(x);
                volume_sort.put(volume,sameCell);
            }
        }
        Integer max = 0;
        Integer  cellCount;
        LinkedList<Mesh> mayorityGroup = new LinkedList<Mesh>();

        for (Map.Entry<Double , LinkedList<Mesh>> entry : volume_sort.entrySet()){
            cellCount = entry.getValue().size();
            if (cellCount.compareTo(max) > 0){
                max = cellCount;
                mayorityGroup = entry.getValue();
            }
        }
        return mayorityGroup;
    }
}
