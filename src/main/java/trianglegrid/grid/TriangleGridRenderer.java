package trianglegrid.grid;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

/**
 * Abstract class that renders a triangle grid on a Graphics2D canvas.
 * It handles the drawing of grid nodes, edges, and faces.
 */
public abstract class TriangleGridRenderer {
    /** Grid to render */
    private final TriangleGrid grid;
    /** Hexagon used for geometry */
    private final Hexagon hexagon;
    /** Triangle shape in the two orientations  */
    private final Shape pointyUpTriangle;
    private final Shape pointyDownTriangle;
    /** Distance between two triangles along x-axis  */
    private final double columnSpacing;
    /** Distance between two triangles along y-axis  */
    private final double rowSpacing;
    /** Offset to be applied to the x-axis on every other row*/
    private final double oddRowOffset;

    /**
     * Constructs a TriangleGridRenderer with a given grid and edge length.
     *
     * @param grid The triangle grid to render.
     * @param edgeLength The length of each hexagon's edge.
     */
    public TriangleGridRenderer(TriangleGrid grid, double edgeLength) {
        this.grid = grid;
        //initialize variables needed for geometry
        this.hexagon = new Hexagon(edgeLength);
        this.pointyUpTriangle = hexagon.createTriangle(0, 1);
        this.pointyDownTriangle = hexagon.createTriangle(3, 4);
        this.columnSpacing = hexagon.edgeLength;
        this.rowSpacing = hexagon.halfHeight;
        this.oddRowOffset = hexagon.halfEdgeLength;
    }

    /**
     * Draws the entire grid on the provided Graphics2D object.
     *
     * @param g2d The Graphics2D object to render the grid onto.
     */
    public void drawGrid(Graphics2D g2d) {
        AffineTransform originalTransform = g2d.getTransform();

        // Iterate through grid drawing operations: Faces, Edges, Vertices
        // moving the Graphics2D object to the required vertex position
        for (int drawingOperation = 0; drawingOperation < 3; drawingOperation++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                if (y % 2 == 1)
                    g2d.translate(-oddRowOffset, 0);

                for (int x = 0; x < grid.getWidth(); x++) {
                    TriangleGridNode node = grid.getNode(x, y);

                    if (node == null)
                        break;

                    drawNode(g2d, node, drawingOperation);
                    g2d.translate(columnSpacing, 0);
                }

                //reset
                g2d.setTransform(originalTransform);
                g2d.translate(0, (y + 1) * rowSpacing);
            }

            //reset
            g2d.setTransform(originalTransform);
        }
    }

    /**
     * Draws the given node based on the current drawing operation (Faces, Edges, Vertices).
     *
     * @param g2d The Graphics2D object to render onto.
     * @param node The node to be drawn.
     * @param drawingOperation The type of drawing (0=Faces, 1=Edges, 2=Vertices).
     */
    protected void drawNode(Graphics2D g2d, TriangleGridNode node, int drawingOperation) {
        switch (drawingOperation) {
            case 0:
                drawFaces(g2d, node.faceUp, node.faceDown);
                break;
            case 1:
                drawEdges(g2d, node.edges);
                break;
            case 2:
                drawVertex(g2d, node.vertex);
                break;
        }
    }

    /**
     * Draws the edges of the given node.
     *
     * @param g2d The Graphics2D object to render onto.
     * @param edges The array of edges for the node.
     */
    protected void drawEdges(Graphics2D g2d, Edge[] edges) {
        for (int edgeIndex = 0; edgeIndex < 3; edgeIndex++) {
            Point2D vertex = hexagon.getVertex(edgeIndex);

            if (edges[edgeIndex] != null)
                drawEdge(g2d, edges[edgeIndex], vertex.getX(), vertex.getY());
        }
    }

    /**
     * Draws the faces of the given node.
     *
     * @param g2d The Graphics2D object to render onto.
     * @param faceUp The "up" facing triangle face.
     * @param faceDown The "down" facing triangle face.
     */
    protected void drawFaces(Graphics2D g2d, TriangleFace faceUp, TriangleFace faceDown) {
        if (faceUp != null)
            drawFace(g2d, faceUp, pointyUpTriangle);

        if (faceDown != null)
            drawFace(g2d, faceDown, pointyDownTriangle);
    }

    /**
     * Draws an individual edge on the Graphics2D object.
     *
     * @param g2d The Graphics2D object to render onto.
     * @param edge The edge to be drawn.
     * @param edgeFinishX The X-coordinate of the end vertex of the edge.
     * @param edgeFinishY The Y-coordinate of the end vertex of the edge.
     */
    protected abstract void drawEdge(Graphics2D g2d, Edge edge, double edgeFinishX, double edgeFinishY);

    /**
     * Draws an individual face on the Graphics2D object.
     *
     * @param g2d The Graphics2D object to render onto.
     * @param face The face to be drawn.
     * @param triangleShape The shape of the triangle.
     */
    protected abstract void drawFace(Graphics2D g2d, TriangleFace face, Shape triangleShape);

    /**
     * Draws an individual vertex on the Graphics2D object.
     *
     * @param g2d The Graphics2D object to render onto.
     * @param vertex The vertex.
     */
    protected abstract void drawVertex(Graphics2D g2d, Vertex vertex);

    public static class Hexagon {
        /** Cache root 3 */
        private static final double SQRT_3 = Math.sqrt(3);
        /** Hexagon vertices from centre of hexagon */
        private final Point2D[] vertices = new Point2D[6];
        /** Length of an edge */
        final double edgeLength;
        final double halfEdgeLength;
        /** Maximum height of hexagon */
        final double height;
        final double halfHeight;
        /** Maximum width of hexagon */
        final double width;

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

        /**
         * Returns the vertex at the given index.
         *
         * @param vertexIndex The index of the vertex (0-5).
         * @return The Point2D representing the vertex.
         */
        public Point2D getVertex(int vertexIndex) {
            return vertices[vertexIndex % 6];
        }

        /**
         * Creates a triangle shape from the centre to two vertices in the hexagon.
         *
         * @param vertexIndexA The first vertex index (0-5).
         * @param vertexIndexB The second vertex index (0-5).
         * @return The Shape representing the triangle.
         */
        public Shape createTriangle(int vertexIndexA, int vertexIndexB) {
            Path2D path = new Path2D.Double();
            path.moveTo(0, 0);
            path.lineTo(getVertex(vertexIndexA).getX(),
                    getVertex(vertexIndexA).getY());

            path.lineTo(getVertex(vertexIndexB).getX(),
                    getVertex(vertexIndexB).getY());

            path.closePath();
            return path;
        }
    }
}
