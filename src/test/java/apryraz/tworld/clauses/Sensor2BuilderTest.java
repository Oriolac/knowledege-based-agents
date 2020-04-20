package apryraz.tworld.clauses;

import apryraz.tworld.data.LiteralEnumerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sat4j.core.VecInt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Sensor2BuilderTest {

    Sensor2Builder sensorBuilder;
    LiteralEnumerator enumerator;

    @BeforeEach
    void setUp() {
        enumerator = new LiteralEnumerator(4);
        sensorBuilder = new Sensor2Builder(enumerator);
    }

    @Test
    void addClause() {
        VecInt vecint = sensorBuilder.addClause(1, 1, 1, 1);
        assertEquals(-enumerator.getLiteralSensor2(1, 1), vecint.get(0));
        assertEquals(-enumerator.getLiteralTPosition(1, 1, LiteralEnumerator.FUTURE), vecint.get(1));
        vecint = sensorBuilder.addClause(2, 3, 2, 3);
        assertEquals(-enumerator.getLiteralSensor2(2, 3), vecint.get(0));
        assertEquals(-enumerator.getLiteralTPosition(2, 3, LiteralEnumerator.FUTURE), vecint.get(1));
    }


}