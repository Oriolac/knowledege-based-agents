package apryraz.tworld.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LiteralEnumeratorTest {

    private static final int WORLD_DIM = 4;
    private static final int WORLD_LINEAL_DIM = WORLD_DIM * WORLD_DIM;
    int[][] pos = new int[][]{{1, 1}, {2, 1}, {1, 2}, {4, 2}};
    int[] res = new int[]{1, 2, 5, 8};
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
    public void getEnumeratePosition() throws NotCorrectPositionException {
        assertEquals(1, en.getEnumeratePosition(1, 1));
        assertEquals(2, en.getEnumeratePosition(2, 1));
        assertEquals(5, en.getEnumeratePosition(1, 2));
        assertEquals(11, en.getEnumeratePosition(3, 3));
    }

    @Test
    public void getLiteralTPosition() throws NotCorrectPositionException {
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
    public void getLiteralDown() throws NotCorrectPositionException {
        for(int i = 0; i < 3; i++)
            assertEquals(WORLD_LINEAL_DIM * 7 + res[i], en.getLiteralDown(pos[i][0], pos[i][1]));
    }

    @Test
    public void getLiteralUp() throws NotCorrectPositionException {
        for(int i = 0; i < 3; i++)
            assertEquals(WORLD_LINEAL_DIM * 6 + res[i], en.getLiteralUp(pos[i][0], pos[i][1]));
    }

    @Test
    public void getLiteralSensor3() throws NotCorrectPositionException {
        for(int i = 0; i < 3; i++)
            assertEquals(WORLD_LINEAL_DIM * 4 + res[i], en.getLiteralSensor3(pos[i][0], pos[i][1]));
    }

    @Test
    public void getLiteralSensor2() throws NotCorrectPositionException {
        for(int i = 0; i < 3; i++)
            assertEquals(WORLD_LINEAL_DIM * 3 + res[i], en.getLiteralSensor2(pos[i][0], pos[i][1]));
    }

    @Test
    public void getLiteralSensor1() throws NotCorrectPositionException {
        for(int i = 0; i < 3; i++)
            assertEquals(WORLD_LINEAL_DIM * 2 + res[i], en.getLiteralSensor1(pos[i][0], pos[i][1]));
    }


    @Test
    void linealToPositionSimple() {
        int x = 1;
        int y = 2;
        int lineal = (x) + (y - 1) * WORLD_DIM;
        assertEquals(5, lineal);
        assertEquals(new Position(x, y),en.linealToPosition(lineal));
    }


    @Test
    void linealToPosition() throws NotCorrectPositionException {
        for (int i = 1; i <= WORLD_DIM; i++) {
            for (int j = 1; j <= WORLD_DIM; j++) {
                assertPosition(i, j, en.getLiteralTPosition(i, j, LiteralEnumerator.PAST));
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
    void negLinealToPosition() throws NotCorrectPositionException {
        for (int i = 1; i <= WORLD_DIM; i++) {
            for (int j = 1; j <= WORLD_DIM; j++) {
                int negi = -i;
                int negj = -j;
                assertPosition(i, j, en.getLiteralTPosition(negi, negj, LiteralEnumerator.PAST));
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