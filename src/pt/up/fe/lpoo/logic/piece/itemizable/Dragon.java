/**
 * Labyrinth
 *
 * Created by Eduardo Almeida and Joao Almeida.
 */

package pt.up.fe.lpoo.logic.piece.itemizable;

import pt.up.fe.lpoo.logic.Board;
import pt.up.fe.lpoo.logic.Coordinate;
import pt.up.fe.lpoo.logic.piece.Piece;

import java.util.Random;

public class Dragon extends ItemizablePiece {
    private Boolean _sleeping = false;
    private Behavior _behavior = Behavior.SLEEP;

    public enum Behavior {STOP, NO_SLEEP, SLEEP};

    @Override public Boolean move(Board.Direction dir) throws Exception {
        if (_behavior == Behavior.STOP)
            return false;
        else if (_behavior == Behavior.NO_SLEEP)
            return super.move(dir);

        Random rand = new Random();

        int num = rand.nextInt(19);

        try {
            if (num < 14) {
                _sleeping = false;

                Piece nextObj;
                Coordinate crdDiff = new Coordinate(0, 0);

                try {
                    nextObj = _moveSharedCode(dir, crdDiff);
                } catch (Exception exc) {
                    throw exc;
                }

                Integer x = crdDiff.x, y = crdDiff.y;

                if (nextObj instanceof Blank) {
                    if (((Blank) nextObj).getIsExit()) {
                        return false;
                    } else {
                        if (((ItemizablePiece) nextObj).getHasItem()) {
                            setHasItem(true);

                            ((Blank) nextObj).setHasItem(false);
                        } else if (getHasItem()) {
                            setHasItem(false);

                            ((Blank) nextObj).setHasItem(true);
                        }
                    }

                    nextObj.setCoordinate(_position);

                    _position = new Coordinate(_position.x + x, _position.y + y);

                    return true;
                }

                if (nextObj instanceof Eagle) {
                    if (((Eagle) nextObj).getOnGround())
                        _board.setGameState(Board.State.LOST);

                    return true;
                }

                return false;
            }

            _sleeping = true;

            return false;
        } catch (Exception exc) {
            throw exc;
        }
    }

    /**
     * Creates a new Dragon.
     */

    public Dragon() {
        _position = new Coordinate(-1, -1);
    }

    /**
     * Creates a new Dragon.
     *
     * @param crd The piece coordinate.
     */

    public Dragon(Coordinate crd) {
        _position = crd;
    }

    /**
     * Creates a new Dragon.
     *
     * @param x The piece x coordinate.
     * @param y The piece y coordinate.
     */

    public Dragon(int x, int y) {
        _position = new Coordinate(x, y);
    }

    /**
     * Marks a dragon as sleeping.
     */

    public void setIsSleeping() {
        _sleeping = true;
    }

    public Boolean getIsSleeping() {
        return _sleeping;
    }

    /**
     * Getter for the current dragon behavior.
     *
     * @return The dragon behavior.
     */

    public Behavior getBehavior() {
        return _behavior;
    }

    /**
     * Setter for the current dragon behavior.
     *
     * @param bhv The new dragon behavior.
     */

    public void setBehavior(Behavior bhv) {
        _behavior = bhv;
    }
}
