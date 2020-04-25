package apryraz.tworld.clauses;

import apryraz.tworld.data.LiteralEnumerator;
import apryraz.tworld.data.NotCorrectPositionException;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;

import java.util.LinkedList;
import java.util.List;

/**
 * class to initialize Gamma at the beginning
 */
public class GammaBuilder {

    private final LiteralEnumerator en;
    private final int WorldDim;
    private ISolver solver;

    /**
     * constructor of the class GammaBuilder
     * @param enumerator Literal Enumerator where we find the functions needed
     * in order to initialize the variables of the class
     */
    public GammaBuilder(LiteralEnumerator enumerator) {
        this.en = enumerator;
        WorldDim = enumerator.getWorldDim();
        int totalNumVariables = enumerator.getNumVars();
        solver = SolverFactory.newDefault();
        solver.setTimeout(3600);
        solver.newVar(totalNumVariables);
    }

    /**
     * adds clauses from a list of VecInt to the solver
     * @param vecs list of clauses
     * @throws ContradictionException
     */
    public void addClauses(List<VecInt> vecs) throws ContradictionException {
        for (VecInt clause: vecs) {
            solver.addClause(clause);
        }
    }

    /**
     * call different function to build the solver
     * @return the solver builded
     * @throws ContradictionException
     */
    public ISolver buildSolver() throws ContradictionException {
        addMemorableClauses();
        addSensorClauses();
        addPirateClauses();
        return solver;
    }

    public ISolver getSolver() {
        return solver;
    }

    /**
     * call different functions in order to add the relevant clauses for every
     * answer of the metal detector
     * @throws ContradictionException
     */
    protected void addSensorClauses() throws ContradictionException, NotCorrectPositionException {
        addSensor0ClauseSquare();
        addSensor1Clause(new Sensor1Builder(en));
        addSensor2Clause();
        addSensor3Clause();
    }

    /**
     * for every position of the world, this function adds a vector with this position
     * with time in past and time in future
     * @return list of VecInt with all the clauses added previously
     * @throws ContradictionException
     */
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

    /**
     * calls the functions which add the pirate clauses
     * @throws ContradictionException
     */
    protected void addPirateClauses() throws ContradictionException, NotCorrectPositionException {
        addPirateUpClauses();
        addPirateDownClauses();
    }

    /**
     * calls the functions which add the clauses relevant to when
     * the value returned by the metal sensor is 3
     * @throws ContradictionException
     */
    protected void addSensor3Clause() throws ContradictionException {
        ClauseBuilder clauseBuilder = new Sensor3Builder(en);
        addSensorClauseDown(2, clauseBuilder);
        addSensorClauseUp(3, clauseBuilder);
        addSensorClauseLeft(2, clauseBuilder);
        addSensorClauseRight(3, clauseBuilder);
        addSensor3ClauseSquare();
    }

    /**
     * adds the relevant clauses when the answer of the pirate is that the treasure is down
     * @return list of VecInt with all the clauses added previously
     * @throws ContradictionException
     */
    protected List<VecInt> addPirateUpClauses() throws ContradictionException, NotCorrectPositionException {
        List<VecInt> vecs = new LinkedList<>();
        for (int x = 1; x <= this.WorldDim; x++) {
            for (int y = 1; y <= this.WorldDim; y++) {
                for (int i = 1; i <= this.WorldDim; i++) {
                    for (int j = 1; j <= y; j++) {
                        int[] vect = {-en.getLiteralUp(x, y), -en.getLiteralTPosition(i, j, 1)};
                        vecs.add(new VecInt(vect));
                    }
                }
            }
        }
        addClauses(vecs);
        return vecs;
    }

    /**
     * adds the relevant clauses when the answer of the pirate is that the treasure is up
     * @return list of VecInt with all the clauses added previously
     * @throws ContradictionException
     */
    protected List<VecInt> addPirateDownClauses() throws ContradictionException, NotCorrectPositionException {
        List<VecInt> vecs = new LinkedList<>();
        for (int x = 1; x <= this.WorldDim; x++) {
            for (int y = 1; y <= this.WorldDim; y++) {
                for (int i = 1; i <= this.WorldDim; i++) {
                    for (int j = y + 1; j <= this.WorldDim; j++) {
                        int[] vect = {-en.getLiteralDown(x, y), -en.getLiteralTPosition(i, j, 1)};
                        vecs.add(new VecInt(vect));
                    }
                }
            }
        }
        addClauses(vecs);
        return vecs;
    }

