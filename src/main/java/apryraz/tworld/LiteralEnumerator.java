package apryraz.tworld;

public class LiteralEnumerator {

    private final int WorldDim;
    private final int WorldLinealDim;

    public LiteralEnumerator(int worldDim) {
        this.WorldDim = worldDim;
        this.WorldLinealDim = worldDim * worldDim;
    }

    public int getLiteralSize() {
        return (2 + 4 + 1 + 1) * this.WorldLinealDim;
    }

    /**
     * Convert a coordinate pair (x,y) to the integer value  t_[x,y]
     * of variable that stores that information in the formula, using
     * offset as the initial index for that subset of position variables
     * (past and future position variables have different variables, so different
     * offset values)
     *
     * @param x      x coordinate of the position variable to encode
     * @param y      y coordinate of the position variable to encode
     * @param offset initial value for the subset of position variables
     *               (past or future subset)
     * @return the integer indentifer of the variable  b_[x,y] in the formula
     **/
    public int coordToLineal(int x, int y, int offset) {
        return ((x - 1) * WorldDim) + (y - 1) + offset;
    }

    /**
     * Perform the inverse computation to the previous function.
     * That is, from the identifier t_[x,y] to the coordinates  (x,y)
     * that it represents
     *
     * @param lineal identifier of the variable
     * @param offset offset associated with the subset of variables that
     *               lineal belongs to
     * @return array with x and y coordinates
     **/
    public int[] linealToCoord(int lineal, int offset) {
        lineal = lineal - offset + 1;
        int[] coords = new int[2];
        coords[1] = ((lineal - 1) % WorldDim) + 1;
        coords[0] = (lineal - 1) / WorldDim + 1;
        return coords;
    }

    public int getEnumeratePosition(int x, int y) {
        return (x - 1) * this.WorldDim + y;
    }

    public int getLiteralTPosition(int x, int y, int t) {
        if (t == -1) {
            return getEnumeratePosition(x, y);
        } else {
            return this.WorldLinealDim + getEnumeratePosition(x, y);
        }
    }

    public int getLiteralDown(int x, int y) {
        return 6 * this.WorldLinealDim + getEnumeratePosition(x, y);
    }

    public int getLiteralUp(int x, int y) {
        return 5 * this.WorldLinealDim + getEnumeratePosition(x, y);
    }

    public int getLiteralSensor3(int x, int y) {
        return 4 * this.WorldLinealDim + getEnumeratePosition(x, y);
    }

    public int getLiteralSensor2(int x, int y) {
        return 3 * this.WorldLinealDim + getEnumeratePosition(x, y);
    }


    public int getLiteralSensor1(int x, int y) {
        return 2 * this.WorldLinealDim + getEnumeratePosition(x, y);
    }

}
