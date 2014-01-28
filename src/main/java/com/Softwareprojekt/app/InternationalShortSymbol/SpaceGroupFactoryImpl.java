package com.Softwareprojekt.app.InternationalShortSymbol;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.Softwareprojekt.app.SpaceGroup.LatticeTypeImpl;
import com.Softwareprojekt.app.SpaceGroup.SpaceGroupImpl;
import com.Softwareprojekt.app.SpaceGroup.TransformationImpl;
import com.Softwareprojekt.app.interfaces.InvalidSpaceGroupIDException;
import com.Softwareprojekt.app.interfaces.LatticeType;
import com.Softwareprojekt.app.interfaces.Matrix4D;
import com.Softwareprojekt.app.interfaces.SpaceGroup;
import com.Softwareprojekt.app.interfaces.SpaceGroupFactory;
import com.Softwareprojekt.app.interfaces.Transformation;



public class SpaceGroupFactoryImpl implements SpaceGroupFactory<ID> {
	JSONParser parser;
	JSONArray spacegroups;
	

	public SpaceGroupFactoryImpl() throws FileNotFoundException, IOException, ParseException {
		parser=new JSONParser();
		spacegroups  = (JSONArray) parser.parse(new FileReader("src/loader/SpaceGroups.txt"));
		return;
	}
	

	public SpaceGroup createSpaceGroup(ID key)throws InvalidSpaceGroupIDException{
		Set<Transformation>transformations= new HashSet<Transformation>();
		int index=-1;
		
			try {
				if (( index=find(key.stringRepr()))<0){throw new InvalidSpaceGroupIDException("wrong SpaceGroupID");}
			} catch (IOException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		JSONObject spgelem= (JSONObject)spacegroups.get(index);					// gesuchte Spacegroup
		String[] transformlist= ((String)spgelem.get("Transformations")).split(";");
	
		//Transformations werden geparst
		for ( String transformation: transformlist){
				Transformation t=parseTransform(transformation);
				transformations.add(t);
			}
		
		LatticeType lattice= new LatticeTypeImpl(LatticeType.CenteringType.valueOf((String)spgelem.get("LatticeType")),LatticeType.System.valueOf((String)spgelem.get("CrystalSystem")));;
		SpaceGroup ret= new SpaceGroupImpl(lattice,transformations);
		return ret;
}


	// bereitet Parsen vor
	private Transformation parseTransform(String transformation){
		transformation.replaceAll("\\/", "/");
		String[] xyz= transformation.split(",");

		Transformation tMatr=new TransformationImpl(new Matrix4D(
				new double[][] {
						
						transform(xyz[0]),
						
						transform(xyz[1]),
						
						transform(xyz[2]),
						{0,0,0,0}
				}));
		return tMatr;
		}

	//durchsucht das JSONArray nach der ges. Raumgruppe	
	private int find(String key) throws FileNotFoundException, IOException, ParseException{
		for (int i=0;i<spacegroups.size();i++){
		   JSONObject elem=(JSONObject)spacegroups.get(i);
		   String name = (String)elem.get("SpaceGroupName");
		   if(name.equals(key)){
					return i;
					
		   }
		}
	return -1;
	}
	
	
	//wandelt String-Schreibweise in Zeilen-Vektoren 
	private  double[] transform(String line) {
		double[]ret= {0,0,0,0};
		int i=0;
		int j=0;
		boolean negative=false;
		while(i<line.length()){
			System.out.println("check start: "+line.substring(i,i+1));
			if (line.substring(i,i+1).matches("[+,-]")){
				System.out.println("False");
				if (line.charAt(i)=='-'){
					++i;
					if(line.substring(i,i+1).matches("[X,Y,Z]")){
						
						ret[getPos(line.charAt(i))]=-1;
						++i;
					}	
					else{negative=true;}
				}
				else {
					++i;
					if(line.substring(i,i+1).matches("[X,Y,Z]")){
						ret[getPos(line.charAt(i))]=1;
						++i;
					}
				}
			}
			else if (line.substring(i,i+1).matches("[0-9]")){
				System.out.println("Zahl");
				j=i+1;
				while(line.charAt(j)!='/'){
					j=j+1;
				}
				double zaehler=Double.valueOf(line.substring(i,j));
				System.out.println("Slash gefunden");
				j=j+1;
				int k=j;
				while(line.substring(k,k+1).matches("[0-9]")){
					k=k+1;
				}
				double nenner=Double.valueOf(line.substring(j,k));
				
				System.out.println("End of number");
				if (negative==false){
					ret[3]=zaehler/nenner;
					System.out.println(ret[3]);
				}else{
					ret[3]=Double.parseDouble("-"+line.substring(i,j));
					negative=false;
				}
				i=k;
			}
			else if(line.substring(i,i+1).matches("[X,Y,Z]")){
				System.out.println("xcheck");
				ret[getPos(line.charAt(i))]=1;
				++i;
				System.out.println("xfin");
			}
		}
		
		return ret;
		
	}
	
	private int getPos(char c){
		if (c=='X'){return 0;}
		else if (c=='Y'){return 1;}
		else{return 2;}
	}
	




}
