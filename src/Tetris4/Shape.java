package Tetris4;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Shape {

    private ArrayList<Cube> cubes = new ArrayList<>();
    private BufferedImage color;
    private int[][] shape;
    private int[] shaperot = new int[16];
    private int rotation = 4;
    private int x,y;

    public Shape(int x ,int y,int[][] shape,BufferedImage color) {
        this.x = x;
        this.y = y;
        this.shape = shape;
        this.color = color;
        UpdateArrayList();
        Rotate();
    }


    private static int rotate(int x,int y,int r) {
        switch(r % 4)
        {
            case 0: return y*4+x;
            case 1: return 12+y-(x*4);
            case 2: return 15-(y*4)-x;
            case 3: return 3-y+(x*4);
        }
        return 0;
    }

    public void PrintShape() {
        int x = 0;
        for(int i= 0; i<shaperot.length;i++){
        System.out.print(shaperot[i]+",");
            x++;
            if(x==4){
                x= 0;
                System.out.println();
            }
        }
            System.out.println();
    }

    public boolean CanRotate() {
        //Om man hamnar i någon annan kub ska den leta efter en plats den får
        //plats på
        Tetris.templayer.setY(y);
        Tetris.templayer.setX(x);
        Tetris.templayer.shape = this.shape;
        Tetris.templayer.rotation = rotation;
        Tetris.templayer.Rotate();
        Tetris.templayer.UpdateArrayList();
        for(Cube rcube:Tetris.templayer.getCubes()){
            for (int y = 0;y<Tetris.mesh.size();y++){
                for(int x = 0;x<Tetris.mesh.get(y).size();x++){
                    if(Tetris.mesh.get(y).get(x)!=null&&
                    rcube.getX()==Tetris.mesh.get(y).get(x).getX()&&
                    rcube.getY()==y*Tetris.dimension){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void Rotate() {
        rotation--;
        if(rotation==0)
            rotation = 4;
        for (int y = 0;y<shape.length;y++) {
            for (int x = 0;x<shape[y].length;x++) {

                shaperot[rotate(x,y,rotation)] = shape[y][x];
            }
        }
    }

    public void UpdateArrayList() {
        cubes.clear();
        int x = 0;
        int row = 0;
        for(int i = 0; i<shaperot.length;i++) {
            if(shaperot[i]==1){
                Cube newCube = new Cube(x*Tetris.dimension+this.x,row*Tetris.dimension+this.y,color);
                cubes.add(newCube);
            }
            x++;
            if(x==4) {
                x=0;
                row++;
            }
        }
    }

    public ArrayList<Cube> getCubes() {
        return cubes;
    }
    public void setCubes(ArrayList<Cube> cubes){
        this.cubes = cubes;
    }

    public void setColor(BufferedImage color){
        this.color = color;
    }

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public void setShape(int[][] shape){
        this.shape = shape;
    }

    public boolean canMove(int xDir, int yDir) {
        for (Cube mycube: cubes){//loops through my cubes
            for(int i = 0; i<Tetris.mesh.size();i++){//loops through the mesh rows
                for (int ii = 0;ii<Tetris.mesh.get(i).size();ii++){//loops through the rows
                    if(Tetris.mesh.get(i).get(ii)!=null){
                        Cube placedCube = Tetris.mesh.get(i).get(ii);
                        if(mycube.getY()+yDir*Tetris.dimension== i*Tetris.dimension
                                &&mycube.getX()+xDir*Tetris.dimension== placedCube.getX()){
                            return false;
                        }
                    }
                }
            }
        }return true;
    }
}
