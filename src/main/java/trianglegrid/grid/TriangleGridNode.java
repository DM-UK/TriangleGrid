package trianglegrid.structure;

public class TriangleGridNode {
    public Vertex vertex;
    public TriangleFace faceUp;
    public TriangleFace faceDown;
    public Edge[] edges;

    public TriangleGridNode(){
        vertex = new Vertex();
        faceUp = new TriangleFace();
        faceDown = new TriangleFace();
        edges = new Edge[3];
        edges[0] = new Edge();
        edges[1] = new Edge();
        edges[2] = new Edge();
    }
}
