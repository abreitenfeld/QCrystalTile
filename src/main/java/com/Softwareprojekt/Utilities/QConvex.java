package com.Softwareprojekt.Utilities;

import java.io.*;
import java.util.LinkedList;

public class QConvex extends CallCProgram {

    protected static String execPath = "qconvex";
    final static boolean verbose = false;

    public static QMesh call(PointList points,String... args){

        LinkedList<Integer[]> indexs=new LinkedList<Integer[]>();

        String[] cmd = new String[args.length+2];
        cmd[0] = execPath;
        for(int i = 1; i <= args.length; i++){
            cmd[i] = args[i-1];
        }
        cmd[args.length+1] = "-o";

        try{
            ProcessBuilder b = new ProcessBuilder(cmd);
            Process qconvex = b.start();

            BufferedReader input_stream = new BufferedReader(new InputStreamReader(qconvex.getInputStream()));
            BufferedReader error_reader = new BufferedReader(new InputStreamReader(qconvex.getErrorStream()));
            final PrintWriter q_stdin  =  new PrintWriter(qconvex.getOutputStream(), true);

            q_stdin.print(points.convert_to_offString());
            q_stdin.close();

            String line;
            int line_counter = 1;

            while ((line = input_stream.readLine()) != null) {
                if( verbose){
                    System.out.println(line);
                }
                if(line_counter >= points.size() + 3){
                    Integer[] index = String2Ints(line);
                    indexs.add(index);
                }else{
                    line_counter++;
                }
            }

            int error_line_counter = 0;
            while ((line = error_reader.readLine()) != null){
               if (error_line_counter == 0){
                   System.out.println("**Errors**");
                   error_line_counter++;
               }
                System.out.println(line);
            }

   System.err.println("");
        } catch (IOException e){}
        return new QMesh(points,indexs);
    }
}
