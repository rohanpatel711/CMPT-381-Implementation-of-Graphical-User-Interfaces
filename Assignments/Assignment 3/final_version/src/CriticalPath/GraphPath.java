package CriticalPath;


import java.util.ArrayList;

public class GraphPath {
    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;
    private double totalCost;

    public GraphPath() {
        nodes= new ArrayList<>();
        edges=new ArrayList<>();
        totalCost=0;
    }

    public void addNode(Node node){
        nodes.add(node);
    }

    public void addEdge(Edge edge){
        edges.add(edge);
    }

    public void increaseTotalCost(double dCost){

    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    public void setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public double getTotalCost() {
        return totalCost;
    }
}
