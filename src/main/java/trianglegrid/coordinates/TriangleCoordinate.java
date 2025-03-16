package trianglegrid.coordinates;

public class TriangleCoordinate {
    public final int a, b, c;

    public static final TriangleCoordinate[] cubeDirectionVectors = {
            new TriangleCoordinate(0, -1, +1),
            new TriangleCoordinate(+1, -1, 0),
            new TriangleCoordinate(+1, 0, -1),
            new TriangleCoordinate(0, +1, -1),
            new TriangleCoordinate(-1, +1, 0),
            new TriangleCoordinate(-1, 0, +1)
    };

    public TriangleCoordinate(int a, int b, int c){
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public int getX(){
        return a + (b + (b&1)) / 2;
    }

    public int getY(){
        return b;

    }

    public TriangleCoordinate addCoordinate(TriangleCoordinate other){
        return new TriangleCoordinate(a + other.a, b + other.b, c + other.c);
    }

    public TriangleCoordinate subtractCoordinate(TriangleCoordinate other){
        return new TriangleCoordinate(a - other.a, b - other.b, c - other.c);
    }

    public TriangleCoordinate getNeighbour(int direction) {
        return addCoordinate(cubeDirectionVectors[direction % 6]);
    }

    public static TriangleCoordinate fromOffsetCoordinate(int x, int y) {
        int a = x - (y + (y&1)) / 2;
        int b = y;
        int c = -a - b;
        return new TriangleCoordinate(a, b, c);
    }

    @Override
    public String toString() {
        return "TriangleCoordinate{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                '}';
    }
}
