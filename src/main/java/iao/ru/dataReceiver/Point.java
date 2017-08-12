package iao.ru.dataReceiver;

import java.awt.geom.Point2D;

/**
 * Created by Zoxy1 on 12.08.17.
 */
public class Point extends Point2D{
    private double x;
    private double y;

    public Point(){

    }
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }
    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
