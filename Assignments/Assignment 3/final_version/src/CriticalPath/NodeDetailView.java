package CriticalPath;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class NodeDetailView extends VBox  {
    protected TextField nodeNameField, nodeCostField;
    private Label heading,activityName,activityCost;
    InteractionModel iModel;

    public NodeDetailView() {
        heading=new Label("Node Detail View");
        heading.setPadding(new Insets(10,4,10,2));
        heading.setFont(new Font("serif",20));

        activityName=new Label("Name: ");
        activityCost=new Label("Cost: ");

        nodeNameField = new TextField();
        nodeCostField = new TextField();

        nodeNameField.setDisable(true);
        nodeCostField.setDisable(true);
        this.getChildren().addAll(heading,activityName, nodeNameField,activityCost, nodeCostField);

    }

    public void setInteractionModel(InteractionModel newIModel) {
        iModel=newIModel;
    }

    public void setController(GraphController controller) {
         nodeNameField.setOnKeyPressed(controller::updateNodeName);
         nodeCostField.setOnKeyPressed(controller::updateNodeCost);
    }

    public void selectorChanged() {


        if (iModel.selected instanceof Node){
            Node selectedNode=(Node) iModel.selected;
            nodeNameField.setDisable(false);
            nodeCostField.setDisable(false);
            nodeNameField.setText(selectedNode.getActivityName());
            nodeCostField.setText(String.valueOf(selectedNode.getWeight()));


        }else{
            nodeNameField.setText("");
            nodeCostField.setText("");

            nodeNameField.setDisable(true);
            nodeCostField.setDisable(true);
        }
    }
}
