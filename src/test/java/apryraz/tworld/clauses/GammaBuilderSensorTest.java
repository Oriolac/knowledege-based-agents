package apryraz.tworld.clauses;

import apryraz.tworld.data.LiteralEnumerator;
import apryraz.tworld.data.Position;
import org.junit.jupiter.api.Test;
import org.sat4j.core.VecInt;
import org.sat4j.specs.ContradictionException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class GammaBuilderSensorTest {

    GammaBuilder gammaBuilder;
    List<VecInt> vecs;
    int WDIM;
    LiteralEnumerator en;
    ClauseBuilder cb;
    int limit;
    int start;

    @Test
    void addSensorClauseDown() throws ContradictionException {
        vecs = gammaBuilder.addSensorClauseDown(limit, cb);
        for (VecInt vec : vecs) {
            Position p1 = en.linealToPosition(vec.get(0));
            Position p2 = en.linealToPosition(vec.get(1));
            assertTrue(p2.isDown(p1));
            assertTrue(p2.getY() < p1.getY() - limit);
        }
    }

    @Test
    void addSensorClauseUp() throws ContradictionException {
        vecs = gammaBuilder.addSensorClauseUp(start, cb);
        for (VecInt vec : vecs) {
            Position p1 = en.linealToPosition(vec.get(0));
            Position p2 = en.linealToPosition(vec.get(1));
            assertTrue(p2.isUp(p1));
            assertTrue(p2.getY() > p1.getY() + limit);
        }

    }

    @Test
    void addSensorClauseLeft() throws ContradictionException {
        vecs = gammaBuilder.addSensorClauseLeft(limit, cb);
        vecs.forEach(a -> System.out.println(en.linealToPosition(a.get(0)).toString() + en.linealToPosition(a.get(1)).toString()));
        for (VecInt vec : vecs) {
            Position p1 = en.linealToPosition(vec.get(0));
            Position p2 = en.linealToPosition(vec.get(1));
            assertTrue(p2.isLeft(p1));
            assertTrue(p2.getX() < p1.getX() - limit);
        }
    }

    @Test
    void addSensorClauseRight() throws ContradictionException {
        vecs = gammaBuilder.addSensorClauseRight(start, cb);
        for (VecInt vec : vecs) {
            Position p1 = en.linealToPosition(vec.get(0));
            Position p2 = en.linealToPosition(vec.get(1));
            assertTrue(p2.isRight(p1));
            assertTrue(p2.getX() > p1.getX() + limit);
        }
    }
}
