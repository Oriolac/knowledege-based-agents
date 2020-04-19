package apryraz.tworld.clauseconstructor;

import org.sat4j.core.VecInt;

public interface ClauseConstructor {
    VecInt addClause(int x, int y, int i, int j);
}
