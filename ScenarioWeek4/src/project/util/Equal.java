package project.util;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.List;

import project.raycasting.Raycast;

public abstract class Equal {

	public static final double EPSILON = 0.0000000001;
	
	public static boolean equal(double a, double b){
		return Math.abs(a - b) < EPSILON;
	}
	
	public static boolean contains(List<double[]> list, double[] c){
		for(double[] coord : list){
			if(equal(coord[0], c[0]) && equal(coord[1], c[1])){
				return true;
			}
		}
		return false;
	}
	
	public static boolean equal(Point2D p1, Point2D p2){
		return equal(p1.getX(), p2.getX()) && equal(p1.getY(), p2.getY());
	}
	
	public static boolean equal(Line2D line_1, Line2D line_2){
		return (equal(line_1.getP1(), line_2.getP1())
				&& equal(line_1.getP2(), line_2.getP2()))
				|| (equal(line_1.getP1(), line_2.getP2())
						&& equal(line_1.getP2(), line_2.getP1()));
	}
	
	public static boolean isVertex(Point2D point, List<Point2D> points){
		for(Point2D p : points){
			if(equal(p, point)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isEdge(Line2D line, List<Line2D> edges){
		for(Line2D edge : edges){
			if(equal(line, edge)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean contains(List<Point2D> list, Point2D point) {
		for (Point2D listPoint : list) {
			if (equal(listPoint, point)) {
				return true;
			}
		}
		return false;
	}
	
	// Java is lovely
	public static class EuclidianDistanceComparator implements Comparator<Point2D> {
		
		private Point2D guard;

		public EuclidianDistanceComparator(Point2D guard) {
			this.guard = guard;
		}

		@Override
		public int compare(Point2D p1, Point2D p2) {
			return Double.compare(guard.distance(p1), guard.distance(p2));
		}
	}
	
	public static class RadianComparator implements Comparator<Point2D>{

		private Point2D guard;
		
		public RadianComparator(Point2D guard){
			this.guard = guard;
		}
		
		@Override
		public int compare(Point2D p1, Point2D p2) {
			double v1 = Math.atan2(p1.getY()- guard.getY(), p1.getX() - guard.getX());
			double v2 = Math.atan2(p2.getY()- guard.getY(), p2.getX() - guard.getX());
			double dist1 = guard.distance(p1);
			double dist2 = guard.distance(p2);
			boolean angleEqual = Math.abs(v1 - v2) < EPSILON;
			boolean distEqual = Math.abs(dist1 - dist2) < EPSILON;
			if(angleEqual && distEqual){
				return 0;
			} else if(angleEqual){
				if(dist1 < dist2) return -1;
				if(dist1 > dist2) return 1;
				return 0;
			} else if(distEqual){
				if(v1 < v2) return -1;
				if(v1 > v2) return 1;
				return 0;
			} else{
				if(v1 < v2) return -1;
				if(v1 > v2) return 1;
				return 0;
			}
		}

	}
	
	
}
