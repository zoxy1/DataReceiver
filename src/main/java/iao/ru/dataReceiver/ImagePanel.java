package iao.ru.dataReceiver;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private Image image;
    private Integer widthImage;
    private Integer heightImage;

    private int widthRealViewImg;
    private int heightRealViewImg;

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            widthImage = image.getWidth(this);
            heightImage = image.getHeight(this);
            //int imgWidth, imgHeight;
            double contRatio = (double) getWidth() / (double) getHeight();
            double imgRatio =  (double) image.getWidth(this) / (double) image.getHeight(this);

            //width limited
            if(contRatio < imgRatio){
                widthRealViewImg = getWidth();
                heightRealViewImg = (int) (getWidth() / imgRatio);

                //height limited
            }else{
                widthRealViewImg = (int) (getHeight() * imgRatio);
                heightRealViewImg = getHeight();
            }

            //to center
            int x = (int) (((double) getWidth() / 2) - ((double) widthRealViewImg / 2));
            int y = (int) (((double) getHeight()/ 2) - ((double) heightRealViewImg / 2));

            g.drawImage(image, x, y, widthRealViewImg, heightRealViewImg, this);
        }

    }

    public Image getImage() {
        return image;
    }
    public void setImage(Image image) {
        this.image = image;
    }
    public Integer getHeightImage() {
        return heightImage;
    }

    public void setHeightImage(Integer heightImage) {
        this.heightImage = heightImage;
    }
    public Integer getWidthImage() {
        return widthImage;
    }

    public void setWidthImage(Integer widthImage) {
        this.widthImage = widthImage;
    }
    public int getWidthRealViewImg() {
        return widthRealViewImg;
    }

    public void setWidthRealViewImg(int widthRealViewImg) {
        this.widthRealViewImg = widthRealViewImg;
    }

    public int getHeightRealViewImg() {
        return heightRealViewImg;
    }

    public void setHeightRealViewImg(int heightRealViewImg) {
        this.heightRealViewImg = heightRealViewImg;
    }

}
