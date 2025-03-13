package trianglegrid;


import trianglegrid.geometry.DirectedTriangleCoordinate;
import trianglegrid.geometry.TriangleCoordinate;
import trianglegrid.grid.*;
import trianglegrid.ui.TriangleGridPane;
import trianglegrid.ui.TriangleSelectionListener;

import javax.swing.*;

public class TriangleGridDemo implements TriangleSelectionListener {
    private final TriangleGrid grid = new TriangleGrid(2000, 2000);
    private final DemoGridRender renderer = new DemoGridRender(grid, 1000, 1000, 50);
    private final TriangleGridPane triangleGridPane = new TriangleGridPane(renderer);

    public TriangleGridDemo()
    {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Triangle Grid Demo");
        frame.setSize(1000, 1000);
        triangleGridPane.addTriangleSelectionListener(this);
        frame.add(triangleGridPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void vertexSelected(TriangleCoordinate vertexCoordinate, Vertex vertex) {
        renderer.getPosition().setLocation(
                vertexCoordinate.x, vertexCoordinate.y);
        triangleGridPane.repaint();
    }

    @Override
    public void edgeSelected(DirectedTriangleCoordinate edgeCoordinate, Edge edge) {
    }

    @Override
    public void faceSelected(DirectedTriangleCoordinate faceCoordinate, TriangleFace face) {
    }

    public static void main(String[] args) {
        new TriangleGridDemo();
    }
}