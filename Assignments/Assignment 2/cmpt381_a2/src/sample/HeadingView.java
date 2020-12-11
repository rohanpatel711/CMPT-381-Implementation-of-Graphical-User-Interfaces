package sample;


import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.io.FileNotFoundException;

public class HeadingView extends HBox implements MainViewChangeListener {

    private Label headLabel;
    private Button showSlideShowsButton;
    private Button showGalleryButton;
    private boolean galleryButtonVisible;
    private boolean slideshowButtonVisible;

    public HeadingView( final HeadingViewController headingViewController)  {

        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        headLabel=new Label();
        this.getChildren().add(headLabel);

        headLabel.setFont(new Font("Ariel",40));
        showGalleryButton=new Button("Show Gallery");
        showSlideShowsButton=new Button("Show Slideshows");
        showGalleryButton.setOnAction(actionEvent -> {
            try {
                headingViewController.showGallery();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        showSlideShowsButton.setOnAction(actionEvent -> {
            try {
                headingViewController.showSlideShows();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        galleryButtonVisible=false;
        slideshowButtonVisible=false;

    }
    public void setConfig(boolean show_gallery, boolean show_setup){
        if (show_gallery){
            if(!galleryButtonVisible){
                this.getChildren().add(showGalleryButton);
                galleryButtonVisible=true;
            }
        }else{
            if(galleryButtonVisible){
                this.getChildren().remove(showGalleryButton);
                galleryButtonVisible=false;
            }
        }

        if (show_setup){
            if(!slideshowButtonVisible){
                this.getChildren().add(showSlideShowsButton);
                slideshowButtonVisible=true;
            }
        }else{
            if(slideshowButtonVisible){
                this.getChildren().remove(showSlideShowsButton);
                slideshowButtonVisible=false;
            }
        }


    }

    public void setHeading(String newheading){
        headLabel.setText(newheading);
    }

    @Override
    public void updateMode(int current_mode) {
        switch (current_mode){

            case DataModel.GALLERY_MODE:
                setConfig(false,true);
                setHeading(DataModel.GALLERY_HEADING);
                break;
            case DataModel.SETUP_MODE:
                setConfig(true,false);
                setHeading(DataModel.SETUP_HEADING);
                break;
            case DataModel.SETUP_GALLERY_MODE:
                setConfig(false,false);
                setHeading(DataModel.SETUP_GALLERY_HEADING);
                break;
            case DataModel.SLIDESHOW_MODE:
                //this view will not exist at this time , so we dont need to do anything

                break;

        }
    }
}
