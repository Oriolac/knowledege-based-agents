package apryraz.tworld;


import apryraz.tworld.data.NotCorrectPositionException;
import apryraz.tworld.data.Position;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.TimeoutException;

import java.io.IOException;
import java.util.List;


/**
 * The class for the main program of the Barcenas World
 **/
public class TreasureWorld {


    /**
     * This function should execute the sequence of steps stored in the file fileSteps,
     * but only up to numSteps steps. Each step must be executed with function
     * runNextStep() of the BarcenasFinder agent.
     *
     * @param wDim        the dimension of world
     * @param tX          x coordinate of Barcenas position
     * @param tY          y coordinate of Barcenas position
     * @param numSteps    num of steps to perform
     * @param fileSteps   file name with sequence of steps to perform
     * @param filePirates file name with sequence of steps to perform
     **/
    public static void runStepsSequence(int wDim, int tX, int tY,
                                        int numSteps, String fileSteps, String filePirates) throws
            IOException, ContradictionException, TimeoutException, NotCorrectPositionException {
        // Make instances of TreasureFinder agent and environment object classes
        TreasureFinder TAgent;
        TreasureWorldEnv EnvAgent;
        TAgent = new TreasureFinder(wDim);

        // Set environment object, and load list of pirate positions
        EnvAgent = new TreasureWorldEnv(wDim, tX, tY, filePirates);
        TAgent.setEnvironment(EnvAgent);
        // load list of steps into the Finder Agent
        TAgent.loadListOfSteps(numSteps, fileSteps);
        // Execute sequence of steps with the Agent
        List<Position> positions = TAgent.getListOfSteps();
        for(Position position: positions) {
            TAgent.runNextStep();
        }

    }

    /**
     * This function should load five arguments from the command line:
     * arg[0] = dimension of the word
     * arg[1] = x coordinate of treasure position
     * arg[2] = y coordinate of treasure position
     * arg[3] = num of steps to perform
     * arg[4] = file name with sequence of steps to perform
     * arg[5] = file name with list of pirate positions
     **/
    public static void main(String[] args) throws ParseFormatException,
            IOException, ContradictionException, TimeoutException, NotCorrectPositionException {
        int wDim = Integer.parseInt(args[0]);
        int tx = Integer.parseInt(args[1]);
        int ty = Integer.parseInt(args[2]);
        int numSteps = Integer.parseInt(args[3]);
        runStepsSequence(wDim, tx, ty, numSteps, args[4], args[5]);
    }

}
