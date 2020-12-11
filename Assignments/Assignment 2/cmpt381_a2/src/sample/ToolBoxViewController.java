package sample;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

public class ToolBoxViewController {
    private DataModel dataModelReference;
    public ToolBoxViewController(DataModel dataModelReference) {
        this.dataModelReference=dataModelReference;
    }


    public void openGalleryForSlideshow() throws FileNotFoundException {
        dataModelReference.updateMainView(DataModel.SETUP_GALLERY_MODE);
    }

    public void saveSlideshow(String text) throws FileNotFoundException {
        System.out.println(text);
        if (text.equals("")) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No slideshow name entered");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter the name of slideshow in the textfield");

            alert.showAndWait();
        }else{
            if(!dataModelReference.saveSlideshowInDatabase(text)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No Images selected");
                alert.setHeaderText(null);
                alert.setContentText("Please select atleast one image by clicking on them");

                alert.showAndWait();
            }
        }
    }

    public void addPicture() throws MalformedURLException {
        FileChooser fileChooser=new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif","*.jpeg")
        );
        File file=fileChooser.showOpenDialog(dataModelReference.getPrimaryStage());

        String imageUrl=file.getAbsolutePath();
        System.out.println(imageUrl);
        dataModelReference.addPicture(imageUrl);
    }

    public void backToGallery() throws FileNotFoundException {
        dataModelReference.updateMainView(DataModel.GALLERY_MODE);
        dataModelReference.clearTempSlideshowList();
    }



}
