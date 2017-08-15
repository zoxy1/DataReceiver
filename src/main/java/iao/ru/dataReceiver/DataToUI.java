package iao.ru.dataReceiver;

import java.awt.image.BufferedImage;

/**
 * Created by Zoxy1 on 15.08.17.
 */
public class DataToUI {

    private BufferedImage bufferedImage;
    private int numberLine;

    public int getNumberLine() {
        return numberLine;
    }

    public void setNumberLine(int numberLine) {
        this.numberLine = numberLine;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }
}
