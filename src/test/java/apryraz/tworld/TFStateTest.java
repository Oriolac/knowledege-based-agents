package apryraz.tworld;

import apryraz.tworld.data.TFState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TFStateTest {

    private int wDim;
    private TFState state;

    @BeforeEach
    public void setUp() {
        wDim = 4;
        state = new TFState(wDim);
    }

    @Test
    public void testEquals() {
        state = new TFState(wDim);
        TFState state2 = new TFState(wDim);
        state2.set(2,3, "X");
        state.set(2,3, "X");
        assertEquals(state, state2);
    }


    @Test
    public void testNotEquals() {
        int dim = 5;
        TFState state2 = new TFState(dim);
        assertNotEquals(state, state2);
        state2 = new TFState(wDim);
        state2.set(2,3, "X");
        assertNotEquals(state, state2);
    }

    @Test
    public void printState() {
        state.set(2,1,"X");
        state.printState();
    }
}