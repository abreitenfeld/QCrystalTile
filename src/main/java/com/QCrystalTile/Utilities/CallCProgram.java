package com.QCrystalTile.utilities;

abstract public class CallCProgram {

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

