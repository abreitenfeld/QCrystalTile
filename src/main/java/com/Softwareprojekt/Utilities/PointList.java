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
}
