package apryraz.tworld.clauses;

import apryraz.tworld.data.LiteralEnumerator;
import apryraz.tworld.data.NotCorrectPositionException;
import apryraz.tworld.data.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sat4j.core.VecInt;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GammaBuilderTest {


    private static final int WDIM = 6;
    private static final int WLINEALDIM = WDIM * WDIM;
    private LiteralEnumerator en;
    private GammaBuilder gammaBuilder;
    private List<VecInt> vecs;

    @BeforeEach
    void setUp() throws ContradictionException {
        en = new LiteralEnumerator(WDIM);
        gammaBuilder = new GammaBuilder(en);
    }

    @Test
    void addMemorableClauses() throws ContradictionException, NotCorrectPositionException {
        vecs = gammaBuilder.addMemorableClauses();
        for (int i = 1; i <= WLINEALDIM; i++) {
            assertEquals(-i, vecs.get(i - 1).get(0));
            assertEquals(-(i + en.FUTURE_OFFSET), vecs.get(i - 1).get(1));
        }
    }

    @Test
    void addPirateUpClauses() throws ContradictionException, NotCorrectPositionException {
        vecs = gammaBuilder.addPirateUpClauses();
        int x = 0;
        for (int v = 0; v < vecs.size(); x++) {
            for (int y = 0; y < WDIM; y++) {
                Position pUp = en.linealToPosition(vecs.get(v).get(0));
                assertEquals(en.newPosition(x+1, y+1), pUp);
                for (int i = 0; i < WDIM; i++) {
                    for (int j = 0; j < (y + 1); j++, v++) {
                        Position pNeg = en.linealToPosition(vecs.get(v).get(1));
                        assertEquals(en.newPosition(i+1, j+1), pNeg);
                    }
                }
            }
        }
    }

    @Test
    void addPirateDownClauses() throws ContradictionException, NotCorrectPositionException {
        vecs = gammaBuilder.addPirateDownClauses();
        int x = 0;
        for (int v = 0; v < vecs.size(); x++) {
            for (int y = 0; y < WDIM - 1; y++) {
                Position pDown = en.linealToPosition(vecs.get(v).get(0));
                System.out.println("CURRENT: " + pDown.toString());
                assertEquals(en.newPosition(x+1, y+1), pDown);
                for (int i = 0; i < WDIM; i++) {
                    for (int j = y + 1; j < WDIM; j++, v++) {
                        Position pNeg = en.linealToPosition(vecs.get(v).get(1));
                        System.out.println("NEG: " + pNeg.toString());
                        assertEquals(en.newPosition(i+1, j+1), pNeg);
                    }
                }
            }
        }
    }

    @Test
    void addSensor3ClauseSquare() throws ContradictionException, NotCorrectPositionException {
        vecs = gammaBuilder.addSensor3ClauseSquare();
        for(VecInt vec: vecs) {
            Position p1 = en.linealToPosition(vec.get(0));
            Position p2 = en.linealToPosition(vec.get(1));
            assertTrue(p1.isAdjacent(p2, 1));
        }
    }

    @Test
    void addSensor0ClauseSquare() throws ContradictionException, NotCorrectPositionException {
        vecs = gammaBuilder.addSensor0ClauseSquare();
        for(VecInt vec: vecs) {
            Position p1 = en.linealToPosition(vec.get(0));
            Position p2 = en.linealToPosition(vec.get(1));
            assertTrue(p1.isAdjacent(p2, 2));
        }
    }

    @Test
    void addSensor1Clause() throws ContradictionException, NotCorrectPositionException {
        ClauseBuilder builder = new Sensor1Builder(en);
        vecs = gammaBuilder.addSensor1Clause(builder);
        assertEquals(WLINEALDIM * (WLINEALDIM - 1), vecs.size());
        for(VecInt vec: vecs) {
            int l1 = vec.get(0);
            int l2 = vec.get(1);
            Position p1 = en.linealToPosition(l1);
            Position p2 = en.linealToPosition(l2);
            assertNotEquals(p1, p2);
            assertTrue(l1 < 0);
            assertTrue(l2 < 0);
        }
    }

    @Test
    void addSensor2ClauseSame() throws ContradictionException, NotCorrectPositionException {
        vecs = gammaBuilder.addSensor2ClauseSame();
        for(VecInt vec: vecs) {
            int l1 = vec.get(0);
            int l2 = vec.get(1);
            Position p1 = en.linealToPosition(l1);
            Position p2 = en.linealToPosition(l2);
            assertEquals(p1, p2);
            assertTrue(l1 < 0 && l2 < 0);
        }
    }
}
