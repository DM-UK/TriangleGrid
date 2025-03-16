package trianglegrid.ui;

import trianglegrid.coordinates.DirectedCoordinate;
import trianglegrid.coordinates.FractionalCoordinate;
import trianglegrid.coordinates.TriangleCoordinate;
import trianglegrid.grid.*;

public interface GridSelectionListener {
    void vertexSelected(TriangleCoordinate vertexCoordinate, Vertex vertex);
    void edgeSelected(DirectedCoordinate edgeCoordinate, Edge edge);
    void faceSelected(DirectedCoordinate faceCoordinate, TriangleFace face);
    void gridSelected(FractionalCoordinate selectionCoordinate);
}
