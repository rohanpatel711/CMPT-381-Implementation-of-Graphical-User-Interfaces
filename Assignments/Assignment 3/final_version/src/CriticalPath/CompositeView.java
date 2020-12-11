package CriticalPath;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class CompositeView extends Pane implements GraphModelListener{

    MainGraphView mainGraphView;
    MiniGraphView miniGraphView;
    private ZoomView zoomView;
    private NodeDetailView nodeDetailView;
    private VBox leftPanel;

    private double mainGraphHeight=DefaultData.mainGraphHeight;
    private double mainGraphWidth=DefaultData.mainGraphWidth;
    Button b;

    class ZoomView extends VBox{
        protected Slider zoomSlider;
        private Label heading;
        private Label miniHeading;
        ZoomView() {
            zoomSlider=new Slider(0.5,2,DefaultData.initialZoomLevel);
            zoomSlider.setBlockIncrement(0.05f);
            zoomSlider.setMajorTickUnit(0.25f);
            zoomSlider.setShowTickMarks(true);
            zoomSlider.setShowTickLabels(true);

            heading=new Label("View Controls");
            heading.setPadding(new Insets(10,4,10,2));
            heading.setFont(new Font("serif",20));

            miniHeading=new Label("Zoom:");
            miniHeading.setPadding(new Insets(0,4,0,2));

            this.getChildren().addAll(heading,miniHeading,zoomSlider);
        }
    }


    public CompositeView() {
        mainGraphView=new MainGraphView(mainGraphWidth,mainGraphHeight);
        miniGraphView=new MiniGraphView(DefaultData.miniGraphWidth,DefaultData.miniGraphHeight);
        HBox container=new HBox();
        leftPanel=new VBox();
        zoomView=new ZoomView();
        nodeDetailView=new NodeDetailView();
        addStyles();

        leftPanel.getChildren().addAll(miniGraphView,zoomView,nodeDetailView);
        container.getChildren().addAll(leftPanel,mainGraphView);
        this.getChildren().add(container);
    }

    private void addStyles() {
        leftPanel.setStyle("-fx-padding: 5;\n" +
                "-fx-background-color: white");

        miniGraphView.setStyle(
                "-fx-background-color: lightgray");

        zoomView.setStyle( "-fx-background-insets:8 0 8 0;"+
                "-fx-background-color: lightgray;"+
                "-fx-padding: 8 0 8 0;"+
                "-fx-background-radius: 6");
        nodeDetailView.setStyle(
                "-fx-background-color: lightgray;"+
                "-fx-padding: 8 0 8 0;"+
                "-fx-background-radius: 6");
    }

    public void setModel(GraphModel newModel) {
        mainGraphView.setModel(newModel);
        miniGraphView.setModel(newModel);
    }

    public void setInteractionModel(InteractionModel newIModel) {
        mainGraphView.setInteractionModel(newIModel);
        miniGraphView.setInteractionModel(newIModel);
        nodeDetailView.setInteractionModel(newIModel);
        newIModel.setMainGraphHeight(mainGraphHeight);
        newIModel.setMainGraphWidth(mainGraphWidth);
    }
    @Override
    public void modelChanged() {
        mainGraphView.draw();
        miniGraphView.draw();
        nodeDetailView.selectorChanged();
    }

    public void draw() {
        mainGraphView.draw();
        miniGraphView.draw();
    }

    public void setController(GraphController controller) {
        mainGraphView.setController(controller);
        nodeDetailView.setController(controller);
        zoomView.zoomSlider.valueProperty().addListener(controller::changed);


    }


}
