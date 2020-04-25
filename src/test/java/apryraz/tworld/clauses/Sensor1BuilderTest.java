package apryraz.tworld.clauses;

import apryraz.tworld.data.LiteralEnumerator;
import apryraz.tworld.data.NotCorrectPositionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sat4j.core.VecInt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Sensor1BuilderTest {

    Sensor1Builder sensorBuilder;
    LiteralEnumerator enumerator;

    @BeforeEach
    public void setUp() {
        enumerator = new LiteralEnumerator(4);
        sensorBuilder = new Sensor1Builder(enumerator);
    }


    @Test
    public void addClause() throws NotCorrectPositionException {
        VecInt vecint = sensorBuilder.addClause(1,1, 4, 4);
        assertEquals(-enumerator.getLiteralSensor1(1,1),vecint.get(0));
        assertEquals(-enumerator.getLiteralTPosition(4,4,LiteralEnumerator.FUTURE),vecint.get(1));
        vecint = sensorBuilder.addClause(2,3, 3, 2);
        assertEquals(-enumerator.getLiteralSensor1(2,3),vecint.get(0));
        assertEquals(-enumerator.getLiteralTPosition(3,2,LiteralEnumerator.FUTURE),vecint.get(1));
        assertThrows(UnsupportedOperationException.class, () -> sensorBuilder.addClause(2,3, 2, 3));

    }


}