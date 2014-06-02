/**
 * Labyrinth
 *
 * Created by Eduardo Almeida and Joao Almeida.
 */

package pt.up.fe.lpoo.logic.piece.itemizable;

        import pt.up.fe.lpoo.logic.Board;
        import pt.up.fe.lpoo.logic.Coordinate;
        import pt.up.fe.lpoo.logic.piece.Piece;

public class ItemizablePiece extends Piece {
    protected Boolean _item = false;

    /**
     * Constructs a new Itemizable Piece.
     */

    public ItemizablePiece() {
        _position = new Coordinate(-1, -1);
    }

    /**
     * Constructs a new Itemizable Piece.
     *
     * @param crd Piece's coordinate.
     */

    public ItemizablePiece(Coordinate crd) {
        _position = crd;
    }

    /**
     * Constructs a new Itemizable Piece.
     *
     * @param x Piece's x coordinate.
     * @param y Piece's y coordinate.
     */

    public ItemizablePiece(int x, int y) {
        _position = new Coordinate(x, y);
    }

    /**
     * Getter for item status.
     *
     * @return true if the piece holds an item, false if not.
     */

    public Boolean getHasItem() {
        return _item;
    }

    /**
     * Setter for item status.
     *
     * @param hi true if the piece should be holding an item, false if not.
     */

    public void setHasItem(Boolean hi) {
        _item = hi;
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
                return false;
            } else {
                if (((ItemizablePiece) nextObj).getHasItem()) {
                    setHasItem(true);

                    ((ItemizablePiece) nextObj).setHasItem(false);
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
}
