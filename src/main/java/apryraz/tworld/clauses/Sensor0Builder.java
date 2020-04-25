package apryraz.tworld.clauses;

import apryraz.tworld.data.LiteralEnumerator;
import apryraz.tworld.data.NotCorrectPositionException;
import org.sat4j.core.VecInt;

public class Sensor0Builder implements ClauseBuilder {

    private final LiteralEnumerator en;

    public Sensor0Builder(LiteralEnumerator en) {
        this.en = en;
    }

    /**
     * creates the clauses with the relevant literals when the
     * answer of the metal sensor is 0
     * @param x coordinate x of the actual position of the agent
     * @param y coordinate y of the actual position of the agent
     * @param i coordinate x of a position of the world
     * @param j coordinate y of a position of the world
     * @return the clause obtained from the literals
     */
    @Override
    public VecInt addClause(int x, int y, int i, int j) throws NotCorrectPositionException {
        int[] vect = {-en.getLiteralSensor0(x, y), -en.getLiteralTPosition(i, j, 1)};
        return new VecInt(vect);
    }
}
