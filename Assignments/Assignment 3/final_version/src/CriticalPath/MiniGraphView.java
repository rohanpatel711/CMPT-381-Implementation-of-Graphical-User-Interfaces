package CriticalPath;


import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;

public class MiniGraphView extends GraphView {
    public MiniGraphView(double width, double height) {
        super(width, height);
    }

    public void draw() {
        double boxLeft, boxTop, boxWidth, boxHeight;
        double edgeX1,edgeX2,edgeY1,edgeY2;
        gc.save();
        gc.clearRect(0,0,width, height);
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.GRAY);
        double viewBoxLeft=iModel.viewPortLeft*width;
        double viewBoxTop=iModel.viewPortTop*height;
        double viewBoxWidth=(iModel.mainGraphWidth/(iModel.viewWidth*iModel.zoomLevel))*width;
        double viewBoxHeight=(iModel.mainGraphHeight/(iModel.viewHeight*iModel.zoomLevel))*height;

        gc.fillRect(viewBoxLeft,viewBoxTop,viewBoxWidth,viewBoxHeight);
        for (Node node : model.getNodes()) {
            gc.setFill(Color.GREEN);
            boxLeft = (node.left) * width;
            boxTop = (node.top) * height;
            boxWidth = node.width * width;
            boxHeight = node.height * height;

            if (model.criticalPath!=null) {
                if (model.criticalPath.getNodes().contains(node)) {
                    gc.setStroke(Color.ORANGE);
                } else {
                    gc.setStroke(Color.BLACK);

                }
            }else {
                gc.setStroke(Color.BLACK);
            }

            gc.setLineWidth(DefaultData.circleStroke*width);
            gc.fillOval(boxLeft,boxTop,boxWidth,boxHeight);
            gc.strokeOval(boxLeft, boxTop, boxWidth, boxHeight);

            gc.setFill(Color.BLACK);
            gc.setFont(new Font(0.02*width));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.fillText(node.getActivityName()+"\n"+node.getWeight(),boxLeft+boxWidth/2,boxTop+boxHeight/2);

            if(model.edges!=null) {
                for (Edge e : model.edges) {
                    edgeX1=(e.startX)*width;
                    edgeY1=(e.startY)*height;
                    edgeX2=(e.endX)*width;
                    edgeY2=(e.endY)*height;

                    gc.setLineWidth(DefaultData.edgeWidth*width);
                    if(e.isCompleted()){
                        if (model.criticalPath!=null) {
                            if (model.criticalPath.getEdges().contains(e)) {
                                gc.setStroke(Color.ORANGE);
                            }else{
                                gc.setStroke(Color.BLACK);
                            }
                        }else {
                            gc.setStroke(Color.BLACK);
                        }
                        gc.strokeLine(edgeX1,edgeY1,edgeX2,edgeY2);
                        gc.save();
                        Rotate r =new Rotate(e.getAbsoluteSlopeAngle(),(e.getPivotPoints()[0])*width,(e.getPivotPoints()[1])*height);
                        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
                        gc.drawImage(triangleImage,edgeX2-(triangleImage.getWidth())/2-6,edgeY2-(triangleImage.getHeight())/2);
                        gc.restore();
                    }


                    }
                    gc.setLineWidth(1);
                }
            }
            gc.setLineDashes(0);

        }

}

