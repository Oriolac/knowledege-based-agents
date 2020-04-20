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
