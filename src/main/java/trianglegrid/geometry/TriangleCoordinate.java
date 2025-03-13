package trianglegrid.geometry;


/** Stores both cube and axial coordinates: https://www.redblobgames.com/grids/hexagons/#coordinates **/
public class TriangleCoordinate {
    public final int a, b, c;
    public final int x, y;

    public static final int[][] cubeDirectionVectors = {
            {0, -1, +1}, {+1, -1, 0}, {+1, 0, -1}, {0, +1, -1}, {-1, +1, 0}, {-1, 0, +1}

    };

    public TriangleCoordinate(int x, int y){
        this.x = x;
        this.y = y;
        a = x - (y + (y&1)) / 2;
        b = y;
        c = -a - b;
    }

    public TriangleCoordinate(int a, int b, int c){
        this.a = a;
        this.b = b;
        this.c = c;
        x = a + (b + (b&1)) / 2;
        y = b;
    }

    public TriangleCoordinate getNeighbour(int direction) {
        int neighbourA = a + cubeDirectionVectors[direction][0];
        int neighbourB = b + cubeDirectionVectors[direction][1];
        int neighbourC = c + cubeDirectionVectors[direction][2];
        return new TriangleCoordinate(neighbourA, neighbourB, neighbourC);
    }
}
