package Tetris4;

import Tetris4.res.RandomShape;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
*           Made by Alfred Roos
*           2021-06-27
 * */

public class Tetris extends JPanel {

    /*
    TODO
    preview
     */

    public static final int width = 12;
    public static final int height = 21;
    public static final int dimension = 45;
    public static int gameDelay = 15;
    public static ArrayList<ArrayList<Cube>> mesh = new ArrayList<>();
    public static String cubeImageSrc = "res/";
    public static int tick= 0;
    private Shape player;
    public static  Shape templayer;
    public static JLabel score;
    public static JLabel highScore;
    public static int downSpeed = dimension;
    public static int scorE=0;
    private NextShapePanel nextShapePanel;
    public static ArrayList<int[][]> shapeQueue = new ArrayList<>();

    public Tetris() {


        InitializeMesh();
        LoadHighScore();
        templayer= new Shape(0,0,Shapes.l,getImage(cubeImageSrc+getCubeImageSrc(Shapes.t)));
        int[][] shape = getShape();
        player = new Shape(((width/2)-2)*dimension,0*dimension,shape,getImage(cubeImageSrc+getCubeImageSrc(shape)));
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==37&&player.canMove(-1,0)) {
                    player.setX(player.getX()-1*dimension);
                }
                else if(e.getKeyCode()==39&&player.canMove(1,0)) {//Go right
                    player.setX(player.getX()+1*dimension);
                }
                else if(e.getKeyCode()==40) {
                    downSpeed = 4;
                }
                else if(e.getKeyCode()==38&&player.CanRotate()){
                    player.Rotate();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==40) {
                    downSpeed = dimension;
                }
            }
        });
        UpdateQueue();
    }

    private void LoadHighScore() {
        try{
            String path = System.getProperty("user.home")+"\\Tetris\\highscore.txt";
            List<String> file = Files.readAllLines(Paths.get(path));
            highScore.setText("HighScore: "+String.valueOf(file.get(0)));
        }
        catch (Exception e){
            highScore.setText("HighScore: ");
        }
    }

    private void InitializeMesh() {
        score.setText("Score: "+String.valueOf(scorE));
        for(int i = 0;i<height;i++) {
            ArrayList<Cube> cubeArray= new ArrayList<>();
            for(int ii = 0;ii<width;ii++){
                if(i == 20) {
                    cubeArray.add(new Cube(ii*dimension,i*dimension,getImage(cubeImageSrc+"blackCube.png")));
                }
                else if(ii == 0||ii==width-1)
                    cubeArray.add(new Cube(ii*dimension,i*dimension,getImage(cubeImageSrc+"blackCube.png")));
                else
                    cubeArray.add(null);

            }
            mesh.add(cubeArray);
        }
    }

    public BufferedImage getImage (String path){
        BufferedImage image = null;
        try {
           // image = ImageIO.read(new File(path));
            image = ImageIO.read(this.getClass().getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private void GameOver() {
        System.out.println("gameover");
        mesh.clear();
        InitializeMesh();
        try{
        if(scorE>Integer.valueOf(highScore.getText().split(":")[1].split(" ")[1])){
            FileWriter fw = new FileWriter(System.getProperty("user.home")+"\\Tetris\\highscore.txt");
            fw.write(String.valueOf(scorE));
            fw.close();
            }
        }
        catch (Exception e){
            try {
                Files.createDirectories(Paths.get(System.getProperty("user.home") + "\\Tetris\\"));
                FileWriter fw = new FileWriter(System.getProperty("user.home")+"\\Tetris\\highscore.txt");
                fw.write(String.valueOf(scorE));
                fw.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        scorE =0;
        LoadHighScore();
    }

    public static int[][] getShape() {
        Random r = new Random();
        RandomShape[] randomShapes = new RandomShape[7];
        randomShapes[0] = new RandomShape(Shapes.i,0,100);
        randomShapes[1] = new RandomShape(Shapes.j,100,200);
        randomShapes[2] = new RandomShape(Shapes.l,200,300);
        randomShapes[3] = new RandomShape(Shapes.o,300,400);
        randomShapes[4] = new RandomShape(Shapes.t,400,500);
        randomShapes[5] = new RandomShape(Shapes.s,500,600);
        randomShapes[6] = new RandomShape(Shapes.z,600,700);
        return randomShapes[r.nextInt(7)].getShape();
    }

    public static String getCubeImageSrc(int[][] t) {
        if(t==Shapes.t)
            return "yellowCube.png";
        else if(t==Shapes.l)
            return "redCube.png";
        else if(t==Shapes.j)
            return "greenCube.png";
        else if(t==Shapes.z)
            return "lightgreenCube.png";
        else if(t==Shapes.i)
            return "blueCube.png";
        else if(t==Shapes.o)
            return "darkblueCube.png";
        else if(t==Shapes.s)
            return "pinkCube.png";

        return "blackCube.png";
    }

    private void DropNewShape() {
        UpdateQueue();
        nextShapePanel.Update();
        for(Cube mycube:player.getCubes()){
            for(int i = 0; i<mesh.get(mycube.getY()/dimension).size();i++){
                mesh.get(mycube.getY()/dimension).set(mycube.getX()/dimension, mycube);
            }
        }
        int[][] shape = shapeQueue.get(0);
        player = new Shape(width/2*dimension,0*dimension,shape,getImage(cubeImageSrc+getCubeImageSrc(shape)));
        scorE+=10;
        score.setText("Score: "+String.valueOf(scorE));
    }

    private ArrayList<Cube> getEmptyArray() {
        ArrayList<Cube> emptylist = new ArrayList<>();
        for(int i= 0; i< width;i++) {
            if(i==0||i==width-1)
                emptylist.add(new Cube(i*dimension,0*dimension,getImage(cubeImageSrc+"blackCube.png")));
            else
            emptylist.add(null);
        }
        return emptylist;
    }

    public static void UpdateQueue() {
        if(shapeQueue.size()>0)
            shapeQueue.remove(0);
        else
            shapeQueue.add(getShape());
        shapeQueue.add(getShape());
    }


    private void UpdateArray(int to) {
        for(int i = 0;i<to;i++) {
            for(int ii = 0;ii<mesh.get(i).size();ii++) {
                if(mesh.get(i).get(ii)!=null)
                    mesh.get(i).get(ii).setY((i)*dimension);
            }
        }
        //printMesh();
    }
    ArrayList<Cube> removeRow = new ArrayList<>();
    int removeY = 0;
    private void removeRow(int i) {

        removeY = i;
        Thread thread = new Thread(){
            public void run(){
                for(Cube cube:mesh.get(i))
                {
                    removeRow.add(cube);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    repaint();
                }
            }
        };
        thread.start();
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        removeRow.clear();
        mesh.remove(mesh.get(i));
        mesh.add(0,getEmptyArray());
        UpdateArray(i);
    }

    private void checkRows() {
        int cubecount = 0;
        for(int i = 0;i<mesh.size();i++) {
            for(int ii = 0;ii<mesh.get(i).size();ii++) {
                if(mesh.get(i).get(ii)!=null&&i!=20) {
                    cubecount++;
                }
                if(cubecount==width) {
                   // printMesh();
                    removeRow(i);
                    scorE+=100;
                    score.setText("Score: "+String.valueOf(scorE));
                }
            }cubecount = 0;
        }
    }

    public void Update() {
    if (tick%downSpeed==0){
        if(player.canMove(0,1)) {
            player.setY(player.getY()+dimension);
        }
        else{
            if(player.getY()==0) {
                GameOver();
            }
            else {
                DropNewShape();
            }
        }
    }
        player.UpdateArrayList();

        repaint();
        checkRows();
        tick++;
    }

    private void DrawGrid(Graphics g) {
        //Draws the grid
        for(int x = 0; x<=width;x++){
            for(int y = 0; y<=height;y++) {
                //g.drawImage(getImage(cubeImageSrc+"blackCube.png"),x*dimension,y*dimension,dimension,dimension,null);
                g.setColor(Color.white);
                g.drawRect(x*dimension,y*dimension,dimension,dimension);
            }
        }
    }
    private void DrawCubes(Graphics g){
        for(int y = 0;y<mesh.size();y++){
            for (int x=0;x<mesh.get(y).size();x++){
                if(mesh.get(y).get(x)!=null) {
                    Cube cube = mesh.get(y).get(x);
                    g.drawImage(cube.getColor(),x*dimension,y*dimension,dimension,dimension,null);
                }
            }
        }
    }
    private void DrawPlayerShape(Graphics g){
        for (Cube cube:
             player.getCubes()) {
          //  System.out.println(cube.getX()+" "+cube.getY());
            g.drawImage(cube.getColor(),cube.getX(),cube.getY(),dimension,dimension,null);
        }
    }
    private void DrawTempPlayerShape(Graphics g){
        for (Cube cube:
                templayer.getCubes()) {
            //  System.out.println(cube.getX()+" "+cube.getY());
          //  g.drawImage(cube.getColor(),cube.getX(),cube.getY(),dimension,dimension,null);
        }
    }
    private void DrawRemoveRow(Graphics g){
        for (int x=0;x<removeRow.size();x++){
            if(removeRow.get(x)!=null) {
                Cube cube =removeRow.get(x);
                g.drawImage(getImage(cubeImageSrc+"blackCube.png"),x*dimension,removeY*dimension,dimension,dimension,null);
            }
        }

    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        DrawGrid(g);
        DrawCubes(g);
        DrawPlayerShape(g);
        DrawTempPlayerShape(g);
        DrawRemoveRow(g);
    }

    public static void main(String[] args) {

        JFrame window = new JFrame();
        window.setSize((width)*dimension+9,((height)+dimension/2/10)*dimension);
        window.setTitle("T  E  T  R  I  S");
        window.setResizable(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel container = new JPanel();
        LayoutManager containerGrid = new BoxLayout(container,BoxLayout.Y_AXIS);
        Box boxes[] = new Box[2];
        boxes[0] = Box.createHorizontalBox();
        boxes[1] = Box.createHorizontalBox();

        boxes[0].createGlue();
        boxes[1].createGlue();


        container.add(boxes[0]);
        container.add(boxes[1]);

        JPanel gameInfoPanel = new JPanel();
        LayoutManager containerGrids = new BoxLayout(container,BoxLayout.Y_AXIS);
        Box boxess[] = new Box[3];
        boxess[0] = Box.createHorizontalBox();
        boxess[1] = Box.createHorizontalBox();
        boxess[2] = Box.createHorizontalBox();

        boxess[0].createGlue();
        boxess[1].createGlue();
        boxess[2].createGlue();

        gameInfoPanel.add(boxess[0]);
        gameInfoPanel.add(boxess[1]);
        gameInfoPanel.add(boxess[2]);

        score = new JLabel();
        score.setForeground(Color.white);
        score.setFont(new Font(Font.DIALOG, Font.BOLD,  20));
        highScore = new JLabel();
        highScore.setForeground(Color.white);
        highScore.setFont(new Font(Font.DIALOG, Font.BOLD,  20));
        Tetris tetris = new Tetris();
        container.setBackground(Color.black);
        NextShapePanel nextShapePanel = new NextShapePanel(tetris);
        nextShapePanel.setBackground(Color.black);
        gameInfoPanel.setBackground(Color.black);
        nextShapePanel.setPreferredSize(new Dimension(60,60));

        boxess[0].add(score);
        boxess[1].add(highScore);
        boxess[2].add(nextShapePanel);

        tetris.nextShapePanel = nextShapePanel;
        tetris.nextShapePanel.Update();
        tetris.setFocusable(true);
        tetris.setBackground(Color.black);
        gameInfoPanel.setPreferredSize(new Dimension(width*dimension,dimension));
        tetris.setPreferredSize(new Dimension(width*dimension,height*dimension));

        boxes[0].add(gameInfoPanel);
        boxes[1].add(tetris);

        window.add(container);
        window.setVisible(true);
        //GameLoop
        while (true)
        {
            tetris.Update();
            try {
                Thread.sleep(gameDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
