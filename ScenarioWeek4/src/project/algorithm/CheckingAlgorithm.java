package project.algorithm;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Point2D;

import project.util.Drawing;
import project.visualization.Guard;
import project.visualization.Museum;

public class CheckingAlgorithm {

	private Museum museum;
	private Point2D refutationPoint;
	
	public CheckingAlgorithm(Museum museum){
		this.museum = museum;			
	}
	
	public void run(){
		this.refutationPoint = getRefutationPoint();
	}
	
	private double random(double lower, double upper){
		return (Math.random() * (upper - lower)) + lower;
	}
	
	private Point2D getRefutationPoint(){
		double greatestX = 0;
		double greatestY = 0;
		for(Point2D p : museum.getPoints()){
			if(p.getX() > greatestX){
				greatestX = p.getX();
			}
			if(p.getY() > greatestY){
				greatestY = p.getY();
			}
		}		
		while(true){
			double randX = random(-greatestX, greatestX);
			double randY = random(-greatestY, greatestY);
			Point2D point = new Point2D.Double(Drawing.pointToPixel(randX, Drawing.WIDTH/2), 
												Drawing.pointToPixel(randY, Drawing.HEIGHT/2));
			if(museum.getPolygon().contains(point)){
				boolean canSee = false;
				for(Guard guard : museum.getGuards()){
					for(Polygon triangle : guard.getPolygons()){
						if(triangle.contains(point)){
							canSee = true;
						}
					}
				}
				if(!canSee) return new Point2D.Double(randX, randY);
			}
		}
	}
	
	public void paintComponent(Graphics2D g2){
		if(refutationPoint != null){
			g2.setColor(Drawing.COLOR_REFUTATION_POINT);
			Drawing.draw(g2, refutationPoint);
		}
	}
	
}
