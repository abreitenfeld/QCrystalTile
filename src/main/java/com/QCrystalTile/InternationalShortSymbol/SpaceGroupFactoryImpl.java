package com.QCrystalTile.internationalshortsymbol;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.QCrystalTile.spacegroup.LatticeTypeImpl;
import com.QCrystalTile.spacegroup.SpaceGroupImpl;
import com.QCrystalTile.spacegroup.TransformationImpl;
import com.QCrystalTile.interfaces.InvalidSpaceGroupIDException;
import com.QCrystalTile.interfaces.LatticeType;
import com.QCrystalTile.interfaces.Matrix4D;
import com.QCrystalTile.interfaces.SpaceGroup;
import com.QCrystalTile.interfaces.SpaceGroupFactory;
import com.QCrystalTile.interfaces.Transformation;



public class SpaceGroupFactoryImpl implements SpaceGroupFactory<ID> {
	JSONParser parser;
	JSONArray spacegroups;
	

    public SpaceGroupFactoryImpl() throws FileNotFoundException, IOException, ParseException {
        parser=new JSONParser();
        final InputStreamReader reader = new InputStreamReader(ClassLoader.getSystemResource("SpaceGroups.json").openStream());
        spacegroups  = (JSONArray) parser.parse(reader);
        reader.close();
        return;
    }

	public SpaceGroup createSpaceGroup(ID key)throws InvalidSpaceGroupIDException{
		Set<Transformation>transformations= new HashSet<Transformation>();
		int index=-1;
		
			try {
				if (( index=find(key.stringRepr()))<0){throw new InvalidSpaceGroupIDException("wrong SpaceGroupID");}
			} catch (IOException | ParseException e) {
				throw new RuntimeException( "error while finding Spacegroup: " + e.getMessage() );

			}
	
		JSONObject spgelem= (JSONObject)spacegroups.get(index);					// gesuchte Spacegroup
		String[] transformlist= ((String)spgelem.get("Transformations")).split(";");
	
		//Transformations werden geparst
		for ( String transformation: transformlist){
				Transformation t=parseTransform(transformation);
				transformations.add(t);
			}
		LatticeType lattice = null;
		{
			String systemStr = (String )spgelem.get( "CrystalSystem" );
			String centeringTypeStr = (String )spgelem.get( "LatticeType" );
			LatticeType.System system = null;
			LatticeType.CenteringType centeringType = null;
			try {
				system = LatticeType.System.valueOf( systemStr );
				centeringType = LatticeType.CenteringType.valueOf( centeringTypeStr );
			}
			catch( Exception e ) {
				throw new RuntimeException( "error while parsing lattice: " + e.getMessage() );
			}
			lattice = new LatticeTypeImpl( centeringType, system );
		}
		//LatticeType lattice = new LatticeTypeImpl(LatticeType.CenteringType.valueOf((String)spgelem.get("LatticeType")),LatticeType.System.valueOf((String)spgelem.get("CrystalSystem")));;
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
			if (line.substring(i,i+1).matches("[+,-]")){
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
					else if (line.substring(i,i+1).matches("[0-9]")){
						j=i+1;
						while(line.charAt(j)!='/'){
							j=j+1;
						}
						double zaehler=Double.valueOf(line.substring(i,j));
						j=j+1;
						int k=j;
						while(k<line.length()&&line.substring(k,k+1).matches("[0-9]")){
							
							k=k+1;
						}
						double nenner=Double.valueOf(line.substring(j,k));
						
						if (negative==false){
							
							ret[3]=zaehler/nenner;
						}else{
							ret[3]=Double.parseDouble("-"+zaehler/nenner);
							negative=false;
						}
						i=k;
					}
				}
			}
			else if (line.substring(i,i+1).matches("[0-9]")){
				j=i+1;
				while(line.charAt(j)!='/'){
					j=j+1;
				}
				double zaehler=Double.valueOf(line.substring(i,j));
				j=j+1;
				int k=j;
				while(line.substring(k,k+1).matches("[0-9]")){
					k=k+1;
				}
				double nenner=Double.valueOf(line.substring(j,k));
				
				if (negative==false){
					ret[3]=zaehler/nenner;
				}else{
					ret[3]=Double.parseDouble("-"+line.substring(i,j));
					negative=false;
				}
				i=k;
			}
			else if(line.substring(i,i+1).matches("[X,Y,Z]")){
				ret[getPos(line.charAt(i))]=1;
				++i;
			}
		}
		return ret;
		
	}
	
	private int getPos(char c){
		if (c=='X'){return 0;}
		else if (c=='Y'){return 1;}
		else{return 2;}
	}
	
	public Set<ID> getIDbyCentering(LatticeType.CenteringType c) {
		String centering = String.valueOf(c);
		Set<ID> res= new HashSet<ID>();
		for (int i = 0; i < spacegroups.size(); i++) {
			JSONObject obj= (JSONObject)spacegroups.get(i);		
			if(obj.get("LatticeType").equals(centering)){try {
				res.add(new ID((String)obj.get("SpaceGroupName")));
			} catch (InvalidSpaceGroupIDException e) {
				throw new RuntimeException( "error while finding Spacegroup: " + e.getMessage() );

			}}
		}
		return res;
	}
	
	public Set<ID> getIDbySystem(LatticeType.System s){
		String system=String.valueOf(s);
		Set<ID> res= new HashSet<ID>();
		for (int i = 0; i < spacegroups.size(); i++) {
			JSONObject obj= (JSONObject)spacegroups.get(i);	
			if(obj.get("CrystalSystem").equals(system)){
				try {
					res.add(new ID((String)obj.get("SpaceGroupName")));
				} catch (InvalidSpaceGroupIDException e) {
					throw new RuntimeException( "error while finding Spacegroup: " + e.getMessage() );
				}}
		}
		return res;
	}
	




}
