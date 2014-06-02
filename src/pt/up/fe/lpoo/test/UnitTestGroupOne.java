/**
 * Labyrinth
 *
 * Created by Eduardo Almeida and Joao Almeida.
 */

package pt.up.fe.lpoo.test;

import org.junit.*;

import pt.up.fe.lpoo.logic.*;
import pt.up.fe.lpoo.logic.piece.*;
import pt.up.fe.lpoo.logic.piece.itemizable.*;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UnitTestGroupOne {
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

        Dragon drag = (Dragon) brd.getPiecesWithType(Board.Type.DRAGON).get(0);

        drag.setBehavior(Dragon.Behavior.STOP);

        assertNotNull("Couldn't generate board.", brd);
    }

    /**
     * Tests the Hero movement.
     */

    @Test public void testHeroMove() {
        try {
            Hero hero = (Hero) brd.getPiecesWithType(Board.Type.HERO).get(0);

            Coordinate oldCoord = new Coordinate(hero.getCoordinate().x, hero.getCoordinate().y);

            hero.move(Board.Direction.DOWN);

            Coordinate newCoord = hero.getCoordinate();

            if (oldCoord.x != newCoord.x || oldCoord.y != newCoord.y - 1)
                fail("Move failed.");
        } catch (Exception exc) {
            fail(exc.getMessage());
        }
    }

    /**
     * Test for the cases where the hero can't move to a desired direction.
     */

    @Test public void testHeroImmobile() {
        try {
            Hero hero = (Hero) brd.getPiecesWithType(Board.Type.HERO).get(0);

            Coordinate oldCoord = new Coordinate(hero.getCoordinate().x, hero.getCoordinate().y);

            hero.move(Board.Direction.LEFT);

            Coordinate newCoord = hero.getCoordinate();

            if (oldCoord.x != newCoord.x || oldCoord.y != newCoord.y)
                fail("Move occurred. But, to where? :o");
        } catch (Exception exc) {
            fail(exc.getMessage());
        }
    }

    /**
     * Tests for the hero grabbing the sword.
     */

    @Test public void testHeroGetSword() {
        try {
            Hero hero = (Hero) brd.getPiecesWithType(Board.Type.HERO).get(0);

            Board.Direction[] moveSequence = {Board.Direction.RIGHT, Board.Direction.RIGHT, Board.Direction.RIGHT,
                    Board.Direction.DOWN, Board.Direction.DOWN, Board.Direction.DOWN, Board.Direction.DOWN,
                    Board.Direction.LEFT, Board.Direction.LEFT, Board.Direction.LEFT,
                    Board.Direction.DOWN, Board.Direction.DOWN, Board.Direction.DOWN};

            _performMultipleMovement(moveSequence, hero);

            assertTrue(hero.getHasItem());
        } catch (Exception exc) {
            fail(exc.getMessage());
        }
    }

    /**
     * Tests for the game lose.
     */

    @Test public void testLoseGame() {
        try {
            Hero hero = (Hero) brd.getPiecesWithType(Board.Type.HERO).get(0);

            hero.move(Board.Direction.DOWN);

            brd.moveDragon();
            brd.checkDragon();
            brd.recheckGameState();

            assertTrue(brd.getGameState() == Board.State.LOST);
        } catch (Exception exc) {
            fail(exc.getMessage());
        }
    }

    /**
     * Tests for the slaying of the dragon.
     *
     * @throws Exception Something went wrong!
     */

    @Test(expected = Exception.class) public void testSlayDragon() throws Exception {
        try {
            testHeroGetSword();

            Hero hero = (Hero) brd.getPiecesWithType(Board.Type.HERO).get(0);

            Board.Direction[] moveSequence = {Board.Direction.UP, Board.Direction.UP, Board.Direction.UP,
                    Board.Direction.UP, Board.Direction.UP, Board.Direction.UP};

            _performMultipleMovement(moveSequence, hero);

            brd.checkDragon();
            brd.recheckGameState();

        } catch (Exception exc) {
            fail();
        }

        Dragon drag = (Dragon) brd.getPiecesWithType(Board.Type.DRAGON).get(0);
    }

    /**
     * Tests for the winning of the game.
     *
     * @throws Exception Something went wrong!
     */

    @Test public void testWinGame() throws Exception {
        Boolean wentWell = false;

        try {
            testSlayDragon();
        } catch (Exception exc) {
            wentWell = true;
        } finally {
            assertTrue(wentWell);
        }

        Hero hero = (Hero) brd.getPiecesWithType(Board.Type.HERO).get(0);

        Board.Direction[] moveSequence = {Board.Direction.UP, Board.Direction.UP, Board.Direction.UP,
                Board.Direction.RIGHT, Board.Direction.RIGHT, Board.Direction.RIGHT, Board.Direction.RIGHT,
                Board.Direction.RIGHT, Board.Direction.RIGHT, Board.Direction.RIGHT,
                Board.Direction.DOWN, Board.Direction.DOWN, Board.Direction.DOWN, Board.Direction.DOWN,
                Board.Direction.RIGHT};

        _performMultipleMovement(moveSequence, hero);

        brd.recheckGameState();

        assertTrue(brd.getGameState() == Board.State.WON);
    }

    /**
     * Tests for the case where a hero may try to exit the puzzle without getting the sword first.
     *
     * @throws Exception Something went wrong!
     */

    @Test public void testGetExitWithoutSword() throws Exception {
        Hero hero = (Hero) brd.getPiecesWithType(Board.Type.HERO).get(0);

        Board.Direction[] moveSequence = {Board.Direction.RIGHT, Board.Direction.RIGHT, Board.Direction.RIGHT,
                Board.Direction.RIGHT, Board.Direction.RIGHT, Board.Direction.RIGHT, Board.Direction.RIGHT,
                Board.Direction.DOWN, Board.Direction.DOWN, Board.Direction.DOWN, Board.Direction.DOWN,
                Board.Direction.RIGHT};

        _performMultipleMovement(moveSequence, hero);

        brd.recheckGameState();

        assertTrue(brd.getGameState() == Board.State.RUNNING);
    }

    /**
     * Clean up after the tests.
     */

    @After public void tearDown() {
        //  Nothing so far, AFAIK at least.
    }
}
