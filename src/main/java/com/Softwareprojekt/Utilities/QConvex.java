package com.Softwareprojekt.Utilities;

import java.io.*;
import java.util.LinkedList;

public class QConvex extends CallCProgram {

    protected static String execPath = "qconvex";
    final static boolean verbose = false;
    final static boolean verbose_error = false;

    public static QMesh call(PointList points,String... args)  throws QHullException {

        LinkedList<Integer[]> indexs=new LinkedList<Integer[]>();

        String[] cmd = new String[args.length+4];
        cmd[0] = execPath;
        for(int i = 1; i <= args.length; i++){
            cmd[i] = args[i-1];
        }
        cmd[args.length+1] = "-o";
        cmd[args.length+2] = "s";
        cmd[args.length+3] = "FA";

        double volume = 0;

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
            boolean gotVolume = false;
            String[] splitContainer = null;


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
                if (error_line_counter == 0 && verbose_error){
                    System.out.println("**Errors**");
                    error_line_counter++;
                }
                if (!gotVolume){
                    line = line.replaceAll(" ","");
                    if (line.contains(":")){
                        splitContainer =  line.split(":");
                        if( splitContainer[0].equals("Approximatevolume" ) || splitContainer[0].equals("Totalvolume")){
                            gotVolume = true;
                            volume = Double.parseDouble(splitContainer[1]);
                        }
                    }
                }
                if(verbose_error){
                    System.out.println(line);
                }
            }

        } catch (IOException e){
            throw new QHullException(execPath);
        }
        return new QMesh(points, indexs, volume);
    }
}
