package apryraz.tworld.clauses;

import apryraz.tworld.LiteralEnumerator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Sensor1BuilderTest {

    Sensor1Builder sensorBuilder;
    LiteralEnumerator enumerator;

    @Before
    public void setUp() {
        enumerator = new LiteralEnumerator(4);
        sensorBuilder = new Sensor1Builder(enumerator);
    }


    @Test
    public void addClause() {
        System.out.println(sensorBuilder.addClause(0,0, 0, 0).toString());
    }
}