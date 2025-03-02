# TriangleGrid

An implementation of a triangle-based grid system with point, edge, and face mechanics.

![](https://github.com/DM-UK/TriangleGrid/blob/master/src/main/img/4.png)

## Usage

        TriangleGrid grid = new TriangleGrid(8, 8);
        TriangleFace face = grid.getFace(2,7, TriangleFace.POINTY_UP);
        Edge edge = grid.getEdge(2,7, Edge.NORTH_WEST);
        Vertex vertex = grid.getVertice(2,7);


Rendering onto a Graphics2D canvas requires extending the TriangleGridRenderer and implementing the following methods:

    drawEdge(Graphics2D g2d, Edge edge, double edgeFinishX, double edgeFinishY);
    drawFace(Graphics2D g2d, TriangleFace face, Shape triangleShape);
    drawVertex(Graphics2D g2d, Vertex vertex);
## Grid Data Structure

Each node contains:
- 1 point
- 2 faces - N, S
- 3 edges - NW, NE, W


![](https://github.com/DM-UK/TriangleGrid/blob/master/src/main/img/0.png)


Tessellation onto a grid:

![](https://github.com/DM-UK/TriangleGrid/blob/master/src/main/img/1.png)

Validating for out of bounds edges (only 1 vertex) and out of bounds faces (less than 3 vertices:

![](https://github.com/DM-UK/TriangleGrid/blob/master/src/main/img/2.png)

Internal representation (each colour representing a different node):

![](https://github.com/DM-UK/TriangleGrid/blob/master/src/main/img/3.png)
