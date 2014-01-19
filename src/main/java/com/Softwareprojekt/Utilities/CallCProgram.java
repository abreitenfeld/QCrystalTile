package com.Softwareprojekt.Utilities;

import org.la4j.vector.dense.BasicVector;

import java.io.*;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: jakob
 * Date: 11/14/13
 * Time: 2:23 PM
 * To change this template use File | Settings | File Templates.
 */


abstract public class CallCProgram {

    protected String execPath;
    protected static String fileName="points.off";

    protected static void preparePointsFile(PointList points){
        try{
            //Open the file on disk
            PrintWriter out = new PrintWriter(new FileWriter(fileName));
            //Write the file on disk
            //Write qconvex header
            out.println("3 rbox D3 "+points.size());
            out.println(points.size());

            for(BasicVector p:points){
                out.println(p.get(0)+" "+p.get(1)+" "+p.get(2));
                System.out.println(p.get(0)+" "+p.get(1)+" "+p.get(2));
            }
            out.close();
        } catch (IOException e) {e.printStackTrace();}
    }

    protected static Integer[] String2Ints(String input){

        String[] strArray = input.split(" ");
        Integer[] intArray = new Integer[strArray.length-1];
        for(int i = 0; i < strArray.length-1; i++) {
            intArray[i] = Integer.valueOf(strArray[i+1]);
        }
        return intArray;
    }
    protected static double[] String2Double(String input){
        input=" "+input;
        String[] strArray = input.split("\\s+");
        double[] doubleArray = new double[strArray.length-1];
        for(int i = 1; i < strArray.length; i++) {
            doubleArray[i-1] = Double.parseDouble(strArray[i]);
        }
        return doubleArray;
    }

}

