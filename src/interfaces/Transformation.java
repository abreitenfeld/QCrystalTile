package interfaces;

public interface Transformation {
	Matrix3D linearPart();
	Vector3D translationPart();
	
	// translation and linear part as one 4D square-matrix:
	Matrix4D getAsHomogeneous();
	
	// return a new Transformation that represents the operation of applying this after b
	Transformation composition(Transformation b);
	
	Vector3D apply(Vector3D point);
	
	// returns the factory, this Transformation was created by:
	TransformationFactory getFactory();
}