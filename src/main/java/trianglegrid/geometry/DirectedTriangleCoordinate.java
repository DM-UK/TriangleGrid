package trianglegrid.geometry;

public class DirectedTriangleCoordinate {
    public final TriangleCoordinate coordinate;
    public final int direction;

    public DirectedTriangleCoordinate(TriangleCoordinate coordinate, int direction){
        this.coordinate = coordinate;
        this.direction = direction;
    }
}
