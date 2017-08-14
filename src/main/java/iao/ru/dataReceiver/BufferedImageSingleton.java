package iao.ru.dataReceiver;

import java.awt.image.BufferedImage;

/**
 * Created by Zoxy1 on 14.08.17.
 */
public class BufferedImageSingleton {
    private static BufferedImage bufferedImage;

    private BufferedImageSingleton() {
    }

    public static BufferedImage getInstanse(int width, int height, int type) {
        if (bufferedImage == null) {
            bufferedImage = new BufferedImage(width, height, type);
        }
        return bufferedImage;
    }
    public static void setBufferedImage(BufferedImage bufferedImage) {
        BufferedImageSingleton.bufferedImage = bufferedImage;
    }
}
