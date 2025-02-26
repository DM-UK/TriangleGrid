package trianglegrid;

import trianglegrid.grid.*;
import trianglegrid.grid.Vertex;

import java.awt.*;
import java.util.Random;

public class DemoGridRender extends TriangleGridRenderer {

    public DemoGridRender(TriangleGrid grid, int edgeLength) {
        super(grid, edgeLength);
    }
    protected void drawNode(Graphics2D g2d, TriangleGridNode node, int drawingOperation) {
        Random random = new Random();
        //Color randomColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));

       // int rnd = node.hashCode();
        //Random random = new Random(rnd);
        Color randomColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));

        g2d.setColor(randomColor);

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
    @Override
    protected void drawEdge(Graphics2D g2d, Edge edge, double edgeFinishX, double edgeFinishY) {
        Color colour = g2d.getColor();
        g2d.setStroke(new BasicStroke(10.0f));
        g2d.setColor(Color.black);
        g2d.drawLine(0, 0, (int) edgeFinishX, (int) edgeFinishY);
        g2d.setColor(colour);
        g2d.setStroke(new BasicStroke(3.0f));
        g2d.drawLine(0, 0, (int) edgeFinishX, (int) edgeFinishY);
    }

    @Override
    protected void drawFace(Graphics2D g2d, TriangleFace faceUp, Shape triangleShape) {
        g2d.fill(triangleShape);
    }

    @Override
    protected void drawVertex(Graphics2D g2d, Vertex vertex) {
        g2d.fillOval(-8, -8, 16, 16);
        g2d.setColor(Color.black);
        g2d.drawOval(-8, -8, 16, 16);
    }

}
