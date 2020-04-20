package apryraz.tworld.clauses;

import apryraz.tworld.LiteralEnumerator;
import org.sat4j.core.VecInt;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;

public class Sensor1Builder implements ClauseBuilder {


    private final LiteralEnumerator en;

    public Sensor1Builder(LiteralEnumerator en) {
        this.en = en;
    }

    @Override
    public VecInt addClause(int x, int y, int i, int j) {
        int[] vect = {-en.getLiteralSensor1(x, y), -en.getLiteralTPosition(i, j, 1)};
        return new VecInt(vect);
    }
}
