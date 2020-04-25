package apryraz.tworld.clauses;

import apryraz.tworld.data.LiteralEnumerator;
import apryraz.tworld.data.NotCorrectPositionException;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;

import java.util.LinkedList;
import java.util.List;

public class GammaBuilder {

    private final LiteralEnumerator en;
    private final int WorldDim;
    private ISolver solver;

    public GammaBuilder(LiteralEnumerator enumerator) {
        this.en = enumerator;
        WorldDim = enumerator.getWorldDim();
        int totalNumVariables = enumerator.getNumVars();
        solver = SolverFactory.newDefault();
        solver.setTimeout(3600);
        solver.newVar(totalNumVariables);
    }

    public void addClauses(List<VecInt> vecs) throws ContradictionException {
        for (VecInt clause: vecs) {
            solver.addClause(clause);
        }
    }

    public ISolver buildSolver() throws ContradictionException, NotCorrectPositionException {
        addMemorableClauses();
        addSensorClauses();
        addPirateClauses();
        return solver;
    }

    public ISolver getSolver() {
        return solver;
    }

    protected void addSensorClauses() throws ContradictionException, NotCorrectPositionException {
        addSensor0ClauseSquare();
        addSensor1Clause(new Sensor1Builder(en));
        addSensor2Clause();
        addSensor3Clause();
    }

    protected List<VecInt> addMemorableClauses() throws ContradictionException, NotCorrectPositionException {
        List<VecInt> vecs = new LinkedList<>();
        for (int y = 1; y <= this.WorldDim; y++) {
            for (int x = 1; x  <= this.WorldDim; x++) {
                int[] vect = {-en.getLiteralTPosition(x, y, LiteralEnumerator.PAST),
                        -en.getLiteralTPosition(x, y, LiteralEnumerator.FUTURE)};
                vecs.add(new VecInt(vect));
            }
        }
        addClauses(vecs);
        return vecs;
    }

    protected void addPirateClauses() throws ContradictionException, NotCorrectPositionException {
        addPirateUpClauses();
        addPirateDownClauses();
    }

    protected void addSensor3Clause() throws ContradictionException, NotCorrectPositionException {
        ClauseBuilder clauseBuilder = new Sensor3Builder(en);
        addSensorClauseDown(2, clauseBuilder);
        addSensorClauseUp(3, clauseBuilder);
        addSensorClauseLeft(2, clauseBuilder);
        addSensorClauseRight(3, clauseBuilder);
        addSensor3ClauseSquare();
    }




    protected List<VecInt> addPirateUpClauses() throws ContradictionException, NotCorrectPositionException {
        List<VecInt> vecs = new LinkedList<>();
        for (int x = 0; x < this.WorldDim; x++) {
            for (int y = 0; y < this.WorldDim; y++) {
                for (int i = 0; i < this.WorldDim; i++) {
                    for (int j = 0; j <= y; j++) {
                        int[] vect = {-en.getLiteralUp(x, y), -en.getLiteralTPosition(i, j, 1)};
                        vecs.add(new VecInt(vect));
                    }
                }
            }
        }
        addClauses(vecs);
        return vecs;
    }

    protected List<VecInt> addPirateDownClauses() throws ContradictionException, NotCorrectPositionException {
        List<VecInt> vecs = new LinkedList<>();
        for (int x = 0; x < this.WorldDim; x++) {
            for (int y = 0; y < this.WorldDim; y++) {
                for (int i = 0; i < this.WorldDim; i++) {
                    for (int j = y + 1; j < this.WorldDim; j++) {
                        int[] vect = {-en.getLiteralDown(x, y), -en.getLiteralTPosition(i, j, 1)};
                        vecs.add(new VecInt(vect));
                    }
                }
            }
        }
        addClauses(vecs);
        return vecs;
    }




    protected List<VecInt> addSensor3ClauseSquare() throws ContradictionException, NotCorrectPositionException {
        List<VecInt> vecs = new LinkedList<>();
        for (int x = 0; x < this.WorldDim; x++) {
            for (int y = 0; y < this.WorldDim; y++) {
                for (int i = x - 1; i <= x + 1 && i < WorldDim; i++) {
                    for (int j = y - 1; j <= y + 1 && j < WorldDim; j++) {
                        if ((i >= 0 && j >= 0)) {
                            int[] vect = {-en.getLiteralSensor3(x, y), -en.getLiteralTPosition(i, j, LiteralEnumerator.FUTURE)};
                            vecs.add(new VecInt(vect));
                        }
                    }
                }
            }
        }
        addClauses(vecs);
        return vecs;
    }

