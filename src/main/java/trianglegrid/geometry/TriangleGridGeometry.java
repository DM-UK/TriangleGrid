package trianglegrid.geometry;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

/** Utility methods associated with all geometric calculations of a TriangleGrid at a given edge length*/
public class TriangleGridGeometry {
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

    public TriangleGridGeometry(int edgeLength){
        hexagon = new TriangleGridGeometry.Hexagon(edgeLength);
        pointyUpTriangle = hexagon.createTriangle(0);
        pointyDownTriangle = hexagon.createTriangle(3);
        columnSpacing = hexagon.edgeLength;;
        rowSpacing = hexagon.halfHeight;
        oddRowOffset = hexagon.halfEdgeLength;
    }

    /** Calculate grid to screen coordinates with a given (camera) offset */
    public Point2D gridToScreen(int gridX, int gridY, double cameraOffsetX, double cameraOffsetY) {
        double xp = gridToScreenX(gridX - cameraOffsetX);
        double yp = gridToScreenY(gridY - cameraOffsetY);

        if (gridY % 2 == 1)
            xp = xp - oddRowOffset;

        return new Point2D.Double(xp, yp);
    }

    /** Calculate the pure screen x coordinate with no oddRowOffset */
    public double gridToScreenX(double gridX) {
        return gridX * columnSpacing;
    }

    /** Calculate the screen y coordinate */
    public double gridToScreenY(double gridY) {
        return gridY * rowSpacing;
    }

    /** Calculate the pure grid x coordinate with no oddRowOffset */
    private double screenToGridX(double screenX) {
        return screenX / columnSpacing;
    }

    /** Calculate the grid y coordinate */
    private double screenToGridY(double screenY) {
        return screenY / rowSpacing;
    }

    /** Create a FractionalTriangleCoordinate coordinate from screen coordinates */
    private FractionalTriangleCoordinate screenToGridCoordinate(int screenX, int screenY) {
        double gridB = screenY / hexagon.halfHeight;
        double gridA = (screenX / hexagon.edgeLength) - (gridB / 2.0);

        return new FractionalTriangleCoordinate(
                gridA, gridB, -gridA - gridB
        );
    }

    /** Calculate the distance (in pixels) from screen coordinates to the nearest vertex */
    public int screenToNearestVertexDistance(int screenX, int screenY){
        FractionalTriangleCoordinate fractionalCoordinate = screenToGridCoordinate(screenX, screenY);
        TriangleCoordinate nearestVertexCoordinate = fractionalCoordinate.roundedTriangleCoordinate();
        double nearestVertexGridX = screenToGridX(screenX) - nearestVertexCoordinate.a - (nearestVertexCoordinate.b / 2.0);
        double nearestVertexScreenX = gridToScreenX(nearestVertexGridX);
        double nearestVertexScreenY = gridToScreenY(fractionalCoordinate.b - nearestVertexCoordinate.b);
        return (int) Math.hypot(nearestVertexScreenX, nearestVertexScreenY);
    }

    /** Calculate TriangleCoordinate of the nearest vertex from screen coordinates */
    public TriangleCoordinate screenToNearestVertexCoordinate(int screenX, int screenY) {
        FractionalTriangleCoordinate fractionalCoordinate = screenToGridCoordinate(screenX, screenY);
        return fractionalCoordinate.roundedTriangleCoordinate();
    }

    /** Calculate TriangleCoordinate and direction of the nearest edge from screen coordinates */
    public DirectedTriangleCoordinate screenToNearestEdgeCoordinate(int screenX, int screenY)
    {
        FractionalTriangleCoordinate fractionalCoordinate = screenToGridCoordinate(screenX, screenY);
        TriangleCoordinate nearestVertex = fractionalCoordinate.roundedTriangleCoordinate();
        double aDist = gridToScreenX(fractionalCoordinate.a - nearestVertex.a);
        double bDist = gridToScreenY(fractionalCoordinate.b - nearestVertex.b);
        double cDist = gridToScreenX(fractionalCoordinate.c - nearestVertex.c);
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

        return new DirectedTriangleCoordinate(nearestVertex, direction);
    }

    /** Calculate the distance (in pixels) of the nearest edge from screen coordinates */
    public double screenToNearestEdgeDistance(int screenX, int screenY) {
        FractionalTriangleCoordinate fractionalCoordinate = screenToGridCoordinate(screenX, screenY);
        TriangleCoordinate nearestVertex = fractionalCoordinate.roundedTriangleCoordinate();
        double pixelXToGridA = gridToScreenX(fractionalCoordinate.a - nearestVertex.a);
        double pixelYToGridB = gridToScreenY(fractionalCoordinate.b - nearestVertex.b);
        double pixelXToGridC = gridToScreenX(fractionalCoordinate.c - nearestVertex.c);

        return Math.min(Math.abs(pixelXToGridA),
                Math.min(Math.abs(pixelYToGridB), Math.abs(pixelXToGridC)));
    }

    /** Calculate TriangleCoordinate and direction of the nearest face from screen coordinates */
    public DirectedTriangleCoordinate screenToFaceCoordinate(int screenX, int screenY) {
        FractionalTriangleCoordinate fractionalCoordinate = screenToGridCoordinate(screenX, screenY);
        TriangleCoordinate flooredCoordinate = fractionalCoordinate.flooredTriangleCoordinate();
        double xDifference = fractionalCoordinate.a - flooredCoordinate.a;
        double yDifference = fractionalCoordinate.b - flooredCoordinate.b;

        if (1 - xDifference > yDifference)
            return new DirectedTriangleCoordinate(flooredCoordinate, 3);
        else
            return new DirectedTriangleCoordinate(flooredCoordinate.getNeighbour(2), 4);
    }

    public static class FractionalTriangleCoordinate
    {
        public final double a, b, c;

        public FractionalTriangleCoordinate(double a, double b, double c){
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public TriangleCoordinate roundedTriangleCoordinate(){
            int roundedA = (int) Math.round(a);
            int roundedB = (int) Math.round(b);
            int roundedC = (int) Math.round(c);
            double diffA = Math.abs(a - roundedA);
            double diffB = Math.abs(b - roundedB);
            double diffC = Math.abs(c - roundedC);

            if (diffA > diffB && diffA > diffC)
                roundedA = -roundedB-roundedC;
            else if (diffB > diffA && diffB > diffC)
                roundedB = -roundedA-roundedC;
            else
                roundedC =-roundedA-roundedB;

            return new TriangleCoordinate(roundedA, roundedB, roundedC);
        }

        public TriangleCoordinate flooredTriangleCoordinate(){
            double flooredA = Math.floor(a);
            double flooredB = Math.floor(b);
            return new TriangleCoordinate((int) flooredA, (int) flooredB, (int) (-flooredA-flooredB));
        }

        @Override
        public String toString() {
            return "FractionalTriangleCoordinate{" +
                    "a=" + a +
                    ", b=" + b +
                    ", c=" + c +
                    '}';
        }
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
