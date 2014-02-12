package com.Softwareprojekt.SpaceGroup;

import com.Softwareprojekt.interfaces.PointSetCreator;
import com.Softwareprojekt.interfaces.SpaceGroup;
import com.Softwareprojekt.interfaces.Vector3D;
import com.Softwareprojekt.interfaces.Transformation;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

public class PointSetCreatorImpl implements PointSetCreator {

	public PointSetCreatorImpl(
		Vector3D spaceToFill, //= new Vector3D( new double[] { 4, 4, 4 } )
		Vector3D patternIterations
	) {
		this.spaceToFill = spaceToFill;
		this.patternIterations = patternIterations;
	}

	/*@Override
	public SpaceGroup getSpaceGroup() {
		return null;
	}*/

	@Override
	public Vector3D getSpaceToFill() {
		return spaceToFill;
	}

	@Override
	public Vector3D getPatternIterations() {
		return patternIterations;
	}

	/*@Override
	public void setSpaceGroup() {

	}*/

	@Override
	public void setSpaceToFill(Vector3D space) {
		this.spaceToFill = space;
	}

	@Override
	public void setPatternIterations(Vector3D iterations) {
		this.patternIterations = iterations;
	}

	@Override
	public Set<Vector3D> get(SpaceGroup sg, Vector3D selectedPoint) {
		// 1. get transformations to fill one unit cell:
		final List<Vector3D> spaceToFill = new ArrayList<Vector3D>();
		spaceToFill.add(
			new Vector3D( new double[] { 1, 0, 0 }));
		spaceToFill.add(
			new Vector3D( new double[] { 0, 1, 0 }));
		spaceToFill.add(
			new Vector3D( new double[] { 0, 0, 1 }));

		Vector3D patternIterations = new Vector3D( new double[] { 1,1,1 } );
	
		Set<Transformation> transformations = sg.getTransformations(
			spaceToFill,
			patternIterations
		);

		// 2. apply transformations to the selected point, while
		// also applying translations to the result to make it
		// fill the "spaceToFill":

		Set<Vector3D> ret = new HashSet<Vector3D>();
		{
			int rangeX = (int )this.spaceToFill.get(0);
			int rangeY = (int )this.spaceToFill.get(1);
			int rangeZ = (int )this.spaceToFill.get(2);
			for( Transformation t : transformations ) {
				Vector3D point = t.apply( selectedPoint );
				for( int x=0; x<rangeX; x++)
					for( int y=0; y<rangeY; y++)
						for( int z=0; z<rangeZ; z++)
						{
							ret.add( new Vector3D(
								point.add(
									new Vector3D( new double[] { x, y, z })
								)
							));
						}
			}
		}
		return ret;
	}

	private Vector3D spaceToFill;
	private Vector3D patternIterations;
}
