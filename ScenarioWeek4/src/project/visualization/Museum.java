package project.visualization;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import project.algorithm.Triangulation;

public class Museum extends FPanel{

	private static final long serialVersionUID = -7657783633657321653L;

	private int id;
	public List<Point2D> points;
	public List<Line2D> edges;
	public List<Guard> guards;
	
	private static final Color COLOR_MUSEUM = new Color(120, 0, 100);
	
	public Museum(int id, List<Double> x, List<Double> y){
		this.id = id;	
		this.points = Functions.getPoints(x, y);
		this.edges = Functions.getEdges(points);
		this.guards = new ArrayList<Guard>();
	}
	
	public void add(Guard guard){
		guards.add(guard);
	}
	
	public int getID(){
		return id;
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);		
		Graphics2D g2 = (Graphics2D) g;
		Polygon polygon = Functions.getPolygon(points);
		g2.setColor(COLOR_MUSEUM);
		g2.fillPolygon(polygon);	
		g2.draw(polygon);
		//g2.setStroke(new BasicStroke(10));
		for(Guard guard : guards){
			guard.paintComponent(g2);
		}
		//List<List<Point2D>> triangles = Triangulation.triangulate(this);
		g2.setColor(Color.ORANGE);
		
	}
	
	@Override
	public String toString(){
		String str = "Museum:";
		for(Point2D p : points){
			str += "(" + p.getX() + "," + p.getY() + "),";
		}
		str = (str.charAt(str.length() - 1) == ',') ? str.substring(0, str.length() - 1) : str;
		str += ";Guards:";
		for(Guard g : guards){
			str += g.toString() + ",";
		}
		str = (str.charAt(str.length() - 1) == ',') ? str.substring(0, str.length() - 1) : str;
		return str;
	}
	
}
