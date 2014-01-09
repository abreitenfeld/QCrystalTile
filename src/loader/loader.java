package loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.LinkedList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import SpaceGroup.TransformationImpl;

public class loader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JSONArray list = new JSONArray();
		BufferedReader br;
		LinkedList <String> lines=new LinkedList<String>();
		try {
			br = new BufferedReader(new FileReader("src/loader/SpaceGroupTable.txt"));
			String str;		
			int linenum=0;
			while((str=br.readLine())!=null){
				linenum++;
				lines.add(clean(str));
			}
			System.out.println("lines: "+linenum);
			for (int i=0;i< 245;i++){lines.removeFirst();}
			int count=0;
			for(int j=0;j<lines.size();j++){
				if (lines.get(j).length()>20&&(lines.get(j).substring(0, 17).equals(" Space Group Name"))){
					count++;
					JSONObject obj = new JSONObject();
					System.out.println(lines.get(j));
					for(int k=0;k<6;k++){
						String[] spgitem=lines.get(j).split("=");
						obj.put(spgitem[0], spgitem[1]);
						j++;
					}
					String[] symline=lines.get(j).split("=");

					String transformations="";
					j++;
					while(symline[0].equals(" symmetry")){
						transformations=transformations+symline[1]+";";
						symline=lines.get(j).split("=");
						j++;
					}
					obj.put("Transformations", transformations);
					list.add(obj);
				}
			}
			FileWriter file = new FileWriter("src/loader/SpaceGroups.txt");
			file.write(list.toJSONString());
			file.flush();
			file.close();
			System.out.println(count);
				//if(line.substring(0, int endIndex);
		}catch(Exception e){e.printStackTrace();}
	}
	
	public static String clean(String line){
		return line.replaceAll("<[^>]*>", "");
		
	}
		
	
}