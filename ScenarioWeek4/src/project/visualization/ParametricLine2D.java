package project.visualization;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public abstract class ParametricLine2D extends Line2D.Double{

	private static final long serialVersionUID = 9007217865191579765L;
	
	public double px;
	public double py;
	public double dx;
	public double dy;
	
	public ParametricLine2D(){
		
	}
	
	public static class Double extends ParametricLine2D{

		private static final long serialVersionUID = -803230857986736431L;
						
		public Double(double x1, double y1, double x2, double y2){
			this.px = x1;
			this.py = y1;
			this.dx = x2 - x1;
			this.dy = y2 - y1;
		}
		
		public Double(Point2D p1, Point2D p2){
			this(p1.getX(), p1.getY(), p2.getX(), p2.getY());
		}	
		
		public Double(Line2D line){
			this(line.getP1(), line.getP2());
		}
	}	
}
