package iao.ru.dataReceiver;

import java.awt.image.BufferedImage;

public interface UICallback {

    //@RequiresEDT
    void setText(int widthReceiveImage);

    /**
     * Sets current progress. Values should be in the range [0,100]. This method can be called from outside EDT.
     *
     * @param progressPercent progress value to set
     */
    //@RequiresEDT
    void setProgress(int progressPercent);

    /**
     * Performs required UI operations when loading starts. This method can be called from outside EDT.
     */
    //@RequiresEDT
    void startLoading();

    /**
     * Performs required UI operations when loading stops. This method can be called from outside EDT.
     */
    //@RequiresEDT
    void stopLoading();

    /**
     * Displays error message. This method can be called from outside EDT.
     *
     * @param message message to display
     */
    //@RequiresEDT(RequiresEDTPolicy.SYNC)
    void showError(String message);

    void appendPixel(BufferedImage bufferedImage);

}
