package CriticalPath;

import java.util.ArrayList;

public class InteractionModel {
    Object selected;
    Edge draggingEdge;
    Node draggingNode;
    public static double viewWidth, viewHeight;
    public double mainGraphWidth,mainGraphHeight;
    public  double viewPortTop,viewPortLeft;
    public double zoomLevel=DefaultData.initialZoomLevel;
    ArrayList<GraphModelListener> subscribers;
    GraphModel model;

    public InteractionModel() {
        selected=null;
        draggingEdge=null;
        viewPortLeft=0;
        viewPortTop=0;
        subscribers = new ArrayList<>();
    }

    public void setModel(GraphModel model) {
        this.model = model;
    }

    public void setDraggingEdge(Edge draggingEdge) {
        this.draggingEdge = draggingEdge;
    }

    public Edge getDraggingEdge() {
        return draggingEdge;
    }

    public void setDraggingNode(Node draggingNode) {
        this.draggingNode = draggingNode;
    }
;
    public Node getDraggingNode() {
        return draggingNode;
    }

    public void unsetDraggingNode(){
        draggingNode=null;
        notifySubscribers();
    }

    public void setSelection(Object o) {
        if(o instanceof Node || o instanceof Edge){
            selected=o;
        }
        notifySubscribers();
    }

    public void unsetSelection() {
        selected = null;
        notifySubscribers();
    }


    public void setViewExtents (double newWidth, double newHeight) {
        viewWidth = newWidth;
        viewHeight = newHeight;
    }

    public void setMainGraphHeight(double mainGraphHeight) {
        this.mainGraphHeight = mainGraphHeight;
    }

    public void setMainGraphWidth(double mainGraphWidth) {
        this.mainGraphWidth = mainGraphWidth;
    }


    public void addSubscriber(GraphModelListener aSub) {
        subscribers.add(aSub);
    }

    private void notifySubscribers() {
        subscribers.forEach(sub -> sub.modelChanged());
    }

    public  void changeViewPort(double dx,double dy){

        if (viewPortLeft-dx>=0 && viewPortLeft+(mainGraphWidth/(viewWidth*zoomLevel))-dx<=1) {
            viewPortLeft -= dx;
        }
        if (viewPortTop-dy>=0 && viewPortTop+(mainGraphHeight/(viewHeight*zoomLevel))-dy<=1) {
            viewPortTop -= dy;
        }
        notifySubscribers();
    }

    public void changeZoomLevel(double newZoomLevel) {
        if (viewPortLeft+(mainGraphWidth/(viewWidth*newZoomLevel))>1){
            viewPortLeft -= (viewPortLeft+(mainGraphWidth/(viewWidth*newZoomLevel))-1);

            if (viewPortTop+(mainGraphHeight/(viewHeight*newZoomLevel))>1){
                viewPortTop-= (viewPortTop+(mainGraphHeight/(viewHeight*newZoomLevel))-1);

            }
        }
        this.zoomLevel=newZoomLevel;
        notifySubscribers();
    }

    public void updateNodeName(String name){
        ((Node)selected).setActivityName(name);
        notifySubscribers();
    }

    public void updateNodeCost(double cost){
        ((Node)selected).setWeight(cost);
        model.findCriticalPath();
        notifySubscribers();

    }
}
