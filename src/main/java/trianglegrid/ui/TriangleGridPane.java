package trianglegrid.ui;

import trianglegrid.DemoGridRender;
import trianglegrid.geometry.DirectedTriangleCoordinate;
import trianglegrid.geometry.TriangleGridGeometry;
import trianglegrid.geometry.TriangleCoordinate;
import trianglegrid.grid.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TriangleGridPane extends JPanel {
    private final static int SELECTABLE_LINE_WIDTH = 5;
    private final static int SELECTABLE_VERTEX_RADIUS = 15;
    private final TriangleGridRenderer renderer;
    private final TriangleGrid grid;

    public TriangleGridPane(DemoGridRender renderer){
        this.renderer = renderer;
        this.grid = renderer.getGrid();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        renderer.drawGrid(g2d);
    }

    public void addTriangleSelectionListener(TriangleSelectionListener listener){
        addMouseListener(new TriangleSelectionMouseAdapter(listener));
    }

    private class TriangleSelectionMouseAdapter extends MouseAdapter {
        private final TriangleSelectionListener listener;

        public TriangleSelectionMouseAdapter(TriangleSelectionListener listener) {
            this.listener = listener;
        }

        public void mousePressed(MouseEvent e){
            if (listener == null)
                return;

            TriangleGridGeometry geometry = renderer.getGeometry();
            //adjust for camera
            int adjustedMouseX = e.getX() + (int) geometry.gridToScreenX(renderer.getCameraOffsetX());
            int adjustedMouseY = e.getY() + (int) geometry.gridToScreenY(renderer.getCameraOffsetY());

            //check for vertex click
            if (geometry.screenToNearestVertexDistance(adjustedMouseX, adjustedMouseY) < SELECTABLE_VERTEX_RADIUS){
                TriangleCoordinate vertexCoordinate = geometry.screenToNearestVertexCoordinate(adjustedMouseX, adjustedMouseY);
                Vertex vertex = grid.getVertex(vertexCoordinate);

                if (vertex != null)
                    listener.vertexSelected(vertexCoordinate, vertex);
            }
            //check for edge click
            else if (geometry.screenToNearestEdgeDistance(adjustedMouseX, adjustedMouseY) < SELECTABLE_LINE_WIDTH){
                DirectedTriangleCoordinate edgeCoordinate = geometry.screenToNearestEdgeCoordinate(adjustedMouseX, adjustedMouseY);
                Edge edge = grid.getEdge(edgeCoordinate.coordinate, edgeCoordinate.direction);

                if (edge != null)
                    listener.edgeSelected(edgeCoordinate, edge);
            }
            //check for face click
            else {
                DirectedTriangleCoordinate faceCoordinate = geometry.screenToFaceCoordinate(adjustedMouseX, adjustedMouseY);
                TriangleFace face = grid.getFace(faceCoordinate.coordinate, faceCoordinate.direction);

                if (face != null)
                    listener.faceSelected(faceCoordinate, face);
            }
        }
    }
}
