package comp1110.ass2;


/**
 * The Coordinate class which helps transforming the string of location to x,y coordinates
 *
 * @author Zihao Li
 */

public class Coordinate {

        //Two instances of a Coordinate object
        public int x;
        public int y;

        /**
         * Coordinate constructor with given x and y values
         *
         * @param x the integer value of x
         * @param y the integer value of y
         *
         * @author Zihao Li
         */
        Coordinate(int x, int y){

            this.x  = x;
            this.y = y;
        }


        public int getX() {
            return this.x;
        }
        public int getY() {
            return this.y;
        }


    /**
     * Since the outer square has been occupied by the stations, I wrote this method to change the
     * old coordinate system to the new one.
     * Transform the string of placement into the x,y coordinates and return a Coordinate object
     *
     * @param placement A valid placement string
     * @author Zihao Li
     */
    public static Coordinate GetCord(String placement) {
        Coordinate c;

        //If x and y are equal, increasing both of them by one. And switch row-column structure to
        // the column-row structure on the board.

        if (Integer.valueOf(placement.charAt(0)) == Integer.valueOf(placement.charAt(1))) {
            int x = Integer.valueOf(placement.substring(0, 1)) + 1;
            int y = Integer.valueOf(placement.substring(1, 2)) + 1;
            c = new Coordinate(x, y);
        } else {
            int x = Integer.valueOf(placement.substring(1, 2)) + 1;
            int y = Integer.valueOf(placement.substring(0, 1)) + 1;
            c = new Coordinate(x, y);
        }
        return c;
    }

}
