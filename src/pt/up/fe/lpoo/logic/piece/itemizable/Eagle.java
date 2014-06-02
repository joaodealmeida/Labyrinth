/**
 * Labyrinth
 *
 * Created by Eduardo Almeida and Joao Almeida.
 */

package pt.up.fe.lpoo.logic.piece.itemizable;

import pt.up.fe.lpoo.logic.Board;
import pt.up.fe.lpoo.logic.Coordinate;
import pt.up.fe.lpoo.logic.piece.Piece;

public class Eagle extends ItemizablePiece {
    public Boolean _onGround = false;
    protected Coordinate _firstPosition;

    /**
     * Constructs an Eagle object with given parameters.
     *
     * @param  onGround  The initial state of the eagle (on ground or not).
     * @param  pos  The initial coordinate of the eagle.
     */

    public Eagle(Boolean onGround, Coordinate pos) {
        super();

        _onGround = onGround;
        _firstPosition = new Coordinate(pos.x, pos.y);
        _position = pos;
    }

    /**
     * Getter for the position the eagle initially started on.
     *
     * @return The eagle's first position coordinate.
     */

    public Coordinate getFirstPosition() {
        return _firstPosition;
    }

    /**
     * Setter for the eagle's state.
     *
     * @param og true if on ground, false if not.
     */

    public void setOnGround(Boolean og) {
        _onGround = og;
    }

    /**
     * Getter for the eagle's state.
     *
     * @return The current eagle ground state.
     */

    public Boolean getOnGround() {
        return _onGround;
    }

    @Override
    public Boolean move(Board.Direction Dir) throws Exception {
        throw new Exception("This can't be called, bro!");
    }

    private Boolean moveEagle(Board.Direction dir) throws Exception {
        Piece nextObj;
        Coordinate crdDiff = new Coordinate(0, 0);

        try {
            nextObj = _moveSharedCode(dir, crdDiff);
        } catch (Exception exc) {
            throw exc;
        }

        Integer x = crdDiff.x, y = crdDiff.y;

        try {
            if (((ItemizablePiece) nextObj).getHasItem()) {
                setHasItem(true);

                ((ItemizablePiece) nextObj).setHasItem(false);
            }
        } catch (Exception exc) {
            //  Expected exception if nextObj.class != ItemizablePiece
        }

        //  nextObj.setCoordinate(_position);

        _position = new Coordinate(_position.x + x, _position.y + y);

        return true;
    }

    /**
     * Moves the eagle by a piece.
     *
     * @param c1 The destination coordinate.
     * @return true if moved, false if not.
     *
     * @throws Exception Thrown when something goes wrong.
     */

    public Boolean move(Coordinate c1) throws Exception {
        // Sword on the same line

        if (_position.y == c1.y) {
            if (c1.x < _position.x)
                return moveEagle(Board.Direction.LEFT);
            if (c1.x > _position.x)
                return moveEagle(Board.Direction.RIGHT);
        }

        // Sword on the same column

        if (_position.x == c1.x) {
            if (c1.y < _position.y)
                return moveEagle(Board.Direction.UP);
            if (c1.y > _position.y)
                return moveEagle(Board.Direction.DOWN);
        }

        // Neither the same line or column

        if (_position.x < c1.x) {
            if (_position.y < c1.y) {
                moveEagle(Board.Direction.DOWN);
                return moveEagle(Board.Direction.RIGHT);
            }

            if (_position.y > c1.y) {
                moveEagle(Board.Direction.UP);
                return moveEagle(Board.Direction.RIGHT);
            }

        }

        if (_position.x > c1.x) {
            if (_position.y < c1.y) {
                moveEagle(Board.Direction.DOWN);
                return moveEagle(Board.Direction.LEFT);
            }

            if (_position.y > c1.y) {
                moveEagle(Board.Direction.UP);
                return moveEagle(Board.Direction.LEFT);
            }

        }

        _item = true; //  Found the Sword!

        return false;
    }
}