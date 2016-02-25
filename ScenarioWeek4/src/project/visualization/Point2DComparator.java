package project.visualization;

import java.awt.geom.Point2D;
import java.util.Comparator;

public class Point2DComparator implements Comparator<Point2D>{

	private Point2D guard;
	
	public Point2DComparator(Point2D guard){
		this.guard = guard;
	}
	
	@Override
	public int compare(Point2D p1, Point2D p2) {
		double a = Math.atan2(guard.getX() - p1.getX(), guard.getY() - p1.getY());
		double b = Math.atan2(guard.getX() - p2.getX(), guard.getY() - p2.getY());
		return (a < b) ? -1 : (a > b) ? 1 : 0;
	}

}
