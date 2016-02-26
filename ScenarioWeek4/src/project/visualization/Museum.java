package project.visualization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import project.algorithm.CheckingAlgorithm;
import project.algorithm.Colourer;
import project.algorithm.Triangulation;
import project.util.Drawing;
import project.util.FPanel;
import project.visualization.polygons.MuseumPolygon;

public class Museum extends FPanel{

	private static final long serialVersionUID = -7657783633657321653L;

	private int id;
	private List<Point2D> points;
	private List<Guard> guards;
	
	private MuseumPolygon polygon;
	
	private Triangulation triangulation;
	private Colourer colourer;
	private CheckingAlgorithm checkingAlgorithm;
	
	public Museum(int id, List<Double> x, List<Double> y){
		this.id = id;			
		this.points = new ArrayList<Point2D>();
		for(int i = 0; i < x.size(); i++){
			points.add(new Point2D.Double(x.get(i), y.get(i)));
		}
		this.polygon = new MuseumPolygon(points);
		this.guards = new ArrayList<Guard>();	
		this.triangulation = new Triangulation(this);
		this.colourer = new Colourer();
		this.checkingAlgorithm = new CheckingAlgorithm(this);
	}
	
	public void run(){		
		List<Point2D> trianglePoly = triangulation.computeTriangles(points);
		double[][][] triangles = triangulation.triangulationToColouring(trianglePoly);
		System.out.println("Museum: " + id);
		colourer.colour(triangles);
		//checkingAlgorithm.run();
	}
	
	public void add(Guard guard){
		guards.add(guard);
	}
	
	public int getID(){
		return id;
	}
	
	public List<Point2D> getPoints(){
		return points;
	}
	
	public Polygon getPolygon(){
		return polygon.getPolygon();
	}
	
	public List<Guard> getGuards(){
		return guards;
	}
	
	public MuseumPolygon getModel(){
		return polygon;
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);		
		Graphics2D g2 = (Graphics2D) g;
		polygon.paint(g2);	
		for(Guard guard : guards){
			guard.paintComponent(g2);
		}
		List<Point2D> trianglePoly = triangulation.computeTriangles(points);
		g2.setColor(Color.ORANGE);
		g2.draw(Drawing.pointsToPolygon(trianglePoly));
		//checkingAlgorithm.paintComponent(g2);
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
