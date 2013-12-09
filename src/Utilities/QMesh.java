package Utilities;

import il.ac.idc.jdt.Point;
import il.ac.idc.jdt.Triangle;
import interfaces.Mesh;
import interfaces.Polygon;
import interfaces.Vector3D;
import org.la4j.vector.dense.BasicVector;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jakob
 * Date: 11/15/13
 * Time: 4:25 PM
 * To change this template use File | Settings | File Templates.
 */

/* ###Description
    This Class offers:
    - 'faces_indexs' maps to 'pointlist'.Every Integer[] in 'faces_indexs' represents a Face (Triangle)
    -  'faces' contains the Triangles maped by 'faces_indexs'
 */

public class QMesh implements Mesh {

    private PointList pointlist=new PointList();
    private LinkedList<Integer[]> faces_index=new LinkedList<Integer[]>();
    private LinkedList<Polygon> faces=new LinkedList<Polygon>();

    public QMesh(PointList plist, LinkedList<Integer[]> indexs){
        this.pointlist=plist;
        this.faces_index=indexs;

        for(Integer[] i:faces_index){
            LinkedList<Vector3D> a=new LinkedList<Vector3D>();
            for(int j=0;j<i.length;j++){
                a.push(pointlist.get(i[j]));
            }
            faces.push(new ImmutablePolygon(a));
        }
    }

    public LinkedList<Integer[]> face_indexs() {
        return faces_index;
    }

    @Override
    public List<Vector3D> getVertices() {
        return pointlist;
    }

    @Override
    public List<Polygon> getFaces() {
        return faces;
    }
}


