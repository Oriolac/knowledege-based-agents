package apryraz.tworld.clauses;

import apryraz.tworld.LiteralEnumerator;
import org.sat4j.core.VecInt;

public class Sensor3Builder implements ClauseBuilder {

    private final LiteralEnumerator en;

    public Sensor3Builder(LiteralEnumerator en) {
        this.en = en;
    }

    @Override
    public VecInt addClause(int x, int y, int i, int j) {
        int[] vect = {-en.getLiteralSensor3(x, y), -en.getLiteralTPosition(i, j, 1)};
        return new VecInt(vect);
    }
}
