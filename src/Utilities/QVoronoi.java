package Utilities;

import interfaces.Vector3D;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class QVoronoi extends CallCProgram{

    protected static String execPath = "qvoronoi";
    protected static String fileName="qVoronoi";

    public static QMesh call(PointList points,String[] args){

        LinkedList<Integer[]> indexs=new LinkedList<Integer[]>();
        PointList points_voro=new PointList();
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
            String line;

            int line_counter=1;
            int verts=0;

            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if(line_counter==2){
                    String[] format=line.split(" ");
                    verts=Integer.parseInt(format[0]);
                }else if(line_counter>2 && line_counter<=verts+2){
                    double[] str2int=String2Double(line);
                    points_voro.add(new Vector3D(str2int));
                }else if(line_counter>verts+2){
                    Integer[] index = String2Ints(line);
                    indexs.add(index);
                }
                line_counter++;
            }
            File points_file = new File(fileName);
            boolean success = points_file.delete();
            if (!success)
                System.err.println("");
        } catch (IOException e){}
        return new QMesh(points_voro,indexs);
    }
}
