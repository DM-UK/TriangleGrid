package trianglegrid.ui;

import trianglegrid.DemoGridRender;
import trianglegrid.coordinates.DirectedCoordinate;
import trianglegrid.coordinates.FractionalCoordinate;
import trianglegrid.coordinates.TriangleCoordinate;
import trianglegrid.grid.GridGeometry;
import trianglegrid.grid.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TriangleGridPane extends JPanel {
    private final static int SELECTABLE_LINE_WIDTH = 3;
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

    public void addTriangleSelectionListener(GridSelectionListener listener){
        addMouseListener(new TriangleSelectionMouseAdapter(listener));
    }

    private class TriangleSelectionMouseAdapter extends MouseAdapter {
        private final GridSelectionListener listener;

        public TriangleSelectionMouseAdapter(GridSelectionListener listener) {
            this.listener = listener;
        }

        public void mousePressed(MouseEvent e) {
            if (listener == null)
                return;

            GridGeometry geometry = renderer.getGeometry();

            //mouse to grid coordinates and adjust for camera
            FractionalCoordinate mouseCoordinate = geometry.screenToGridCoordinate(
                    e.getX() - renderer.getRenderWidth() / 2,
                    e.getY() - renderer.getRenderHeight() / 2);

            FractionalCoordinate cameraCoordinate = renderer.getPosition();
            FractionalCoordinate adjustedMouseCoordinate = mouseCoordinate.addCoordinate(cameraCoordinate);

            listener.gridSelected(adjustedMouseCoordinate);

            //check for vertex click
            if (geometry.nearestVertexDistance(adjustedMouseCoordinate) < SELECTABLE_VERTEX_RADIUS){
                TriangleCoordinate vertexCoordinate = geometry.nearestVertexCoordinate(adjustedMouseCoordinate);
                Vertex vertex = grid.getVertex(vertexCoordinate);

                if (vertex != null)
                   listener.vertexSelected(vertexCoordinate, vertex);
            }
            //check for edge click
            else if (geometry.nearestEdgeDistance(adjustedMouseCoordinate) < SELECTABLE_LINE_WIDTH){
                DirectedCoordinate edgeCoordinate = geometry.nearestEdgeCoordinate(adjustedMouseCoordinate);
                Edge edge = grid.getEdge(edgeCoordinate.coordinate, edgeCoordinate.direction);

                if (edge != null)
                    listener.edgeSelected(edgeCoordinate, edge);
            }
            //check for face click
            else {
                DirectedCoordinate faceCoordinate = geometry.getFaceCoordinate(adjustedMouseCoordinate);
                TriangleFace face = grid.getFace(faceCoordinate.coordinate, faceCoordinate.direction);

                if (face != null)
                    listener.faceSelected(faceCoordinate, face);
            }

        }
    }
}
