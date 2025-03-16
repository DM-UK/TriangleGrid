package trianglegrid.coordinates;

public class FractionalCoordinate {
    public final double a, b, c;

    public FractionalCoordinate(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public TriangleCoordinate roundedTriangleCoordinate() {
        int roundedA = (int) Math.round(a);
        int roundedB = (int) Math.round(b);
        int roundedC = (int) Math.round(c);
        double diffA = Math.abs(a - roundedA);
        double diffB = Math.abs(b - roundedB);
        double diffC = Math.abs(c - roundedC);

        if (diffA > diffB && diffA > diffC)
            roundedA = -roundedB - roundedC;
        else if (diffB > diffA && diffB > diffC)
            roundedB = -roundedA - roundedC;
        else
            roundedC = -roundedA - roundedB;

        return new TriangleCoordinate(roundedA, roundedB, roundedC);
    }

    public TriangleCoordinate flooredTriangleCoordinate() {
        double flooredA = Math.floor(a);
        double flooredB = Math.floor(b);
        return new TriangleCoordinate((int) flooredA, (int) flooredB, (int) (-flooredA - flooredB));
    }

    public FractionalCoordinate addCoordinate(FractionalCoordinate other){
        return new FractionalCoordinate(a + other.a, b + other.b, c + other.c);
    }

    public FractionalCoordinate subtractCoordinate(FractionalCoordinate other){
        return new FractionalCoordinate(a - other.a, b - other.b, c - other.c);
    }

    @Override
    public String toString() {
        return "FractionalTriangleCoordinate{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                '}';
    }
}
