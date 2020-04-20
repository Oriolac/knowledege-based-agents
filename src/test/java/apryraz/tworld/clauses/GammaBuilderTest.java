package apryraz.tworld.clauses;

import apryraz.tworld.data.LiteralEnumerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sat4j.core.VecInt;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GammaBuilderTest {


    private static final int WDIM = 5;
    private static final int WLINEALDIM = WDIM * WDIM;
    private LiteralEnumerator en;
    private GammaBuilder gammaBuilder;
    private ISolver solver;
    private List<VecInt> vecs;

    @BeforeEach
    void setUp() throws ContradictionException {
        en = new LiteralEnumerator(WDIM);
        gammaBuilder = new GammaBuilder(en);
    }

    @Test
    void addMemorableClauses() throws ContradictionException {
        vecs = gammaBuilder.addMemorableClauses();
        for(int i = 1; i <= WLINEALDIM; i++) {
            assertEquals(-i, vecs.get(i-1).get(0));
        }
    }

    @Test
    void addPirateUpClauses() throws ContradictionException {
        vecs = gammaBuilder.addPirateUpClauses();
        for(int i = 1; i <= WLINEALDIM; i++) {
            assertEquals(-(i + en.UP_OFFSET), vecs.get(i-1).get(0));
        }
    }

    @Test
    void addPirateDownClauses() throws ContradictionException {
        vecs = gammaBuilder.addPirateDownClauses();
        for(int i = 1; i <= WLINEALDIM; i++) {
            assertEquals(-(i + en.DOWN_OFFSET), vecs.get(i-1).get(0));
        }
    }

    @Test
    void addSensor3ClauseSquare() throws ContradictionException {
        vecs = gammaBuilder.addSensor3ClauseSquare();
        for(int i = 1; i <= WLINEALDIM; i++) {
            assertEquals(-(i + en.SENSOR3_OFFSET), vecs.get(i-1).get(0));
        }
    }

    @Test
    void addSensor0ClauseSquare() {
    }

    @Test
    void addSensor1Clause() {
    }

    @Test
    void addSensor2ClauseSame() {
    }

    @Test
    void addSensorClauseDown() {
    }

    @Test
    void addSensorClauseUp() {
    }

    @Test
    void addSensorClauseLeft() {
    }

    @Test
    void addSensorClauseRight() {
    }

    @AfterEach
    void finish() {
        for (int i = 1; i <= WLINEALDIM; i++) {
            assertEquals(-(i + en.FUTURE_OFFSET), vecs.get(i-1).get(1));
        }
    }
}
