package project.algorithm;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple implementation of the ear cutting algorithm to Triangulation simple
 * polygons without holes. For more information:
 * http://cgm.cs.mcgill.ca/~godfried/teaching/cg-projects/97/Ian/algorithm2.html
 * http://www.geometrictools.com/Documentation/TriangulationByEarClipping.pdf
 * 
 * @author badlogicgames@gmail.com
 * @author Nicolas Gramlich (Improved performance. Collinear edges are now supported.)
 */
public class Triangulation {
	

  private static final int CONCAVE = 1;
  private static final int CONVEX = -1;

  private int concaveVertexCount;

  /**
   * Triangulations the given (concave) polygon to a list of triangles. The
   * resulting triangles have clockwise order. 
   * @param polygon the polygon
   * @return the triangles
   */
  public List<Point2D> computeTriangles(final List<Point2D> polygon) {
    // TODO Check if LinkedList performs better
    final ArrayList<Point2D> triangles = new ArrayList<Point2D>();
    final ArrayList<Point2D> vertices = new ArrayList<Point2D>(polygon.size());
    vertices.addAll(polygon);

    if(vertices.size() == 3) {
      triangles.addAll(vertices);
      return triangles;
    }

    while(vertices.size() >= 3) {
      // TODO Usually(Always?) only the Types of the vertices next to the ear change! --> Improve
      final int vertexTypes[] = this.classifyVertices(vertices);

      final int vertexCount = vertices.size();
      for(int index = 0; index < vertexCount; index++) {
        if(this.isEarTip(vertices, index, vertexTypes)) {
          this.cutEarTip(vertices, index, triangles);
          break;
        }
      }
    }

    return triangles;
  }

