package com.Softwareprojekt.Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class QConvex extends CallCProgram {

    protected static String execPath = "qconvex";
    protected static String fileName="qConvex";

    public static QMesh call(PointList points,String... args){

        LinkedList<Integer[]> indexs=new LinkedList<Integer[]>();

        preparePointsFile(points);
        String[] cmd=new String[args.length+4];

        cmd[0]=execPath;
        for(int i=1;i<args.length+1;i++){
            cmd[i]=args[i-1];
        }
        cmd[args.length+1]="-o";
        cmd[args.length+2]="-TI";
        cmd[args.length+3]="points.off";

        try{
            ProcessBuilder b = new ProcessBuilder(cmd);
            Process qconvex=b.start();

            BufferedReader br = new BufferedReader(new InputStreamReader(qconvex.getInputStream()));
            BufferedReader error_reader= new BufferedReader(new InputStreamReader(qconvex.getErrorStream()));
            String line;
            int line_counter=1;

            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if(line_counter>=points.size()+3){
                    Integer[] index = String2Ints(line);
                    indexs.add(index);
                }else{
                    line_counter++;
                }
            }
            System.out.println("Error Log...");
            while ((line=error_reader.readLine()) !=null){
                System.out.println(line);
            }

            File points_file = new File(fileName);
            boolean success = points_file.delete();
            if (!success)
                System.err.println("");
        } catch (IOException e){}
        return new QMesh(points,indexs);
    }
}
