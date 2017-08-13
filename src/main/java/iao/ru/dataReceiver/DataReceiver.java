package iao.ru.dataReceiver;

import jssc.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Zoxy1 on 20.07.17.
 */
public class DataReceiver extends JFrame {
    private JLabel pathText = new JLabel();
    static String comPortName;
    private JButton startReceivePictureButton = new JButton("Start receive picture");
    private JButton startReceiveText = new JButton("Start receive text");
    private JLabel lineTextExeption = new JLabel();
    private JLabel developedBy = new JLabel("Developed by Andrey Kudryavtsev");
    private SerialPort serialPortOpen = new SerialPort("COM1");
    private int portSpeed = 115200;
    private ArrayList<JRadioButtonMenuItem> jRadioButtonSpeedMenuItems = new ArrayList<JRadioButtonMenuItem>();
    private JLabel pictureLabel = new JLabel("");
    private ImagePanel imagePanel = new ImagePanel();
    private JFrame frame = new JFrame("Data Receiver");
    private JProgressBar progressBar = new JProgressBar();
    private JPanel progressBarPanel = new JPanel();
    private SwingWorkerLoaderText loaderText = null;
    private SwingWorkerLoaderPicture loaderPicture = null;
    private JButton cancel = new JButton("Cancel");
    private File filePicture;
    private BufferedImage bufferedImage;
    private GraphicsPanel graphicsPanel = new GraphicsPanel();


    void init() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setExtendedState(JFrame.NORMAL);
                Font font = new Font("Verdana", Font.PLAIN, 11);

                JMenuBar menuBar = new JMenuBar();
                JMenu fileMenu = new JMenu("File");
                fileMenu.setFont(font);
                /* JMenuItem openPictureMenuItem = new JMenuItem("Open file picture");
                openPictureMenuItem.addActionListener(new OpenPictureActionListener());
                openPictureMenuItem.setFont(font);

                JMenuItem openTextMenuItem = new JMenuItem("Open file text");
                openTextMenuItem.addActionListener(new OpenTextActionListener());
                openTextMenuItem.setFont(font);*/

