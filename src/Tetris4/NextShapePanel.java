package Tetris4;

import javax.swing.*;
import java.awt.*;

public class NextShapePanel extends JPanel {

    Tetris tetris;
    private Shape shape;
    public NextShapePanel(Tetris tetris){
        shape = new Shape(0,0,Shapes.t,tetris.getImage(Tetris.cubeImageSrc+"redCube.png"));
        this.tetris = tetris;
        this.shape.UpdateArrayList();
    }
    public void Update()
    {
        shape.setShape(Tetris.shapeQueue.get(1));
        shape.setColor(tetris.getImage(Tetris.cubeImageSrc+Tetris.getCubeImageSrc(Tetris.shapeQueue.get(1))));
        shape.Rotate();
        shape.Rotate();
        shape.Rotate();
        shape.Rotate();
        shape.UpdateArrayList();

        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int dim = 15;
        int dimm = 3;
        for (Cube cube:
                shape.getCubes()) {
            System.out.println(cube.getX());
            g.drawImage(cube.getColor(),cube.getX()/dimm,cube.getY()/dimm,dim,dim,null);
        }
    }
}
