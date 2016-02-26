package project.visualization;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import project.raycasting.Raycast;
import project.util.Drawing;
import project.util.Equal;
import project.visualization.polygons.GuardPolygon;

public class Guard {
	
	private Point2D guard;
	public List<Point2D> points;
	private GuardPolygon polygon;
	
	public Guard(double x, double y, Museum museum){
		this.guard = new Point2D.Double(x, -y);
		this.points = computePoints(guard, museum);
		this.polygon = new GuardPolygon(points, guard);
	}
	
	private static List<Point2D> computePoints(Point2D guard, Museum museum){
		List<Point2D> guardPoints = new ArrayList<Point2D>();
		guardPoints.add(guard);
		List<Point2D> points = new ArrayList<Point2D>();
		for(Point2D point : museum.getPoints()){
			points.add(point);
		}
		Collections.sort(points, new Equal.RadianComparator(guard)); // Garrett: Order points, not guardPoints, attempt fix
		for(Point2D point : points){
			if(Equal.equal(guard, point)) continue;
			List<Point2D> intersections = Raycast.raycast(guard, point, museum.getModel());
			for(Point2D intersection : intersections){
				if(Equal.equal(guard, intersection)) continue; // Garrett: Attempt fix
				guardPoints.add(intersection);
			}
		}
		//Collections.sort(guardPoints, new Equal.RadianComparator(guard));		
		return guardPoints;
	}
	
	public List<Point2D> getPoints(){
		return points;
	}
	
	public List<Polygon> getPolygons(){
		return polygon.getPolygons();
	}
	
	public GuardPolygon getModel(){
		return polygon;
	}
	
	public void paintComponent(Graphics2D g2){		
		polygon.paint(g2);
		g2.setColor(Drawing.COLOR_GUARD);
		Drawing.draw(g2, guard);
	}
	
	@Override
	public String toString(){
		return "(" + guard.getX() + "," + guard.getY() + ")";
	}
}
