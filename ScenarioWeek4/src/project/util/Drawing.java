package project.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;

public class Drawing {
	
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final Dimension DIMENSION = new Dimension(WIDTH, HEIGHT);
	
	public static final Color COLOR_MUSEUM = new Color(120, 0, 100);
	public static final Color COLOR_GUARD = Color.MAGENTA;
	public static final Color COLOR_GUARD_SIGHT = new Color(120, 250, 100);
	public static final Color COLOR_REFUTATION_POINT = Color.RED;
	
	public static int pointToPixel(double pixel, double length){
		return (int) Math.round(pixel * (double) FPanel.scale + length);
	}
	
	public static Polygon pointsToPolygon(List<Point2D> vertex){
		int[] x = new int[vertex.size()];
		int[] y = new int[vertex.size()];
		for(int i = 0; i < vertex.size(); i++){
			x[i] = pointToPixel(vertex.get(i).getX(), WIDTH/2);
			y[i] = pointToPixel(vertex.get(i).getY(), HEIGHT/2);
		}
		return new Polygon(x, y, vertex.size());
	}
	
	public static void draw(Graphics2D g2, Line2D line){
		g2.drawLine(pointToPixel(line.getX1(), WIDTH/2),
					pointToPixel(line.getY1(), HEIGHT/2),
					pointToPixel(line.getX2(), WIDTH/2),
					pointToPixel(line.getY2(), HEIGHT/2));
	}

	public static void draw(Graphics2D g2, Point2D point){
		g2.fillRoundRect(pointToPixel(point.getX(), WIDTH/2)-5, 
						 pointToPixel(point.getY(), HEIGHT/2)-5, 10, 10, 90, 90);
	}

	
	
}
