package apryraz.tworld;

import apryraz.tworld.clauses.ClauseBuilder;
import apryraz.tworld.clauses.Sensor1Builder;
import apryraz.tworld.clauses.Sensor2Builder;
import apryraz.tworld.clauses.Sensor3Builder;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.exit;


/**
 * This agent performs a sequence of movements, and after each
 * movement it "senses" from the evironment the resulting position
 * and then the outcome from the smell sensor, to try to locate
 * the position of Treasure
 **/
public class TreasureFinder {


    /**
     * The list of steps to perform
     **/
    ArrayList<Position> listOfSteps;
    /**
     * index to the next movement to perform, and total number of movements
     **/
    int idNextStep, numMovements;
    /**
     * Array of clauses that represent conclusiones obtained in the last
     * call to the inference function, but rewritten using the "past" variables
     **/
    ArrayList<VecInt> futureToPast = null;
    /**
     * the current state of knowledge of the agent (what he knows about
     * every position of the world)
     **/
    TFState tfstate;
    /**
     * The object that represents the interface to the Treasure World
     **/
    TreasureWorldEnv EnvAgent;
    /**
     * SAT solver object that stores the logical boolean formula with the rules
     * and current knowledge about not possible locations for Treasure
     **/
    ISolver solver;
    /**
     * Variable to record if there is a pirate
     * at that current position
     **/
    int pirateFound;

    /**
     * Agent position in the world.
     */
    Position currentPosition;

    /**
     * Dimension of the world and total size of the world (Dim^2)
     **/
    int WorldDim, WorldLinealDim;

    /**
     * This set of variables CAN be use to mark the beginning of different sets
     * of variables in your propositional formula (but you may have more sets of
     * variables in your solution).
     **/
    int TreasurePastOffset;
    int TreasureFutureOffset;
    int DetectorOffset;
    int actualLiteral;
    private LiteralEnumerator enumerator;


    /**
     * The class constructor must create the initial Boolean formula with the
     * rules of the Treasure World, initialize the variables for indicating
     * that we do not have yet any movements to perform, make the initial state.
     *
     * @param WDim the dimension of the Treasure World
     **/
    public TreasureFinder(int WDim) {

        WorldDim = WDim;
        WorldLinealDim = WorldDim * WorldDim;
        enumerator = new LiteralEnumerator(WDim);

        try {
            solver = buildGamma();
        } catch (IOException | ContradictionException ex) {
            Logger.getLogger(TreasureFinder.class.getName()).log(Level.SEVERE, null, ex);
        }
        numMovements = 0;
        idNextStep = 0;
        System.out.println("STARTING TREASURE FINDER AGENT...");


        tfstate = new TFState(WorldDim);  // Initialize state (matrix) of knowledge with '?'
        tfstate.printState();
    }

    /**
     * Store a reference to the Environment Object that will be used by the
     * agent to interact with the Treasure World, by sending messages and getting
     * answers to them. This function must be called before trying to perform any
     * steps with the agent.
     *
     * @param environment the Environment object
     **/
    public void setEnvironment(TreasureWorldEnv environment) {
        EnvAgent = environment;
    }


    /**
     * Load a sequence of steps to be performed by the agent. This sequence will
     * be stored in the listOfSteps ArrayList of the agent.  Steps are represented
     * as objects of the class Position.
     *
     * @param numSteps  number of steps to read from the file
     * @param stepsFile the name of the text file with the line that contains
     *                  the sequence of steps: x1,y1 x2,y2 ...  xn,yn
     **/
    public void loadListOfSteps(int numSteps, String stepsFile) {
        String[] stepsList;
        String steps = ""; // Prepare a list of movements to try with the FINDER Agent
        try {
            BufferedReader br = new BufferedReader(new FileReader(stepsFile));
            System.out.println("STEPS FILE OPENED ...");
            steps = br.readLine();
            br.close();
        } catch (FileNotFoundException ex) {
            System.out.println("MSG.   => Steps file not found");
            exit(1);
        } catch (IOException ex) {
            Logger.getLogger(TreasureFinder.class.getName()).log(Level.SEVERE, null, ex);
            exit(2);
        }
        stepsList = steps.split(" ");
        listOfSteps = new ArrayList<Position>(numSteps);
        for (int i = 0; i < numSteps; i++) {
            String[] coords = stepsList[i].split(",");
            listOfSteps.add(new Position(Integer.parseInt(coords[0]), Integer.parseInt(coords[1])));
        }
        numMovements = listOfSteps.size(); // Initialization of numMovements
        idNextStep = 0;
    }

    /**
     * Returns the current state of the agent.
     *
     * @return the current state of the agent, as an object of class TFState
     **/
    public TFState getState() {
        return tfstate;
    }

