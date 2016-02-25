package project.visualization;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;

import project.main.AngleRange;
import project.main.Main;
import project.main.MuseumPolygon;

public class Functions {

	public static final double EPSILON = 0.0000000001;
	
	public static int pointToPixel(double pixel, int scalar, double length){
		return (int) Math.round(pixel * (double) FPanel.scale + length);
	}
	
	public static Point2D lineIntersect(double x1, double y1, double x2, double y2, 
			double x3, double y3, double x4, double y4){
		double d = (x1-x2)*(y3-y4) - (y1-y2)*(x3-x4);
		if (d == 0) return null;
		    
		double xi = ((x3-x4)*(x1*y2-y1*x2)-(x1-x2)*(x3*y4-y3*x4))/d;
		double yi = ((y3-y4)*(x1*y2-y1*x2)-(y1-y2)*(x3*y4-y3*x4))/d;
		    		
		return new Point2D.Double(xi, yi);
	}
	
	public static Point2D lineIntersect(Line2D line_1, Line2D line_2){
		return lineIntersect(line_1.getX1(), line_1.getY1(), line_1.getX2(), line_1.getY2(),
								line_2.getX1(), line_2.getY1(), line_2.getX2(), line_2.getY2());
	}
/*
	public static boolean isOutsidePolygon(Point2D point, List<Point2D> points){
		double x1, x2;
		int crossings = 0;
		for(int i = 0; i < points.size(); i++){
			x2 = points.get(i).getX();
			x1 = points.get((i+1)%points.size()).getX();
			if(points.get(i).getX() < points.get((i+1)%points.size()).getX()){
				double temp = x1;
				x1 = x2;
				x2 = temp;
			}
			if(point.getX() > x1 && point.getX() <= x2 && (point.getY() < points.get(i).getY() || point.getY() <= points.get((i+1)%points.size()).getY())){
				double eps = 0.000001;
				double dx = points.get((i+1)%points.size()).getX() - points.get(i).getX();
				double dy = points.get((i+1)%points.size()).getY() - points.get(i).getY();
				double k;
				if(Math.abs(dx) < eps){
					k = Double.POSITIVE_INFINITY;
				} else{
					k = dy/dx;
				}
				double m = points.get(i).getY() - k * points.get(i).getX();
				double y2 = k * point.getX() + m;
				if(point.getY() <= y2){
					crossings++;
				}
			}
		}
		return !(crossings % 2 == 1);
	}
	*/

 	
	public static boolean lineBreaks(Line2D line_1, Line2D line_2){
		Point2D intersection;
		if((intersection = lineIntersect(line_1, line_2)) != null){
			if(!line_1.contains(intersection) && !line_2.contains(intersection)){
				return false;
			} else if(intersection.equals(line_1.getP1()) || intersection.equals(line_1.getP2())
					|| intersection.equals(line_2.getP1()) || intersection.equals(line_2.getP2())){
				return false;
			} else{
				return true;
			}
		}
		return false;
	}

	public static void draw(Graphics2D g2, Line2D line){
		g2.drawLine(Functions.pointToPixel(line.getX1(), 100, Main.DIMENSION.width/2),
					Functions.pointToPixel(line.getY1(), 100, Main.DIMENSION.height/2),
					Functions.pointToPixel(line.getX2(), 100, Main.DIMENSION.width/2),
					Functions.pointToPixel(line.getY2(), 100, Main.DIMENSION.height/2));
	}

	public static void draw(Graphics2D g2, Point2D point){
		g2.fillRoundRect(Functions.pointToPixel(point.getX(), 100, Main.DIMENSION.width/2)-5, 
				Functions.pointToPixel(point.getY(), 100, Main.DIMENSION.height/2)-5, 10, 10, 90, 90);
	}
	
	public static Point2D getIntersection(Line2D ray, Line2D edge){
		if(equal(ray.getP2(), edge.getP1()) || equal(ray.getP2(), edge.getP2())){
			return ray.getP2();
		}
		ParametricLine2D r = new ParametricLine2D.Double(ray);
		ParametricLine2D s = new ParametricLine2D.Double(edge);
		double r_mag = Math.sqrt(r.dx*r.dx+r.dy*r.dy);
		double s_mag = Math.sqrt(s.dx*s.dx+s.dy*s.dy);
		if(r.dx/r_mag == s.dx/s_mag && r.dy/r_mag == s.dy/s_mag){
			return null;
		}
		if((s.dx*r.dy - s.dy*r.dx) == 0 || r.dx == 0){
			return null;
		}
		double T2 = (r.dx*(s.py-r.py) + r.dy*(r.px-s.px))/(s.dx*r.dy - s.dy*r.dx);		
		double T1 = (s.px+s.dx*T2-r.px)/r.dx;
		if(T1 < 0) return null;
		if(T2 < 0 || T2 > 1) return null;		
		return new Point2D.Double(r.px+r.dx*T1, r.py+r.dy*T1);
	} 
	
