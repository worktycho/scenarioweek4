package project.visualization.polygons;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import project.util.Drawing;

public class GuardPolygon {

	private List<Point2D> vertex;
	private Point2D guard;
	
	public GuardPolygon(List<Point2D> vertex, Point2D guard){
		this.vertex = vertex;
		this.guard = guard;
	}
	
	public List<Polygon> getPolygons(){
		List<Polygon> triangles = new ArrayList<Polygon>();
		for(int i = 0; i < vertex.size(); i++){
			Point2D p1 = vertex.get(i);
			Point2D p2 = vertex.get((i+1)%vertex.size());
			List<Point2D> guardPoints = new ArrayList<Point2D>();
			guardPoints.add(guard);
			guardPoints.add(p1);
			guardPoints.add(p2);
			Polygon triangle = Drawing.pointsToPolygon(guardPoints);
			triangles.add(triangle);
		}
		return triangles;
	}
	
	public void paint(Graphics2D g2){
		g2.setColor(Drawing.COLOR_GUARD_SIGHT);
		for(Polygon triangle : getPolygons()){
			
			g2.fillPolygon(triangle);
			//g2.setColor(Color.ORANGE);
			g2.draw(triangle);
		}
	}
	
	
}