    /**
     * adds part of the relevant clauses for the positions inside the 5x5
     * box when the metal sensor returns 3
     * @return list of VecInt with all the clauses added previously
     * @throws ContradictionException
     */
    protected List<VecInt> addSensor3ClauseSquare() throws ContradictionException, NotCorrectPositionException {
        List<VecInt> vecs = new LinkedList<>();
        for (int x = 1; x <= this.WorldDim; x++) {
            for (int y = 1; y <= this.WorldDim; y++) {
                for (int i = x - 1; i <= x + 1 && i <= WorldDim; i++) {
                    for (int j = y - 1; j <= y + 1 && j <= WorldDim; j++) {
                        if ((i > 0 && j > 0)) {
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

    /**
     * add part of the relevant clauses when the metal sensor returns 0, which are the ones
     * in which the postions are not outside the 5x5 box
     * @return list of VecInt with all the clauses added previously
     * @throws ContradictionException
     */
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

    /**
     * add the relevant clauses when the metal sensor returns 1
     * @param clauseBuilder adds the clauses with the literals that
     * correspond to the sensor
     * @return list of VecInt with all the clauses added previously
     * @throws ContradictionException
     */
    protected List<VecInt> addSensor1Clause(ClauseBuilder clauseBuilder) throws ContradictionException, NotCorrectPositionException {
        List<VecInt> vecs = new LinkedList<>();
        for (int x = 1; x <= this.WorldDim; x++) {
            for (int y = 1; y <= this.WorldDim; y++) {
                for(int i = 1; i <= this.WorldDim; i++) {
                    for(int j = 1; j <= this.WorldDim; j++) {
                        if (!(x == i && j == y)) {
                            vecs.add(clauseBuilder.addClause(x, y, i, j));
                        }
                    }
                }
            }
        }
        addClauses(vecs);
        return vecs;
    }

    /**
     * calls all the necessary functions in order to add all the clauses when
     * the metal sensor returns 2
     * @throws ContradictionException
     */
    protected void addSensor2Clause() throws ContradictionException, NotCorrectPositionException {
        ClauseBuilder clauseBuilder = new Sensor2Builder(en);
        addSensorClauseDown(1, clauseBuilder);
        addSensorClauseUp(2, clauseBuilder);
        addSensorClauseLeft(1, clauseBuilder);
        addSensorClauseRight(2, clauseBuilder);
        addSensor2ClauseSame();
    }

    /**
     * adds the clause which correspond to the actual position of the agent,
     * for the case that the metal sensor returns 2
     * @return list of VecInt with all the clauses added previously
     * @throws ContradictionException
     */
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

    /**
     * adds the relevant clauses for the positions below the coordinate y where
     * the agent is
     * @param limit coordinate y of the actual position of the agent
     * @param clauseBuilder adds the clauses with the literals that correspond
     * to the sensor
     * @return list of VecInt with all the clauses added previously
     * @throws ContradictionException
     */
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

    /**
     * adds the relevant clauses for the positions above the coordinate y
     * where the agent is
     * @param start coordinate y of the actual position of the agent
     * @param clauseBuilder adds the clauses with the literals that correspond
     * to the sensor
     * @return list of VecInt with all the clauses added previously
     * @throws ContradictionException
     */
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

    /**
     * adds the relevant clauses for the positions to the left of the
     * coordinate y where the agent is
     * @param limit coordinate y of the actual position of the agent
     * @param clauseBuilder adds the clauses with the literals that
     * correspond to the sensor
     * @return list of VecInt with all the clauses added previously
     * @throws ContradictionException
     */
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

    /**
     * adds the relevant clauses for the positions to teh right of te
     * coordinate y where the agent is
     * @param start coordinate y of the actual position of the agent
     * @param clauseBuilder adds the clauses with the literals that
     * correspond to the sensor
     * @return list of VecInt with all the clauses added previously
     * @throws ContradictionException
     */
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
