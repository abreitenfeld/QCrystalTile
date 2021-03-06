package com.QCrystalTile.spacegroup;

import org.la4j.factory.CRSFactory;
import org.la4j.matrix.functor.MatrixFunction;
import org.la4j.vector.functor.VectorFunction;
import org.la4j.vector.Vector;
import org.la4j.vector.dense.BasicVector;

import com.QCrystalTile.interfaces.Matrix3D;
import com.QCrystalTile.interfaces.Matrix4D;
import com.QCrystalTile.interfaces.Transformation;
import com.QCrystalTile.interfaces.TransformationFactory;
import com.QCrystalTile.interfaces.Vector3D;



public class TransformationImpl implements Transformation {

	public TransformationImpl(Matrix3D linearPart, Vector3D translationPart) {
		init(linearPart, translationPart);
	}
	
	public TransformationImpl(Matrix4D homogeneousM) {
		Matrix3D linearPart = new Matrix3D( homogeneousM.slice(0,0,3,3) );

		Vector3D translationPart = new Vector3D( homogeneousM.getColumn(3).sliceLeft(3));
		init(linearPart, translationPart);
	}
	public TransformationImpl(
			Vector3D rot, // rotation (in Degrees) around x-, y-, z-axis (applies z after y after x)
			Vector3D translation
	) {
		this.isPureRot = true;
		this.rotVec = 
			new Vector3D( rot.transform( new VectorFunction() {
				public double evaluate(int i, double val) {
					return Math.toRadians(val);
				}
			}));

		this.translationPart = new Vector3D( translation );

		this.rasterize();
	}

	private void init(Matrix3D linearPart_, Vector3D translationPart) {
		Matrix3D linearPart = linearPart_;
		isPureRot = true;
		// 1. check if inproper rotation:
		double det = linearPart.determinant();
		if( det < -0.98 && det > -1.02) { // det == -1
			isPureRot = false;
			linearPart = new Matrix3D( linearPart.multiply(
					pointReflMatr
				));
		}
		else if( det < 1.02 && det > 0.98) {}
		//else if( det > -0.02 && det < 0.02 ) {}
		else throw new RuntimeException("det error: " + linearPart_.determinant());

		// 2. set fields:
		this.rotVec = calcRotations( linearPart );
		this.translationPart = translationPart;

		// 3. rasterize fields:
		this.rasterize();
	}

	private Vector3D calcRotations(Matrix3D linearPart) {
		// code from "Computing Euler angles from a rotation matrix" by Gregory G. Slabaugh:
		double rotX = 0, rotY = 0, rotZ = 0;
		double r31 = linearPart.get(2,0);
		if( r31 != 1 && r31 != -1 ) { // R31 != +-1
		//if( r31 < 0.98 || r31 > 1.02 ) { // != 1
			
			rotY = -Math.asin( r31 ); // rotY2 =  (pi - rotY)
			double cosRotY = Math.cos( rotY ); // cosRotY2 = cos( rotY2 )
			double r32 = linearPart.get(2,1),
			       r33 = linearPart.get(2,2);
			rotX = Math.atan2( r32/cosRotY, r33/cosRotY );
			// rotX2  = atan2( r32/cosRotY2, r33/cosRotY2 );
			
			double r21 = linearPart.get(1,0),
				r11 = linearPart.get(0,0);
			rotZ = Math.atan2( r21/cosRotY, r11/cosRotY );
			// rotZ2 = Math.atan2( r21/cosRotY2, r11/cosRotZ2 );
		}
		else {
			rotZ = 0; // anything, in fact!
			double r12 = linearPart.get(0,1),
				r13 = linearPart.get(0,2);
			if( r31 == -1 ) { 
			//if( r31 > -1.02 || r31 < -0.98 ) { // == -1
				rotY = Math.PI / 2;
				rotX = rotZ + Math.atan2( r12, r13 );
			}
			else { // r31 == 1
				rotY = -Math.PI / 2;
				rotX = -rotZ + Math.atan2( -r12, -r13 );
			}
		}
		return new Vector3D( new double[] { rotX, rotY, rotZ } );
	}

	private Matrix3D calcRotationMatr( Vector3D rotations ) {
		double rotX = rotations.get(0);
		double rotY = rotations.get(1) ;
		double rotZ = rotations.get(2) ;
		return new Matrix3D(
			// rotate around z-axis ...
			new Matrix3D( new double[][] {
				{ Math.cos(rotZ), -Math.sin(rotZ), 0 },
				{ Math.sin(rotZ), Math.cos(rotZ), 0 },
				{ 0, 0, 1 }
			}).multiply(
				// ... after rotating around y-axis...
				new Matrix3D( new double[][] {
					{Math.cos(rotY), 0, Math.sin(rotY)},
					{0, 1, 0},
					{-Math.sin(rotY), 0, Math.cos(rotY) }

				}).multiply(
					// ... after rotating around x-axis...
					new Matrix3D( new double[][] {
						{1, 0, 0},
						{0, Math.cos(rotX), -Math.sin(rotX)},
						{0, Math.sin(rotX), Math.cos(rotX)}
	
					})
				)
			)
		);
	}

