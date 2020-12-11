package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Set;

public class Main extends Application {

    PicturePanel picturePanel;
    SetupSlideshowView setupSlideshowView;
    SlideshowView slideshowView;
    RootView rootBox;
    HeadingView headingView;
    ToolBoxView toolBoxView;

    private final int windowWidth=1920;
    private final int windowHeight=1080;

    HeadingViewController headingViewController;
    ToolBoxViewController toolBoxViewController;
    PicturePanelController picturePanelController;
    SetupSlideshowController setupSlideshowController;

    private final int STARTING_MODE= DataModel.GALLERY_MODE;

    DataModel dataModel;
    @Override
    public void start(Stage primaryStage) throws Exception{

        dataModel=new DataModel();
        headingViewController =new HeadingViewController(dataModel);
        toolBoxViewController=new ToolBoxViewController(dataModel);
        picturePanelController=new PicturePanelController(dataModel);
        setupSlideshowController=new SetupSlideshowController(dataModel);

        rootBox=new RootView(dataModel);
        toolBoxView=new ToolBoxView(dataModel,toolBoxViewController);
        setupSlideshowView= new SetupSlideshowView(dataModel,setupSlideshowController);
        slideshowView= new SlideshowView(dataModel);
        headingView =new HeadingView( headingViewController);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(rootBox, windowWidth, windowHeight));
        primaryStage.show();
        Helper.setActualWidth(primaryStage.widthProperty().getValue());
        System.out.println(Helper.getActualWidth());
        picturePanel=new PicturePanel(picturePanelController,dataModel);

        rootBox.setGalleryView(picturePanel);
        rootBox.setHeadingBox(headingView);
        rootBox.setSetupView(setupSlideshowView);
        rootBox.setSlideShowView(slideshowView);
        rootBox.setToolBox(toolBoxView);


        dataModel.addMainSubscriber(rootBox);
        dataModel.addMainSubscriber(headingView);
        dataModel.addMainSubscriber(toolBoxView);
        dataModel.addMainSubscriber(picturePanelController);
        dataModel.setPicturePanel(picturePanel);

        dataModel.setSlideShowModelListener(setupSlideshowView);
        dataModel.setSlideshowView(slideshowView);
        dataModel.setPrimaryStage(primaryStage);
        dataModel.setToolbarChangeListener(toolBoxView);

        //Starting Views
        dataModel.updateMainView(STARTING_MODE);


    }


    public static void main(String[] args) {
        launch(args);
    }
}
