package project.raycasting;

import java.awt.geom.Line2D;

/**
 * Created by tycho on 25/02/16.
 */
public class AngleRange {
	
	private double initialAngle;
	private double differenceAngle;
	private Line2D edge_1;
	private Line2D edge_2;

	public AngleRange(double initialAngle, double differenceAngle, Line2D edge_1, Line2D edge_2){
		this.initialAngle = initialAngle;
		this.differenceAngle = differenceAngle;
		this.edge_1 = edge_1;
		this.edge_2 = edge_2;
	}

	public double getInitial(){
		return initialAngle;
	}
	
	public double getDifference(){
		return differenceAngle;
	}
	
	public Line2D getEdge1(){
		return edge_1;
	}
	
	public Line2D getEdge2(){
		return edge_2;
	}
	
	public boolean contains(double angle) {
		//angle += 2*Math.PI;
		angle = angle % (2*Math.PI);
		double angle_1 = initialAngle % (2*Math.PI);
		double angle_2 = (initialAngle + differenceAngle);
		if(initialAngle > Math.PI) initialAngle -= 2*Math.PI;
		return (angle >= angle_1) && (angle <= angle_2);
	}
	
	public String toString(){
		return "AngleRange:" + initialAngle + " + " + differenceAngle;
	}
}
