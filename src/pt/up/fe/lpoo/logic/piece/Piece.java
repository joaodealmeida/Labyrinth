/**
 * Labyrinth
 *
 * Created by Eduardo Almeida and Joao Almeida.
 */

package pt.up.fe.lpoo.logic.piece;

import pt.up.fe.lpoo.logic.Board;
import pt.up.fe.lpoo.logic.Coordinate;
import pt.up.fe.lpoo.logic.piece.itemizable.Blank;

import java.io.Serializable;

import java.util.ArrayList;

public class Piece implements Serializable {
    protected Coordinate _position;

    protected Board _board;

    /**
     * Constructs a new Piece.
     */

    public Piece() {
        _position = new Coordinate(-1, -1);
    }

    /**
     * Constructs a new Piece.
     *
     * @param crd Piece's coordinate.
     */

    public Piece(Coordinate crd) {
        _position = crd;
    }

    /**
     * Constructs a new Piece.
     *
     * @param x Piece's x coordinate.
     * @param y Piece's y coordinate.
     */

    public Piece(int x, int y) {
        _position = new Coordinate(x, y);
    }

    /**
     * Getter for the piece's coordinate.
     *
     * @return The piece's coordinate.
     */

    public Coordinate getCoordinate() {
        return _position;
    }

    /**
     * Getter for the piece's board.
     *
     * @return The board associated with the piece.
     */

    public Board getBoard() {
        return _board;
    }

    /**
     * Setter for the piece's coordinate.
     *
     * @param crd The new piece's coordinate.
     */

    public void setCoordinate(Coordinate crd) {
        _position = crd;
    }

    /**
     * Setter for the piece's board.
     *
     * @param brd The new board associated with the piece.
     */

    public void setBoard(Board brd) {
        _board = brd;
    }

    protected Piece _moveSharedCode(Board.Direction dir, Coordinate crdDiff) throws Exception {
        ArrayList<Piece> near = new ArrayList<Piece>();

        try {
            near.add(_board.getPiece(new Coordinate(_position.x, _position.y - 1)));
            near.add(_board.getPiece(new Coordinate(_position.x - 1, _position.y)));
            near.add(_board.getPiece(new Coordinate(_position.x, _position.y + 1)));
            near.add(_board.getPiece(new Coordinate(_position.x + 1, _position.y)));
        } catch (Exception exc) {
            throw exc;
        }

        int x = 0, y = 0;

        Piece nextObj;

        switch (dir) {

            case UP:

                nextObj = near.get(0);

                y--;

                break;

            case LEFT:

                nextObj = near.get(1);

                x--;

                break;

            case DOWN:

                nextObj = near.get(2);

                y++;

                break;

            case RIGHT:

                nextObj = near.get(3);

                x++;

                break;

            default:

                throw new Exception("What is this? A new coordinate system!?");

        }

        crdDiff.x = x;
        crdDiff.y = y;

        return nextObj;
    }

    /**
     * Moves the piece in a desired direction.
     *
     * @param dir The desired direction.
     * @return true if the move was successful, false if it was impossible.
     *
     * @throws Exception Thrown when something goes wrong.
     */

    public Boolean move(Board.Direction dir) throws Exception {
        Piece nextObj;
        Coordinate crdDiff = new Coordinate(0, 0);

        try {
            nextObj = _moveSharedCode(dir, crdDiff);
        } catch (Exception exc) {
            throw exc;
        }

        Integer x = crdDiff.x, y = crdDiff.y;

        if (nextObj instanceof Blank) {
            if (((Blank) nextObj).getIsExit())
                return false;

            nextObj.setCoordinate(_position);

            _position = new Coordinate(_position.x + x, _position.y + y);

            return true;
        }

        return false;
    }
}
