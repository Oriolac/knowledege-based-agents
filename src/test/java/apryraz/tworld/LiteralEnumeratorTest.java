package apryraz.tworld;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LiteralEnumeratorTest {

    private static final int WORLD_DIM = 4;
    private static final int WORLD_LINEAL_DIM = WORLD_DIM * WORLD_DIM;
    LiteralEnumerator enumerator;

    @Before
    public void setUp() {
        enumerator = new LiteralEnumerator(WORLD_DIM);
    }

    @Test
    public void getNumClauses() {
        assertEquals(128, enumerator.getNumClauses());
    }

    @Test
    public void coordToLineal() {
    }

    @Test
    public void linealToCoord() {
    }

    @Test
    public void getEnumeratePosition() {
        assertEquals(1, enumerator.getEnumeratePosition(1, 1));
        assertEquals(2, enumerator.getEnumeratePosition(2, 1));
        assertEquals(5, enumerator.getEnumeratePosition(1, 2));
        assertEquals(11, enumerator.getEnumeratePosition(3, 3));
    }

    @Test
    public void getLiteralTPosition() {
        assertEquals(1, enumerator.getLiteralTPosition(1, 1, -1));
        assertEquals(2, enumerator.getLiteralTPosition(2, 1, -1));
        assertEquals(5, enumerator.getLiteralTPosition(1, 2, -1));
        assertEquals(WORLD_LINEAL_DIM + 1, enumerator.getLiteralTPosition(1, 1, 1));
        assertEquals(WORLD_LINEAL_DIM + 2, enumerator.getLiteralTPosition(2, 1, 1));
        assertEquals(WORLD_LINEAL_DIM + 5, enumerator.getLiteralTPosition(1, 2, 1));
    }

    @Test
    public void getLiteralDown() {
        assertEquals(WORLD_LINEAL_DIM * 6 + 1, enumerator.getLiteralSensor2(1, 1));
        assertEquals(WORLD_LINEAL_DIM * 6 + 2, enumerator.getLiteralSensor2(2, 1));
        assertEquals(WORLD_LINEAL_DIM * 6 + 5, enumerator.getLiteralSensor2(1, 2));
    }

    @Test
    public void getLiteralUp() {
        assertEquals(WORLD_LINEAL_DIM * 5 + 1, enumerator.getLiteralSensor2(1, 1));
        assertEquals(WORLD_LINEAL_DIM * 5 + 2, enumerator.getLiteralSensor2(2, 1));
        assertEquals(WORLD_LINEAL_DIM * 5 + 5, enumerator.getLiteralSensor2(1, 2));
    }

    @Test
    public void getLiteralSensor3() {
        assertEquals(WORLD_LINEAL_DIM * 4 + 1, enumerator.getLiteralSensor2(1, 1));
        assertEquals(WORLD_LINEAL_DIM * 4 + 2, enumerator.getLiteralSensor2(2, 1));
        assertEquals(WORLD_LINEAL_DIM * 4 + 5, enumerator.getLiteralSensor2(1, 2));
    }

    @Test
    public void getLiteralSensor2() {
        assertEquals(WORLD_LINEAL_DIM * 3 + 1, enumerator.getLiteralSensor2(1, 1));
        assertEquals(WORLD_LINEAL_DIM * 3 + 2, enumerator.getLiteralSensor2(2, 1));
        assertEquals(WORLD_LINEAL_DIM * 3 + 5, enumerator.getLiteralSensor2(1, 2));
    }

    @Test
    public void getLiteralSensor1() {
        assertEquals(WORLD_LINEAL_DIM * 2 + 1, enumerator.getLiteralSensor1(1, 1));
        assertEquals(WORLD_LINEAL_DIM * 2 + 2, enumerator.getLiteralSensor1(2, 1));
        assertEquals(WORLD_LINEAL_DIM * 2 + 5, enumerator.getLiteralSensor1(1, 2));
    }
}