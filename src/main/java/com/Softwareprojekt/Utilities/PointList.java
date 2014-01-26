package com.Softwareprojekt.Utilities;

import com.Softwareprojekt.interfaces.Vector3D;
import java.util.LinkedList;
import java.util.Random;

public class PointList extends LinkedList<Vector3D>{

    public void gen_randomPoints(int a){
        Random point_gen = new Random();
        double[] point;
        for(int i=0;i<a;i++){
            point= new double[]{point_gen.nextDouble(), point_gen.nextDouble(), point_gen.nextDouble()};
            this.push(new Vector3D(point));
        }
    }

    public String convert_to_offString(){
        String off_String = "3 rbox D3 " + this.size() + "\n" + this.size() + "\n";

        for(Vector3D p: this){
            off_String = off_String.concat(p.get(0) + " " + p.get(1) + " " + p.get(2) + "\n");
        }
        return off_String;
    }
    /*
     Demo SpaceGroup for testing the visualisation

    (+x, +y, +z);          (-x, -y, +z);
    (1/2-x, 1/2 +y, 1/2+z); (1/2+x, 1/2-y, 1/2+z);
    (-y, +x, +z);           (+y, -x, +z);
    (1/2+y, 1/2 +x, 1/2+z); (1/2-y, 1/2 -x, 1/2+z)

    public void gen_pseudoSpaceGroup(double[] p){
        // init unit box
        double x = p[0];
        double y = p[1];
        double z = p[2];

        this.push(new Vector3D(p));
        x = -x;
        y = -y;
        this.push(new Vector3D(new double[]{x, y, z}));
        x = 0.5 - x;
        y = 0.5 + y;
        z = 0.5 + z;
        this.push(new Vector3D(new double[]{x, y, z}));
        x = 0.5 + x;
        y = 0.5 - y;
        z = 0.5 + z;
        this.push(new Vector3D(new double[]{x, y, z}));
        x =-y;
        y = x;
        this.push(new Vector3D(new double[]{x, y, z}));
        x = +y;
        y = -x;
        this.push(new Vector3D(new double[]{x, y, z}));
        x = 0.5 + y;
        y = 0.5 + x;
        z = 0.5 + z;
        this.push(new Vector3D(new double[]{x, y, z}));
        x = 0.5 - x;
        y = 0.5 - y;
        z = 0.5 + z;
        this.push(new Vector3D(new double[]{x, y, z}));
        // duplicate and move boxes

        Vector3D translation = null;

        for (int j = 0; j<3 ; j++){
            PointList[] points = new PointList[3];
            for (int i = 0; i<3 ; i++){
                points[i] = (PointList) this.clone();
            }
            switch (j){
                case 0:
                    translation = new Vector3D(new double[] {1.0d, 0.0d, 0.0d });
                    break;
                case 1:
                    translation = new Vector3D(new double[] {0.0d, 1.0d, 0.0d });
                    break;
                default:
                    translation = new Vector3D(new double[] {0.0d, 0.0d, 1.0d });
            }
            PointList current = null;
            for (int i = 1; i<4 ; i++){
                current = points[i-1];
                translation = (Vector3D) translation.multiply(3.0);
                System.out.println(translation.toString());
                for(Vector3D point: current){
                    point.add(translation);
                    this.push(point);
                }
            }
        }
    }
    */
}
