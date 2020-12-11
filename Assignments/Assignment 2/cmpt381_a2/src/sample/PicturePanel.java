package sample;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class PicturePanel extends ScrollPane implements PicturePanelListener {

    ArrayList<ImageView> imageViews;
    private GridPane picturesGrid;
    private double screenWidth;



    private PicturePanelController picturePanelController;
    private DataModel dataModelReference;
    private Label noPicturesLabel;


    public PicturePanel( PicturePanelController picturePanelController, DataModel dataModelReference) throws FileNotFoundException {

        this.picturePanelController=picturePanelController;
        this.dataModelReference=dataModelReference;

        screenWidth=Helper.getActualWidth();

        this.setFitToWidth(true);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setVbarPolicy(ScrollBarPolicy.NEVER);
        imageViews=new ArrayList<ImageView>();
        picturesGrid=new GridPane();
        picturesGrid.setHgap(Helper.getH_gap());
        picturesGrid.setVgap(Helper.getV_gap());
        if(dataModelReference.getCURRENT_MODE()==DataModel.SETUP_GALLERY_MODE)
            noPicturesLabel=new Label("No Images yet . you have to add the images from the gallery menu first");
        else
            noPicturesLabel=new Label("No Images yet . Add from the button below");

        noPicturesLabel.setFont(new Font("Ariel",30));
        this.setContent(noPicturesLabel);


//        imageViews.get(0).setFitWidth(100);
//        imageViews.get(0).setPreserveRatio(true);





    }



    private void setPictures() throws FileNotFoundException {
        imageViews.clear();
        for(String img_url:dataModelReference.getImages()){
            imageViews.add(new ImageView(new Image(new FileInputStream(img_url))));
        }
        if(imageViews.size()!=0){
            StackPane imageParent;
            for (int i = 0; i < imageViews.size(); i++) {
                imageParent = new StackPane();
                imageParent.setPrefWidth(Helper.getOneImageWidth());
                imageParent.setPrefHeight(Helper.getOneImageHeight());

                imageParent.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(10), null)));


                imageViews.get(i).fitWidthProperty().bind(imageParent.prefWidthProperty());
                imageViews.get(i).setPreserveRatio(true);
                imageParent.getChildren().add(imageViews.get(i));


                int image_index = i;
                imageParent.setOnMouseClicked(mouseEvent -> picturePanelController.pictureClicked(image_index));
                picturesGrid.add(imageParent, i % 4, i / 4);
            }
            this.setContent(picturesGrid);

        }

    }

    public void updateSelectedList(ArrayList<Integer> selectedImages){
        int index=0;
        Label selectedPictureLabel;

        for(Node stackPane:this.picturesGrid.getChildren()){
            StackPane selectedStackPane= ((StackPane)stackPane);
            boolean hasLabel=false;
            for(Node stackPaneChildren: selectedStackPane.getChildren()){
                if(stackPaneChildren instanceof Label){
                    hasLabel=true;
                }
            }

            if (hasLabel)
                selectedStackPane.getChildren().remove(1);//label is at index 1

            if(selectedImages!=null) {
                if (selectedImages.contains(index)) {
                    selectedPictureLabel = new Label("Picture Selected");
                    selectedPictureLabel.setFont(new Font("Ariel",30));
                    selectedPictureLabel.setTextFill(Color.web("#FFFFFF"));


                    selectedStackPane.getChildren().add(selectedPictureLabel);
                }
            }

            index++;
        }

    }


    @Override
    public void updatePictures() throws FileNotFoundException {
        setPictures();
    }
}
