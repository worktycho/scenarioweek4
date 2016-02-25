package project.visualization;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import project.main.Main;

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
	
	
	public static boolean isOutsidePolygon(Point2D point, List<Point2D> points){
		for(Line2D edge : getEdges(points)){
			System.out.println("Val: " + side(point, edge));
			if(side(point, edge) == 1){
				//return true;
			}
		}
		return false;
	}
	
	public static boolean isOutsidePolygon(Line2D line, List<Point2D> points){
		for(Point2D point : new LineIterator(line)){
			if(equal(line.getP1(), point) || equal(line.getP2(), point)){
				continue;
			}
			if(isOutsidePolygon(point, points)){
				return true;
			}
		}
		return false;
	}
	
	public static List<Point2D> getPoints(List<Double> x, List<Double> y){
		List<Point2D> points = new ArrayList<Point2D>();
		for(int i = 0; i < x.size(); i++){
			points.add(new Point2D.Double(x.get(i), y.get(i)));
		}
		return points;
	}
	
 	public static List<Line2D> getEdges(List<Point2D> points){
		List<Line2D> edges = new ArrayList<Line2D>();
		for(int i = 0; i < points.size(); i++){
			edges.add(new Line2D.Double(points.get(i), points.get((i + 1) % points.size())));
		}
		return edges;
	}
 	
 	public static Polygon getPolygon(List<Point2D> points){
 		int[] x = new int[points.size()];
		int[] y = new int[points.size()];
		for(int i = 0; i < points.size(); i++){
			x[i] = Functions.pointToPixel(points.get(i).getX(), 100, Main.DIMENSION.width/2);
			y[i] = Functions.pointToPixel(points.get(i).getY(), 100, Main.DIMENSION.height/2);
		}
		return new Polygon(x, y, points.size());
 	}
 	
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
			//return ray.getP2();
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
	
	public static Point2D getClosestIntersection(Line2D ray, List<Point2D> points){
		//(isOutsidePolygon(ray, points) && contains(points, ray.getP1())) return null;
		//if(isEdge(ray, getEdges(points))) return ray.getP2();
		Point2D closestIntersection = null;
		double lowestDist = Double.MAX_VALUE;
		ParametricLine2D r = new ParametricLine2D.Double(ray);
		List<Line2D> edges = getEdges(points);
		for(Line2D edge : edges){
			//if(equal(edge.getP1(), ray.getP1()) || equal(edge.getP2(), ray.getP1())) continue;
			Point2D intersection = getIntersection(ray, edge);
			//System.out.println("Intersection: " + intersection + " | Edge: " + edge.getP1() +"," + edge.getP2());
			if(intersection == null) continue;
			double dist = dist(ray.getP1(), intersection);
			//if((equal(edge.getP1(), ray.getP1()) || equal(edge.getP2(), ray.getP1())) && dist == 0) continue;
			if(closestIntersection == null || dist < lowestDist){
				lowestDist = dist;
				closestIntersection = intersection;
			}
		}
		return closestIntersection;		
	}
	
	public static List<Point2D> raycast(Point2D guard, Point2D point, List<Point2D> points, int counter){
		//System.out.println("Recursion: " + counter);
		counter++;
		//System.out.println("Point: " + point.getX() + " " + point.getY());
		//System.out.println("Guard: " + guard.getX() + " " + guard.getY());
		List<Point2D> intersections = new ArrayList<Point2D>();
		Line2D ray = new Line2D.Double(guard, point);
		Point2D intersection = getClosestIntersection(ray, points);
		if(intersection == null){
			//System.out.println("Intersection null");
			return intersections;
		}		
		if(equal(intersection, guard)){			
			//System.out.println("Intersection = Guard");
			return intersections;
		} 
		intersections.add(intersection);
		//System.out.println("Closest Intersection: " + intersection);
		//System.out.println("X:" + intersection.getX() + "| Y:" + intersection.getY() + " | isVertex:" + 
		//					((isVertex(intersection, points)) ? "true" : "false"));	
		if(isVertex(intersection, points)){
			double angle = Math.atan2(ray.getY2() - ray.getY1(), ray.getX2() - ray.getX1());
			double dist = dist(ray.getP1(), ray.getP2());
			double delta = 0.00001;
			Point2D p1 = new Point2D.Double(guard.getX() + dist*Math.cos(angle+delta),
											guard.getY() + dist*Math.sin(angle+delta));
			Point2D p2 = new Point2D.Double(guard.getX() + dist*Math.cos(angle-delta),
											guard.getY() + dist*Math.sin(angle-delta));
			intersections.addAll(raycast(guard, p1, points, counter));
			intersections.addAll(raycast(guard, p2, points, counter));
		}				
		return intersections;
	}
	
	public static boolean hasLineOfSight(Point2D guard, Point2D point, List<Point2D> points){
		Line2D ray = new Line2D.Double(guard, point);
		Point2D intersection = getClosestIntersection(ray, points);
		return intersection != null && equal(point, intersection);
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
}
