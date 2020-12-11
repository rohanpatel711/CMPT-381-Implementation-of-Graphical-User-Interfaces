package sample;

import javafx.scene.layout.StackPane;

public class PicturePanelController implements MainViewChangeListener {
    private boolean setupslideshowmode;
    private DataModel dataModelReference;

    public PicturePanelController(DataModel dataModelReference) {
        this.dataModelReference = dataModelReference;
    }

    public void pictureClicked( int image_index){
        if(setupslideshowmode)
            dataModelReference.slideShowSetupPictureClicked(image_index);
    }

    @Override
    public void updateMode(int current_mode) {
        switch (current_mode){
            case DataModel.GALLERY_MODE:
                setupslideshowmode=false;
                break;

            case DataModel.SETUP_GALLERY_MODE:
                setupslideshowmode=true;
                break;

        }
    }


}