    /**
     * Execute the next step in the sequence of steps of the agent, and then
     * use the agent sensor to get information from the environment. In the
     * original Treasure World, this would be to use the Smelll Sensor to get
     * a binary answer, and then to update the current state according to the
     * result of the logical inferences performed by the agent with its formula.
     **/
    public void runNextStep() throws
            IOException, ContradictionException, TimeoutException {
        pirateFound = 0;
        // Add the conclusions obtained in the previous step
        // but as clauses that use the "past" variables
        addLastFutureClausesToPastClauses();

        // Ask to move, and check whether it was successful
        // Also, record if a pirate was found at that position
        processMoveAnswer(moveToNext());


        // Next, use Detector sensor to discover new information
        processDetectorSensorAnswer(DetectsAt());
        // If a pirate was found at new agent position, ask question to
        // pirate and process Answer to discover new information
        if (pirateFound == 1) {
            processPirateAnswer(IsTreasureUpOrDown());
        }

        // Perform logical consequence questions for all the positions
        // of the Treasure World
        performInferenceQuestions();
        tfstate.printState();      // Print the resulting knowledge matrix
    }


    /**
     * Ask the agent to move to the next position, by sending an appropriate
     * message to the environment object. The answer returned by the environment
     * will be returned to the caller of the function.
     *
     * @return the answer message from the environment, that will tell whether the
     * movement was successful or not.
     **/
    public AMessage moveToNext() {
        Position nextPosition;

        if (idNextStep < numMovements) {
            nextPosition = listOfSteps.get(idNextStep);
            idNextStep = idNextStep + 1;
            return moveTo(nextPosition.x, nextPosition.y);
        } else {
            System.out.println("NO MORE steps to perform at agent!");
            return (new AMessage("NOMESSAGE", "", "", ""));
        }
    }

    /**
     * Use agent "actuators" to move to (x,y)
     * We simulate this why telling to the World Agent (environment)
     * that we want to move, but we need the answer from it
     * to be sure that the movement was made with success
     *
     * @param x horizontal coordinate of the movement to perform
     * @param y vertical coordinate of the movement to perform
     * @return returns the answer obtained from the environment object to the
     * moveto message sent
     **/
    public AMessage moveTo(int x, int y) {
        // Tell the EnvironmentAgentID that we want  to move
        AMessage msg, ans;

        msg = new AMessage("moveto", String.valueOf(x), String.valueOf(x), "");
        ans = EnvAgent.acceptMessage(msg);
        System.out.println("FINDER => moving to : (" + x + "," + y + ")");

        return ans;
    }

    /**
     * Process the answer obtained from the environment when we asked
     * to perform a movement
     *
     * @param moveans the answer given by the environment to the last move message
     **/
    public void processMoveAnswer(AMessage moveans) {
        if (moveans.getComp(0).equals("movedto")) {
            int x = Integer.parseInt(moveans.getComp(1));
            int y = Integer.parseInt(moveans.getComp(2));
            currentPosition = new Position(x, y);
            pirateFound = Integer.parseInt(moveans.getComp(3));
            System.out.println("FINDER => moved to : (" + currentPosition.getX() + "," + currentPosition.getY() + ")" + " Pirate found : " + pirateFound);
        }
    }

    /**
     * Send to the environment object the question:
     * "Does the detector sense something around(agentX,agentY) ?"
     *
     * @return return the answer given by the environment
     **/
    public AMessage DetectsAt() {
        AMessage msg, ans;

        msg = new AMessage("detectsat", String.valueOf(currentPosition.getX()),
                String.valueOf(currentPosition.getY()), "");
        ans = EnvAgent.acceptMessage(msg);
        System.out.println("FINDER => detecting at : (" + currentPosition.getX() + "," + currentPosition.getY() + ")");
        return ans;
    }


    /**
     * Process the answer obtained for the query "Detects at (x,y)?"
     * by adding the appropriate evidence clause to the formula
     *
     * @param ans message obtained to the query "Detects at (x,y)?".
     *            It will a message with three fields: [0,1,2,3] x y
     **/
    public void processDetectorSensorAnswer(AMessage ans) throws
            IOException, ContradictionException, TimeoutException {

        int x = Integer.parseInt(ans.getComp(1));
        int y = Integer.parseInt(ans.getComp(2));
        String detects = ans.getComp(0);

        // Call your function/functions to add the evidence clauses
        // to Gamma to then be able to infer new NOT possible positions


        // CALL your functions HERE
    }


