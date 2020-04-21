package apryraz.tworld.clauses;

import apryraz.tworld.data.LiteralEnumerator;
import org.junit.jupiter.api.BeforeEach;

public class GammaSensor3Test extends GammaBuilderSensorTest {

    @BeforeEach
    void setUp() {
        WDIM = 4;
        en = new LiteralEnumerator(WDIM);
        gammaBuilder = new GammaBuilder(en);
        cb = new Sensor3Builder(en);
        limit = 2;
    }
}
