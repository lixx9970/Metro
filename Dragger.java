package comp1110.ass2;


import javafx.scene.shape.Rectangle;


/**
 * The Dragger class which helps us when placing the tile on the specific location on the board, also help us determine the location on the board
 * It will appear as one black square with SQUARE_SIZE  for each of the dragger object, and it will store the x and y coordinates on the board.
 *
 * @author Zihao Li
 */

public class Dragger extends Rectangle {

    public double side;

    //Two instances which represent x anc y coordinates seperately
    public int x_cord;
    public int y_cord;

    /**
     * Dragger constructor with given side value
     *
     * @param side the double value of side for a square
     *
     * @author Zihao Li
     */

    //Constructor of the dragger which takes the side as the argument
    public Dragger(double side){
        super(side, side);
    }

    /**
     * A method to record the readable x,y coordinate for each dragger object
     *
     * @param col the int value of x coordinate of a dragger object on the gridpane
     *        y the int value of y coordinate of a dragger object on the gridpane
     *
     * @author Zihao Li
     */

    public void StoreAndTransferCoordinate(int col, int row){
        this.x_cord = row - 1;
        this.y_cord = col - 1;
    }

    /**
     * A method to transfer the x,y coordinate from integer value to String type
     *
     * @param d A dragger object
     *
     * @return result A string type of x,y coordinates for the given dragger object
     *
     * @author Zihao Li
     */
    public String CoordToString(Dragger d){
        String result = Integer.toString(d.x_cord) + Integer.toString(d.y_cord);
        return result;
    }

    @Override
    public String toString() {
        return "Dragger{" +
                "side=" + side +
                ", x_cord=" + x_cord +
                ", y_cord=" + y_cord +
                '}';
    }
}
