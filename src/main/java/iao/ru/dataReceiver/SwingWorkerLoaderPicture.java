package iao.ru.dataReceiver;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Random;
/**
 * Created by Zoxy1 on 09.08.17.
 */
public class SwingWorkerLoaderPicture extends SwingWorker<String, BufferedImage> {

    private File file;
    /**
     * UI callback
     */
    private UICallback ui;
    private SerialPort serialPortOpen;
    private ImagePanel imagePanel;
    private static final Random random = new Random();
    /**
     * Creates data loader.
     *
     * @param ui UI callback to use when publishing data and manipulating UI
     *           //@param reader data source
     */
    public SwingWorkerLoaderPicture(UICallback ui, File file, SerialPort serialPortOpen, ImagePanel imagePanel) {
        this.imagePanel = imagePanel;
        this.file = file;
        this.serialPortOpen = serialPortOpen;
        this.ui = ui;
        this.ui.startLoading();
    }

    /**
     * Background part of loader. This method is called in background thread. It reads data from data source and
     * places it to UI  by calling {@link SwingWorker#publish(Object[])}
     *
     * @return background execution result - all data loaded
     * @throws Exception if any error occures
     */
    @Override
    protected String doInBackground() throws Exception {
        Byte[] byteMass = {1, 2, 3, 4, 5};

        int type = BufferedImage.TYPE_BYTE_GRAY;
        int w = 100;
        int h = 100;
        BufferedImage bufferedImage = new BufferedImage(w, h, type);

        while (true) {
            while (serialPortOpen.getInputBufferBytesCount() == 0) {
            }
            while(serialPortOpen.getInputBufferBytesCount()>0){

                System.out.println(serialPortOpen.readBytes(1)+" ");
            }

        }


        /*for(int j=0;j<255;j++) {
                //int color = j; // RGBA value, each component in a byte
                Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));

                for (int x = 0; x < w; x++) {
                    for (int y = 0; y < h; y++) {
                        bufferedImage.setRGB(x, y, color.getRGB());
                    }
                }
            publish(bufferedImage);
        Thread.sleep(100);
            setProgress((int) ((j * 100) / 255));
        }*/
        /*BufferedImage scaleImage = new BufferedImage(graphicsPanel.getWidthRealViewImg(), graphicsPanel.getHeightRealViewImg(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = scaleImage.createGraphics();
        Graphics gr =scaleImage.getGraphics();*/
        //gr.fillOval(100, 100, 1000, 700);


        /*graphics.drawImage(bufferedImage, 0, 0, imagePanel.getWidthRealViewImg(), imagePanel.getHeightRealViewImg(), null);
        graphics.dispose();
        int height = scaleImage.getHeight();
        int width = scaleImage.getWidth();
        int sleep = 100;
        long countByte32 = 0;
        long countByte = 0;
        long sizePicture = height * width;
        for (int i = 0; i < height; i++) {
            System.out.print("line ");
            Charset cset = Charset.forName("Windows-1251");
            ByteBuffer byteBuffer = cset.encode("line ");
            byte[] bytes = byteBuffer.array();
            for (int k = 0; k < bytes.length; k++) {
                serialPortOpen.writeByte(bytes[k]);
                if (countByte32 > 31) {
                    countByte32 = 0;
                    Thread.sleep(sleep);
                }
                countByte32++;
            }

            for (int j = 0; j < width; j++) {
                int rgba = scaleImage.getRGB(j, i);
                Color color = new Color(rgba, true);
                int r = color.getRed();
                System.out.print(r + " ");
                if (countByte32 > 31) {
                    countByte32 = 0;
                    Thread.sleep(sleep);
                }
                serialPortOpen.writeByte((byte) r);
                countByte32++;
                setProgress((int) ((countByte * 100) / sizePicture));
                countByte++;
            }
            System.out.println(" ");
        }
*/
        /*Date currentData = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        System.out.println(format1.format(currentData));
        try {
            File fileWrite = new File("Pictures is received\\picture_" + format1.format(currentData) + ".bmp");
            fileWrite.mkdirs();
            ImageIO.write(scaleImage, "bmp", fileWrite);
        } catch (IOException e1) {
            e1.printStackTrace();
        }*/
        //return "";
    }

    /**
     * EDT part of loader. This method is called in EDT
     *
     * @param chunks data, that was passed to UI in {@link #doInBackground()} by calling
     *               {@link SwingWorker#publish(Object[])}
     */
    @Override
    protected void process(List<BufferedImage> chunks) {
        for (BufferedImage bufferedImage : chunks) {
            ui.appendPixel(bufferedImage);
        }

        ui.setProgress(getProgress());
    }

    /**
     * Cancels execution
     */
    public void cancel() {
        cancel(true);
    }

    /**
     * This method is called in EDT after {@link #doInBackground()} is finished.
     */
    @Override
    protected void done() {
        ui.stopLoading();
        ui.setText("File transmitted");
    }

}
