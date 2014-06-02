/**
 * Labyrinth
 *
 * Created by Eduardo Almeida and Joao Almeida.
 */

package pt.up.fe.lpoo.logic.piece.itemizable;

import pt.up.fe.lpoo.logic.Coordinate;
import pt.up.fe.lpoo.logic.piece.Piece;

public class Blank extends ItemizablePiece {
    private Boolean _isExit = false;

    /**
     * Checks if the piece is an exit.
     *
     * @return true if yes, false if not.
     */

    public Boolean getIsExit() {
        return _isExit;
    }

    /**
     * Marks or unmarks the piece as an exit.
     * @param set true to make the piece an exit, false otherwise.
     */

    public void setIsExit(Boolean set) {
        _isExit = set;
    }

    /**
     * Constructs a new Blank piece.
     */

    public Blank() {
        _position = new Coordinate(-1, -1);
    }

    /**
     * Constructs a new Blank piece.
     *
     * @param crd The piece coordinate.
     */

    public Blank(Coordinate crd) {
        _position = crd;
    }

    /**
     * Constructs a new Blank piece.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */

    public Blank(int x, int y) {
        _position = new Coordinate(x, y);
    }
}
