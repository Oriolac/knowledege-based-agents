package apryraz.tworld.clauses;

import apryraz.tworld.data.LiteralEnumerator;
import org.junit.jupiter.api.BeforeEach;
import org.sat4j.core.VecInt;

import java.util.List;

public class GammaSensor2Test extends GammaBuilderSensorTest {

    @BeforeEach
    void setUp() {
        WDIM = 4;
        en = new LiteralEnumerator(WDIM);
        gammaBuilder = new GammaBuilder(en);
        cb = new Sensor2Builder(en);
        limit = 1;
        start = 2;
    }
}
