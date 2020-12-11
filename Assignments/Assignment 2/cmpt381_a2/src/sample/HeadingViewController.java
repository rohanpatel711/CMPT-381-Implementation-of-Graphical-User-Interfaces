package sample;

import java.io.FileNotFoundException;

public class HeadingViewController {

    DataModel dataModelReference;
    public HeadingViewController(DataModel dataModel) {
        this.dataModelReference=dataModel;
    }

    public void showGallery() throws FileNotFoundException {
        dataModelReference.updateMainView(DataModel.GALLERY_MODE);
    }

    public void showSlideShows() throws FileNotFoundException {
        dataModelReference.updateMainView(DataModel.SETUP_MODE);

    }
}
