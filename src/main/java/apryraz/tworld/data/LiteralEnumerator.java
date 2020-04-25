package apryraz.tworld.data;

import org.sat4j.core.VecInt;

import java.security.InvalidParameterException;

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

    void checkCorrectPosition(int x, int y) throws NotCorrectPositionException {
        if (!(x > 0 && x <= WorldDim && y > 0 && y <= WorldDim))
            throw new NotCorrectPositionException();
    }

    public Position newPosition(int x, int y) throws NotCorrectPositionException {
        checkCorrectPosition(x, y);
        return new Position(x, y);
    }

    public int getNumVars() {
        return (2 + 4 + 1 + 1) * this.WorldLinealDim;
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
        lineal = Math.abs(lineal);
        int pos = (lineal - 1) % WorldLinealDim;
        int x = pos % this.WorldDim;
        int y = pos / this.WorldDim;
        return new Position(x+1, y+1);
    }

    public int getEnumeratePosition(int x, int y) throws NotCorrectPositionException {
        x = Math.abs(x);
        y = Math.abs(y);
        checkCorrectPosition(x, y);
        return (x) + (y - 1) * this.WorldDim;
    }

    public int getLiteralTPosition(int x, int y, int t) throws NotCorrectPositionException {
        if (t == PAST) {
            return PAST_OFFSET + getEnumeratePosition(x, y);
        } else {
            return FUTURE_OFFSET + getEnumeratePosition(x, y);
        }
    }

    public int getLiteralTPosition(VecInt vec, int t) throws NotCorrectPositionException {
        return this.getLiteralTPosition(Math.abs(vec.get(0)), Math.abs(vec.get(1)), t);
    }

    public int getLiteralTPosition(Position position, int t) throws NotCorrectPositionException {
        return getLiteralTPosition(position.getX(), position.getY(), t);
    }

    public int getLiteralDown(int x, int y) throws NotCorrectPositionException {
        return DOWN_OFFSET + getEnumeratePosition(x, y);
    }


    public int getLiteralUp(int x, int y) throws NotCorrectPositionException {
        return UP_OFFSET + getEnumeratePosition(x, y);
    }

    public int getLiteralSensor0(int x, int y) throws NotCorrectPositionException {
        return SENSOR0_OFFSET + getEnumeratePosition(x, y);
    }

    public int getLiteralSensor3(int x, int y) throws NotCorrectPositionException {
        return SENSOR3_OFFSET + getEnumeratePosition(x, y);
    }


    public int getLiteralSensor2(int x, int y) throws NotCorrectPositionException {
        return SENSOR2_OFFSET + getEnumeratePosition(x, y);
    }

    public int getLiteralSensor1(int x, int y) throws NotCorrectPositionException {
        return SENSOR1_OFFSET + getEnumeratePosition(x, y);
    }


    public int getWorldDim() {
        return this.WorldDim;
    }
}
