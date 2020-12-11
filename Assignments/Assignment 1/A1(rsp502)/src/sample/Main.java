package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {
    private VBox root,slideShowVBox;
    private HBox slideShowHBox,toolBox;
    private final String title="Slideshow";
    private final int windowWidth=1920;
    private final int windowHeight=1080;
    private final int imgHeight=900;

    private Button prevImgBtn,nextImgBtn,paurseOrResumeBtn,switchBtn;
    private ImageView mainImg;
    private final String utilImgs="imgs/util/";

    //index of the image being currently viewed
    private int currImgIndex;
    //store the name of all the images to be viewed in the gallery here
    private String[] imgNames={"img1.png","img2.png","img3.png","img4.png","img5.png"};
    private Image[] imgsList;

    //timer to switch photos for slideshow
    private Timer slideshowTimer;
    //period of time between showing next picture in slideshow
    private final int timerPeriod=3000;

    //initial slideshow mode 'either slideshow or manual
    private String mode="SLIDESHOW";
    //boolean to check if tool bar is visible or not
    private boolean toolbarVisible=false;
    //boolean to check if it is the first time mouse is moved on screen
    private boolean firstTime=true;


    @Override
    public void start(Stage primaryStage) throws Exception{
        currImgIndex=0;
        initialise(primaryStage);
        setActions();
        primaryStage.setTitle(title);
        Scene scene = new Scene(root,windowWidth,windowHeight);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);


        primaryStage.show();


    }

    /**
     * Initialise all the widgets and layouts to be used on the stage
     * @param stage primary stage to be displayed
     * @throws FileNotFoundException for purpose of loading images
     */
    private void initialise(Stage stage) throws FileNotFoundException {
        root=new VBox();

        //main hbox containing left button ,image view and right button layouts
        slideShowHBox=new HBox();

        //vbox containing the above hbox as to center it vertically
        slideShowVBox=new VBox();

       // imageBox.prefWidthProperty().bind(stage.widthProperty());
        ImageView leftImage= new ImageView(new Image(new FileInputStream(utilImgs+"leftarrow.png")));
        leftImage.setFitHeight(40);
        leftImage.setFitWidth(40);
        prevImgBtn=new Button("",leftImage);


        ImageView rightImage= new ImageView(new Image(new FileInputStream(utilImgs+"rightarrow.png")));
        rightImage.setFitHeight(40);
        rightImage.setFitWidth(40);

        nextImgBtn=new Button("",rightImage);

        //using both vbox and hbox for a single imageview as to center it both horizontally and vertically
        HBox mainImgHBox= new HBox(); //hbox for containing the center image view
        VBox mainImgVBox= new VBox(); //vbox for containing the center image view

        //initialising the images array and getting images from imgNames array
        imgsList=new Image[imgNames.length];
        for (int i=0;i<imgNames.length;i++){
            imgsList[i]=new Image(new FileInputStream("imgs/"+imgNames[i]));
        }

        //Main imageview of the slideshow initially set to the first picture in imgsList
        mainImg=new ImageView(imgsList[0]);
        mainImg.setFitHeight(600);
        //mainImg.setPreserveRatio(true);

        //center image vertically
        mainImgVBox.getChildren().add(mainImg);
        mainImgVBox.setAlignment(Pos.CENTER);

        //center image horizontally
        mainImgHBox.getChildren().add(mainImgVBox);
        mainImgHBox.setAlignment(Pos.CENTER);

        //vbox to center prev button horizontally
        VBox leftBtnBox= new VBox();
        leftBtnBox.getChildren().add(prevImgBtn);
        leftBtnBox.setAlignment(Pos.CENTER);

        //vbox to center next button horizontally
        VBox rightBtnBox = new VBox();
        rightBtnBox.getChildren().add(nextImgBtn);
        rightBtnBox.setAlignment(Pos.CENTER);

        //set visibility of navigation buttons to false because the mode is SLIDESHOW in the start
        prevImgBtn.setVisible(false);
        nextImgBtn.setVisible(false);

        //let the main imageview get all the remaining width
        HBox.setHgrow(mainImgHBox,Priority.ALWAYS);

        slideShowHBox.getChildren().addAll(leftBtnBox,mainImgHBox,rightBtnBox);

        slideShowVBox.getChildren().add(slideShowHBox);


        slideShowVBox.setAlignment(Pos.CENTER);
        //imageBox.setAlignment(Pos.CENTER);

        //bottom hbox containing the tool buttons
        toolBox=new HBox();

        paurseOrResumeBtn=new Button("Pause");
        switchBtn= new Button("Manual Navigation Mode");
        toolBox.getChildren().addAll(paurseOrResumeBtn,switchBtn);
        toolBox.setAlignment(Pos.CENTER);
        toolBox.setSpacing(20);
        toolBox.setVisible(false);




        VBox.setVgrow(slideShowVBox,Priority.ALWAYS);
        root.getChildren().add(slideShowVBox);
        root.getChildren().add(toolBox);

        root.prefHeightProperty().bind(stage.heightProperty());
        root.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));

        startTimer();

    }


    private void setActions(){
        prevImgBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                setPrevImg();
            }
        });

        nextImgBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                setNextImg();
            }
        });

        paurseOrResumeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(paurseOrResumeBtn.getText()=="Pause"){
                    paurseOrResumeBtn.setText("Resume");
                    pauseTimer();
                }else if(paurseOrResumeBtn.getText()=="Resume") {
                    paurseOrResumeBtn.setText("Pause");
                    startTimer();
                }
            }
        });

        switchBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //if current mode is slidheshow
                if(mode=="SLIDESHOW"){
                    switchBtn.setText("Slideshow Mode");
                    pauseTimer(); // restart the slideshow
                    paurseOrResumeBtn.setVisible(false);
                    prevImgBtn.setVisible(true);
                    nextImgBtn.setVisible(true);

                    mode="MANUAL";

                }else if(mode=="MANUAL"){ //if current mode is MANUAL
                    switchBtn.setText("Manual Navigation Mode");
                    startTimer(); //pause the slideshow
                    paurseOrResumeBtn.setVisible(true);
                    prevImgBtn.setVisible(false);
                    nextImgBtn.setVisible(false);
                    mode="SLIDESHOW";
                }

            }
        });

        //switch toolbar visibilty every time the screen is clicked
        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!toolbarVisible) {
                    setToolsVisbility(true);
                }
                else {
                    setToolsVisbility(false);

                }
            }
        });

        //make toolbar visible the first time the mouse is moved on screen
        root.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (firstTime && !toolbarVisible) {
                    setToolsVisbility(true);
                    firstTime=false;
                }
            }
        });


    }

    private void setToolsVisbility(boolean visbility) {
        if(visbility) {
            toolBox.setVisible(true);
            if(mode=="MANUAL") {
                prevImgBtn.setVisible(true);
                nextImgBtn.setVisible(true);
            }

            toolbarVisible = true;
        }else{
            toolBox.setVisible(false);
            toolbarVisible = false;
            if(mode=="MANUAL") {
                prevImgBtn.setVisible(false);
                nextImgBtn.setVisible(false);
            }
        }
    }

    /**
     * Pause the sliedeshow timer
     */
    private void pauseTimer() {
        slideshowTimer.cancel();
    }

    /**
     * Start the slideshow timer
     */
    private void startTimer() {
        if(slideshowTimer!=null)
            slideshowTimer.cancel();
        slideshowTimer=new Timer();
        slideshowTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                setNextImg();
            }
        }, timerPeriod, timerPeriod);
    }

    /**
     * Increase the image index and sets next image for the main image view
     */
    private void setNextImg(){
        this.currImgIndex++;
        if(this.currImgIndex>=imgsList.length){
            this.currImgIndex=0;
        }
        System.out.println(currImgIndex);
        mainImg.setImage(imgsList[currImgIndex]);

    }

    /**
     * Decreates the image index and sets previous image for the main image view
     */
    private void setPrevImg(){
        this.currImgIndex--;
        if(this.currImgIndex<0){
            this.currImgIndex=imgsList.length-1;
        }
        mainImg.setImage(imgsList[currImgIndex]);

    }
    public static void main(String[] args) {
        launch(args);
    }
}
