package project.main;

import project.visualization.Functions;
import project.visualization.Guard;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by tycho on 25/02/16.
 */
public class MuseumPolygon {
	private List<Point2D> vertexs;
	private List<AngleRange> insideRanges;

	public MuseumPolygon(List<Double> x, List<Double> y) {
		vertexs = new ArrayList<Point2D>();
		insideRanges = new ArrayList<>();
		for(int i = 0; i < x.size(); i++){
			vertexs.add(new Point2D.Double(x.get(i), y.get(i)));
		}
		for (int i = 0; i < vertexs.size(); i++){
			double angle = Functions.getLineAngle(vertexs.get(i), vertexs.get((i+1)% vertexs.size()));
			double secondAngle = Functions.getLineAngle(vertexs.get(i), vertexs.get((i-1 + vertexs.size())% vertexs.size()));
			insideRanges.add(new AngleRange(angle, secondAngle - angle));
		}
	}

	public List<Line2D> getEdges() {
		List<Line2D> edges = new ArrayList<Line2D>();
		for(int i = 0; i < vertexs.size(); i++){
			edges.add(new Line2D.Double(vertexs.get(i), vertexs.get((i + 1) % vertexs.size())));
		}
		return edges;
	}

	public boolean hasVertex(Point2D point2D) {
		return Functions.ListContains(vertexs, point2D);
	}

	public AngleRange getInsideRangeFromPoint(Point2D point) {
		for (int i = 0; i < vertexs.size(); i++) {
			if (Functions.equal(vertexs.get(i), point)){
				return insideRanges.get(i);
			}
		}
		return new AngleRange(0, 2 * Math.PI);
	}

	public Polygon getGraphicsPolygon()
	{
		return Functions.PointListToPolygon(this.vertexs);
	}

	public List<Point2D> getVerticies() {
		return vertexs;
	}
}
