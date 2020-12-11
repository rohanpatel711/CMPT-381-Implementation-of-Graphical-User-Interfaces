package sample;

import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DataModel {
    private ArrayList<String> images;
    private ArrayList<String> slideshows;
    private HashMap<String,ArrayList<String>> slideshowHashMap;
    private ArrayList<MainViewChangeListener> mainViewSubscribers;

    private SlideShowModelListener slideShowModelListener;

    private PicturePanel picturePanel;

    ToolbarChangeListener toolbarChangeListener;

    private Stage primaryStage;

    public final static int GALLERY_MODE=1;
    public final static int SETUP_MODE=2;
    public final static int SLIDESHOW_MODE=3;
    public final static int SETUP_GALLERY_MODE=4;

    public final static String GALLERY_HEADING="All Pictures";
    public final static String SETUP_HEADING="Your Slideshows";
    public final static String SETUP_GALLERY_HEADING="Select from your pictures";

    private int CURRENT_MODE=0;

    private String selectedSlideshow;
    private PicturePanelListener picturePanelListener;



    private MainViewChangeListener mainBox;
    private ArrayList<Integer> tempSlideshowSetupList;

    private SlideshowView slideshowView;

    public DataModel() {
        images=new ArrayList<String>();
        slideshows=new ArrayList<String>();
        mainViewSubscribers=new ArrayList<MainViewChangeListener>();
        tempSlideshowSetupList=new ArrayList<Integer>();
        slideshowHashMap=new HashMap<String, ArrayList<String>>();



    }

    public void setSlideShowModelListener(SlideShowModelListener slideShowModelListener) {
        this.slideShowModelListener = slideShowModelListener;
    }

    public void setToolbarChangeListener(ToolbarChangeListener toolbarChangeListener) {
        this.toolbarChangeListener = toolbarChangeListener;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setPicturePanel(PicturePanel picturePanel) {
        this.picturePanel = picturePanel;
    }

    public void addMainSubscriber(MainViewChangeListener e){
        mainViewSubscribers.add(e);
    }



    public void updateMainView(int MODE) throws FileNotFoundException {
        CURRENT_MODE=MODE;
        this.picturePanel.updateSelectedList(null);
        if(MODE==SETUP_MODE){
            slideShowModelListener.setSlideshows();
        }
        notifyMainSubscribers();
    }

    private void notifyMainSubscribers()  {
        for(MainViewChangeListener view : mainViewSubscribers){
            view.updateMode(CURRENT_MODE);

        }
    }

    public void setSlideshowView(SlideshowView slideshowView) {
        this.slideshowView = slideshowView;
    }



    public void slideShowSetupPictureClicked(int image_index){

        if(tempSlideshowSetupList.contains((Integer)image_index)){
            tempSlideshowSetupList.remove((Integer)image_index);
        }else{
            tempSlideshowSetupList.add((Integer)image_index);
        }

        this.picturePanel.updateSelectedList(tempSlideshowSetupList);
    }

    public void clearTempSlideshowList() {
        this.tempSlideshowSetupList.clear();
    }

    public PicturePanel getPicturePanel() {
        return picturePanel;
    }

    public boolean saveSlideshowInDatabase(String slideshowName) throws FileNotFoundException {
        if (tempSlideshowSetupList.size()!=0){
            ArrayList<String> selectedSlideshowImages=new ArrayList<String>();

            for (Integer index: tempSlideshowSetupList){
                selectedSlideshowImages.add(images.get(index));
            }

            slideshowHashMap.put(slideshowName,selectedSlideshowImages);

            updateMainView(SETUP_MODE);
            notifyMainSubscribers();
            return true;
        }else{
            return false;
        }
    }

    public String getSelectedSlideshow() {
        return selectedSlideshow;
    }

    public HashMap<String, ArrayList<String>> getSlideshowHashMap() {
        return slideshowHashMap;
    }

    public int getCURRENT_MODE() {
        return CURRENT_MODE;
    }

    public void setSelectedSlideshow(String slideshowName) throws FileNotFoundException {
        selectedSlideshow=slideshowName;
        System.out.println(Arrays.toString(slideshowHashMap.get(selectedSlideshow).toArray()));
        slideshowView.start();
        updateMainView(SLIDESHOW_MODE);
    }

    public void addPicture(String image_url)  {
        images.add(image_url);
        try{
        this.picturePanel.updatePictures();}
        catch (FileNotFoundException exception){
            exception.printStackTrace();
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
