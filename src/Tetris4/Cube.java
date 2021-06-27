package Tetris4;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Cube {
    private int x,y;

    private Image color;

    public Cube(int x,int y, Image color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public Image getColor() {
        return color;
    }
    public void setColor(BufferedImage color){
        this.color = color;
    }

    public int getX() {
        return x;
    }
    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }
    public int getY() {
        return y;
    }


}
