package Tetris4.res;

public class RandomShape {

    int index = 1;
    int min,max;
    int[][] shape;

    public RandomShape(int[][] shape,int min, int max){
        this.shape = shape;
        this.max = max;
        this.min = min;
    }

    public int getIndex() {
        return index;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public int[][] getShape() {
        return shape;
    }

    public void setShape(int[][] shape) {
        this.shape = shape;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void increseIndex(){
        index++;
    }
}
