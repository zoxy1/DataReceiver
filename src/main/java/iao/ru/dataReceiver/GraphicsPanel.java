package iao.ru.dataReceiver;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Zoxy1 on 12.08.17.
 */
public class GraphicsPanel extends JPanel{

    @Override
    public void paintComponent (Graphics g){
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, 10, 10);

    }
}