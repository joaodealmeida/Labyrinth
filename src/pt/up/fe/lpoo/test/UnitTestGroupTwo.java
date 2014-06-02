/**
 * Labyrinth
 *
 * Created by Eduardo Almeida and Joao Almeida.
 */

package pt.up.fe.lpoo.test;

import org.junit.*;

import pt.up.fe.lpoo.logic.Board;
import pt.up.fe.lpoo.logic.BoardGenerator;
import pt.up.fe.lpoo.logic.Coordinate;
import pt.up.fe.lpoo.logic.piece.Piece;
import pt.up.fe.lpoo.logic.piece.itemizable.Dragon;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UnitTestGroupTwo {
    private Board brd;

    /**
     * Performs an array of movements on a given piece.
     *
     * @param moves The movements to perform.
     * @param pc The piece to perform the movements on.
     *
     * @throws Exception Thrown when something goes wrong.
     */

    private void _performMultipleMovement(Board.Direction[] moves, Piece pc) throws Exception {
        for (Board.Direction mv : moves)
            pc.move(mv);
    }

    /**
     * Sets up the testing scenario.
     *
     * @throws Exception Something went wrong!
     */

    @Before public void setUp() throws Exception {
        brd = new Board();

        BoardGenerator gen = new BoardGenerator(10, 10, 1);

        ArrayList<Piece> ooBoard = gen.getDefaultBoard();

        for (Piece pc : ooBoard)
            pc.setBoard(brd);

        brd.setBoardPieces(ooBoard);
        brd.setWidth(10);
        brd.setHeight(10);

        assertNotNull("Couldn't generate board.", brd);
    }

    /**
     * Moves one of the dragons.
     *
     * @throws Exception Something went wrong!
     */

    @Test public void moveOneDragon() throws Exception {
        Dragon drag = (Dragon) brd.getPiecesWithType(Board.Type.DRAGON).get(0);

        Coordinate initialCrd = new Coordinate(drag.getCoordinate().x, drag.getCoordinate().y);

        drag.setBehavior(Dragon.Behavior.NO_SLEEP);

        Board.Direction[] moves = {Board.Direction.DOWN, Board.Direction.DOWN};

        _performMultipleMovement(moves, drag);

        assertTrue(drag.getCoordinate().x == initialCrd.x && drag.getCoordinate().y - 2 == initialCrd.y);
    }

    /**
     * Tries to move one of the stopped dragons.
     *
     * @throws Exception Something went wrong!
     */

    @Test public void moveOneStoppedDragon() throws Exception {
        Dragon drag = (Dragon) brd.getPiecesWithType(Board.Type.DRAGON).get(0);

        Coordinate initialCrd = new Coordinate(drag.getCoordinate().x, drag.getCoordinate().y);

        drag.setBehavior(Dragon.Behavior.STOP);

        Board.Direction[] moves = {Board.Direction.DOWN, Board.Direction.DOWN};

        _performMultipleMovement(moves, drag);

        assertTrue(drag.getCoordinate().x == initialCrd.x && drag.getCoordinate().y == initialCrd.y);
    }

    /**
     * Tests whether the dragon can sleep.
     *
     * @throws Exception Something went wrong!
     */

    @Test public void testDragonCanSleep() throws Exception {
        Dragon drag = (Dragon) brd.getPiecesWithType(Board.Type.DRAGON).get(0);

        drag.setBehavior(Dragon.Behavior.SLEEP);

        drag.move(Board.Direction.DOWN);

        int i = 0;

        for (; i < 10000 && !drag.getIsSleeping(); i++) {
            drag.move(Board.Direction.DOWN);

            if (drag.getIsSleeping())
                break;

            drag.move(Board.Direction.UP);
        }

        assertFalse(i == 10000);
    }

    /**
     * Clean up after the tests.
     */

    @After public void tearDown() {
        //  Nothing so far, AFAIK at least.
    }
}