  private static boolean areVerticesClockwise(final ArrayList<Point2D> pVertices) {
    final int vertexCount = pVertices.size();

    float area = 0;
    for(int i = 0; i < vertexCount; i++) {
      final Point2D p1 = pVertices.get(i);
      final Point2D p2 = pVertices.get(Triangulation.computeNextIndex(pVertices, i));
      area += p1.getX() * p2.getY() - p2.getX() * p1.getY();
    }

    if(area < 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * @param pVertices
   * @return An array of length <code>pVertices.size()</code> filled with either {@link Triangulation#CONCAVE} or
   * {@link Triangulation#CONVEX}.
   */
  private int[] classifyVertices(final ArrayList<Point2D> pVertices) {
    final int vertexCount = pVertices.size();

    final int[] vertexTypes = new int[vertexCount];
    this.concaveVertexCount = 0;

    /* Ensure vertices are in clockwise order. */
    if(!Triangulation.areVerticesClockwise(pVertices)) {
      Collections.reverse(pVertices);
    }

    for(int index = 0; index < vertexCount; index++) {
      final int previousIndex = Triangulation.computePreviousIndex(pVertices, index);
      final int nextIndex = Triangulation.computeNextIndex(pVertices, index);

      final Point2D previousVertex = pVertices.get(previousIndex);
      final Point2D currentVertex = pVertices.get(index);
      final Point2D nextVertex = pVertices.get(nextIndex);

      if(Triangulation.isTriangleConvex((float) previousVertex.getX(), (float) previousVertex.getY(), (float)currentVertex.getX(),
    		  (float) currentVertex.getY(), (float) nextVertex.getX(),(float) nextVertex.getY())) {
        vertexTypes[index] = CONVEX;
      } else {
        vertexTypes[index] = CONCAVE;
        this.concaveVertexCount++;
      }
    }

    return vertexTypes;
  }

  private static boolean isTriangleConvex(final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3) {
    if(Triangulation.computeSpannedAreaSign(pX1, pY1, pX2, pY2, pX3, pY3) < 0) {
      return false;
    } else {
      return true;
    }
  }

  private static int computeSpannedAreaSign(final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3) {
    float area = 0;

    area += pX1 * (pY3 - pY2);
    area += pX2 * (pY1 - pY3);
    area += pX3 * (pY2 - pY1);

    return (int)Math.signum(area);
  }

  /**
   * @return <code>true</code> when the Triangles contains one or more vertices, <code>false</code> otherwise.
   */
  private static boolean isAnyVertexInTriangle(final ArrayList<Point2D> pVertices, final int[] pVertexTypes, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3) {
    int i = 0;

    final int vertexCount = pVertices.size();
    while(i < vertexCount - 1) {
      if((pVertexTypes[i] == CONCAVE)) {
        final Point2D currentVertex = pVertices.get(i);

        final float currentVertexX = (float) currentVertex.getX();
        final float currentVertexY = (float) currentVertex.getY();

        /* TODO The following condition fails for perpendicular, axis aligned triangles! 
         * Removing it doesn't seem to cause problems. 
         * Maybe it was an optimization?
         * Maybe it tried to handle collinear pieces ? */
//        if(((currentVertexX != pX1) && (currentVertexY != pY1)) || ((currentVertexX != pX2) && (currentVertexY != pY2)) || ((currentVertexX != pX3) && (currentVertexY != pY3))) {
          final int areaSign1 = Triangulation.computeSpannedAreaSign(pX1, pY1, pX2, pY2, currentVertexX, currentVertexY);
          final int areaSign2 = Triangulation.computeSpannedAreaSign(pX2, pY2, pX3, pY3, currentVertexX, currentVertexY);
          final int areaSign3 = Triangulation.computeSpannedAreaSign(pX3, pY3, pX1, pY1, currentVertexX, currentVertexY);

          if(areaSign1 > 0 && areaSign2 > 0 && areaSign3 > 0) {
            return true;
          } else if(areaSign1 <= 0 && areaSign2 <= 0 && areaSign3 <= 0) {
            return true;
          }
//        }
      }
      i++;
    }
    return false;
  }

  private boolean isEarTip(final ArrayList<Point2D> pVertices, final int pEarTipIndex, final int[] pVertexTypes) {
    if(this.concaveVertexCount != 0) {
      final Point2D previousVertex = pVertices.get(Triangulation.computePreviousIndex(pVertices, pEarTipIndex));
      final Point2D currentVertex = pVertices.get(pEarTipIndex);
      final Point2D nextVertex = pVertices.get(Triangulation.computeNextIndex(pVertices, pEarTipIndex));

      if(Triangulation.isAnyVertexInTriangle(pVertices, pVertexTypes,(float) previousVertex.getX(), (float) previousVertex.getY(), 
    		  (float) currentVertex.getX(), (float) currentVertex.getY(), (float) nextVertex.getX(), (float) nextVertex.getY())) {
        return false;
      } else {
        return true;
      }
    } else {
      return true;
    }
  }

  private void cutEarTip(final ArrayList<Point2D> pVertices, final int pEarTipIndex, final ArrayList<Point2D> pTriangles) {
    final int previousIndex = Triangulation.computePreviousIndex(pVertices, pEarTipIndex);
    final int nextIndex = Triangulation.computeNextIndex(pVertices, pEarTipIndex);

    if(!Triangulation.isCollinear(pVertices, previousIndex, pEarTipIndex, nextIndex)) {
      pTriangles.add((pVertices.get(previousIndex)));
      pTriangles.add((pVertices.get(pEarTipIndex)));
      pTriangles.add((pVertices.get(nextIndex)));
    }

    pVertices.remove(pEarTipIndex);
    if(pVertices.size() >= 3) {
      Triangulation.removeCollinearNeighborEarsAfterRemovingEarTip(pVertices, pEarTipIndex);
    }
  }

  private static void removeCollinearNeighborEarsAfterRemovingEarTip(final ArrayList<Point2D> pVertices, final int pEarTipCutIndex) {
    final int collinearityCheckNextIndex = pEarTipCutIndex % pVertices.size();
    int collinearCheckPreviousIndex = Triangulation.computePreviousIndex(pVertices, collinearityCheckNextIndex);

    if(Triangulation.isCollinear(pVertices, collinearityCheckNextIndex)) {
      pVertices.remove(collinearityCheckNextIndex);

      if(pVertices.size() > 3) {
        /* Update */
        collinearCheckPreviousIndex = Triangulation.computePreviousIndex(pVertices, collinearityCheckNextIndex);
        if(Triangulation.isCollinear(pVertices, collinearCheckPreviousIndex)){
          pVertices.remove(collinearCheckPreviousIndex);
        }
      }
    } else if(Triangulation.isCollinear(pVertices, collinearCheckPreviousIndex)){
      pVertices.remove(collinearCheckPreviousIndex);
    }
  }

  private static boolean isCollinear(final ArrayList<Point2D> pVertices, final int pIndex) {
    final int previousIndex = Triangulation.computePreviousIndex(pVertices, pIndex);
    final int nextIndex = Triangulation.computeNextIndex(pVertices, pIndex);

    return Triangulation.isCollinear(pVertices, previousIndex, pIndex, nextIndex);
  }

  private static boolean isCollinear(final ArrayList<Point2D> pVertices, final int pPreviousIndex, final int pIndex, final int pNextIndex) {
    final Point2D previousVertex = pVertices.get(pPreviousIndex);
    final Point2D vertex = pVertices.get(pIndex);
    final Point2D nextVertex = pVertices.get(pNextIndex);

    return Triangulation.computeSpannedAreaSign((float)previousVertex.getX(), (float) previousVertex.getY(), 
    		(float)vertex.getX(), (float)vertex.getY(), (float)nextVertex.getX(), (float)nextVertex.getY()) == 0;
  }

  private static int computePreviousIndex(final List<Point2D> pVertices, final int pIndex) {
    return pIndex == 0 ? pVertices.size() - 1 : pIndex - 1;
  }

  private static int computeNextIndex(final List<Point2D> pVertices, final int pIndex) {
    return pIndex == pVertices.size() - 1 ? 0 : pIndex + 1;
  }
  
  public double[][][] triangulationToColouring(List<Point2D> points){
	  double[][][] triangles = new double[points.size()/3][3][2];
	  int index = 0;
	  for(int i = 0; i < points.size()/3; i++){
		  for(int j = 0; j < 3; j++){			  
			  triangles[i][j][0] = points.get(index).getX();
			  triangles[i][j][1] = points.get(index++).getY();
		  }
	  }
	  return triangles;
  }
  
  public List<Point2D> colouringToPoints(double[][][] triangles){
	  List<Point2D> points = new ArrayList<Point2D>();
	  for(int i = 0; i < triangles.length; i++){
		  for(int j = 0; j < triangles[i].length; j++){
			  points.add(new Point2D.Double(triangles[i][j][0], triangles[i][j][1]));
		  }
	  }
	  return points;
  }
  
}


