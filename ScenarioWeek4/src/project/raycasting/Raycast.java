package project.raycasting;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import project.util.Equal;
import project.util.ParametricLine2D;
import project.visualization.polygons.MuseumPolygon;

public class Raycast {
	
	public static final Line2D VERTICAL = new Line2D.Double(0.0, 0.0, 0.0, 1.0);
	
	/**
	 * Measures the angle between two lines
	 * @param line1 Considering line e.g. edge
	 * @param line2 Measuring against e.g. VERTICAL
	 * @return
	 */
	public static double getAngle(Line2D line1, Line2D line2){
		double angle1 = Math.atan2(line1.getY1() - line1.getY2(),
                line1.getX1() - line1.getX2());
		double angle2 = Math.atan2(line2.getY1() - line2.getY2(),
                line2.getX1() - line2.getX2());
		double angle = angle1-angle2;
		while(angle >= 2*Math.PI) angle -= 2*Math.PI;
		while(angle < 0) angle += 2*Math.PI;
		return angle;
	}
	
	/*public static double getLineAngle(Point2D guard, Point2D point) {
		Line2D vertical = new Line2D.Double(0.0, 0.0, 0.0, 1.0);
		Line2D edge = new Line2D.Double(guard, point);
		double angle1 = Math.atan2(vertical.getY1() - vertical.getY2(), vertical.getX1() - vertical.getX2());
		double angle2 = Math.atan2(edge.getY1() - edge.getY2(), edge.getX1() - edge.getX2());
		return -(angle1-angle2);
	}*/
	
	private static Point2D getIntersection(Line2D ray, Line2D edge){
		if(Equal.equal(ray.getP2(), edge.getP1()) || Equal.equal(ray.getP2(), edge.getP2())){
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
	
	private static List<Point2D> getAllIntersections(Line2D ray, MuseumPolygon points){
		//(isOutsidePolygon(ray, points) && contains(points, ray.getP1())) return null;
		//if(isEdge(ray, getEdges(points))) return ray.getP2();
		List<Point2D> intersections = new ArrayList<>();
		List<Line2D> edges = points.getEdges();
		for(Line2D edge : edges){
			//if(equal(edge.getP1(), ray.getP1()) || equal(edge.getP2(), ray.getP1())) continue;
			Point2D intersection = getIntersection(ray, edge);
			if(intersection == null) continue;
			intersections.add(intersection);
		}
		return intersections;
	}
	
	public static List<Point2D> raycast(Point2D guard, Point2D point, MuseumPolygon polygon){
		List<Point2D> visibleIntersections = new ArrayList<Point2D>();
		Line2D ray = new Line2D.Double(guard, point);
		Line2D rayRev = new Line2D.Double(point, guard);
		List<Point2D> intersections = getAllIntersections(ray, polygon);
		intersections.sort(new Equal.EuclidianDistanceComparator(guard));
		
		double rayAngle = getAngle(rayRev, VERTICAL);
		//System.out.println("Guard: " + guard + " | Point: " + point);
		//System.out.println("Ray: " + Math.toDegrees(rayAngle));
		if(Equal.equal(intersections.get(0), guard)){
			//System.out.println("Intersection = Guard");
			// ToDo: Handle zero length intersections
			AngleRange homeIntersectionRange = polygon.getInsideRangeFromPoint(guard);
			//System.out.println(homeIntersectionRange);
			if (!homeIntersectionRange.contains(rayAngle)) {
				return visibleIntersections;
			}
		}		
		int i = 0;			
		do {
			Point2D intersection = intersections.get(i);
			if(!Equal.contains(visibleIntersections, intersection)){
				visibleIntersections.add(intersection);
			}
		} while (i + 1 < intersections.size() 
				&& polygon.hasVertex(intersections.get(i))
				&& polygon.getInsideRangeFromPoint(intersections.get(i)).contains(rayAngle) 
				&& ++i > 0
				);
		// Garrett: Start attempt reversal for fix
		Collections.sort(visibleIntersections, new Equal.EuclidianDistanceComparator(guard));
		AngleRange range = polygon.getInsideRangeFromPoint(point);
		double angle_1 = getAngle(range.getEdge1(), rayRev) - Math.PI;
		double angle_2 = getAngle(range.getEdge2(), rayRev) - Math.PI;
		rayAngle -= Math.PI;
		if(angle_2 <= 0 && rayAngle >= 0){
			Collections.reverse(visibleIntersections);
		}
		// Garrett End attempt
		return visibleIntersections;
	}

}
