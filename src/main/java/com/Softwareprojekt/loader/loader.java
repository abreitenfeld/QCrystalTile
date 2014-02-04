package com.Softwareprojekt.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.LinkedList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Softwareprojekt.SpaceGroup.TransformationImpl;


public class loader {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JSONArray list = new JSONArray();
		BufferedReader br;
		LinkedList <String> lines=new LinkedList<String>();
		try {
			br = new BufferedReader(new FileReader("src/main/resources/SpaceGroupTable.txt"));
			String str;		
			int linenum=0;
			while((str=br.readLine())!=null){
				linenum++;
				lines.add(clean(str));
			}
			for (int i=0;i< 245;i++){lines.removeFirst();}
			for(int j=0;j<lines.size();j++){
				if (lines.get(j).length()>30&&(lines.get(j).substring(0, 30).equals(" Number of Symmetry Operators "))){
					JSONObject obj = new JSONObject();
					for(int k=0;k<7;k++){
						String[] spgitem=lines.get(j).split("=");
						obj.put(spgitem[0].replaceAll("\\p{Z}",""), spgitem[1].replaceAll("\\p{Z}",""));
						j++;
					}
					String[] symline=lines.get(j).split("=");
					String transformations="";
					j++;
					while(symline[0].equals(" symmetry")){
						transformations=transformations+symline[1].substring(1,symline[1].length()-1)+";";
						symline=lines.get(j).split("=");
						j++;
					}
					
					obj.put("Transformations", transformations);
					list.add(obj);
				}
			}
			FileWriter file = new FileWriter("src/main/resources/SpaceGroups.txt");
			file.write(list.toJSONString());
			file.flush();
			file.close();
		
		}catch(Exception e){e.printStackTrace();}
	}
	
	public static String clean(String line){
		return line.replaceAll("<[^>]*>", "");
		
	}
		
	
}