                JMenuItem exitMenuItem = new JMenuItem("Exit");
                exitMenuItem.setFont(font);
                exitMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        System.exit(0);
                    }

                });

                //fileMenu.add(openPictureMenuItem);
                //fileMenu.add(openTextMenuItem);
                fileMenu.addSeparator();
                fileMenu.add(exitMenuItem);
                menuBar.add(fileMenu);

                JMenu settingsMenu = new JMenu("Settings");
                settingsMenu.setFont(font);
                String[] portNames = SerialPortList.getPortNames();

                JMenu portMenu = new JMenu("Ports available");
                portMenu.setFont(font);
                settingsMenu.add(portMenu);

                JMenu speedMenu = new JMenu("Speed");
                speedMenu.setFont(font);

                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("110"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("300"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("600"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("1200"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("4800"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("9600"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("14400"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("1920"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("38400"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("57600"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("115200"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("128000"));
                jRadioButtonSpeedMenuItems.add(new JRadioButtonMenuItem("256000"));

                jRadioButtonSpeedMenuItems.get(10).setSelected(true);

                ButtonGroup buttonGroupSpeed = new ButtonGroup();

                for (JRadioButtonMenuItem speedItem : jRadioButtonSpeedMenuItems) {
                    speedItem.setFont(font);
                    buttonGroupSpeed.add(speedItem);
                    speedMenu.add(speedItem);
                    speedItem.addActionListener(new BaundRateActionListener());
                }

                settingsMenu.add(speedMenu);

                ArrayList<JMenuItem> comPortItems = new ArrayList<JMenuItem>();
                for (int i = 0; i < portNames.length; i++) {
                    comPortItems.add(new JMenuItem(portNames[i]));
                    comPortItems.get(i).setFont(font);
                    portMenu.add(comPortItems.get(i));
                    comPortItems.get(i).addActionListener(new SelectComPortActionListener(portNames[i]));
                }

                if (serialPortOpen.isOpened()) {
                    try {
                        serialPortOpen.closePort();
                    } catch (SerialPortException e1) {
                        e1.printStackTrace();
                        lineTextExeption.setText(e1.getExceptionType());
                    }
                }
                try {
                    //Открываем порт
                    serialPortOpen.openPort();
                    //Выставляем параметры
                    serialPortOpen.setParams(portSpeed,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    if (serialPortOpen.isOpened()) {
                        lineTextExeption.setText(serialPortOpen.getPortName() + " is open");
                    }
                } catch (SerialPortException ex) {
                    System.out.println(ex);
                    lineTextExeption.setText(ex.getExceptionType());
                }
                menuBar.add(settingsMenu);
                frame.setJMenuBar(menuBar);
                frame.setPreferredSize(new Dimension(600, 500));
                frame.setLayout(new GridBagLayout());
                JPanel filePathPanel = new JPanel();
                filePathPanel.add(pictureLabel);
                filePathPanel.setLayout(new GridBagLayout());
                frame.add(filePathPanel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));

                imagePanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.gray, 2),
                        BorderFactory.createEmptyBorder(1, 1, 1, 1)));


                GridBagConstraints gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.gridx = 0; // расположение элемента по х
                gridBagConstraints.gridy = 1; // расположение элемента по y
                gridBagConstraints.gridwidth = 2; // количество элементов, которое будет занимать по горизонтали
                gridBagConstraints.gridheight = 1; // количество элементов, которое будет занимать  по вертикали
                gridBagConstraints.weightx = 0.9; //как должна осуществляться растяжка компонента
                gridBagConstraints.weighty = 0.9;
                gridBagConstraints.anchor = GridBagConstraints.CENTER;
                gridBagConstraints.fill = GridBagConstraints.BOTH;
                gridBagConstraints.insets = new Insets(1, 1, 1, 1); // отступы от компонета (top, left, down, right)
                gridBagConstraints.ipadx = 0; // говорят о том на сколько будут увеличены минимальные размеры компонента
                gridBagConstraints.ipady = 0;
                //imagePanel.setAutoscrolls(true);
                //imagePanel.setMinimumSize(new Dimension(163, 100));
                //imagePanel.setPreferredSize(new Dimension(490, 300));

                frame.add(imagePanel, gridBagConstraints);

                startReceivePictureButton.setLayout(new GridBagLayout());
                frame.add(startReceivePictureButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.LAST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 0, 0));
                startReceivePictureButton.addActionListener(new StartReceivePictureButtonActionListener());

                lineTextExeption.setText("Select COM port");
                lineTextExeption.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.gray, 2),
                        BorderFactory.createEmptyBorder(1, 1, 1, 1)));

                startReceiveText.setLayout(new GridBagLayout());
                startReceiveText.addActionListener(new StartReceiveTextButtonActionListener());
                frame.add(startReceiveText, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 0, 0));

                pathText.setLayout(new GridBagLayout());
                pathText.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.gray, 2),
                        BorderFactory.createEmptyBorder(1, 1, 1, 1)));
                lineTextExeption.setText("Please select COM port");
                lineTextExeption.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.gray, 2),
                        BorderFactory.createEmptyBorder(1, 1, 1, 1)));

                frame.add(cancel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.LAST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 0, 0));
                cancel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(loaderText != null) {
                            loaderText.cancel();
                        }
                        if(loaderPicture != null) {
                            loaderPicture.cancel();
                        }
                    }
                });

                progressBar.setMinimum(0);
                progressBar.setMaximum(100);
                progressBar.setStringPainted(true);
                progressBar.setLayout(new GridBagLayout());
                //progressBar.setMinimumSize(new Dimension(100, 20));
                progressBar.setPreferredSize(new Dimension(360, 20));
                progressBar.setForeground(new Color(0,191,32));
                progressBarPanel.add(progressBar);
                progressBarPanel.setVisible(false);
                frame.add(progressBarPanel, new GridBagConstraints(1, 5, 1, 1, 0.9, 0.0, GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(1, 1, 1, 1), 0, 0));

                frame.add(lineTextExeption, new GridBagConstraints(0, 6, 2, 1, 0.0, 0.0, GridBagConstraints.LAST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 0, 0));
                Font fontdevelopedBy = new Font("Verdana", Font.PLAIN, 8);
                developedBy.setFont(fontdevelopedBy);
                frame.add(developedBy, new GridBagConstraints(0, 7, 2, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_END, GridBagConstraints.LINE_END, new Insets(1, 1, 1, 1), 0, 0));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

    /*private static class PortReader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    //Получаем ответ от устройства, обрабатываем данные и т.д.
                    String data = serialPort.readString(event.getEventValue());
                    System.out.println(data);
                    //И снова отправляем запрос
                    serialPort.writeString("Get data");
                } catch (SerialPortException ex) {
                    System.out.println(ex);
                }
            }
        }*/
            }
        });
    }


    public class SelectComPortActionListener implements ActionListener {

        private String portName;

        public SelectComPortActionListener(String portName) {
            this.portName = portName;
        }

        public void actionPerformed(ActionEvent e) {
            comPortName = portName;
            if (serialPortOpen.isOpened()) {
                try {
                    serialPortOpen.closePort();
                } catch (SerialPortException e1) {
                    e1.printStackTrace();
                    lineTextExeption.setText(e1.getExceptionType());
                }
            }


            SerialPort serialPort = new SerialPort(comPortName);
            try {
                //Открываем порт
                serialPort.openPort();
                //Выставляем параметры
                serialPort.setParams(portSpeed,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
                if (serialPort.isOpened()) {
                    lineTextExeption.setText(serialPort.getPortName() + " is open");
                    serialPortOpen = serialPort;
                }
            } catch (SerialPortException ex) {
                System.out.println(ex);
                lineTextExeption.setText(ex.getExceptionType());
            }
        }

    }

    public class BaundRateActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            for (JRadioButtonMenuItem speedItem : jRadioButtonSpeedMenuItems) {

                if (speedItem.isSelected()) {
                    portSpeed = Integer.parseInt(speedItem.getText());
                    lineTextExeption.setText("Select speed = " + portSpeed);
                }
            }
        }
    }

    public class StartReceivePictureButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

                if (serialPortOpen.isOpened()) {
                    UICallback ui = new UICallbackImpl();
                    loaderPicture = new SwingWorkerLoaderPicture(ui, filePicture, serialPortOpen, imagePanel);
                    loaderPicture.execute();
                    /*loaderPicture.addPropertyChangeListener(new PropertyChangeListener() {
                        public void propertyChange(PropertyChangeEvent evt) {
                            if ("progress".equals(evt.getPropertyName())) {
                                progressBar.setValue((Integer) evt.getNewValue());
                            }
                        }
                    });*/
                } else {
                    lineTextExeption.setText("Don`t send " + serialPortOpen.getPortName() + " is closed");
                }
        }
    }

    public class StartReceiveTextButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
                if (serialPortOpen.isOpened()) {
                    UICallback ui = new UICallbackImpl();
                    loaderText = new SwingWorkerLoaderText(ui, serialPortOpen);
                    loaderText.execute();
                    loaderText.addPropertyChangeListener(new PropertyChangeListener() {
                        public void propertyChange(PropertyChangeEvent evt) {
                            if ("progress".equals(evt.getPropertyName())) {
                                progressBar.setValue((Integer) evt.getNewValue());
                            }
                        }
                    });
                } else {
                    lineTextExeption.setText("Don`t send " + serialPortOpen.getPortName() + " is closed");
                }
        }
    }

    /**
     * UI callback implementation
     */
    private class UICallbackImpl implements UICallback {

        @Override
        public void setText(final String text) {
            lineTextExeption.setText(text);
        }

        /**
         * Sets loading progress
         *
         * @param progressPercent progress value to set
         */

        @Override
        public void setProgress(final int progressPercent) {
            progressBar.setValue(progressPercent);
        }

        /**
         * Performs visual operations on loading start - clears the text and shows popup with the progress bar .
         */
        @Override
        public void startLoading() {
            lineTextExeption.setText("Send file...");
            progressBar.setValue(0);
            progressBarPanel.setVisible(true);
        }

        /**
         * Performs visual operations on loading stop - hides progress bar
         */
        @Override
        public void stopLoading() {
            progressBarPanel.setVisible(false);
            loaderText = null;
        }

        /**
         * Shows error message to user
         *
         * @param message message to display
         */
        @Override
        public void showError(final String message) {
            JOptionPane.showMessageDialog(DataReceiver.this, message, "Error", JOptionPane.ERROR_MESSAGE);
        }

        @Override
        public void appendPixel(BufferedImage bufferedImage) {

            imagePanel.setImage(bufferedImage);
            imagePanel.updateUI();
            System.out.println("text test");
        }
    }


}





