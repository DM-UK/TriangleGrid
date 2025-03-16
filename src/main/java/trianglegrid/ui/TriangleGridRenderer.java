package trianglegrid.ui;

import trianglegrid.coordinates.FractionalCoordinate;
import trianglegrid.coordinates.TriangleCoordinate;
import trianglegrid.grid.GridGeometry;
import trianglegrid.grid.*;

import java.awt.*;
import java.awt.geom.Point2D;

/** Abstract class that provides basic functionality to render a triangle grid on a Graphics2D canvas. */
public abstract class TriangleGridRenderer {
    /** Grid to render */
    private final TriangleGrid grid;
    /** Geometry operations associated with grid */
    private final GridGeometry geometry;
    /** Length in pixels of the canvas to be rendered */
    private final int canvasWidthPixels;
    private final int canvasHeightPixels;
    /** Length in triangles of the canvas to be rendered */
    private final int halfCanvasWidthTriangles;
    private final int halfCanvasHeightTriangles;

    /** Centre position of grid to be renderered */
    private FractionalCoordinate cameraPosition = new FractionalCoordinate(0,0,0);

    /** Constructs a TriangleGridRenderer with a given grid, canvas dimensions and edge length. */
    public TriangleGridRenderer(TriangleGrid grid, int canvasWidthPixels, int canvasHeightPixels, int edgeLength) {
        this.grid = grid;
        this.canvasWidthPixels = canvasWidthPixels;
        this.canvasHeightPixels = canvasHeightPixels;
        this.geometry = new GridGeometry(edgeLength);
        halfCanvasWidthTriangles = (int) (canvasWidthPixels / geometry.columnSpacing) + 1 / 2 ;
        halfCanvasHeightTriangles = (int) (canvasHeightPixels / geometry.rowSpacing) + 1 / 2;
    }

    /** Draws the grid on the provided Graphics2D object. */
    public void drawGrid(Graphics2D g2d) {
        // 1) Loop through each drawing operation
        // 2) Calculate render position of each node
        // 3) Render via the drawNode method

        TriangleCoordinate flooredCamera = cameraPosition.flooredTriangleCoordinate();

        //camera centre offset by half the screen
        double offsetX = geometry.gridToScreenX(cameraPosition.a, cameraPosition.b) - canvasWidthPixels / 2;
        double offsetY = geometry.gridToScreenY(cameraPosition.b) - canvasHeightPixels / 2;

        for (int drawingOperation = 0; drawingOperation < 3; drawingOperation++) {
            for (int y = -halfCanvasHeightTriangles; y < halfCanvasHeightTriangles; y++) {
                for (int x = -halfCanvasWidthTriangles; x <  halfCanvasWidthTriangles; x++) {
                    //floored camera centre offset by current loop coordinate
                    //this allows for the fractional camera coordinate to be centred exactly
                    TriangleCoordinate loopCoordinate = TriangleCoordinate.fromOffsetCoordinate(x , y);
                    TriangleCoordinate renderCoordinate = flooredCamera.subtractCoordinate(loopCoordinate);

                    //convert to screen coords and subtract offset
                    Point2D vertexPosition = geometry.gridToScreenCoordinate(renderCoordinate);
                    vertexPosition = new Point2D.Double((vertexPosition.getX() - offsetX), (vertexPosition.getY() - offsetY));

                    //centre graphics to centre of node
                    g2d.translate(vertexPosition.getX(), vertexPosition.getY());
                    drawNode(g2d, renderCoordinate, drawingOperation);

                    //reset
                    g2d.translate(-vertexPosition.getX(), -vertexPosition.getY());
                }
            }
        }
    }

    /** Draws the given node based on the current drawing operation */
    protected void drawNode(Graphics2D g2d, TriangleCoordinate coordinate, int drawingOperation) {
        TriangleGrid.TriangleGridNode node = grid.getNode(coordinate);

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


        Font font = new Font("Arial", Font.PLAIN, 12);
        g2d.setColor(Color.black);

        g2d.setFont(font);
        g2d.drawString(coordinate.a+","+coordinate.b,-10,5);
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

    public GridGeometry getGeometry() {
        return geometry;
    }

    public FractionalCoordinate getPosition() {
        return cameraPosition;
    }

    public void setPosition(FractionalCoordinate coordinate) {
        cameraPosition = coordinate;
    }

    public void setPosition(int x, int y) {
        setPosition(
                geometry.screenToGridCoordinate(x, y));
    }

    /** Draws any of the given triangle edges onto the Graphics2D object. The Graphics2D origin is set to the start of the edge. */
    protected abstract void drawEdge(Graphics2D g2d, Edge edge, double edgeFinishX, double edgeFinishY);

    /** Draws either a pointy up, or pointy down triangle onto the Graphics2D object. The Graphics2D origin is aligned with the triangleShape. */
    protected abstract void drawFace(Graphics2D g2d, TriangleFace face, Shape triangleShape);

    /** Draws the given vertex onto the Graphics2D object. The Graphics2D origin is set to the vertex centre. */
    protected abstract void drawVertex(Graphics2D g2d, Vertex vertex);

    public int getRenderWidth() {
        return canvasWidthPixels;
    }

    public int getRenderHeight() {
        return canvasHeightPixels;
    }
}
