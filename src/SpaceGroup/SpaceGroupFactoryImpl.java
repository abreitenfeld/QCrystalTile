package SpaceGroup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import interfaces.LatticeType;
import interfaces.Matrix4D;
import interfaces.InvalidSpaceGroupIDException;
import interfaces.SpaceGroup;
import interfaces.SpaceGroupFactory;
import interfaces.SpaceGroupID;
import interfaces.Transformation;

public class SpaceGroupFactoryImpl implements SpaceGroupFactory{
	

	public void SpaceGrouFactoryImpl() {
		return;
	}
	

	public SpaceGroup createSpaceGroup(SpaceGroupID key)throws InvalidSpaceGroupIDException{
		BufferedReader br;
		Set<Transformation>transformations= new HashSet<Transformation>();
		String latticename=null;
		String system=null;
		try {
			br = new BufferedReader(new FileReader("src/Spacegroup/SpaceGroups.txt"));
			String str;	
		
			while(!(str=br.readLine()).equals(key.stringRepr())&&str!=null){
			}
		
			if (str==null){throw new InvalidSpaceGroupIDException("wrong SpaceGroupID");}
			latticename=br.readLine();
			system=br.readLine();
			while(!(str=br.readLine()).equals("}")){
				transformations.add(parseTransform(str));
			}
		} catch (IOException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LatticeType lattice= new LatticeTypeImpl(latticename,LatticeType.CenteringType.valueOf((String.valueOf(key.stringRepr().charAt(0)))),LatticeType.System.valueOf(system));
		SpaceGroupImpl ret= new SpaceGroupImpl(lattice,transformations);
		return ret;
}
	
	
	private TransformationImpl parseTransform(String transformation){
		String[] xyz= transformation.split(",");
		TransformationImpl tMatr=new TransformationImpl(new Matrix4D(
				new double[][] {
						transform(xyz[0]),
						transform(xyz[1]),
						transform(xyz[2]),
						{0,0,0,0}
				}));
		
		return tMatr;
		}
	
	private  double[] transform(String line) {
		double[]ret= {0,0,0,0};
		int i=0;
		int j=0;
		boolean negative=false;
		while(i<line.length()){
			if (line.substring(i,i+1).matches("[+,-]")){
				if (line.charAt(i)=='-'){
					++i;
					if(line.substring(i,i+1).matches("[x,y,z]")){
						
						ret[getPos(line.charAt(i))]=-1;
						++i;
					}	
					else{negative=true;}
				}
				else {
					++i;
					if(line.substring(i,i+1).matches("[x,y,z]")){
						ret[getPos(line.charAt(i))]=1;
						++i;
					}
				}
			}
			else if (line.substring(i,i+1).matches("[0-9]")){
				j=i+1;
				while(line.charAt(j)!='.'){
					j=j+1;
				}
				j=j+1;
				while(line.substring(j,j+1).matches("[0-9]")){
					j=j+1;
				}
				if (negative==false){
					ret[3]=Double.parseDouble(line.substring(i,j));
				}else{
					ret[3]=Double.parseDouble("-"+line.substring(i,j));
					negative=false;
				}
				i=j;
			}
			else if(line.substring(i,i+1).matches("[x,y,z]")){
				ret[getPos(line.charAt(i))]=1;
				++i;
			}
		}
		
		return ret;
		
	}
	
	private int getPos(char c){
		if (c=='x'){return 0;}
		else if (c=='y'){return 1;}
		else{return 2;}
	}
	




}
