package project.main;

/**
 * Created by tycho on 25/02/16.
 */
public class AngleRange {
	private double offsetAngle, width;

	public AngleRange(double angle, double v) {
		offsetAngle = angle;
		width = v;
	}

	public boolean containsAngle(double Angle) {
		Angle += 2 * Math.PI;
		Angle = Angle % (2 * Math.PI);
		return Angle > offsetAngle && Angle < offsetAngle + width;
	}
}
