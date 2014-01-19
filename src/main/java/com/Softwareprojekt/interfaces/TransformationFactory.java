package interfaces;

public interface TransformationFactory {
	
	Transformation identity();
	
	Transformation create(
			Matrix3D linearPart,
			Vector3D translation
		);
	Transformation fromLinearPart(Matrix3D linearPart);
	Transformation translation(double x, double y, double z);
	
	Transformation scale(double x, double y, double z);
	
	Transformation rotationX(double angle);
	Transformation rotationY(double angle);
	Transformation rotationZ(double angle);
	
	Transformation rotation(Vector3D axis, double angle);
}