    protected List<VecInt> addSensor0ClauseSquare() throws ContradictionException, NotCorrectPositionException {
        List<VecInt> vecs = new LinkedList<>();
        Sensor0Builder sensorBuilder = new Sensor0Builder(this.en);
        for (int x = 1; x <= this.WorldDim; x++) {
            for (int y = 1; y <= this.WorldDim; y++) {
                for (int i = x - 2; i <= x + 2 && i <= WorldDim; i++) {
                    for (int j = y - 2; j <= y + 2 && j <= WorldDim; j++) {
                        if (i > 0 && j > 0) {
                            vecs.add(sensorBuilder.addClause(x, y, i, j));
                        }
                    }
                }
            }
        }
        addClauses(vecs);
        return vecs;
    }


    protected List<VecInt> addSensor1Clause(ClauseBuilder clauseBuilder) throws ContradictionException, NotCorrectPositionException {
        List<VecInt> vecs = new LinkedList<>();
        for (int i = 0; i < this.WorldDim; i++) {
            for (int j = 0; j < this.WorldDim; j++) {
                vecs.add(clauseBuilder.addClause(i, j, i, j));
            }
        }
        addClauses(vecs);
        return vecs;
    }


    protected void addSensor2Clause() throws ContradictionException, NotCorrectPositionException {
        ClauseBuilder clauseBuilder = new Sensor2Builder(en);
        addSensorClauseDown(1, clauseBuilder);
        addSensorClauseUp(2, clauseBuilder);
        addSensorClauseLeft(1, clauseBuilder);
        addSensorClauseRight(2, clauseBuilder);
        addSensor2ClauseSame();
    }

    protected List<VecInt> addSensor2ClauseSame() throws ContradictionException, NotCorrectPositionException {
        List<VecInt> vecs = new LinkedList<>();
        for (int x = 1; x <= this.WorldDim; x++) {
            for (int y = 1; y <= this.WorldDim; y++) {
                int[] vect = {-en.getLiteralSensor2(x, y), -en.getLiteralTPosition(x, y, 1)};
                vecs.add(new VecInt(vect));
            }
        }
        addClauses(vecs);
        return vecs;
    }

    protected List<VecInt> addSensorClauseDown(int limit, ClauseBuilder clauseBuilder) throws ContradictionException, NotCorrectPositionException {
        List<VecInt> vecs = new LinkedList<>();
        for (int x = 1; x <= this.WorldDim; x++) {
            for (int y = 1; y <= this.WorldDim; y++) {
                for (int i = 1; i <= this.WorldDim; i++) {
                    for (int j = 1; j < y - limit; j++) {
                        vecs.add(clauseBuilder.addClause(x, y, i, j));
                    }
                }
            }
        }
        addClauses(vecs);
        return vecs;
    }

    protected List<VecInt> addSensorClauseUp(int start, ClauseBuilder clauseBuilder) throws ContradictionException, NotCorrectPositionException {
        List<VecInt> vecs = new LinkedList<>();
        for (int x = 1; x <= this.WorldDim; x++) {
            for (int y = 1; y <= this.WorldDim; y++) {
                for (int i = 1; i <= this.WorldDim; i++) {
                    for (int j = y + start; j <= this.WorldDim; j++) {
                        vecs.add(clauseBuilder.addClause(x, y, i, j));
                    }
                }
            }
        }
        addClauses(vecs);
        return vecs;
    }

    protected List<VecInt> addSensorClauseLeft(int limit, ClauseBuilder clauseBuilder) throws ContradictionException, NotCorrectPositionException {
        List<VecInt> vecs = new LinkedList<>();
        for (int x = 1; x <= this.WorldDim; x++) {
            for (int y = 1; y <= this.WorldDim; y++) {
                for (int i = 1; i < x - limit; i++) {
                    for (int j = 1; j <= this.WorldDim; j++) {
                        vecs.add(clauseBuilder.addClause(x, y, i, j));
                    }
                }
            }
        }
        addClauses(vecs);
        return vecs;
    }

    protected List<VecInt> addSensorClauseRight(int start, ClauseBuilder clauseBuilder) throws ContradictionException, NotCorrectPositionException {
        List<VecInt> vecs = new LinkedList<>();
        for (int x = 1; x <= this.WorldDim; x++) {
            for (int y = 1; y <= this.WorldDim; y++) {
                for (int i = x + start; i <= this.WorldDim; i++) {
                    for (int j = 1; j <= this.WorldDim; j++) {
                        vecs.add(clauseBuilder.addClause(x, y, i, j));
                    }
                }
            }
        }
        addClauses(vecs);
        return vecs;
    }
}
