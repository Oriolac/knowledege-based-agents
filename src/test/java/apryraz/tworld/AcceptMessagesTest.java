package apryraz.tworld;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class AcceptMessageTest {

    private static final int X = 3;
    private static final int Y = 3;
    private static final int TreasureX1 = 3;
    private static final int TreasureY1 = 3;
    private static final int TreasureX2 = 4;
    private static final int TreasureY2 = 3;
    private static final int TreasureX3 = 5;
    private static final int TreasureY3 = 5;
    private static final int WorldDim = 5;

    TreasureWorldEnv tWorldEnv1;
    TreasureWorldEnv tWorldEnv2;
    TreasureWorldEnv tWorldEnv3;
    LiteralEnumerator enumerator;

    @Before
    public void setUp() throws IOException {
        enumerator = new LiteralEnumerator(WorldDim);
        //tFinder = new TreasureFinder(WorldDim);
        tWorldEnv1 = new TreasureWorldEnv(WorldDim, TreasureX1, TreasureY1, "tests/pirates1.txt");
        tWorldEnv2 = new TreasureWorldEnv(WorldDim, TreasureX2, TreasureY2, "tests/pirates1.txt");
        tWorldEnv3 = new TreasureWorldEnv(WorldDim, TreasureX3, TreasureY3, "tests/pirates1.txt");
    }

    @Test
    public void acceptDetectSat1Message() {
        AMessage msg = new AMessage("detectsat", String.valueOf(X), String.valueOf(Y), "");
        AMessage ansExpected = new AMessage("detectedsat", "3", "3", "3");
        AMessage ans = tWorldEnv1.acceptMessage(msg);
        assertEquals(ansExpected.getComp(0), ans.getComp(0));
        assertEquals(ansExpected.getComp(1), ans.getComp(1));
        assertEquals(ansExpected.getComp(2), ans.getComp(2));
        assertEquals(ansExpected.getComp(1), ans.getComp(1));
    }

    @Test
    public void acceptDetectSat2Message() {
        AMessage msg = new AMessage("detectsat", String.valueOf(X), String.valueOf(Y), "");
        AMessage ansExpected = new AMessage("detectedsat", "3", "3", "3");
        AMessage ans = tWorldEnv2.acceptMessage(msg);
        assertEquals(ansExpected.getComp(0), ans.getComp(0));
        assertEquals(ansExpected.getComp(1), ans.getComp(1));
        assertEquals(ansExpected.getComp(2), ans.getComp(2));
        assertEquals(ansExpected.getComp(2), ans.getComp(2));
    }

    @Test
    public void acceptDetectSat3Message() {
        AMessage msg = new AMessage("detectsat", String.valueOf(X), String.valueOf(Y), "");
        AMessage ansExpected = new AMessage("detectedsat", "3", "3", "3");
        AMessage ans = tWorldEnv3.acceptMessage(msg);
        assertEquals(ansExpected.getComp(0), ans.getComp(0));
        assertEquals(ansExpected.getComp(1), ans.getComp(1));
        assertEquals(ansExpected.getComp(2), ans.getComp(2));
        assertEquals(ansExpected.getComp(3), ans.getComp(3));
    }
    //TODO: s'ha d'acabar la part del 0
    @Test
    public void acceptDetectSat0Message() {
        AMessage msg = new AMessage("detectsat", String.valueOf(X), String.valueOf(Y), "");
        AMessage ansExpected = new AMessage("detectedsat", "3", "3", "3");
        AMessage ans = tWorldEnv2.acceptMessage(msg);
        assertEquals(ansExpected.getComp(0), ans.getComp(0));
        assertEquals(ansExpected.getComp(1), ans.getComp(1));
        assertEquals(ansExpected.getComp(2), ans.getComp(2));
        assertEquals(ansExpected.getComp(0), ans.getComp(0));
    }

    @Test
    public void acceptTreasureUp() {
        AMessage msg = new AMessage("treasureup", String.valueOf(X), String.valueOf(Y), "");
        AMessage ansExpected = new AMessage("yes", "3", "3", "");
        AMessage ans = tWorldEnv3.acceptMessage(msg);
        assertEquals(ansExpected.getComp(0), ans.getComp(0));
        assertEquals(ansExpected.getComp(1), ans.getComp(1));
        assertEquals(ansExpected.getComp(2), ans.getComp(2));
        assertEquals(ansExpected.getComp(0), ans.getComp(0));
    }

    @Test
    public void acceptTreasureDown() {
        AMessage msg = new AMessage("treasureup", String.valueOf(X), String.valueOf(Y), "");
        AMessage ansExpected = new AMessage("no", "3", "3", "");
        AMessage ans = tWorldEnv1.acceptMessage(msg);
        assertEquals(ansExpected.getComp(0), ans.getComp(0));
        assertEquals(ansExpected.getComp(1), ans.getComp(1));
        assertEquals(ansExpected.getComp(2), ans.getComp(2));
        assertEquals(ansExpected.getComp(0), ans.getComp(0));
    }

}
