package trianglegrid.grid;

import trianglegrid.geometry.TriangleCoordinate;

/** Represents a grid of triangle vertices, edges, and faces. */
public class TriangleGrid {
    private TriangleGridNode[][] nodes;

    /** Dimensions in number of vertices. */
    private final int gridWidth;
    private final int gridHeight;

    public TriangleGrid(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        nodes = new TriangleGridNode[gridHeight][];
        createGrid();
    }

    private void createGrid() {
        for (int y = 0; y < gridHeight; y++) {
            nodes[y] = new TriangleGridNode[gridWidth];
            for (int x = 0; x < gridWidth; x++) {
                TriangleCoordinate coordinate = new TriangleCoordinate(x, y);
                //validate for border vertices
                boolean vertex0 = isInBounds(coordinate.getNeighbour(0));
                boolean vertex1 = isInBounds(coordinate.getNeighbour(1));
                boolean vertex2 = isInBounds(coordinate.getNeighbour(2));
                boolean vertex3 = isInBounds(coordinate.getNeighbour(3));
                boolean vertex4 = isInBounds(coordinate.getNeighbour(4));
                boolean facePointyUp = vertex0 && vertex1;
                boolean facePointyDown = vertex3 && vertex4;
                nodes[y][x] = new TriangleGridNode(vertex0, vertex1, vertex2, facePointyUp, facePointyDown);
            }
        }
    }

    /** Determines whether a coordinate falls within the grid */
    private boolean isInBounds(TriangleCoordinate coordinate) {
        return isInBounds(coordinate.x, coordinate.y);
    }

    /** Determines whether a coordinate falls within the grid */
    private boolean isInBounds(int x, int y) {
        if (y < 0 || y >= gridHeight || x < 0 || x >= gridWidth)
            return false;
        else
            return true;
    }

    /** Return a TriangleGridNode. null if coordinate falls outside grid  */
    public TriangleGridNode getNode(int x, int y) {
        if (!isInBounds(x, y))
            return null;
        else
            return nodes[y][x];
    }

    /** Return a Vertex. null if coordinate falls outside grid */
    public Vertex getVertex(TriangleCoordinate coordinate) {
        TriangleGridNode node = getNode(coordinate.x, coordinate.y);

        if (node != null)
            return node.vertex;
        else
            return null;
    }

    /** Return Edge given a vertex coordinate and a direction. null if coordinate falls outside grid */
    public Edge getEdge(TriangleCoordinate coordinate, int edgeIndex) {
        if (edgeIndex >= 3) {
            coordinate = coordinate.getNeighbour(edgeIndex);
            edgeIndex = edgeIndex - 3;
        }

        TriangleGridNode node = getNode(coordinate.x, coordinate.y);

        if (node != null)
            return node.edges[edgeIndex];
        else
            return null;
    }

    private TriangleFace getFace(TriangleCoordinate coordinate, boolean pointing) {
        TriangleGridNode node = getNode(coordinate.x, coordinate.y);

        if (node != null) {
            if (pointing == TriangleFace.POINTY_UP) {
                return node.faceUp;
            } else {
                return node.faceDown;
            }
        } else
            return null;
    }

    /** Return Face given a vertex coordinate and a direction. null if coordinate falls outside grid */
    public TriangleFace getFace(TriangleCoordinate coordinate, int direction) {
        switch (direction % 6) {
            case 0:
                return getFace(coordinate.getNeighbour(0), TriangleFace.POINTY_DOWN);
            case 1:
                return getFace(coordinate, TriangleFace.POINTY_UP);
            case 2:
                return getFace(coordinate.getNeighbour(1), TriangleFace.POINTY_DOWN);
            case 3:
                return getFace(coordinate.getNeighbour(3), TriangleFace.POINTY_UP);
            case 4:
                return getFace(coordinate, TriangleFace.POINTY_DOWN);
            case 5:
                return getFace(coordinate.getNeighbour(4), TriangleFace.POINTY_UP);
        }

        //never called
        return null;
    }

    /** Grid width in vertices */
    public int getWidth() {
        return gridWidth;
    }

    /** Grid height in vertices */
    public int getHeight() {
        return gridHeight;
    }

    /** Structure holding all grid data.
     *  Each node contains:
     *  1 vertex,
     *  2 faces,
     *  3 edges.
     *
     *  Every cell is guaranteed to contain a vertex, but not necessarily all faces and edges (around the grid border).
     *  */
    public static class TriangleGridNode {
        public final Vertex vertex;
        public final TriangleFace faceUp;
        public final TriangleFace faceDown;
        public final Edge[] edges = new Edge[3];

        public TriangleGridNode(boolean edge0, boolean edge1, boolean edge2, boolean facePointyUp, boolean facePointyDown) {
            vertex = new Vertex();

            //boolean values determine whether they exist or not (eg. border)
            if (edge0)
                edges[0] = new Edge();
            if (edge1)
                edges[1] = new Edge();
            if (edge2)
                edges[2] = new Edge();

            if (facePointyUp)
                faceUp = new TriangleFace();
            else
                faceUp = null;

            if (facePointyDown)
                faceDown = new TriangleFace();
            else
                faceDown = null;
        }
    }
}
