package trianglegrid.coordinates;

public class DirectedCoordinate {
    public final TriangleCoordinate coordinate;
    public final int direction;

    public DirectedCoordinate(TriangleCoordinate coordinate, int direction){
        this.coordinate = coordinate;
        this.direction = direction;
    }
}
