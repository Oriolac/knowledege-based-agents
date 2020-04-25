package apryraz.tworld.clauses;

import apryraz.tworld.data.NotCorrectPositionException;
import org.sat4j.core.VecInt;

public interface ClauseBuilder {
    VecInt addClause(int x, int y, int i, int j) throws NotCorrectPositionException;
}
