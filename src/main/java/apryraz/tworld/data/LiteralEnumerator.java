package apryraz.tworld.data;

public class LiteralEnumerator {

    private final int WorldDim;
    private final int WorldLinealDim;
    static public final int PAST = -1;
    static public final int FUTURE = 1;
    public final int PAST_OFFSET = 0;
    public final int FUTURE_OFFSET;
    public final int SENSOR1_OFFSET;
    public final int SENSOR2_OFFSET;
    public final int SENSOR3_OFFSET;
    public final int SENSOR0_OFFSET;
    public final int UP_OFFSET;
    public final int DOWN_OFFSET;


    public LiteralEnumerator(int worldDim) {
        this.WorldDim = worldDim;
        this.WorldLinealDim = worldDim * worldDim;
        FUTURE_OFFSET = WorldLinealDim;
        SENSOR1_OFFSET = 2 * WorldLinealDim;
        SENSOR2_OFFSET = 3 * WorldLinealDim;
        SENSOR3_OFFSET = 4 * WorldLinealDim;
        SENSOR0_OFFSET = 5 * WorldLinealDim;
        UP_OFFSET = 6 * WorldLinealDim;
        DOWN_OFFSET = 7 * WorldLinealDim;

    }

    public int getNumVars() {
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
     * @return array with x and y coordinates
     **/
    public Position linealToPosition(int lineal) {
        int pos = (lineal - 1) % WorldLinealDim + 1;
        int x = pos % this.WorldDim + 1;
        int y = pos / this.WorldDim + 1;
        return new Position(x, y);
    }

    public int getEnumeratePosition(int x, int y) {
        return (y) * this.WorldDim + x + 1;
    }

    public int getLiteralTPosition(int x, int y, int t) {
        if (t == PAST) {
            return PAST_OFFSET + getEnumeratePosition(x, y);
        } else {
            return FUTURE_OFFSET + getEnumeratePosition(x, y);
        }
    }

    public int getLiteralTPosition(Position position, int t) {
        return getLiteralTPosition(position.getX(), position.getY(), t);
    }

    public int getLiteralDown(int x, int y) {
        return DOWN_OFFSET + getEnumeratePosition(x, y);
    }

    public int getLiteralUp(int x, int y) {
        return UP_OFFSET + getEnumeratePosition(x, y);
    }

    public int getLiteralSensor0(int x, int y) {
        return SENSOR0_OFFSET + getEnumeratePosition(x, y);
    }

    public int getLiteralSensor3(int x, int y) {
        return SENSOR3_OFFSET + getEnumeratePosition(x, y);
    }

    public int getLiteralSensor2(int x, int y) {
        return SENSOR2_OFFSET + getEnumeratePosition(x, y);
    }


    public int getLiteralSensor1(int x, int y) {
        return SENSOR1_OFFSET + getEnumeratePosition(x, y);
    }


    public int getWorldDim() {
        return this.WorldDim;
    }
}
