package sample;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

public class ToolBoxView extends HBox implements MainViewChangeListener,ToolbarChangeListener {
    private Button add_picture;
    private Button add_slideshow;

    private Button save_slieshow;
    private TextField slideshow_name;
    private Button backToGallery;

    private ToolBoxViewController toolBoxViewController;
    private DataModel dataModelReference;

    public ToolBoxView(DataModel dataModelReference, ToolBoxViewController toolBoxViewController) {
        this.toolBoxViewController=toolBoxViewController;
        this.dataModelReference=dataModelReference;

        this.setAlignment(Pos.CENTER);
        add_picture=new Button("Add Picture");
        add_slideshow=new Button("Add Slideshow");



        save_slieshow=new Button("Save Slideshow");
        slideshow_name=new TextField();
        slideshow_name.setPromptText("Slideshow Name");
        backToGallery=new Button("Back to Gallery");


        add_picture.setOnAction(actionEvent -> {
            try {
                toolBoxViewController.addPicture();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });

        add_slideshow.setOnAction(actionEvent -> {
            try {
                toolBoxViewController.openGalleryForSlideshow();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        save_slieshow.setOnAction(actionEvent -> {
            try {
                toolBoxViewController.saveSlideshow(slideshow_name.getText());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        backToGallery.setOnAction(actionEvent -> {
            try {
                toolBoxViewController.backToGallery();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    public void updateMode(int current_mode) {
        switch (current_mode){
            case DataModel.GALLERY_MODE:
                if(this.getChildren().size()>0)
                    this.getChildren().clear();;
                this.getChildren().addAll(add_picture);
                break;

            case DataModel.SETUP_MODE:
                if(this.getChildren().size()>0)
                    this.getChildren().clear();;
                this.getChildren().addAll(add_slideshow);
                break;

            case DataModel.SETUP_GALLERY_MODE:
                if(this.getChildren().size()>0)
                    this.getChildren().clear();;
                slideshow_name.setText("");
                this.getChildren().addAll(slideshow_name,save_slieshow,backToGallery);
                break;

        }
    }

    @Override
    public void updateTools() {

    }
}
