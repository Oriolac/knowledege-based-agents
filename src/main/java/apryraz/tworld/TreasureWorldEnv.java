package apryraz.tworld;

import apryraz.tworld.data.AMessage;
import apryraz.tworld.data.LiteralEnumerator;
import apryraz.tworld.data.NotCorrectPositionException;
import apryraz.tworld.data.Position;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class TreasureWorldEnv {
    /**
     * X,Y position of Treasure and world dimension
     **/
    int TreasureX, TreasureY, WorldDim;
    List<Position> pirateLocations;
    private LiteralEnumerator en;


    /**
     * Class constructor
     *
     * @param dim         dimension of the world
     * @param tx          X position of Treasure
     * @param ty          Y position of Treasure
     * @param piratesFile File with list of pirates locations
     **/
    public TreasureWorldEnv(int dim, int tx, int ty, String piratesFile) throws IOException, NotCorrectPositionException {
        TreasureX = tx;
        TreasureY = ty;
        WorldDim = dim;
        en = new LiteralEnumerator(dim);
        loadPiratesLocations(piratesFile);
    }

    /**
     * Load the list of pirates locations
     *
     * @param piratesFile: name of the file that should contain a
     * set of pirate locations in a single line.
     **/
    public void loadPiratesLocations(String piratesFile) throws IOException, NotCorrectPositionException {
        BufferedReader reader = new BufferedReader(new FileReader(piratesFile));
        String line =reader.readLine();
        pirateLocations = new LinkedList<>();
        if (line != null) {
            getPirateLocations(line);
        }
    }

    protected void getPirateLocations(String line) throws NotCorrectPositionException {
        StringTokenizer strtok = new StringTokenizer(line, ", ");
        while (strtok.hasMoreTokens()) {
            int x = Integer.parseInt(strtok.nextToken());
            int y = Integer.parseInt(strtok.nextToken());
            pirateLocations.add(en.newPosition(x, y));
        }
    }


    /**
     * Process a message received by the TFinder agent,
     * by returning an appropriate answer
     * This version only process answers to moveto and detectsat messages
     *
     * @param msg message sent by the Agent
     * @return a msg with the answer to return to the agent
     **/
    public AMessage acceptMessage(AMessage msg) throws NotCorrectPositionException {
        AMessage ans = new AMessage("voidmsg", "", "", "");

        msg.showMessage();
        if (msg.getComp(0).equals("moveto")) {
            int nx = Integer.parseInt(msg.getComp(1));
            int ny = Integer.parseInt(msg.getComp(2));

            if (withinLimits(nx, ny)) {
                int pirate = isPirateInMyCell(nx, ny);

                ans = new AMessage("movedto", msg.getComp(1), msg.getComp(2),
                        Integer.valueOf(pirate).toString());
            } else
                ans = new AMessage("notmovedto", msg.getComp(1), msg.getComp(2), "");

        } else if (msg.getComp(0).equals("detectsat")) {
            int cx = Integer.parseInt(msg.getComp(1));
            int cy = Integer.parseInt(msg.getComp(2));
            if (cx == TreasureX && cy == TreasureY) {
                ans = new AMessage("detectedsat", msg.getComp(1), msg.getComp(2), "1");
            } else if (TreasureX >= cx - 1 && TreasureX <= cx + 1 && TreasureY >= cy - 1 && TreasureY <= cy + 1) {
                ans = new AMessage("detectedsat", msg.getComp(1), msg.getComp(2), "2");
            } else if (TreasureX >= cx - 2 && TreasureX <= cx + 2 && TreasureY >= cy - 2 && TreasureY <= cy + 2) {
                ans = new AMessage("detectedsat", msg.getComp(1), msg.getComp(2), "3");
            } else {
                ans = new AMessage("detectedsat", msg.getComp(1), msg.getComp(2), "0");
            }

        } else if (msg.getComp(0).equals("treasureup")) {
            int cy = Integer.parseInt(msg.getComp(2));
            if (TreasureY > cy) {
                ans = new AMessage("yes", msg.getComp(1), msg.getComp(2), "");
            } else {
                ans = new AMessage("no", msg.getComp(1), msg.getComp(2), "");
            }

        }
        // YOU MUST ANSWER ALSO TO THE OTHER MESSAGE TYPES:
        //   ( "detectsat", "x" , "y", "" )
        //   ( "treasureup", "x", "y", "" )
        return ans;

    }

    /**
     * Check if there is a pirate in position (x,y)
     *
     * @param x x coordinate of agent position
     * @param y y coordinate of agent position
     * @return 1  if (x,y) contains a pirate, 0 otherwise
     **/
    public int isPirateInMyCell(int x, int y) throws NotCorrectPositionException {
        for (Position position: pirateLocations) {
            if( position.equals(en.newPosition(x, y)))
                return 1;
        }
        return 0;
    }


    /**
     * Check if position x,y is within the limits of the
     * WorldDim x WorldDim   world
     *
     * @param x x coordinate of agent position
     * @param y y coordinate of agent position
     * @return true if (x,y) is within the limits of the world
     **/
    public boolean withinLimits(int x, int y) {

        return (x >= 1 && x <= WorldDim && y >= 1 && y <= WorldDim);
    }

}
