/**
 * Labyrinth
 *
 * Created by Eduardo Almeida and Joao Almeida.
 */

package pt.up.fe.lpoo.cli;

import pt.up.fe.lpoo.logic.Board;
import pt.up.fe.lpoo.logic.Coordinate;
import pt.up.fe.lpoo.logic.piece.Piece;
import pt.up.fe.lpoo.logic.piece.Wall;
import pt.up.fe.lpoo.logic.piece.itemizable.Blank;
import pt.up.fe.lpoo.logic.piece.itemizable.Dragon;
import pt.up.fe.lpoo.logic.piece.itemizable.Eagle;
import pt.up.fe.lpoo.logic.piece.itemizable.Hero;

public class Printer {
    private Board _board = null;

    private Printer() {
        //  You shall not... call this!
    }

    /**
     * Constructs a Printer object with a given Board.
     *
     * @param  board  The board to assign the Printer with.
     */

    public Printer(Board board) {
        _board = board;
    }

    /**
     * Prints a board on the CLI.
     */

    public void printBoard() {
        for (int i = 0; i < _board.getHeight(); i++) {
            for (int j = 0; j < _board.getWidth(); j++) {
                Piece pc;

                try {
                    pc = _board.getPiece(new Coordinate(j, i));
                } catch (Exception exc) {
                    System.out.print(" ");

                    continue;
                }

                if (pc instanceof Wall)
                    System.out.print("+");
                else if (pc instanceof Hero)
                    System.out.print(((Hero) pc).getHasItem() ? "A" : "H");
                else if (pc instanceof Blank && ((Blank) pc).getHasItem())
                    System.out.print("E");
                else if (pc instanceof Dragon) {
                    if (((Dragon) pc).getIsSleeping())
                        System.out.print(((Dragon) pc).getHasItem() ? "F" : "d");
                    else {
                        System.out.print(((Dragon) pc).getHasItem() ? "F" : "D");
                    }
                } else if (pc instanceof Blank && ((Blank) pc).getIsExit())
                    System.out.print("S");
                else if (pc instanceof Eagle) {
                    if (!((Eagle) pc).getOnGround())
                        System.out.print(((Eagle) pc).getHasItem() ? "x" : "X");
                    else
                        System.out.print(((Eagle) pc).getHasItem() ? "~" : "^");
                } else
                    System.out.print(" ");
            }

            System.out.println();
        }
    }
}
