package CriticalPath;

import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;

public class MainGraphView extends GraphView {
    public MainGraphView(double width, double height) {
        super(width,height);

        //super(width, height);
    }

    public void draw() {
        myCanvas.requestFocus();
        double boxLeft, boxTop, boxWidth, boxHeight;
        double edgeX1,edgeX2,edgeY1,edgeY2;
        gc.save();
        gc.clearRect(0,0,width, height);
        gc.setStroke(Color.BLACK);
        for (Node node : model.getNodes()) {
            if (node == iModel.getDraggingNode()) {
                gc.setFill(Color.LIGHTGREEN);
            }
            else{
                gc.setFill(Color.GREEN);
            }
            if (node==iModel.selected){
                gc.setLineDashes(new double[]{2,14,14,2});

            }

            //get coords of nodes
            boxLeft = (node.left-iModel.viewPortLeft) * widthWorkspace*iModel.zoomLevel;
            boxTop = (node.top-iModel.viewPortTop) * heightWorkspace*iModel.zoomLevel;
            boxWidth = node.width * widthWorkspace*iModel.zoomLevel;
            boxHeight = node.height * heightWorkspace*iModel.zoomLevel;


            gc.setLineWidth(DefaultData.circleStroke*widthWorkspace*iModel.zoomLevel);
            gc.fillOval(boxLeft,boxTop,boxWidth,boxHeight);

            //check if node is in critical path
            if (model.criticalPath!=null) {
                if (model.criticalPath.getNodes().contains(node)) {
                    gc.setStroke(Color.ORANGE);
                } else {
                    gc.setStroke(Color.BLACK);

                }
            }else {
                gc.setStroke(Color.BLACK);
            }

            //draw boundary
            gc.strokeOval(boxLeft, boxTop, boxWidth, boxHeight);

            //draw text
            gc.setFill(Color.BLACK);
            gc.setFont(new Font(15*iModel.zoomLevel));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.fillText(node.getActivityName()+"\n"+node.getWeight(),boxLeft+boxWidth/2,boxTop+boxHeight/2);

            if(model.edges!=null) {
                for (Edge e : model.edges) {
                    edgeX1=(e.startX-iModel.viewPortLeft)*widthWorkspace*iModel.zoomLevel;
                    edgeY1=(e.startY-iModel.viewPortTop)*heightWorkspace*iModel.zoomLevel;
                    edgeX2=(e.endX-iModel.viewPortLeft)*widthWorkspace*iModel.zoomLevel;
                    edgeY2=(e.endY-iModel.viewPortTop)*heightWorkspace*iModel.zoomLevel;

                    gc.setLineWidth(DefaultData.edgeWidth*widthWorkspace*iModel.zoomLevel);
                    if(e.isCompleted()){
                        gc.setStroke(Color.BLACK);
                        if(e==iModel.selected){
                            gc.setLineDashes(new double[]{2,14,14,2});
                        }else{
                            gc.setLineDashes(0);
                        }
                        if (model.criticalPath!=null) {
                            if (model.criticalPath.getEdges().contains(e)) {
                                gc.setStroke(Color.ORANGE);
                            }
                        }

                    }else{
                        gc.setStroke(Color.GRAY);
                    }
                    gc.strokeLine(edgeX1,edgeY1,edgeX2,edgeY2);

                    //draw the arrow
                    if(e.isCompleted()){
                        gc.save();
                        Rotate r =new Rotate(e.getAbsoluteSlopeAngle(),(e.getPivotPoints()[0]-iModel.viewPortLeft)*widthWorkspace*iModel.zoomLevel,(e.getPivotPoints()[1]-iModel.viewPortTop)*heightWorkspace*iModel.zoomLevel);
                        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
                        gc.drawImage(triangleImage,edgeX2-(triangleImage.getWidth())/2-(20*iModel.zoomLevel)+10,edgeY2-(triangleImage.getHeight())/2-(9.334*iModel.zoomLevel)+9.667,triangleImage.getWidth()*iModel.zoomLevel,triangleImage.getHeight()*iModel.zoomLevel);
                        gc.restore();
                    }
                    gc.setLineWidth(1);
                }
            }
            gc.setLineDashes(0);

        }
    }
    public void setController(GraphController newController) {
        myCanvas.setOnMousePressed(newController::handlePressed);
        myCanvas.setOnMouseReleased(newController::handleReleased);
        myCanvas.setOnMouseDragged(newController::handleDrag);
        myCanvas.setOnKeyPressed(newController::deleteElement);

    }

}
