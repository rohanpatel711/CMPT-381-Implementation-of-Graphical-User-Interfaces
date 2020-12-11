package sample;

public class Helper {
    private static double actualWidth;
    private static double oneImageWidth;
    private static double oneImageHeight;
    private static final double h_gap=10;
    private static final double v_gap=10;

    private static final int numOfImagesInOneRow=4;

    public static void setActualWidth(double width) {

        actualWidth= width;
        oneImageWidth=(actualWidth/numOfImagesInOneRow)-h_gap;
        oneImageHeight=oneImageWidth*9/16;
    }

    public static double getOneImageHeight() {
        return oneImageHeight;
    }

    public static double getOneImageWidth() {
        return oneImageWidth;
    }

    public static double getH_gap() {
        return h_gap;
    }

    public static double getV_gap() {
        return v_gap;
    }

    public static double getActualWidth() {
        return actualWidth;
    }
}
