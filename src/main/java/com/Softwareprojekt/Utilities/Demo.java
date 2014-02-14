package com.Softwareprojekt.Utilities;

//import com.Softwareprojekt.interfaces.Vector3D;

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
        p.gen_randomPoints(7);
        /*
        p.push(new Vector3D(new double[]{1.0d,1.0d,7.0d}));
        p.push(new Vector3D(new double[]{2.0d,1.0d,1.0d}));
        p.push(new Vector3D(new double[]{2.0d,2.0d,2.0d}));
        p.push(new Vector3D(new double[]{1.0d,2.0d,4.0d}));
        p.push(new Vector3D(new double[]{1.0d,3.0d,5.0d}));
        */
        System.out.println(p.toString());
        try {
            QMesh m1= QConvex.call(p," ");
            System.out.println(m1.getFaces());

            QMesh m2= QDelaunay.call(p," ");
            System.out.println(m2.getFaces());

            QMesh m3=QVoronoi.call(p," ");
            System.out.println(m3.getVertices());
        }
        catch(QHullException e) {
            e.printStackTrace();
        }
    }
}