	public static List<Point2D> getAllIntersections(Line2D ray, MuseumPolygon points){
		//(isOutsidePolygon(ray, points) && contains(points, ray.getP1())) return null;
		//if(isEdge(ray, getEdges(points))) return ray.getP2();
		List<Point2D> intersections = new ArrayList<>();
		List<Line2D> edges = points.getEdges();
		for(Line2D edge : edges){
			//if(equal(edge.getP1(), ray.getP1()) || equal(edge.getP2(), ray.getP1())) continue;
			Point2D intersection = getIntersection(ray, edge);
			//System.out.println("Intersection: " + intersection + " | Edge: " + edge.getP1() +"," + edge.getP2());
			if(intersection == null) continue;
			intersections.add(intersection);
		}
		return intersections;
	}
	public static List<Point2D> raycast(Point2D guard, Point2D point, MuseumPolygon polygon, int counter){
		//System.out.println("Recursion: " + counter);
		//System.out.println("Point: " + point.getX() + " " + point.getY());
		//System.out.println("Guard: " + guard.getX() + " " + guard.getY());
		List<Point2D> visibleIntersections = new ArrayList<Point2D>();
		Line2D ray = new Line2D.Double(guard, point);
		List<Point2D> intersections = getAllIntersections(ray, polygon);
		if(intersections == null) {
			//System.out.println("Intersection null");
			return visibleIntersections;
		}

		intersections.sort(new EuclidianDistanceComparitor(guard));

		double rayAngle = getLineAngle(guard, point);

		if(equal(intersections.get(0), guard)){
			//System.out.println("Intersection = Guard");
			// ToDo: Handle zero length intersections
			AngleRange homeIntersectionRange = polygon.getInsideRangeFromPoint(guard);
			if (!homeIntersectionRange.containsAngle(rayAngle)) {
				return visibleIntersections;
			}
		}
		int i = 0;
		do {
			visibleIntersections.add(intersections.get(i));
		} while (i + 1 < intersections.size() &&
				polygon.hasVertex(intersections.get(i)) &&
				polygon.getInsideRangeFromPoint(intersections.get(i)).containsAngle(rayAngle) &&
				++i > 0);
		return visibleIntersections;
	}

	public static double getLineAngle(Point2D guard, Point2D point) {
		return Math.atan2(guard.getX() - point.getX(), guard.getY() - point.getY());
	}

	public static double dist(Point2D p1, Point2D p2){
		return Math.sqrt((p1.getX()-p2.getX())*(p1.getX()-p2.getX())
				+ (p1.getY()-p2.getY())*(p1.getY()-p2.getY()));
				
	}

	public static boolean isVertex(Point2D point, List<Point2D> points){
		for(Point2D p : points){
			if(equal(p, point)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean equal(Point2D p1, Point2D p2){
		return Math.abs(p1.getX() - p2.getX()) < EPSILON
				&& Math.abs(p1.getY() - p2.getY()) < EPSILON;
	}
	
	public static boolean equal(Line2D line_1, Line2D line_2){
		return (equal(line_1.getP1(), line_2.getP1())
				&& equal(line_1.getP2(), line_2.getP2()))
				|| (equal(line_1.getP1(), line_2.getP2())
						&& equal(line_1.getP2(), line_2.getP1()));
	}
	
	public static boolean isEdge(Line2D line, List<Line2D> edges){
		for(Line2D edge : edges){
			if(equal(line, edge)){
				return true;
			}
		}
		return false;
	}
	
	public static int side(Point2D point, Line2D line){
		double value = (line.getX2() - line.getX1()) * (point.getY() - line.getY1()) 
			- (line.getY2() - line.getY1()) * (point.getX() - line.getX1());
		return (value > 0) ? 1 : (value < 0) ? -1 : 0;
	}

	public static boolean ListContains(List<Point2D> list, Point2D point) {
		for (Point2D listPoint : list) {
			if (equal(listPoint, point)) {
				return true;
			}
		}
		return false;
	}

	public static Polygon PointListToPolygon(List<Point2D> vertexs)
	{
		int[] x = new int[vertexs.size()];
		int[] y = new int[vertexs.size()];
		for(int i = 0; i < vertexs.size(); i++){
			x[i] = Functions.pointToPixel(vertexs.get(i).getX(), 100, Main.DIMENSION.width/2);
			y[i] = Functions.pointToPixel(vertexs.get(i).getY(), 100, Main.DIMENSION.height/2);
		}
		return new Polygon(x, y, vertexs.size());
	}
	// Stupid Java
	private static class EuclidianDistanceComparitor implements Comparator<Point2D> {
		private Point2D guard;

		public EuclidianDistanceComparitor(Point2D guard) {
			this.guard = guard;
		}

		@Override
		public int compare(Point2D p1, Point2D p2) {
			return Double.compare(guard.distance(p1), guard.distance(p2));
		}
	}
}
