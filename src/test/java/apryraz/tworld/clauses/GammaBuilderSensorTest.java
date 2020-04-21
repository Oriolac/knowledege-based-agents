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

    @Test
    void addSensorClauseDown() throws ContradictionException {
        vecs = gammaBuilder.addSensorClauseDown(limit, cb);
        for (int x = 0; x < vecs.size(); x++) {
            VecInt vec = vecs.get(x);
            Position p1 = en.linealToPosition(vec.get(0));
            Position p2 = en.linealToPosition(vec.get(1));
            for (int i = 0; i < WDIM; i++) {
                for (int j = 0; j < p1.getY() - limit; j++, x++) {
                    assertTrue(p2.isDown(p1));
                }
            }
        }
    }

    void addSensorClauseUp() throws ContradictionException {
        vecs = gammaBuilder.addSensorClauseUp(limit, cb);
        vecs.forEach(a -> System.out.println(en.linealToPosition(a.get(0)).toString() + en.linealToPosition(a.get(1)).toString()));


    }

    void addSensorClauseLeft() {

    }

    void addSensorClauseRight() {

    }
}