    /**
     * Send to the pirate (using the environment object) the question:
     * "Is the treasure up or down of (agentX,agentY)  ?"
     *
     * @return return the answer given by the pirate
     **/
    public AMessage IsTreasureUpOrDown() {
        AMessage msg, ans;

        msg = new AMessage("treasureup", String.valueOf(currentPosition.getX()),
                String.valueOf(currentPosition.getY()), "");
        ans = EnvAgent.acceptMessage(msg);
        System.out.println("FINDER => checking treasure up of : (" + currentPosition.getX() + "," + currentPosition.getY() + ")");
        return ans;
    }

    public void processPirateAnswer(AMessage ans) throws
            IOException, ContradictionException, TimeoutException {

        int y = Integer.parseInt(ans.getComp(2));
        String isup = ans.getComp(0);
        // isup should be either "yes" (is up of agent position), or "no"

        // Call your function/functions to add the evidence clauses
        // to Gamma to then be able to infer new NOT possible positions


        // CALL your functions HERE to update the solver object with more
        // clauses
    }


    /**
     * This function should add all the clauses stored in the list
     * futureToPast to the formula stored in solver.
     * Use the function addClause( VecInt ) to add each clause to the solver
     **/
    public void addLastFutureClausesToPastClauses() throws IOException,
            ContradictionException, TimeoutException {
        for (VecInt conc: futureToPast) {
            Position notPossiblePosition = enumerator.linealToPosition(Math.abs(conc.get(0)));
            int[] ints = new int[]{-enumerator.getLiteralTPosition(notPossiblePosition, LiteralEnumerator.PAST)};
            VecInt vec = new VecInt(ints);
            solver.addClause(vec);
        }
    }

    /**
     * This function should check, using the future variables related
     * to possible positions of Treasure, whether it is a logical consequence
     * that Treasure is NOT at certain positions. This should be checked for all the
     * positions of the Treasure World.
     * The logical consequences obtained, should be then stored in the futureToPast list
     * but using the variables corresponding to the "past" variables of the same positions
     * <p>
     * An efficient version of this function should try to not add to the futureToPast
     * conclusions that were already added in previous steps, although this will not produce
     * any bad functioning in the reasoning process with the formula.
     **/
    public void performInferenceQuestions() throws IOException,
            ContradictionException, TimeoutException {
        futureToPast = new ArrayList<>();
        int linealIndex = enumerator.getLiteralTPosition(currentPosition,  LiteralEnumerator.FUTURE);
        int linealIndexPast = enumerator.getLiteralTPosition(currentPosition, LiteralEnumerator.PAST);

        VecInt variablePositive = new VecInt();
        variablePositive.insertFirst(linealIndex);

        if (!(solver.isSatisfiable(variablePositive))) {
            VecInt concPast = new VecInt();
            concPast.insertFirst(-(linealIndexPast));
            futureToPast.add(concPast);
            tfstate.set(currentPosition, "X");
        }

    }

    /**
     * This function builds the initial logical formula of the agent and stores it
     * into the solver object.
     *
     * @return returns the solver object where the formula has been stored
     **/
    public ISolver buildGamma() throws UnsupportedEncodingException,
            FileNotFoundException, IOException, ContradictionException {
        int totalNumVariables = enumerator.getNumVars();

        solver = SolverFactory.newDefault();
        solver.setTimeout(3600);
        solver.newVar(totalNumVariables);
        // This variable is used to generate, in a particular sequential order,
        // the variable indentifiers of all the variables
        actualLiteral = 1;
        addMemorableClauses();
        addSensorClauses();
        addPirateClauses();
        // call here functions to add the differen sets of clauses
        // of Gamma to the solver object

        return solver;
    }

    private void addMemorableClauses() throws ContradictionException {
        for(int x = 0; x < this.WorldDim; x++) {
            for (int y = 0; y < this.WorldDim; y++) {
                int[] vect = {-enumerator.getLiteralTPosition(x, y, LiteralEnumerator.PAST),
                        -enumerator.getLiteralTPosition(x,y, LiteralEnumerator.FUTURE)};
                solver.addClause(new VecInt(vect));
            }
        }
    }

    private void addPirateClauses() throws ContradictionException {
        addPirateUpClauses();
        addPirateDownClauses();
    }

    private void addPirateUpClauses() throws ContradictionException {
        for(int x = 0; x < this.WorldDim; x++){
            for(int y = 0; y < this.WorldDim; y++){
                for(int i = 0; i < this.WorldDim; i++) {
                    for(int j = 0; j <= y; j++) {
                        int[] vect = {-enumerator.getLiteralUp(x,y), -enumerator.getLiteralTPosition(i, j, 1)};
                        solver.addClause(new VecInt(vect));
                    }
                }
            }
        }
    }

