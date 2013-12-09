package Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class QConvex extends CallCProgram {

    protected static String execPath = "qconvex";
    protected static String fileName="qConvex";

    public static QMesh call(PointList points){

        LinkedList<Integer[]> indexs=new LinkedList<Integer[]>();

        preparePointsFile(points);

        try{
            ProcessBuilder b = new ProcessBuilder(execPath, "-o","TI", "points.off");
            Process qconvex=b.start();

            BufferedReader br = new BufferedReader(new InputStreamReader(qconvex.getInputStream()));
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

            File points_file = new File(fileName);
            boolean success = points_file.delete();
            if (!success)
                System.err.println("");
        } catch (IOException e){}
        return new QMesh(points,indexs);
    }
}
