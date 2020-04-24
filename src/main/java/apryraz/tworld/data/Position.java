package apryraz.tworld.data;

import java.util.Objects;

public class Position {
    /**
     * class to indicate a position
     **/
    private int x, y;

    /**
     * constructor of the class
     * @param a coordinate x of the position
     * @param b coordinate y of the position
     */
    public Position(int a, int b) {
        x = a;
        y = b;
    }

    //TODO
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    /**
     * add the position inside a hash
     * @return approval integer
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * converts the position into a string
     * @return the string
     */
    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    /**
     * obtain the coordinate x of the position
     * @return value f the coordinate x of the position
     */
    public int getX() {
        return x;
    }

    /**
     * obtain the coordinate y of the position
     * @return value of the coordinate y of the position
     */
    public int getY() {
        return y;
    }

    /**
     * check if the position is adjacent to another position considering a determinate distance
     * @param p2 position we want to know if is adjacent of the one we have
     * @param dist distance
     * @return True is the two positions are adjacent and False if not
     */
    public boolean isAdjacent(Position p2, int dist) {
        return x <= p2.getX() + dist && x >= p2.getX() - dist && y <= p2.getY() + dist && y >= p2.getY() - dist;
    }

    /**
     * check if the position is under another position
     * @param p2 position we want to compare to
     * @return True if our position is under the position passed as
     * a parameter and False if not
     */
    public boolean isDown(Position p2) {
        return this.y < p2.getY();
    }

    /**
     * check if the position is above another position
     * @param p2 position we want to compare to
     * @return True if our position is above the position passed as
     * a parameter and False if not
     */
    public boolean isUp(Position p2) {
        return this.y > p2.getY();
    }

    /**
     * check if the two positions have the same coordinate y
     * @param p2 position we want to compare to
     * @return True if the two positions have the same value for coordinate
     * y and False if not
     */
    public boolean isInSameLine(Position p2) {
        return this.y == p2.getY();
    }

    /**
     * check if the position is at the left of another position
     * @param p2 position we want to compare to
     * @return True if our position is at the left of the position passed as
     * a parameter and False if not
     */
    public boolean isLeft(Position p2) {
        return this.x < p2.getX();
    }

    /**
     * check if the position is at the right of another position
     * @param p2 position we want to compare to
     * @return True if our position is at the right of the position passed as
     * a parameter and False if not
     */
    public boolean isRight(Position p2) {
        return this.x > p2.getX();
    }
}
