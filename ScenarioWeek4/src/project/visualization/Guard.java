package project.visualization;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import project.main.Main;

public class Guard {

	private static final Color COLOR_GUARD = new Color(120, 250, 100);
	
	private Point2D guard;
	public List<Point2D> points;

	public Guard(double x, double y, Museum museum){
		this.guard = new Point2D.Double(x, -y);
		this.points = getPoints(guard, museum);
	}
	
	private static List<Point2D> getPoints(Point2D guard, Museum museum){
		List<Point2D> guardPoints = new ArrayList<Point2D>();
		for(Point2D point : museum.Modelpolygon.getVerticies()){
			if(point.equals(guard)) continue;
			List<Point2D> intersections = Functions.raycast(guard, point, museum.Modelpolygon, 0);
			guardPoints.addAll(intersections);
		}
		Collections.sort(guardPoints, new Point2DComparator(guard));
		return guardPoints;
	}
	
	public void paintComponent(Graphics2D g2){		
		for(int i = 0; i < points.size(); i++){
			Point2D p1 = points.get(i);
			Point2D p2 = points.get((i+1)%points.size());
			List<Point2D> guardPoints = new ArrayList<Point2D>();
			guardPoints.add(guard);
			guardPoints.add(p1);
			guardPoints.add(p2);
			Polygon triangle = Functions.PointListToPolygon(guardPoints);
			g2.setColor(COLOR_GUARD);
			g2.fillPolygon(triangle);
			//g2.setColor(Color.ORANGE);
			g2.draw(triangle);
		}
		g2.setColor(new Color(192, 64, 0));
		Functions.draw(g2, guard);
	}
	
	@Override
	public String toString(){
		return "(" + guard.getX() + "," + guard.getY() + ")";
	}
}
