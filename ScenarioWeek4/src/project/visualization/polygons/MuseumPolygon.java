package project.visualization.polygons;

import project.raycasting.AngleRange;
import project.raycasting.Raycast;
import project.util.Drawing;
import project.util.Equal;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tycho on 25/02/16.
 */
public class MuseumPolygon {
	private List<Point2D> vertex;
	private List<AngleRange> insideRanges;

	public MuseumPolygon(List<Point2D> vertex) {	
		this.vertex = vertex;
		this.insideRanges = getInsideRanges(vertex);		
	}	
	
	private List<AngleRange> getInsideRanges(List<Point2D> vertex){
		List<AngleRange> insideRanges = new ArrayList<AngleRange>();	
		for(int i = 0; i < vertex.size(); i++){
			int next = (i+1)%vertex.size();
			int prev = ((i-1)+vertex.size())%vertex.size();
			Line2D edge_1 = new Line2D.Double(vertex.get(next), vertex.get(i));
			Line2D edge_2 = new Line2D.Double(vertex.get(prev), vertex.get(i));
			double initialAngle = Raycast.getAngle(edge_2, Raycast.VERTICAL);
			double differenceAngle = Raycast.getAngle(edge_1, edge_2);			
			AngleRange range = new AngleRange(initialAngle, differenceAngle, edge_1, edge_2);
			insideRanges.add(range);
			//System.out.println("Point: " + vertex.get(i));
			//System.out.println("Initial: " + Math.toDegrees(range.getInitial()) + 
			//		" | Difference: " + Math.toDegrees(range.getDifference()));
		}
		
		//for (int i = 0; i < vertex.size(); i++){
		/*	double angle = Raycast.getLineAngle(vertex.get(i), vertex.get((i+1)% vertex.size()));
			double secondAngle = Raycast.getLineAngle(vertex.get((i-1 + vertex.size())% vertex.size()), vertex.get(i));
			//System.out.println("Angle: " + Math.toDegrees(angle) + " | SecondAngle: " + Math.toDegrees(secondAngle));
			insideRanges.add(new AngleRange(angle, secondAngle - angle));
		}*/
		return insideRanges;
	}

	public List<Line2D> getEdges() {
		List<Line2D> edges = new ArrayList<Line2D>();
		for(int i = 0; i < vertex.size(); i++){
			edges.add(new Line2D.Double(vertex.get(i), vertex.get((i + 1) % vertex.size())));
		}
		return edges;
	}

	public boolean hasVertex(Point2D point2D) {
		return Equal.contains(vertex, point2D);
	}

	public AngleRange getInsideRangeFromPoint(Point2D point){		
		for (int i = 0; i < vertex.size(); i++) {
			if (Equal.equal(vertex.get(i), point)){
				return insideRanges.get(i);
			}
		}
		return new AngleRange(0, 2 * Math.PI, Raycast.VERTICAL, Raycast.VERTICAL);
	}

	public Polygon getPolygon(){
		return Drawing.pointsToPolygon(getVertices());
	}

	public List<Point2D> getVertices() {
		return vertex;
	}
	
	public void paint(Graphics2D g2){
		Polygon polygon = getPolygon();
		g2.setColor(Drawing.COLOR_MUSEUM);
		g2.fillPolygon(polygon);			
		g2.draw(polygon);
	}
}
