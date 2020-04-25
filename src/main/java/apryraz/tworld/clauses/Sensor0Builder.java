package apryraz.tworld.clauses;

import apryraz.tworld.data.LiteralEnumerator;
import apryraz.tworld.data.NotCorrectPositionException;
import org.sat4j.core.VecInt;

public class Sensor0Builder implements ClauseBuilder {

    private final LiteralEnumerator en;

    public Sensor0Builder(LiteralEnumerator en) {
        this.en = en;
    }


    @Override
    public VecInt addClause(int x, int y, int i, int j) throws NotCorrectPositionException {
        int[] vect = {-en.getLiteralSensor0(x, y), -en.getLiteralTPosition(i, j, 1)};
        return new VecInt(vect);
    }
}
