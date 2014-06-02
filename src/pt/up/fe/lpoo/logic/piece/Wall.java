/**
 * Labyrinth
 *
 * Created by Eduardo Almeida and Joao Almeida.
 */

package pt.up.fe.lpoo.logic.piece;

import pt.up.fe.lpoo.logic.Coordinate;

public class Wall extends Piece {
    /**
     * Constructs a new Wall.
     */

    public Wall() {
        _position = new Coordinate(-1, -1);
    }

    /**
     * Constructs a new Wall.
     *
     * @param crd Wall's coordinate.
     */

    public Wall(Coordinate crd) {
        _position = crd;
    }

    /**
     * Constructs a new Wall.
     *
     * @param x Wall's x coordinate.
     * @param y Wall's y coordinate.
     */

    public Wall(int x, int y) {
        _position = new Coordinate(x, y);
    }
}
