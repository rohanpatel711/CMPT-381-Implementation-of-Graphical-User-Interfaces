package sample;

import java.io.FileNotFoundException;

public class SetupSlideshowController {

    DataModel dataModel;
    public SetupSlideshowController(DataModel dataModel) {
        this.dataModel=dataModel;
    }

    public void selectSlideshow(String slideshowName) throws FileNotFoundException {
        dataModel.setSelectedSlideshow(slideshowName);
    }
}
