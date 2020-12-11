package sample;

import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class RootView extends VBox implements MainViewChangeListener {

    private Node headingBox;
    private Node galleryView;
    private Node slideShowView;
    private Node setupView;

    private Node toolBox;
    private DataModel dataModelReference;

    public RootView(DataModel dataModel) {
        this.dataModelReference=dataModel;
    }

    public void setHeadingBox(Node headingBox) {
        this.headingBox = headingBox;
    }

    public void setGalleryView(Node galleryView) {
        this.galleryView = galleryView;
        VBox.setVgrow(galleryView, Priority.ALWAYS);

    }

    public void setSlideShowView(Node slideShowView) {
        this.slideShowView = slideShowView;
    }

    public void setSetupView(Node setupView) {
        this.setupView = setupView;
        VBox.setVgrow(setupView, Priority.ALWAYS);

    }

    public void setToolBox(Node toolBox) {
        this.toolBox = toolBox;
    }

    @Override
    public void updateMode(int current_mode) {

        switch (current_mode){
            case DataModel.GALLERY_MODE:
                if (this.getChildren().size()>0) {
                    this.getChildren().clear();
                }
                this.getChildren().addAll(headingBox,galleryView,toolBox);

                break;

            case DataModel.SLIDESHOW_MODE:
                if(this.getChildren().contains(slideShowView)){
                    this.getChildren().remove(slideShowView);
                }
                if (this.getChildren().size()>0) {
                    this.getChildren().clear();
                }

                VBox.setVgrow(slideShowView,Priority.ALWAYS);
                this.getChildren().add(slideShowView);


                break;

            case DataModel.SETUP_MODE:

                if (this.getChildren().size()>0) {
                    this.getChildren().clear();
                }
                this.getChildren().addAll(headingBox,setupView,toolBox);

                break;
            case DataModel.SETUP_GALLERY_MODE:
                if (this.getChildren().size()>0) {
                    this.getChildren().clear();
                }

                this.getChildren().addAll(headingBox,galleryView,toolBox);

                break;


        }
    }
}
