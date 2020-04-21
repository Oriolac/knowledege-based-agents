package apryraz.tworld.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LiteralEnumeratorTest {

    private static final int WORLD_DIM = 4;
    private static final int WORLD_LINEAL_DIM = WORLD_DIM * WORLD_DIM;
    int[][] pos = new int[][]{{0, 0}, {1, 0}, {0, 1}};
    int[] res = new int[]{1, 2, 5};
    LiteralEnumerator en;

    @BeforeEach
    public void setUp() {
        en = new LiteralEnumerator(WORLD_DIM);
    }

    @Test
    public void getNumClauses() {
        assertEquals(128, en.getNumVars());
    }

    @Test
    public void getEnumeratePosition() {
        assertEquals(1, en.getEnumeratePosition(0, 0));
        assertEquals(2, en.getEnumeratePosition(1, 0));
        assertEquals(5, en.getEnumeratePosition(0, 1));
        assertEquals(11, en.getEnumeratePosition(2, 2));
    }

    @Test
    public void getLiteralTPosition() {
        for(int i=0; i < 3; i++)
            assertEquals(res[i], en.getLiteralTPosition(pos[i][0], pos[i][1], LiteralEnumerator.PAST));
        for(int i=0; i < 3; i++)
            assertEquals(WORLD_LINEAL_DIM + res[i], en.getLiteralTPosition(pos[i][0], pos[i][1], LiteralEnumerator.FUTURE));
        for (int i=0; i < 3; i++) {
            Position p = new Position(pos[i][0], pos[i][1]);
            assertEquals(res[i], en.getLiteralTPosition(p, LiteralEnumerator.PAST));
        }
        for (int i=0; i < 3; i++) {
            Position p = new Position(pos[i][0], pos[i][1]);
            assertEquals(WORLD_LINEAL_DIM + res[i], en.getLiteralTPosition(p, LiteralEnumerator.FUTURE));
        }
    }

    @Test
    public void getLiteralDown() {
        for(int i = 0; i < 3; i++)
            assertEquals(WORLD_LINEAL_DIM * 7 + res[i], en.getLiteralDown(pos[i][0], pos[i][1]));
    }

    @Test
    public void getLiteralUp() {
        for(int i = 0; i < 3; i++)
            assertEquals(WORLD_LINEAL_DIM * 6 + res[i], en.getLiteralUp(pos[i][0], pos[i][1]));
    }

    @Test
    public void getLiteralSensor3() {
        for(int i = 0; i < 3; i++)
            assertEquals(WORLD_LINEAL_DIM * 4 + res[i], en.getLiteralSensor3(pos[i][0], pos[i][1]));
    }

    @Test
    public void getLiteralSensor2() {
        for(int i = 0; i < 3; i++)
            assertEquals(WORLD_LINEAL_DIM * 3 + res[i], en.getLiteralSensor2(pos[i][0], pos[i][1]));
    }

    @Test
    public void getLiteralSensor1() {
        for(int i = 0; i < 3; i++)
            assertEquals(WORLD_LINEAL_DIM * 2 + res[i], en.getLiteralSensor1(pos[i][0], pos[i][1]));
    }

    @Test
    void linealToPosition() {
        for (int i = 0; i < WORLD_DIM; i++) {
            for (int j = 0; j < WORLD_DIM; j++) {
                assertPosition(i, j, j * WORLD_DIM + i + 1);
                assertPosition(i, j, en.getLiteralTPosition(i, j, LiteralEnumerator.FUTURE));
                assertPosition(i, j, en.getLiteralSensor0(i, j));
                assertPosition(i, j, en.getLiteralSensor1(i, j));
                assertPosition(i, j, en.getLiteralSensor2(i, j));
                assertPosition(i, j, en.getLiteralSensor3(i, j));
                assertPosition(i, j, en.getLiteralUp(i, j));
                assertPosition(i, j, en.getLiteralDown(i, j));
            }
        }
    }

    @Test
    void negLinealToPosition() {
        for (int i = 0; i < WORLD_DIM; i++) {
            for (int j = 0; j < WORLD_DIM; j++) {
                int negi = -i;
                int negj = -j;
                assertPosition(i, j, j * WORLD_DIM + i + 1);
                assertPosition(i, j, en.getLiteralTPosition(negi, negj, LiteralEnumerator.FUTURE));
                assertPosition(i, j, en.getLiteralSensor0(negi, negj));
                assertPosition(i, j, en.getLiteralSensor1(negi, negj));
                assertPosition(i, j, en.getLiteralSensor2(negi, negj));
                assertPosition(i, j, en.getLiteralSensor3(negi, negj));
                assertPosition(i, j, en.getLiteralUp(negi, negj));
                assertPosition(i, j, en.getLiteralDown(negi, negj));
            }
        }
    }

    private void assertPosition(int i, int j, int lineal) {
        assertEquals(new Position(i, j), en.linealToPosition(lineal));
    }
}