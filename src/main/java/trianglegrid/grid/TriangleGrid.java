package trianglegrid.grid;

/**
 * The TriangleGrid class represents a grid of triangular nodes, with methods to manage and access nodes, edges, and faces.
 */
public class TriangleGrid {
    // 2D array of node objects representing the grid.
    private TriangleGridNode[][] nodes;

    // Grid dimensions: width and height.
    private final int gridWidth;
    private final int gridHeight;

    // Direction differences for the grid (for even and odd rows).
    public static final int[][][] directionDifferences = {
            // even rows
            {
                    {0,  -1}, { +1, -1}, {+1,  0}, {+1, +1}, { 0, +1}, {-1, 0}
            },
            // odd rows
            {
                    {-1, -1}, { 0, -1}, {+1, 0}, {0,  +1}, { -1, +1}, { -1, +0}
            }
    };

    /**
     * Constructor to create a TriangleGrid with specified width and height.
     *
     * @param gridWidth the number of vertices in the horizontal direction.
     * @param gridHeight the number of vertices in the vertical direction.
     */
    public TriangleGrid(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        nodes = new TriangleGridNode[gridHeight][];
        createGrid();
        trimBorderNodes();
    }

    /**
     * Initializes the grid by creating a new TriangleGridNode for each cell in the grid.
     */
    private void createGrid() {
        // Iterate over the rows
        for (int y = 0; y < gridHeight; y++) {
            // Initialize the columns for each row
            nodes[y] = new TriangleGridNode[gridWidth];
            for (int x = 0; x < gridWidth; x++) {
                // Create a new node for each (x, y) position
                nodes[y][x] = new TriangleGridNode();
            }
        }
    }

    /**
     * Trims the border nodes, removing edges and faces that don't have valid neighbors.
     */
    private void trimBorderNodes() {
        // Iterate over each node in the grid
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                // Trim edges that don't have neighbors
                for (int edgeIndex = 0; edgeIndex < 3; edgeIndex++) {
                    if (!nodeNeighbourExists(x, y, edgeIndex))
                        getNode(x, y).edges[edgeIndex] = null;  // Remove edge
                }

                // Trim faces that don't have valid neighbors
                if (!nodeNeighbourExists(x, y, 0) || !nodeNeighbourExists(x, y, 1))
                    getNode(x, y).faceUp = null;  // Remove upper face

                if (!nodeNeighbourExists(x, y, 3) || !nodeNeighbourExists(x, y, 4))
                    getNode(x, y).faceDown = null;  // Remove lower face
            }
        }
    }

    /**
     * Checks if a neighbor exists for the node at the given (x, y) position in the given direction.
     *
     * @param x the x-coordinate of the node.
     * @param y the y-coordinate of the node.
     * @param hexagonDirectionIndex the direction index (0-5).
     * @return true if the neighbor exists, false otherwise.
     */
    private boolean nodeNeighbourExists(int x, int y, int hexagonDirectionIndex) {
        return getNodeNeighbour(x, y, hexagonDirectionIndex) != null;
    }

    /**
     * Gets the neighboring node at the specified (x, y) position and direction.
     *
     * @param x the x-coordinate of the node.
     * @param y the y-coordinate of the node.
     * @param hexagonDirectionIndex the direction index (0-5).
     * @return the neighboring node, or null if no neighbor exists.
     */
    public TriangleGridNode getNodeNeighbour(int x, int y, int hexagonDirectionIndex) {
        int parity = y & 1;  // Check if the row is odd or even
        int[] diff = directionDifferences[parity][hexagonDirectionIndex];  // Get the direction difference
        return getNode(x + diff[0], y + diff[1]);  // Get the neighboring node based on the direction
    }

    /**
     * Gets the node at the specified (x, y) position.
     *
     * @param x the x-coordinate of the node.
     * @param y the y-coordinate of the node.
     * @return the node at the given coordinates, or null if out of bounds.
     */
    public TriangleGridNode getNode(int x, int y) {
        if (y < 0 || y >= gridHeight || x < 0 || x >= gridWidth) {
            return null;  // Return null if the coordinates are out of bounds
        }
        return nodes[y][x];
    }

    /**
     * Gets the vertex of the node at the specified (x, y) position.
     *
     * @param x the x-coordinate of the node.
     * @param y the y-coordinate of the node.
     * @return the vertex of the node at the given position.
     */
    public Vertex getVertex(int x, int y) {
        return nodes[y][x].vertex;
    }

    /**
     * Gets the edge at the specified (x, y) position and edge index.
     *
     * @param x the x-coordinate of the node.
     * @param y the y-coordinate of the node.
     * @param edgeIndex the index of the edge (0-2).
     * @return the edge at the given position and index.
     */
    public Edge getEdge(int x, int y, int edgeIndex) {
        return nodes[y][x].edges[edgeIndex];
    }

    /**
     * Gets the face of the node at the specified (x, y) position, based on the direction (up or down).
     *
     * @param x the x-coordinate of the node.
     * @param y the y-coordinate of the node.
     * @param pointing true for the upper face (pointy up), false for the lower face (pointy down).
     * @return the face of the node in the specified direction.
     */
    public TriangleFace getFace(int x, int y, boolean pointing) {
        if (pointing == TriangleFace.POINTY_UP) {
            return nodes[y][x].faceUp;  // Return the upper face
        } else {
            return nodes[y][x].faceDown;  // Return the lower face
        }
    }

    /**
     * Gets the width of the grid (number of nodes in the horizontal direction).
     *
     * @return the width of the grid.
     */
    public int getWidth() {
        return gridWidth;
    }

    /**
     * Gets the height of the grid (number of nodes in the vertical direction).
     *
     * @return the height of the grid.
     */
    public int getHeight() {
        return gridHeight;
    }
}
