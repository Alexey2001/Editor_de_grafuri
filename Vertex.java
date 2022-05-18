package editor_grafuri;


import java.awt.Color;

public class Vertex {

    private String key;
    private String edgeKey;
    private Vertex adjacentVertex;
    private Vertex nextVertex;
    protected int xCoord; //coordonate
    protected int yCoord;
    protected int zCoord;
    Color color;

    public Vertex(String key) { //constructor

        this.key = key; //cheia pentru varf (pt moduri)
        this.edgeKey = "";
        this.nextVertex = null;
        this.adjacentVertex = null;
        color = new Color(59, 118, 59); //setarea culorii cand adugam initial un varf
    }

    public String getKey() { //geter-ul clasei (returnarea unei valori pt un anumit model)
        return key;
    }

    public Vertex getNextVertex() {
        return this.nextVertex;
    }

    public Vertex getAdjacentVertex() {
        return this.adjacentVertex;
    }

    public String getEdgeKey() {
        return edgeKey;
    }


    public int getxCoord() {
        return xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public int getzCoord() {
        return zCoord;
    }

    public Color getColor() {
        return color;
    }


    public void setX(int xCoord) { //opusul lui get
        this.xCoord = xCoord;
    }

    public void setY(int yCoord) {
        this.yCoord = yCoord;
    }

    public void setZ(int zCoord) {
        this.zCoord = zCoord;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setNextVertex(Vertex nextVertex) {
        this.nextVertex = nextVertex;
    }

    public void setAdjacentVertex(Vertex vertex) {
        this.adjacentVertex = vertex;
    }

    public void setEdgeKey(String edgeKey) {
        this.edgeKey = edgeKey;
    }

}
