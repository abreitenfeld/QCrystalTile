package Utilities;

/**
 * Created with IntelliJ IDEA.
 * User: jakob
 * Date: 11/15/13
 * Time: 1:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class Demo {
    public static void main(String[] args){

        PointList p=new PointList();
        p.gen_randomPoints(8);
        System.out.println(p.toString());

        QMesh m1= (QMesh) QConvex.call(p);
        System.out.println(m1.getFaces());

        QMesh m2= (QMesh) QDelaunay.call(p);
        System.out.println(m2.getFaces());

        Utilities.QMesh m3= (QMesh) QVoronoi.call(p);
        System.out.println(m3.getVertices());
    }
}
