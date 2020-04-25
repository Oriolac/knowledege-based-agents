package apryraz.tworld.data;

import java.util.Objects;

public class Position {
    /**
     *
     **/
    private int x, y;

    protected Position(int a, int b) {
        x = a;
        y = b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isAdjacent(Position p2, int dist) {
        return x <= p2.getX() + dist && x >= p2.getX() - dist && y <= p2.getY() + dist && y >= p2.getY() - dist;
    }

    public boolean isDown(Position p2) {
        return this.y < p2.getY();
    }

    public boolean isUp(Position p2) {
        return this.y > p2.getY();
    }

    public boolean isInSameLine(Position p2) {
        return this.y == p2.getY();
    }

    public boolean isLeft(Position p2) {
        return this.x < p2.getX();
    }

    public boolean isRight(Position p2) {
        return this.x > p2.getX();
    }
}
