package trianglegrid;


import trianglegrid.coordinates.DirectedCoordinate;
import trianglegrid.coordinates.FractionalCoordinate;
import trianglegrid.coordinates.TriangleCoordinate;
import trianglegrid.grid.*;
import trianglegrid.ui.TriangleGridPane;
import trianglegrid.ui.GridSelectionListener;

import javax.swing.*;

public class TriangleGridDemo implements GridSelectionListener {
    private final TriangleGrid grid = new TriangleGrid(2000, 2000);
    private final DemoGridRender renderer = new DemoGridRender(grid, 1000, 1000, 80);
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
        renderer.selection = vertex;
        triangleGridPane.repaint();
    }

    @Override
    public void edgeSelected(DirectedCoordinate edgeCoordinate, Edge edge) {
        renderer.selection = edge;
        triangleGridPane.repaint();
    }

    @Override
    public void faceSelected(DirectedCoordinate faceCoordinate, TriangleFace face) {
        renderer.selection = face;
        triangleGridPane.repaint();
    }

    @Override
    public void gridSelected(FractionalCoordinate selectionCoordinate) {
        renderer.setPosition(selectionCoordinate);
        triangleGridPane.repaint();
    }

    public static void main(String[] args) {
        new TriangleGridDemo();
    }
}