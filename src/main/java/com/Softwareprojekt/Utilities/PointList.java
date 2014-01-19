package com.Softwareprojekt.Utilities;

import com.Softwareprojekt.interfaces.Vector3D;
import java.util.LinkedList;
import java.util.Random;

public class PointList extends LinkedList<Vector3D>{
    private int dimension;

    public int getDimension(){
        return this.size();
    }

    public void gen_randomPoints(int a){
        Random point_gen = new Random();
        double[] point;
        for(int i=0;i<a;i++){
            point= new double[]{point_gen.nextDouble(), point_gen.nextDouble(), point_gen.nextDouble()};
            this.push(new Vector3D(point));
        }
    }
}