    private void addPirateDownClauses() throws ContradictionException {
        for(int x = 0; x < this.WorldDim; x++){
            for(int y = 0; y < this.WorldDim; y++){
                for(int i = 0; i < this.WorldDim; i++) {
                    for(int j = y + 1; j < this.WorldDim; j++) {
                        int[] vect = {-enumerator.getLiteralDown(x,y), -enumerator.getLiteralTPosition(i, j, 1)};
                        solver.addClause(new VecInt(vect));
                    }
                }
            }
        }
    }


    private void addSensorClauses() throws ContradictionException {
        addSensor1Clause(solver, new Sensor1Builder(enumerator));
        addSensor2Clause(solver);
        addSensor3Clause(solver);
    }


    private void addSensor3Clause(ISolver solver) throws ContradictionException {
        ClauseBuilder clauseBuilder = new Sensor3Builder(enumerator);
        addSensorClauseDown(solver, 2, clauseBuilder);
        addSensorClauseUp(solver, 3, clauseBuilder);
        addSensorClauseLeft(solver, 2, clauseBuilder);
        addSensorClauseRight(solver, 3, clauseBuilder);
        addSensor3ClauseSquare(solver);
    }

    private void addSensor3ClauseSquare(ISolver solver) throws ContradictionException {
        for(int x = 0; x < this.WorldDim; x++) {
            for (int y= 0; y < this.WorldDim; y++) {
                for(int i = x -1 ; i <= x +1; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {
                        if(!( i < 0 && j < 0)){
                            int[] vect = {-enumerator.getLiteralSensor3(x, y), -enumerator.getLiteralTPosition(i, j, 1)};
                            solver.addClause(new VecInt(vect));
                        }

                    }
                }
            }
        }
    }



    private void addSensor1Clause(ISolver solver, ClauseBuilder clauseBuilder) throws ContradictionException {
        for (int i = 0; i < this.WorldDim; i++) {
            for (int j = 0; j < this.WorldDim; j++) {
                solver.addClause(clauseBuilder.addClause(i,j,i,j));
            }
        }
    }


    private void addSensor2Clause(ISolver solver) throws ContradictionException {
        ClauseBuilder clauseBuilder = new Sensor2Builder(enumerator);
        addSensorClauseDown(solver, 1, clauseBuilder);
        addSensorClauseUp(solver, 2, clauseBuilder);
        addSensorClauseLeft(solver, 1, clauseBuilder);
        addSensorClauseRight(solver, 2, clauseBuilder);
        addSensor2ClauseSame(solver);
    }

    private void addSensor2ClauseSame(ISolver solver) throws ContradictionException {
        for (int x = 0; x < this.WorldDim; x++){
            for (int y = 0; y < this.WorldDim; y++) {
                int[] vect = {-enumerator.getLiteralSensor2(x, y), -enumerator.getLiteralTPosition(x,y, 1)};
                solver.addClause(new VecInt(vect));
            }
        }
    }

    private void addSensorClauseDown(ISolver solver, int limit, ClauseBuilder clauseBuilder) throws ContradictionException {
        for (int x = 0; x < this.WorldDim; x++) {
            for (int y = 0; y < this.WorldDim; y++) {
                for (int i = 0; i < this.WorldDim; i++) {
                    for (int j = 0; j < y - limit; j++) {
                        solver.addClause(clauseBuilder.addClause(x, y, i, j));
                    }
                }
            }
        }
    }

    private void addSensorClauseUp(ISolver solver, int start, ClauseBuilder clauseBuilder) throws ContradictionException {
        for (int x = 0; x < this.WorldDim; x++) {
            for (int y = 0; y < this.WorldDim; y++) {
                for (int i = 0; i < this.WorldDim; i++) {
                    for (int j = y + start; j < this.WorldDim; j++) {
                        solver.addClause(clauseBuilder.addClause(x, y, i, j));
                    }
                }
            }
        }
    }

    private void addSensorClauseLeft(ISolver solver, int limit, ClauseBuilder clauseBuilder) throws ContradictionException {
        for (int x = 0; x < this.WorldDim; x++) {
            for (int y = 0; y < this.WorldDim; y++) {
                for (int i = 0; i < x - limit; i++) {
                    for (int j = 0; j < this.WorldDim; j++) {
                        solver.addClause(clauseBuilder.addClause(x, y, i, j));
                    }
                }
            }
        }
    }

    private void addSensorClauseRight(ISolver solver, int start, ClauseBuilder clauseBuilder) throws ContradictionException {
        for (int x = 0; x < this.WorldDim; x++) {
            for (int y = 0; y < this.WorldDim; y++) {
                for (int i = x + start; i < this.WorldDim; i++) {
                    for (int j = 0; j < this.WorldDim; j++) {
                        solver.addClause(clauseBuilder.addClause(x, y, i, j));
                    }
                }
            }
        }
    }

    public ArrayList<Position> getListOfSteps() {
        return listOfSteps;
    }
}
