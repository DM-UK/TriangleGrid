package trianglegrid.ui;

import trianglegrid.geometry.DirectedTriangleCoordinate;
import trianglegrid.geometry.TriangleCoordinate;
import trianglegrid.grid.*;

public interface TriangleSelectionListener {
    void vertexSelected(TriangleCoordinate vertexCoordinate, Vertex vertex);
    void edgeSelected(DirectedTriangleCoordinate edgeCoordinate, Edge edge);
    void faceSelected(DirectedTriangleCoordinate faceCoordinate, TriangleFace face);
}
