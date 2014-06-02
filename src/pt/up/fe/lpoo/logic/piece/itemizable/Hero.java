/**
 * Labyrinth
 *
 * Created by Eduardo Almeida and Joao Almeida.
 */

package pt.up.fe.lpoo.logic.piece.itemizable;

import pt.up.fe.lpoo.logic.Board;
import pt.up.fe.lpoo.logic.Coordinate;
import pt.up.fe.lpoo.logic.piece.Piece;

public class Hero extends ItemizablePiece {

    /**
     * Constructs a new Hero.
     */

    public Hero() {
        _position = new Coordinate(-1, -1);
    }

    /**
     * Constructs a new Hero.
     *
     * @param crd Hero's position.
     */

    public Hero(Coordinate crd) {
        _position = crd;
    }

    /**
     * Constructs a new Hero.
     *
     * @param x Hero's x position.
     * @param y Hero's y position.
     */

    public Hero(int x, int y) {
        _position = new Coordinate(x, y);
    }

    @Override public Boolean move(Board.Direction dir) throws Exception {
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
                if (!getHasItem())
                    return false;

                ((Blank) nextObj).setIsExit(false);
            } else {
                if (((Blank) nextObj).getHasItem()) {
                    setHasItem(true);

                    ((Blank) nextObj).setHasItem(false);
                }
            }

            nextObj.setCoordinate(_position);

            _position = new Coordinate(_position.x + x, _position.y + y);

            return true;
        } else if (nextObj instanceof Eagle) {
            Eagle eg = (Eagle) nextObj;

            if (eg.getHasItem()) {
                this.setHasItem(true);

                eg.setHasItem(false);

                this.getBoard().setEagle(null);
            }

            return true;
        }

        return false;
    }
}
