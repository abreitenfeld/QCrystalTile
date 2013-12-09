package Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: jakob
 * Date: 11/18/13
 * Time: 2:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class QDelaunay extends CallCProgram {

        protected static String execPath = "/usr/bin/qdelaunay";
        protected static String fileName="qDelaunay";

    public static QMesh call(PointList points){

        LinkedList<Integer[]> indexs=new LinkedList<Integer[]>();

        preparePointsFile(points);
        try{
            ProcessBuilder b = new ProcessBuilder(execPath, "-o","TI", "points.off");
            Process p=b.start();

            // Read the STDOUT and save it in a String
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;

            // Convert the InputStream in a String
            int line_counter=1;

            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if(line_counter>=points.size()+3){
                    //System.out.println(line);
                    Integer[] index = String2Ints(line);
                    indexs.add(index);
                }else{
                    line_counter++;
                }
            }

            File points_file = new File(fileName);
            boolean success = points_file.delete();
            if (!success)
                System.err.println("");

        } catch (IOException e){}
        return new QMesh(points,indexs);
    }
}


