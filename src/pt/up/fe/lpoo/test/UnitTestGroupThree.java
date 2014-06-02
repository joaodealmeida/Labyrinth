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
import pt.up.fe.lpoo.logic.piece.itemizable.Blank;
import pt.up.fe.lpoo.logic.piece.itemizable.Hero;
import pt.up.fe.lpoo.logic.piece.itemizable.Dragon;
import pt.up.fe.lpoo.logic.piece.itemizable.Eagle;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UnitTestGroupThree {
    private Eagle _egl = null;
    private Board _board = null;
    private Blank _swordPiece = null;

    /**
     * Performs an array of movements on a given piece.
     *
     * @param moves The movements to perform.
     * @param pc The piece to perform the movements on.
     *
     * @throws Exception Thrown when something goes wrong.
     */

    private void _performMultipleMovement(Board.Direction[] moves, Piece pc) throws Exception {
        for (Board.Direction mv : moves) {
            if (_egl != null) {
                if (_swordPiece != null && _swordPiece.getCoordinate().x == _egl.getCoordinate().x && _swordPiece.getCoordinate().y == _egl.getCoordinate().y) {
                    if (!_egl.getOnGround()) {
                        _egl.setOnGround(true);
                        _egl.setHasItem(true);

                        _swordPiece = null;
                    } else {
                        _egl.move(_egl.getFirstPosition());

                        _egl.setOnGround(false);
                    }
                } else {
                    if (!(_egl.getFirstPosition().equals(_egl.getCoordinate()) && _egl.getHasItem())) {
                        _egl.setOnGround(false);

                        if (_swordPiece != null)
                            _egl.move(_swordPiece.getCoordinate());
                        else
                            _egl.move(_egl.getFirstPosition());
                    } else
                        _egl.setOnGround(true);
                }
            }

            pc.move(mv);
        }
    }

    /**
     * Sets up the testing scenario.
     *
     * @throws Exception Something went wrong!
     */

    @Before public void setUp() throws Exception {
        _board = new Board();

        BoardGenerator gen = new BoardGenerator(10, 10, 1);

        ArrayList<Piece> ooBoard = gen.getDefaultBoard();

        for (Piece pc : ooBoard)
            pc.setBoard(_board);

        _board.setBoardPieces(ooBoard);
        _board.setWidth(10);
        _board.setHeight(10);

        Dragon drag = (Dragon) _board.getPiecesWithType(Board.Type.DRAGON).get(0);

        drag.setBehavior(Dragon.Behavior.STOP);

        assertNotNull("Couldn't generate board.", _board);

        Hero hero = (Hero) _board.getPiecesWithType(Board.Type.HERO).get(0);

        Coordinate crd = hero.getCoordinate();

        _egl = new Eagle(false, new Coordinate(crd.x, crd.y));

        _board.setEagle(_egl);
        _egl.setBoard(_board);

        ArrayList<Piece> blankPieces = _board.getPiecesWithType(Board.Type.BLANK);

        if (_swordPiece == null)
            for (Piece pc : blankPieces)
                if (((Blank) pc).getHasItem())
                    _swordPiece = (Blank) pc;

        assertNotNull("Couldn't generate Eagle.", _egl);
    }

    /**
     * Tests if the eagle chooses the most correct path.
     *
     * @throws Exception Something went wrong!
     */

    @Test public void testEagleAutoPath() throws Exception {
        try {
            Hero hero = (Hero) _board.getPiecesWithType(Board.Type.HERO).get(0);

            Board.Direction[] moveSequence = {Board.Direction.RIGHT, Board.Direction.RIGHT};

            _performMultipleMovement(moveSequence, hero);

            assertTrue(_egl.getCoordinate().x == 1 && _egl.getCoordinate().y == 3);
        } catch (Exception exc) {
            fail();
        }
    }

    /**
     * Tests if the eagle can get the sword.
     *
     * @throws Exception Something went wrong!
     */

    @Test public void testEagleGetSword() throws Exception {
        try {
            Hero hero = (Hero) _board.getPiecesWithType(Board.Type.HERO).get(0);

            Board.Direction[] moveSequence = {Board.Direction.RIGHT, Board.Direction.RIGHT, Board.Direction.RIGHT,
                    Board.Direction.RIGHT, Board.Direction.RIGHT, Board.Direction.RIGHT, Board.Direction.RIGHT,
                    Board.Direction.RIGHT, Board.Direction.DOWN, Board.Direction.DOWN, Board.Direction.DOWN,
                    Board.Direction.DOWN, Board.Direction.DOWN, Board.Direction.DOWN, Board.Direction.DOWN,
                    Board.Direction.DOWN, Board.Direction.DOWN};

            _performMultipleMovement(moveSequence, hero);

            assertTrue(_egl.getHasItem());
        } catch (Exception exc) {
            fail();
        }
    }

    /**
     * Tests if the eagle goes back correctly to the first position.
     *
     * @throws Exception Something went wrong!
     */

    @Test public void testEagleGoBack() throws Exception {
        testEagleGetSword();

        assertTrue(_egl.getCoordinate().x == 1 && _egl.getCoordinate().y == 1);
    }

    /**
     * Tests if the eagle will wait for the hero at the first position.
     *
     * @throws Exception Something went wrong!
     */

    @Test public void testEagleWaitForHero() throws Exception {
        testEagleGoBack();

        Coordinate expectedCrd = _egl.getCoordinate();

        Hero hero = (Hero) _board.getPiecesWithType(Board.Type.HERO).get(0);

        for (int i = 0; i < 3; i++) {
            Board.Direction[] moveSequence = {Board.Direction.UP};

            _performMultipleMovement(moveSequence, hero);

            assertTrue(_egl.getCoordinate().x == expectedCrd.x && _egl.getCoordinate().y == expectedCrd.y);
        }
    }

    /**
     * Tests if the eagle can be caught back by the hero.
     *
     * @throws Exception Something went wrong!
     */

    @Test public void testEagleCaughtByHero() throws Exception {
        testEagleWaitForHero();

        Hero hero = (Hero) _board.getPiecesWithType(Board.Type.HERO).get(0);

        Board.Direction[] moveSequence = {Board.Direction.UP, Board.Direction.UP, Board.Direction.UP, Board.Direction.UP,
                Board.Direction.UP, Board.Direction.UP, Board.Direction.UP, Board.Direction.LEFT, Board.Direction.LEFT,
                Board.Direction.LEFT, Board.Direction.LEFT, Board.Direction.LEFT, Board.Direction.LEFT, Board.Direction.LEFT,
                Board.Direction.LEFT, Board.Direction.LEFT, Board.Direction.LEFT, };

        _performMultipleMovement(moveSequence, hero);

        assertTrue(hero.getHasItem());
    }

    /**
     * Tests if the eagle can be eaten by the dragon.
     *
     * @throws Exception Something went wrong!
     */

    @Test public void testEagleEatenByDragon() throws Exception {
        Dragon drag = (Dragon) _board.getPiecesWithType(Board.Type.DRAGON).get(0);

        drag.setBehavior(Dragon.Behavior.NO_SLEEP);

        _egl.move(drag.getCoordinate());

        drag.move(Board.Direction.UP);

        drag.move(Board.Direction.DOWN);
    }

    /**
     * Clean up after the tests.
     */

    @After public void tearDown() {
        //  Nothing so far, AFAIK at least.
    }
}
