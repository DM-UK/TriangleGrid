package trianglegrid.grid;

import trianglegrid.coordinates.DirectedCoordinate;
import trianglegrid.coordinates.TriangleCoordinate;
import trianglegrid.coordinates.FractionalCoordinate;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

/** Utility methods associated with all geometric calculations of a TriangleGrid at a given edge length*/
public class GridGeometry {
    /** Hexagon used for geometry */
    public final Hexagon hexagon;
    /** Triangle shape in the two orientations  */
    public final Shape pointyUpTriangle;
    public final Shape pointyDownTriangle;
    /** Distance between two triangles along x-axis  */
    public final double columnSpacing;
    /** Distance between two triangles along y-axis  */
    public final double rowSpacing;
    /** Offset to be applied to the x-axis on every other row */
    public final double oddRowOffset;

    public GridGeometry(int edgeLength){
        hexagon = new GridGeometry.Hexagon(edgeLength);
        pointyUpTriangle = hexagon.createTriangle(0);
        pointyDownTriangle = hexagon.createTriangle(3);
        columnSpacing = hexagon.edgeLength;;
        rowSpacing = hexagon.halfHeight;
        oddRowOffset = hexagon.halfEdgeLength;
    }

    public Point2D.Double gridToScreenCoordinate(TriangleCoordinate coordinate) {
        return new Point2D.Double(
                gridToScreenX(coordinate.a, coordinate.b),
                gridToScreenY(coordinate.b));
    }

    public double gridToScreenX(double a, double b)
    {
        return (a * columnSpacing) + (b * oddRowOffset);
    }

    public double gridToScreenY(double b)
    {
        return b * rowSpacing;
    }

    /** Create a FractionalTriangleCoordinate coordinate from screen coordinates */
    public FractionalCoordinate screenToGridCoordinate(int screenX, int screenY) {
        double gridB = screenY / rowSpacing;
        double gridA = (screenX - (gridB * oddRowOffset)) / columnSpacing;

        return new FractionalCoordinate(
                gridA, gridB, -gridA - gridB
        );
    }

    /** Calculate the distance (in pixels) from grid coordinates to the nearest vertex */
    public double nearestVertexDistance(FractionalCoordinate coordinate){
        TriangleCoordinate nearestVertex = coordinate.roundedTriangleCoordinate();
        double distanceA = coordinate.a - nearestVertex.a;
        double distanceB = coordinate.b - nearestVertex.b;
        double distanceX = gridToScreenX(distanceA, distanceB);
        double distanceY = gridToScreenY(distanceB);
        return Math.hypot(distanceX, distanceY);
    }

    /** Calculate TriangleCoordinate of the nearest vertex from grid coordinates */
    public TriangleCoordinate nearestVertexCoordinate(FractionalCoordinate coordinate) {
        return coordinate.roundedTriangleCoordinate();
    }

    /** Calculate the distance (in pixels) of the nearest edge from grid coordinates */
    public double nearestEdgeDistance(FractionalCoordinate coordinate) {
        TriangleCoordinate nearestVertex = coordinate.roundedTriangleCoordinate();
        double gridDistanceA = coordinate.a - nearestVertex.a;
        double gridDistanceB = coordinate.b - nearestVertex.b;
        double gridDistanceC = coordinate.c - nearestVertex.c;
        double pixelDistanceA = gridToScreenX(gridDistanceA, 0);
        double pixelDistanceB = gridToScreenY(gridDistanceB);
        double pixelDistanceC = gridToScreenX(gridDistanceC, 0);

        return Math.min(Math.abs(pixelDistanceA),
                Math.min(Math.abs(pixelDistanceB), Math.abs(pixelDistanceC)));
    }

    /** Calculate TriangleCoordinate and direction of the nearest edge from grid coordinates */
    public DirectedCoordinate nearestEdgeCoordinate(FractionalCoordinate coordinate)
    {
        TriangleCoordinate nearestVertex = coordinate.roundedTriangleCoordinate();
        double aDist = gridToScreenX(coordinate.a - nearestVertex.a, 0);
        double bDist = gridToScreenY(coordinate.b - nearestVertex.b);
        double cDist = gridToScreenX(coordinate.c - nearestVertex.c, 0);
        double aDistAbs = Math.abs(aDist);
        double bDistAbs = Math.abs(bDist);
        double cDistAbs = Math.abs(cDist);

        int direction;

        if (aDistAbs < bDistAbs && aDistAbs < cDistAbs)
            direction = bDist < 0 ? 0 : 3;
        else if (bDistAbs < aDistAbs && bDistAbs < cDistAbs)
            direction = aDist < 0 ? 5 : 2;
        else
            direction = bDist < 0 ? 1 : 4;

        return new DirectedCoordinate(nearestVertex, direction);
    }

    /** Calculate TriangleCoordinate and direction of the nearest face from grid coordinates */
    public DirectedCoordinate getFaceCoordinate(FractionalCoordinate coordinate) {
        TriangleCoordinate nearestVertex = coordinate.roundedTriangleCoordinate();
        TriangleCoordinate flooredCoordinate = coordinate.flooredTriangleCoordinate();
        double xDifference = coordinate.a - flooredCoordinate.a;
        double yDifference = coordinate.b - flooredCoordinate.b;

        if (1 - xDifference > yDifference)
            return new DirectedCoordinate(flooredCoordinate, 3);
        else
            return new DirectedCoordinate(flooredCoordinate.getNeighbour(2), 4);
    }

    /** All calculations of a triangle grid can be derived from a hexagon */
    public static class Hexagon {
        /** Cache root 3 */
        private static final double SQRT_3 = Math.sqrt(3);
        /** Hexagon vertices from centre of hexagon */
        public final Point2D[] vertices = new Point2D[6];
        /** Length of an edge */
        public final double edgeLength;
        public final double halfEdgeLength;
        /** Maximum height of hexagon */
        public final double height;
        public final double halfHeight;
        /** Maximum width of hexagon */
        public final double width;

        public Hexagon(double edgeLength) {
            this.edgeLength = edgeLength;
            this.halfEdgeLength = edgeLength / 2;
            this.height = (SQRT_3 * edgeLength);
            this.halfHeight = (SQRT_3 * edgeLength / 2.0);
            this.width = edgeLength * 2;
            vertices[0] = new Point2D.Double(-halfEdgeLength, -halfHeight);
            vertices[1] = new Point2D.Double(+halfEdgeLength, -halfHeight);
            vertices[2] = new Point2D.Double(+edgeLength, 0);
            vertices[3] = new Point2D.Double(+halfEdgeLength, +halfHeight);
            vertices[4] = new Point2D.Double(-halfEdgeLength, +halfHeight);
            vertices[5] = new Point2D.Double(-edgeLength, 0);
        }

        /** Returns the vertex at the given index - origin is hexagon centre. */
        public Point2D getVertex(int vertexIndex) {
            return vertices[vertexIndex % 6];
        }

        /** Construct a triangle from: a) hexagon centre, b) vertexIndex and c) vertexIndex + 1 */
        public Shape createTriangle(int vertexIndexA) {
            int vertexIndexB = (vertexIndexA + 1) % 6;

            Path2D path = new Path2D.Double();
            //a) hexagon centre
            path.moveTo(0, 0);
            //b) vertexIndex
            path.lineTo(getVertex(vertexIndexA).getX(),
                    getVertex(vertexIndexA).getY());
            //c) vertexIndex + 1
            path.lineTo(getVertex(vertexIndexB).getX(),
                    getVertex(vertexIndexB).getY());

            path.closePath();
            return path;
        }
    }
}
