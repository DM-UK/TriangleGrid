package trianglegrid.ui;

import trianglegrid.geometry.TriangleCoordinate;
import trianglegrid.geometry.TriangleGridGeometry;
import trianglegrid.grid.*;

import java.awt.*;
import java.awt.geom.Point2D;

/** Abstract class that provides basic functionality to render a triangle grid on a Graphics2D canvas. */
public abstract class TriangleGridRenderer {
    /** Grid to render */
    private final TriangleGrid grid;
    /** Geometry operations associated with grid */
    private final TriangleGridGeometry geometry;
    /** Length in pixels of the canvas to be rendered */
    private final int canvasWidthPixels;
    private final int canvasHeightPixels;
    /** Length in triangles of the canvas to be rendered */
    private final double canvasWidthTriangles;
    private final double canvasHeightTriangles;
    /** Centre position of grid to be renderered */
    private Point2D cameraPosition = new Point2D.Double();

    /** Constructs a TriangleGridRenderer with a given grid, canvas dimensions and edge length. */
    public TriangleGridRenderer(TriangleGrid grid, int canvasWidthPixels, int canvasHeightPixels, int edgeLength) {
        this.grid = grid;
        this.canvasWidthPixels = canvasWidthPixels;
        this.canvasHeightPixels = canvasHeightPixels;
        this.geometry = new TriangleGridGeometry(edgeLength);
        canvasWidthTriangles = (canvasWidthPixels / geometry.columnSpacing) + 1;
        canvasHeightTriangles = (canvasHeightPixels / geometry.rowSpacing) + 1;
        cameraPosition.setLocation(0, 0);
    }

    /** Draws the grid on the provided Graphics2D object. */
    public void drawGrid(Graphics2D g2d) {
        // iterate through grid drawing operations: faces, edges, vertices
        // moving the Graphics2D origin to the required vertex position
        for (int drawingOperation = 0; drawingOperation < 3; drawingOperation++) {
            for (int y = (int) getCameraOffsetY(); y < canvasHeightTriangles + getCameraOffsetY() ; y++) {
                for (int x = (int) getCameraOffsetX(); x < getCameraOffsetX() + canvasWidthTriangles; x++) {
                    Point2D position = geometry.gridToScreen(x, y, getCameraOffsetX(), getCameraOffsetY());
                    g2d.translate((int)position.getX(), (int)position.getY());
                    drawNode(g2d, x, y, drawingOperation);
                    g2d.translate(-(int)position.getX(), -(int)position.getY());
                }
            }
        }
    }

    /** Draws the given node based on the current drawing operation */
    protected void drawNode(Graphics2D g2d, int x, int y, int drawingOperation) {
        TriangleGrid.TriangleGridNode node = grid.getNode(x, y);

        if (node == null)
            return;

        switch (drawingOperation) {
            case 0:
                drawAllFaces(g2d, node.faceUp, node.faceDown);
                break;
            case 1:
                drawAllEdges(g2d, node.edges);
                break;
            case 2:
                drawVertex(g2d, node.vertex);
                break;
        }

        TriangleCoordinate c= new TriangleCoordinate(x,y);
        g2d.drawString(c.a+","+c.b+","+c.c,0,0);
    }

    protected void drawAllEdges(Graphics2D g2d, Edge[] edges) {
        for (int edgeIndex = 0; edgeIndex < 3; edgeIndex++) {
            //vertex from hexagon centre to edge direction
            Point2D vertex = geometry.hexagon.getVertex(edgeIndex);

            if (edges[edgeIndex] != null)
                drawEdge(g2d, edges[edgeIndex], vertex.getX(), vertex.getY());
        }
    }

    protected void drawAllFaces(Graphics2D g2d, TriangleFace faceUp, TriangleFace faceDown) {
        if (faceUp != null)
            drawFace(g2d, faceUp, geometry.pointyUpTriangle);
        if (faceDown != null)
            drawFace(g2d, faceDown, geometry.pointyDownTriangle);
    }

    public TriangleGrid getGrid() {
        return grid;
    }

    public double getCameraOffsetX(){
        return (cameraPosition.getX() - (canvasWidthTriangles / 2.0));
    }

    public double getCameraOffsetY(){
        return (cameraPosition.getY() - (canvasHeightTriangles / 2.0));
    }

    public TriangleGridGeometry getGeometry() {
        return geometry;
    }

    public Point2D getPosition() {
        return cameraPosition;
    }

    /** Draws any of the given triangle edges onto the Graphics2D object. The Graphics2D origin is set to the start of the edge. */
    protected abstract void drawEdge(Graphics2D g2d, Edge edge, double edgeFinishX, double edgeFinishY);

    /** Draws either a pointy up, or pointy down triangle onto the Graphics2D object. The Graphics2D origin is aligned with the triangleShape. */
    protected abstract void drawFace(Graphics2D g2d, TriangleFace face, Shape triangleShape);

    /** Draws the given vertex onto the Graphics2D object. The Graphics2D origin is set to the vertex centre. */
    protected abstract void drawVertex(Graphics2D g2d, Vertex vertex);
}
