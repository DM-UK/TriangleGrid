package trianglegrid.structure;

public class TriangleGrid {
    private TriangleGridNode[][] nodes;
    private final int gridWidth;
    private final int gridHeight;
    public static final int[][][] directionDifferences = {
            // even rows
            {
                {0,  -1},{ +1, -1}, {+1,  0}, {+1, +1}, { 0, +1}, {-1, 0}
            },
            // odd rows
            {
                {-1, -1}, { 0, -1}, {+1, 0}, {0,  +1}, { -1, +1}, { -1, +0}
            }
    };

    public TriangleGrid(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        nodes = new TriangleGridNode[gridHeight][];
        createGrid();
        trimBorderNodes();
    }

    private void createGrid() {
        for (int y=0; y < gridHeight; y++) {
            nodes[y] = new TriangleGridNode[gridWidth];
            for (int x = 0; x < gridWidth; x++) {
                nodes[y][x] = new TriangleGridNode();
            }
        }
    }

    private void trimBorderNodes() {
        for (int y=0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                for (int edgeIndex = 0; edgeIndex < 3; edgeIndex++)
                    if (!nodeNeighbourExists(x, y, edgeIndex))
                        getNode(x, y).edges[edgeIndex] = null;

                if (!nodeNeighbourExists(x, y, 0) || !nodeNeighbourExists(x, y, 1))
                    getNode(x, y).faceUp = null;

                if (!nodeNeighbourExists(x, y, 3) || !nodeNeighbourExists(x, y, 4))
                    getNode(x, y).faceDown = null;
            }
        }
    }

    private boolean nodeNeighbourExists(int x, int y, int hexagonDirectionIndex) {
        if (getNodeNeighbour(x, y, hexagonDirectionIndex) == null)
            return false;
        else
            return true;
    }

    public TriangleGridNode getNodeNeighbour(int x, int y, int hexagonDirectionIndex){
        int parity = y & 1;
        int[] diff = directionDifferences[parity][hexagonDirectionIndex];
        return getNode(x + diff[0], y + diff[1]);
    }

    public TriangleGridNode getNode(int x, int y){
        if  (y < 0 || y >= gridHeight)
            return null;

        if  (x < 0 || x >= gridWidth)
            return null;

        return nodes[y][x];
    }

    public Vertex getVertice(int x, int y){
        return nodes[y][x].vertex;
    }

    public Edge getEdge(int x, int y, int edgeIndex){
        return nodes[y][x].edges[edgeIndex];
    }

    public TriangleFace getFace(int x, int y, boolean pointing){
        if (pointing = TriangleFace.POINTY_UP)
            return nodes[y][x].faceUp;
        else
            return nodes[y][x].faceDown;
    }

    public int getWidth() {
        return gridWidth;
    }

    public int getHeight() {
        return gridHeight;
    }
}
