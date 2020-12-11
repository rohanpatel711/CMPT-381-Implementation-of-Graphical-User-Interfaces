package CriticalPath;


public class Edge {
    double startX,startY,endX,endY;
    private boolean completed;
    private Node startNode;
    private Node endNode;




    public Edge(double startX, double startY, double endX, double endY,Node startNode) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        completed=false;
        this.startNode=startNode;
    }

    public void setEndPoints(double endX,double endY){
        this.endX=endX;
        this.endY=endY;
    }


    public boolean checkNode(Node node){
        if(this.startNode.equals(node)|| this.endNode.equals(node)){
            return true;
        }else{
            return false;
        }
    }

    public boolean checkHit(double x, double y) {

        //get Ax+By+C=0 form of equation
        double A=-1*getSlope();

        double B=1;
        double C=getSlope()*startX-startY;

        //formula for prependicular distance= abs(Ax+By+C)/sqrt(A^2+B^2)
        double prependicularDistance=Math.abs(A*x+B*y+C)/Math.sqrt(A*A+B*B);


        //check if prependicularDistance from line is less then or equal half of edge width
        if(prependicularDistance<=DefaultData.edgeWidth){
            return true;
        }else{
            return false;
        }

    }

    public void realignEdge(){

        double circleX1=this.startNode.left+this.startNode.width/2;
        double circleY1=this.startNode.top+this.startNode.height/2;

        double circleX2=this.endNode.left+this.startNode.width/2;
        double circleY2=this.endNode.top+this.startNode.height/2;

        double distance=Math.sqrt(Math.pow(circleX1-circleX2,2)+Math.pow(circleY1-circleY2,2));

        this.startX=circleX1-(DefaultData.defaultNodeWidth*(circleX1-circleX2))/(2*distance);
        this.startY=circleY1-(DefaultData.defaultNodeHeight*(circleY1-circleY2))/(2*distance);


        this.endX=circleX2-(DefaultData.defaultNodeWidth*(circleX2-circleX1))/(2*distance);
        this.endY=circleY2-(DefaultData.defaultNodeHeight*(circleY2-circleY1))/(2*distance);


    }

    public double getSlope(){
        if(endX-startX==0){
            return Double.POSITIVE_INFINITY;
        }else {
            return (endY - startY) / (endX - startX); //coz the y axis is negative
        }
    }

    public double getAbsoluteSlopeAngle(){
        if (startX-endX<0){
            if(endY-startY>=0){
                return Math.toDegrees(Math.atan(getSlope()));
            }else{

                return 360+Math.toDegrees(Math.atan(getSlope()));
            }
        }else{
            if(endY-startY>=0){

                return 180+Math.toDegrees(Math.atan(getSlope()));
            }else{

                return 180+Math.toDegrees(Math.atan(getSlope()));
            }
        }
    }

    private double getEdgeLength(){

        return Math.sqrt(Math.pow(startX-endX,2)+Math.pow(startY-endY,2));

    }
    public boolean isCompleted() {
        return completed;
    }

    public double[] getPivotPoints(){

        double arrowLength =0.0001;

        double t=arrowLength/getEdgeLength();


        double pivX=((1-t)*this.endX+t*this.startX);
        double pivY=((1-t)*this.endY+t*this.startY);


        return new double[]{pivX,pivY};

    }

    public void finished(Node endNode){
    this.completed=true;
    this.endNode=endNode;
    realignEdge();
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getEndNode() {
        return endNode;
    }
}
