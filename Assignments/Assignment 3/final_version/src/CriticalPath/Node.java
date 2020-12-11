package CriticalPath;

import java.util.ArrayList;

public class Node{
    double left, top, width, height;

    public boolean visited,exploring;
    private double weight;

    private String activityName;

    ArrayList<Edge> outGoingEdges;

    public Node(double newLeft, double newTop, double newWidth, double newHeight) {
        left = newLeft;
        top = newTop;
        width = newWidth;
        height = newHeight;
        weight=1.0;
        visited=false;
        exploring=false;
        activityName="Activity";
        outGoingEdges=new ArrayList<>();
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityName() {
        return activityName;
    }

    public double getWeight() {
        return weight;
    }


    public boolean checkHit(double x, double y) {

        return x >= left && x <= left+width && y >= top && y <= top+height;

    }

    public void move(double dX, double dY) {
        left += dX;
        top += dY;
    }

    @Override
    public String toString() {
        return getActivityName();
    }

    public void addOutGoingEdge(Edge edge){
        outGoingEdges.add(edge);
    }

    public boolean findCycles(){
        if (visited)
            return false;
        if (exploring)
            return true;

        exploring=true;

        for (Edge outGoingEdge:outGoingEdges){
            boolean check=outGoingEdge.getEndNode().findCycles();
            if (check)
                return true;
        }
        visited=true;
        return false;

    }

    public void removeOutGoingEdge(Edge edge){
        outGoingEdges.remove(edge);
    }

    public int getNumOfOutgoingEdges(){
        return outGoingEdges.size();
    }

    public void findPaths(GraphPath currentPath,ArrayList<GraphPath> allPaths){
        if(outGoingEdges.size()==0){
            allPaths.add(currentPath);
            return;
        }

        for (Edge outGoingEdge:outGoingEdges){
            GraphPath continuePath=new GraphPath();
            continuePath.setNodes((ArrayList<Node>) currentPath.getNodes().clone());
            continuePath.setEdges((ArrayList<Edge>) currentPath.getEdges().clone());

            continuePath.addNode(outGoingEdge.getEndNode());
            continuePath.addEdge(outGoingEdge);

            outGoingEdge.getEndNode().findPaths(continuePath,allPaths);

        }
    }
}