	/*private Matrix3D calcScaleMatr( Vector3D scale ) {
		return new Matrix3D( new double[][] {
			{ scale.get(0), 0, 0 },
			{ 0, scale.get(1), 0 },
			{ 0, 0, scale.get(2) }
		});
	}*/

	@Override
	public Matrix3D linearPart() {
		Matrix3D ret = calcRotationMatr( rotVec );
		if( !isPureRot ) {
			ret = new Matrix3D(
				ret.multiply( pointReflMatr ));
		}
		return ret;
	}

	@Override
	public Vector3D translationPart() {
		return translationPart;
	}

	@Override
	public Matrix4D getAsHomogeneous() {
		Matrix3D linearPart = linearPart();
		CRSFactory factory = new CRSFactory();
		
		Matrix4D ret = new Matrix4D(factory.createIdentityMatrix(4));
		for( int i=0; i< 3; i++) {
			Vector row = new BasicVector(new double[] { linearPart.get(i, 0), linearPart.get(i, 1), linearPart.get(i, 2), 0}); //linearPart.getRow(i);
			ret.setRow(i, row);
		}
		ret.setColumn(3, factory.createVector(new double[] { translationPart.get(0), translationPart.get(1), translationPart.get(2), 1 }));
		return ret;
	}

	@Override
	public Transformation composition(Transformation b) {
		Matrix4D matr = new Matrix4D(this.getAsHomogeneous().multiply(b.getAsHomogeneous()));
		//System.out.println("res" + matr);
		TransformationImpl ret = new TransformationImpl(matr);
		//ret.rasterize();
		return ret;
	}
	
	private void rasterize() {
		// raster rotVec:
		for( int i=0; i<3; i++)
			rotVec.set(i, rasterRot(rotVec.get(i)));

		// raster translationPart:
		final int translationDivision = 12; // => raster = 1/12
		final double translationTolerance = 1/36; // has to be smaller than 1/translationDivision !
		VectorFunction translationFunc = new VectorFunction () {
			public double evaluate(int index, double val) {
				return Math.floor( val*translationDivision + translationTolerance) / translationDivision;
			}
		};
		this.translationPart.transform(translationFunc);

	}

	/*private double rasterDouble(int division, double val) {
		double tolerance = 1/division / 4;
		return Math.floor( val*division + tolerance ) / division;
	}*/

	private double rasterRot(double angle) {
		int division = 12; // => raster = 1/12 

		double tolerance = 1.0/36.0; // has to be smaller than 1/division !
		return (Math.floor( angle /(2*Math.PI) *division + tolerance )%division) / division * 2*Math.PI ;
	}
	
	@Override
	public Vector3D apply(Vector3D point) {
		return new Vector3D(
				this.getAsHomogeneous().multiply(point.getAsHomogeneous()).sliceLeft(3)
			);
	}
	@Override
	public TransformationFactory getFactory() {
		return factory;
	}
	
	// for debugging:
	public String getInternalRepr() {
		return "pure=" + isPureRot +
				", rotVec=" + rotVec +
				", translationPart= " + translationPart;
	}
	
	public static TransformationFactory factory = new TransformationFactoryImpl();
	
	// rotation around x-,y-,z-axis in radians
	// (z after y after x)
	private Vector3D rotVec ; 
	private boolean isPureRot;

	private Vector3D translationPart;

	private static Matrix3D pointReflMatr = new Matrix3D( new double[][] {
					{-1,0,0},
				    	{0,-1,0},
				    	{0,0,-1}
				});
	
	public boolean equals(Object other_) {
		if( other_ instanceof Transformation) {
			Transformation other = (Transformation )other_;
			return 
				this.getAsHomogeneous().equals(other.getAsHomogeneous());
		}
		return false;
	}
	public int hashCode() {
		Matrix4D matr = new Matrix4D(this.getAsHomogeneous().transform(roundToInt));
		return matr.hashCode();
	}
	
	private static MatrixFunction roundToInt= new MatrixFunction() {
		public double evaluate(int arg0, int arg1, double entry) {
			return new Double(entry + 0.005).intValue();
		}
	};
}
