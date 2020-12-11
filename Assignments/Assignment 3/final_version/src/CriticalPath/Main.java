package CriticalPath;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        StackPane root = new StackPane();


        // MVC components
        CompositeView view=new CompositeView();
        GraphModel model = new GraphModel();
        GraphController controller = new GraphController();
        InteractionModel iModel = new InteractionModel();


        // Connect MVC components
        model.addSubscriber(view);
        iModel.addSubscriber(view);
        iModel.setModel(model);
        view.setModel(model);
        view.setInteractionModel(iModel);
        controller.setModel(model);
        controller.setInteractionModel(iModel);

        // Layout
        root.getChildren().add(view);
        view.draw();
        Scene scene=new Scene(root);
        primaryStage.setTitle("Node Demo");
        primaryStage.setScene(scene);


        primaryStage.show();
        view.requestFocus();
         view.setController(controller);


    }


    public static void main(String[] args) {
        launch(args);
    }
}
