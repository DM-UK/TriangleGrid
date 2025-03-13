package trianglegrid;

import trianglegrid.grid.*;
import trianglegrid.grid.Vertex;
import trianglegrid.ui.TriangleGridRenderer;

import java.awt.*;

public class DemoGridRender extends TriangleGridRenderer {
    public DemoGridRender(TriangleGrid grid, int canvasWidth, int canvasHeight, int edgeLength) {
        super(grid, canvasWidth, canvasHeight, edgeLength);
    }

    @Override
    protected void drawEdge(Graphics2D g2d, Edge edge, double edgeFinishX, double edgeFinishY) {
        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(10.0f));
        g2d.drawLine(0, 0, (int) edgeFinishX, (int) edgeFinishY);
        if (edge.binaryValue)
            g2d.setColor(Color.red);
        else
            g2d.setColor(Color.blue);
        g2d.setStroke(new BasicStroke(3.0f));
        g2d.drawLine(0, 0, (int) edgeFinishX, (int) edgeFinishY);
    }

    @Override
    protected void drawFace(Graphics2D g2d, TriangleFace face, Shape triangleShape) {
        g2d.setColor(Color.green);
        g2d.fill(triangleShape);
    }

    @Override
    protected void drawVertex(Graphics2D g2d, Vertex vertex) {
        g2d.setColor(Color.white);
        g2d.fillOval(-6, -6, 12, 12);
        g2d.setColor(Color.black);
        g2d.drawOval(-6, -6, 12, 12);
    }
}
