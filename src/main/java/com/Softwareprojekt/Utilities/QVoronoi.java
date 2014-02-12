package com.Softwareprojekt.Utilities;

import com.Softwareprojekt.interfaces.Vector3D;

import java.io.*;
import java.util.LinkedList;

public class QVoronoi extends CallCProgram{

    protected static String execPath = "qvoronoi";
    final static boolean verbose = false;

    public static QMesh call(PointList points,String... args){

        LinkedList<Integer[]> indexs=new LinkedList<Integer[]>();
        PointList points_voro=new PointList();


        String[] cmd=new String[args.length+2];

        cmd[0]=execPath;
        for(int i=1;i<args.length+1;i++){
            cmd[i]=args[i-1];
        }
        cmd[args.length+1]="-o";
        try{
            ProcessBuilder b = new ProcessBuilder(cmd);
            Process p = b.start();

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader error_reader= new BufferedReader(new InputStreamReader(p.getErrorStream()));
            final PrintWriter q_stdin  =  new PrintWriter(p.getOutputStream(), true);

            q_stdin.print(points.convert_to_offString());
            q_stdin.close();

            String line;

            int line_counter = 1;
            int verts = 0;

            // Parse QVoronoi's Output to QMesh Objet
            while ((line = br.readLine()) != null) {
                if (verbose){
                    System.out.println(line);
                }
                if(line_counter==2){
                    String[] format=line.split("\\s+");
                    verts = Integer.parseInt(format[0]);
                }else if(line_counter>2 && line_counter<=verts+2){
                    double[] str2int=String2Double(line);
                    points_voro.add(new Vector3D(str2int));
                }else if(line_counter>verts+2){
                    Integer[] index = String2Ints(line);
                    // Killing random zeros line of qvoronoi
                    if (index.length > 1){
                        indexs.add(index);
                    }
                }
                line_counter++;
            }

            int error_line_counter = 0;
            while ((line=error_reader.readLine()) !=null){
                if (error_line_counter == 0){
                    System.out.println("**Errors**");
                    error_line_counter++;
                }
                System.out.println(line);
            }

        } catch (IOException e){}
        return new QMesh(points_voro,indexs);
    }
}
