/**
 * Labyrinth
 *
 * Created by Eduardo Almeida and Joao Almeida.
 */

package pt.up.fe.lpoo.cli;

import pt.up.fe.lpoo.logic.Board;
import pt.up.fe.lpoo.logic.BoardGenerator;
import pt.up.fe.lpoo.logic.Coordinate;
import pt.up.fe.lpoo.logic.piece.Piece;
import pt.up.fe.lpoo.logic.piece.itemizable.Blank;
import pt.up.fe.lpoo.logic.piece.itemizable.Dragon;
import pt.up.fe.lpoo.logic.piece.itemizable.Eagle;
import pt.up.fe.lpoo.logic.piece.itemizable.Hero;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Controller {
    static Blank _swordPiece = null;

    static Eagle egl = null;

    public static void main(String args[]) {
        Board brd = new Board();

        int width = 10, height = 10, dragons = 1;

        Dragon.Behavior bhv = Dragon.Behavior.STOP;

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String readLine;

        Boolean skipBoardGeneration = false;

        try {
            System.out.print("Press D for default or any other string to create a new board: ");

            readLine = br.readLine();

            if (readLine.equalsIgnoreCase("d")) {
                skipBoardGeneration = true;
            } else {
                System.out.print("Board Width? (Must be bigger or equal than 5): ");

                readLine = br.readLine();

                if (Integer.parseInt(readLine) > 4)
                    width = Integer.parseInt(readLine);
                else {
                    while (true) {
                        System.out.print("Invalid Width. Please try again. (Must be bigger than 5): ");
                        readLine = br.readLine();

                        if (Integer.parseInt(readLine) > 4) {
                            width = Integer.parseInt(readLine);

                            break;
                        }
                    }
                }

                System.out.println();

                System.out.print("Board Height? (Must be bigger or equal than 5): ");

                readLine = br.readLine();

                if (Integer.parseInt(readLine) > 4)
                    height = Integer.parseInt(readLine);
                else {
                    while (true) {
                        System.out.print("Invalid Height. Please try again. (Must be bigger than 5): ");
                        readLine = br.readLine();

                        if (Integer.parseInt(readLine) > 4) {
                            height = Integer.parseInt(readLine);

                            break;
                        }
                    }
                }

                System.out.println();

                System.out.print("Number of Dragons? (Must be bigger or equal than 1): ");

                readLine = br.readLine();

                if (Integer.parseInt(readLine) > 0)
                    dragons = Integer.parseInt(readLine);
                else {
                    while (true) {
                        System.out.print("Invalid Number of Dragons Please try again. (Must be bigger than 1): ");
                        readLine = br.readLine();

                        if (Integer.parseInt(readLine) > 0) {
                            dragons = Integer.parseInt(readLine);

                            break;
                        }
                    }

                }

                System.out.println("");

                System.out.print("Dragons Behavior? Always (S)leeping, Always (M)oving, (R)andomly Sleeping: ");

                readLine = br.readLine();

                while (!readLine.equalsIgnoreCase("s") && !readLine.equalsIgnoreCase("m") && !readLine.equalsIgnoreCase("r")) {
                    System.out.print("Invalid Choice. Dragons Behavior? Always (S)leeping, Always (M)oving, (R)andomly Sleeping: ");

                    readLine = br.readLine();
                }

                if (readLine.equalsIgnoreCase("m"))
                    bhv = Dragon.Behavior.NO_SLEEP;
                else if (readLine.equalsIgnoreCase("r"))
                    bhv = Dragon.Behavior.SLEEP;

                System.out.println("Board Size is " + width + "w * " + height + "h.");
                System.out.println("Number of Dragons: " + dragons + " .");
            }

            System.out.println();
        } catch (IOException exception) {
            width = 10;
            height = 10;
            dragons = 1;

            System.out.println("An error has occurred while attempting to ask for board size values. Continuing with default values (10w * 10h)...");
        }

        Boolean boardGenSuccess = false;

        for (int i = 0; i < 10; i++) {
            try {
                BoardGenerator brdGen = new BoardGenerator(width, height, dragons);

                ArrayList<Piece> ooBoard;

                ooBoard = (skipBoardGeneration ? brdGen.getDefaultBoard() : brdGen.generateBoard());

                for (Piece pc : ooBoard) {
                    pc.setBoard(brd);

                    if (pc instanceof Dragon)
                        ((Dragon) pc).setBehavior(bhv);
                }

                brd.setBoardPieces(ooBoard);

                brd.setWidth(width);
                brd.setHeight(height);

                boardGenSuccess = true;

                break;
            } catch (Exception exc) {

            }
        }

        if (!boardGenSuccess) {
            System.out.println("Unable to get a board.");

            System.exit(1);
        }

        Printer prt = new Printer(brd);

        prt.printBoard();

        Hero heroPiece = null;

        try {
            heroPiece = (Hero) brd.getPiecesWithType(Board.Type.HERO).get(0);
        } catch (Exception exc) {
            System.out.println("A Hero wasn't found. This can not happen. Aborting...");

            System.exit(1);
        }

        try {
            System.out.println();

            System.out.println("Please type (U)p/(L)eft/(D)own/(R)ight/Launch (E)agle to play, followed by Return.");

            readLine = br.readLine();

            System.out.println();

            while (!readLine.equalsIgnoreCase("quit")) {
                try {
                    if (readLine.equalsIgnoreCase("up") || readLine.equalsIgnoreCase("u"))
                        heroPiece.move(Board.Direction.UP);
                    else if (readLine.equalsIgnoreCase("down") || readLine.equalsIgnoreCase("d"))
                        heroPiece.move(Board.Direction.DOWN);
                    else if (readLine.equalsIgnoreCase("right") || readLine.equalsIgnoreCase("r"))
                        heroPiece.move(Board.Direction.RIGHT);
                    else if (readLine.equalsIgnoreCase("left") || readLine.equalsIgnoreCase("l"))
                        heroPiece.move(Board.Direction.LEFT);
                    else if (readLine.equalsIgnoreCase("e")) {
                        if (egl == null) {
                            Coordinate crd = heroPiece.getCoordinate();

                            egl = new Eagle(false, new Coordinate(crd.x, crd.y));

                            brd.setEagle(egl);
                            egl.setBoard(brd);

                            ArrayList<Piece> blankPieces = brd.getPiecesWithType(Board.Type.BLANK);

                            if (_swordPiece == null)
                                for (Piece pc : blankPieces)
                                    if (((Blank) pc).getHasItem())
                                        _swordPiece = (Blank) pc;
                        }
                    } else {
                        readLine = br.readLine();

                        continue;
                    }
                } catch (Exception exc) {

                }

                try {
                    brd.moveDragon();

                    if (egl != null) {
                        if (_swordPiece != null && _swordPiece.getCoordinate().x == egl.getCoordinate().x && _swordPiece.getCoordinate().y == egl.getCoordinate().y) {
                            if (!egl.getOnGround()) {
                                egl.setOnGround(true);
                                egl.setHasItem(true);

                                _swordPiece = null;
                            } else {
                                egl.move(egl.getFirstPosition());

                                egl.setOnGround(false);
                            }
                        } else {
                            if (!(egl.getFirstPosition().equals(egl.getCoordinate()) && egl.getHasItem())) {
                                egl.setOnGround(false);

                                if (_swordPiece != null)
                                    egl.move(_swordPiece.getCoordinate());
                                else
                                    egl.move(egl.getFirstPosition());
                            } else
                                egl.setOnGround(true);
                        }
                    }
                } catch (Exception exc) {

                }

                try {
                    brd.checkDragon();
                } catch (Exception exc) {

                }

                try {
                    brd.recheckGameState();
                } catch (Exception exc) {
                    System.out.println(exc.getMessage());
                }

                System.out.println();

                prt.printBoard();

                System.out.println();

                if (brd.getGameState() == Board.State.WON) {
                    System.out.println("You won, congratulations!");

                    break;
                } else if (brd.getGameState() == Board.State.LOST) {
                    System.out.println("You lost... :(");

                    break;
                }

                if (egl == null)
                    System.out.println("(U)p/(L)eft/(D)own/(R)ight/Launch/(E)agle to play, followed by Return.");
                else
                    System.out.println("(U)p/(L)eft/(D)own/(R)ight, followed by Return.");

                readLine = br.readLine();
            }
        } catch (IOException exception) {
            //  Take care of it.
        }
    }
}
