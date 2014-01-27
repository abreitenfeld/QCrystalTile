package loader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LoaderTest {

	/**
	 * @param args
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		JSONParser parser=new JSONParser();
		JSONArray spacegroups  = (JSONArray) parser.parse(new FileReader("src/loader/SpaceGroups.txt"));
		for (int i=0;i<spacegroups.size();i++){
			   JSONObject elem=(JSONObject)spacegroups.get(i);
			   String name = (String)elem.get("SpaceGroupName");
			   if(name.equals("I4(1)32")){
				   
				   String transformations =(String)elem.get("Transformations");
				   String[] list = transformations.split(";");
				   int j=0;
				   for(String x: list){
					   j++;
					   System.out.println(x);
				   }
				   System.out.println("Anzahl Transformationen: "+j);
			   }
		}
	}

}
