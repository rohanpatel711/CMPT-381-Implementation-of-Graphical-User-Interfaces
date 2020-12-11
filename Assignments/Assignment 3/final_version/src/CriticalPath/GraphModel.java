package CriticalPath;


import java.util.*;

public class GraphModel {
    private HashMap<Node, LinkedList<Node>> graph;
    public ArrayList<Edge> edges;

    private ArrayList<GraphModelListener> subscribers;

    GraphPath criticalPath;

    Node source;

    public GraphModel() {
        graph=new HashMap<>();
        subscribers = new ArrayList<>();
        source=new Node(0.25,0.25,DefaultData.defaultNodeWidth,DefaultData.defaultNodeHeight);
        source.setActivityName("Source");
        source.setWeight(0);
        edges=new ArrayList<>();

        graph.put(source,new LinkedList<>());
    }

    public ArrayList<Node> getNodes(){
        return new ArrayList<>(graph.keySet());
    }


    public void addEdge(Edge e ){
        edges.add(e);
    }

    public void removeEdge(Edge e){
        edges.remove(e);
        e.getStartNode().removeOutGoingEdge(e);
        findCriticalPath();
        notifySubscribers();
    }

    public void finishEdge(Edge edge,Node endNode){

        Node startNode=edge.getStartNode();
        edge.setEndNode(endNode);
        startNode.addOutGoingEdge(edge);
    //                    edge.finished(endNode);
//                    notifySubscribers();
        boolean cycleExists=checkForCycles();
        if (!cycleExists){
            edge.finished(endNode);
            findCriticalPath();
            notifySubscribers();
        }else{
            removeEdge(edge);
        }

    }

    public void createNode(double left, double top, double width, double height) {
        if(left+width<1 && top+height<1) {
            Node node = new Node(left, top, width, height);
            graph.put(node, new LinkedList<>());
            notifySubscribers();
        }
    }

    public void addSubscriber(GraphModelListener aSub) {
        subscribers.add(aSub);
    }

    private void notifySubscribers() {
        subscribers.forEach(sub -> sub.modelChanged());
    }

    public boolean checkNodeHit(double x, double y) {
        return graph.keySet().stream()
                .anyMatch(n -> n.checkHit(x,y));
    }

    public boolean checkEdgeHit(double x,double y){
        if (edges!=null) {
            return edges.stream().anyMatch(e -> e.checkHit(x, y));
        }else{
            return false;
        }
    }

    public Node findNode(double x, double y) {
        Node found = null;
        for (Node n : getNodes()) {
            if (n.checkHit(x,y)) {
                found = n;
            }
        }
        return found;
    }

    public Edge findEdge(double x, double y) {
        Edge found = null;
        for (Edge e : edges) {
            if (e.checkHit(x,y)) {
                found = e;
            }
        }
        return found;
    }

    public void moveNode(Node n, double dX, double dY) {
        if (n.left+dX>0 && n.top+dY>0 && n.left+n.width+dX<1 && n.top+n.height+dY<1){

            n.move(dX,dY);
            for(Edge edge:edges){
                if (edge.checkNode(n)){
                    edge.realignEdge();
                }
            }
            notifySubscribers();
         }
    }

    public void moveEdge(Edge e, double normX, double normY) {
        e.setEndPoints(normX,normY);
        notifySubscribers();
    }

    public void removeNode(Node n){
        ArrayList<Edge> edgesToRemove=new ArrayList<>();
        if(!n.equals(source)) {
            graph.remove(n);
            for (Edge edge : edges) {
                if (edge.checkNode(n)) {
                    edgesToRemove.add(edge);
                }
            }

            for (Edge edge:edgesToRemove){
                edges.remove(edge);
            }
            notifySubscribers();
        }

    }

    public boolean checkForCycles(){
        for (Node node:getNodes()){
            node.visited=false;
            node.exploring=false;
        }


        return source.findCycles();
    }

    public void findCriticalPath(){
        GraphPath current=new GraphPath();
        current.addNode(source);

        ArrayList<GraphPath> allPaths=new ArrayList<>();

        if (source.getNumOfOutgoingEdges()==0){
            criticalPath=null;
        }else {
            source.findPaths(current, allPaths);

            double maxTotalCost = 0;


            //no paths found
//        if (allPaths.size()==1){
//           // criticalPath=null;
//        }else {
            //otherwise get the shortest path from all the paths
            for (GraphPath path : allPaths) {

                double tc = 0;
                for (Node node : path.getNodes()) {
                    tc += node.getWeight();
                }
                if (tc > maxTotalCost) {
                    criticalPath = path;
                    maxTotalCost = tc;
                }

            }
        }



    }


}
