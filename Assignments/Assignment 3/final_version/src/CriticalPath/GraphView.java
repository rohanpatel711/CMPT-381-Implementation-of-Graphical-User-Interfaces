package CriticalPath;


import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GraphView extends Pane implements GraphModelListener {
    Canvas myCanvas;
    GraphicsContext gc;
    final double widthWorkspace=DefaultData.workspaceWidth;
    final double heightWorkspace=DefaultData.workspaceHeight;

    double width, height;
    GraphModel model;
    InteractionModel iModel;
    Image triangleImage;

    public GraphView(double width,double height) {

        this.width=width;
        this.height=height;
        myCanvas = new Canvas(width, height);

        try {
            triangleImage=new Image(new FileInputStream("imgs/download.png"),0.03*width,0.03*height,false,false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        gc = myCanvas.getGraphicsContext2D();
        // add the canvas to ourselves
        this.getChildren().add(myCanvas);

    }

    public void setModel(GraphModel newModel) {
        model = newModel;
    }

    public void setInteractionModel(InteractionModel newIModel) {
        iModel = newIModel;
        iModel.setViewExtents(widthWorkspace, heightWorkspace);
    }


    public void modelChanged() {
      //  draw();
    }



}
