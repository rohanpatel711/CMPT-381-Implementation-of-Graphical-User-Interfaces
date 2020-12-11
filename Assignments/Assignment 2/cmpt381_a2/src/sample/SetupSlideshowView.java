package sample;


import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class SetupSlideshowView extends ScrollPane implements  SlideShowModelListener{

    private GridPane slideshowsGrid;
    private DataModel dataModelReference;
    private SetupSlideshowController setupSlideshowController;
    Label noSlideshowsLabel;

    public SetupSlideshowView(DataModel dataModelReference, SetupSlideshowController setupSlideshowController) {
        this.dataModelReference=dataModelReference;
        this.setupSlideshowController=setupSlideshowController;

        slideshowsGrid=new GridPane();
        slideshowsGrid.setVgap(15);
        slideshowsGrid.setHgap(15);

        this.setFitToWidth(true);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollBarPolicy.NEVER);

        noSlideshowsLabel=new Label("No Slideshows yet . Add from the button below");
        noSlideshowsLabel.setFont(new Font("Ariel",30));
        this.setContent(noSlideshowsLabel);


    }



    @Override
    public void setSlideshows() throws FileNotFoundException {
        slideshowsGrid.getChildren().clear();
        HashMap<String , ArrayList<String>> allSlideshows=dataModelReference.getSlideshowHashMap();

        VBox cell;
        StackPane imageParent;
        Label slideshowName;
        int i=0;

        if(allSlideshows.keySet().size()!=0){
            this.setContent(slideshowsGrid);
        }
        for(String slideshow_name_key:allSlideshows.keySet()){
            cell=new VBox();
            imageParent=new StackPane();

            String image_url=allSlideshows.get(slideshow_name_key).get(0);

            imageParent.setMinHeight(Helper.getOneImageHeight());
          // imageParent.setPrefHeight(Helper.getOneImageHeight());
            imageParent.setPrefWidth(Helper.getOneImageWidth());
            imageParent.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY,new CornerRadii(10),null)));

            ImageView imageView=new ImageView(new Image(new FileInputStream(image_url)));
            imageView.fitWidthProperty().bind(imageParent.prefWidthProperty());
            imageView.setPreserveRatio(true);

            imageParent.getChildren().add(imageView);

            slideshowName=new Label(slideshow_name_key);
            System.out.println(slideshow_name_key);


            cell.getChildren().addAll(imageParent,slideshowName);
            int finalI = i;

            imageParent.setOnMouseClicked(mouseEvent -> {
                try {
                    setupSlideshowController.selectSlideshow(slideshow_name_key);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
            VBox.setVgrow(imageParent,Priority.ALWAYS);
            slideshowsGrid.add(cell,i%4,i/4);

            i++;
        }
    }


}
