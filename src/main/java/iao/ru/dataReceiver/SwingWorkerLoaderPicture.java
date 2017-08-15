package iao.ru.dataReceiver;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by Zoxy1 on 09.08.17.
 */
public class SwingWorkerLoaderPicture extends SwingWorker<String, DataToUI> {
    /**
     * UI callback
     */
    private UICallback ui;
    private SerialPort serialPortOpen;
    private boolean exitTread;

    /**
     * Creates data loader.
     *
     * @param ui UI callback to use when publishing data and manipulating UI
     *           //@param reader data source
     */
    public SwingWorkerLoaderPicture(UICallback ui, SerialPort serialPortOpen, boolean exitTread) {
        this.serialPortOpen = serialPortOpen;
        this.ui = ui;
        this.ui.startLoading();
        this.exitTread = exitTread;
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
        BufferedImageSingleton.setBufferedImage(null);
        int type = BufferedImage.TYPE_BYTE_GRAY;
        ArrayList<Integer> receiveData = new ArrayList<Integer>();
        ArrayList<Integer> positionLine = new ArrayList<Integer>();
        DataToUI dataToUI = new DataToUI();
        int numberLine = 0;
        while (true) {
            if (exitTread) {
                break;
            }
            while (serialPortOpen.getInputBufferBytesCount() == 0) {
            }
            while (serialPortOpen.getInputBufferBytesCount() > 0) {
                int byteRead = (serialPortOpen.readBytes(1)[0]) & 0xFF;
                receiveData.add(byteRead);
                int numberKeyLetters = 4;
                if (receiveData.size() > numberKeyLetters) {
                    for (int position = 0; position < (receiveData.size() - numberKeyLetters); position++) {
                        if (receiveData.get(position) == 108 && receiveData.get(position + 1) == 105 && receiveData.get(position + 2) == 110 && receiveData.get(position + 3) == 101 && receiveData.get(position + numberKeyLetters) == 32) {
                            if (!(positionLine.contains(position))) {
                                positionLine.add(position);
                                if (positionLine.size() >= 2) {
                                    int currentIndexLine = positionLine.size() - 1;
                                    int width = position - ((positionLine.get(currentIndexLine - 1)) + numberKeyLetters + 1);
                                    dataToUI.setBufferedImage(BufferedImageSingleton.getInstanse(width, width, type));
                                    int column = 0;
                                    for (int pixelValue = ((positionLine.get(currentIndexLine - 1)) + numberKeyLetters + 1); pixelValue < position; pixelValue++) {
                                        System.out.print(receiveData.get(pixelValue) + " ");
                                        Color color = new Color(receiveData.get(pixelValue), receiveData.get(pixelValue), receiveData.get(pixelValue));
                                        if (column < dataToUI.getBufferedImage().getWidth() && numberLine < dataToUI.getBufferedImage().getHeight()) {
                                            dataToUI.getBufferedImage().setRGB(column, numberLine, color.getRGB());
                                        }
                                        column++;
                                    }
                                    System.out.println(" ");
                                    dataToUI.setNumberLine(numberLine);
                                    publish(dataToUI);
                                    numberLine++;
                                }
                            }
                        }
                    }
                }

            }

        }
        return "";
    }

    /**
     * EDT part of loader. This method is called in EDT
     *
     * @param chunks data, that was passed to UI in {@link #doInBackground()} by calling
     *               {@link SwingWorker#publish(Object[])}
     */
    @Override
    protected void process(List<DataToUI> chunks) {
        for (DataToUI dataToUI : chunks) {
            ui.appendPixel(dataToUI.getBufferedImage());
            ui.setProgress(dataToUI.getNumberLine());
        }
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
    }

    public boolean isExitTread() {
        return exitTread;
    }

    public void setExitTread(boolean exitTread) {
        this.exitTread = exitTread;
    }
}
