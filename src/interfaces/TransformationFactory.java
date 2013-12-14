package interfaces;

public interface TransformationFactory {
	
	Transformation translation(double x, double y, double z);
	
	Transformation scale(double x, double y, double z);
	
	Transformation rotationX(double angle);
	Transformation rotationY(double angle);
	Transformation rotationZ(double angle);
	
	Transformation rotation(Vector3D axis, double angle);
}
