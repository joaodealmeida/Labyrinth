/**
 * Labyrinth
 *
 * Created by Eduardo Almeida and Joao Almeida.
 */

package pt.up.fe.lpoo.gui;

import pt.up.fe.lpoo.logic.Board;
import pt.up.fe.lpoo.logic.BoardGenerator;
import pt.up.fe.lpoo.logic.Coordinate;
import pt.up.fe.lpoo.logic.piece.Piece;
import pt.up.fe.lpoo.logic.piece.Wall;
import pt.up.fe.lpoo.logic.piece.itemizable.Blank;
import pt.up.fe.lpoo.logic.piece.itemizable.Dragon;
import pt.up.fe.lpoo.logic.piece.itemizable.Eagle;
import pt.up.fe.lpoo.logic.piece.itemizable.Hero;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class LabyrinthBuilder extends JPanel implements MouseListener {
    private Board _board = new Board();

    public Coordinate _window = null;

    /**
     * Constructs a new labyrinth builder.
     *
     * @param  x  The width of the new board.
     * @param  y  The height of the new board.
     */

    public LabyrinthBuilder(int x, int y) {
        try {
            BoardGenerator bg = new BoardGenerator(x, y, 1);

            _window = new Coordinate(x * 50, y * 50);

            ArrayList<Piece> ooBoard = bg.generateBoard();

            for (Piece pc : ooBoard)
                pc.setBoard(_board);

            _board.setBoardPieces(ooBoard);
            _board.setWidth(x);
            _board.setHeight(y);

            addMouseListener(this);
        } catch (Exception exc) {

        }
    }

    /**
     * Getter for the board object.
     *
     * @return The generated Board object.
     */

    public Board getBoard() {
        return _board;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int sepX = _window.x / _board.getWidth();
        int sepY = _window.y / _board.getHeight();

        for (int i = 1; i <= _board.getWidth(); i++) {
            g.drawLine(sepX * i, 0, sepX * i, _window.y);
            g.drawLine(0, sepY * i, _window.x, sepY * i);
        }

        File img = new File("HootHoot.png");

        try {
            BufferedImage bimg = ImageIO.read(img);

            g.drawImage(bimg, 0, 0, sepX, sepY, null);
        } catch (Exception e) {

        }

        for (int i = 0; i < _board.getHeight(); i++) {
            for (int j = 0; j < _board.getWidth(); j++) {
                Piece pc;

                try {
                    pc = _board.getPiece(new Coordinate(j, i));
                } catch (Exception exc) {
                    System.out.print(" ");

                    continue;
                }

                if (pc instanceof Wall) {
                    File wood = new File("wooden_bg.jpg");

                    try {
                        BufferedImage woodbg = ImageIO.read(wood);

                        g.drawImage(woodbg, 0 + sepX * j, 0 + sepY * i, sepX, sepY, null);
                    } catch (Exception e) {

                    }
                } else {
                    File gr = new File("grass.png");

                    try {
                        BufferedImage gri = ImageIO.read(gr);

                        g.drawImage(gri, 0 + sepX * j, 0 + sepY * i, sepX, sepY, null);
                    } catch (Exception e) {

                    }

                    if (pc instanceof Hero) {
                        File hero = new File("teemo.png");

                        try {
                            BufferedImage heroi = ImageIO.read(hero);

                            g.drawImage(heroi, 0 + sepX * j, 0 + sepY * i, sepX, sepY, null);
                        } catch (Exception e) {

                        }
                    } else if (pc instanceof Blank && ((Blank) pc).getHasItem()) {
                        File sw = new File("sword.png");

                        try {
                            BufferedImage esp = ImageIO.read(sw);

                            g.drawImage(esp, 0 + sepX * j, 0 + sepY * i, sepX, sepY, null);
                        } catch (Exception e) {

                        }
                    } else if (pc instanceof Eagle)
                        System.out.print(((Eagle) pc).getOnGround() ? "E" : "X");
                    else if (pc instanceof Dragon) {
                        File dragon = new File(((Dragon) pc).getIsSleeping() ? "gyarados_zzz.png" : "gyarados.png");

                        try {
                            BufferedImage drg = ImageIO.read(dragon);

                            g.drawImage(drg, 0 + sepX * j, 0 + sepY * i, sepX, sepY, null);
                        } catch (Exception e) {

                        }
                    } else if (pc instanceof Blank && ((Blank) pc).getIsExit()) {
                        File exit = new File("door.png");

                        try {
                            BufferedImage exi = ImageIO.read(exit);

                            g.drawImage(exi, 0 + sepX * j, 0 + sepY * i, sepX, sepY, null);
                        } catch (Exception e) {

                        }
                    } else {

                    }
                }
            }
        }
    }

    /**
     * Changes a piece at a given coordinate.
     *
     * @param crd The coordinate of the piece to change.
     * @throws Exception An exception will be thrown when an invalid coordinate is passed.
     */

    public void changePieceAtCoordinate(Coordinate crd) throws Exception {
        Piece pc = _board.getPiece(crd);
        Piece newPiece;

        if (crd.x == 0 || crd.y == 0 || crd.x == _board.getWidth() - 1 || crd.y == _board.getHeight() - 1) {
            if (pc instanceof Wall) {
                newPiece = new Blank(crd);
                ((Blank) newPiece).setIsExit(true);
            } else
                newPiece = new Wall(crd);
        } else {
            if (pc instanceof Wall) {
                newPiece = new Hero(crd);
            } else if (pc instanceof Hero) {
                newPiece = new Blank(crd);
                ((Blank) newPiece).setHasItem(true);
            } else if (pc instanceof Blank && ((Blank) pc).getHasItem()) {
                newPiece = new Dragon(crd);
            } else if (pc instanceof Dragon) {
                newPiece = new Blank(crd);
            } else if (pc instanceof Blank) {
                newPiece = new Wall(crd);
            } else {
                throw new Exception("No suitable piece found.");
            }
        }

        _board.removePiece(pc);
        _board.addPiece(newPiece);

        repaint();
        revalidate();
    }

    /**
     * Checks the board for logic errors.
     *
     * @return true if the board is valid, false if not.
     */

    public Boolean validateBoard() {
        try {
            ArrayList<Piece> heroPcs = _board.getPiecesWithType(Board.Type.HERO);

            if (heroPcs.size() != 1)
                return false;

            ArrayList<Piece> swordPcs = _board.getPiecesWithType(Board.Type.SWORD);

            if (swordPcs.size() != 1)
                return false;

            ArrayList<Piece> exitPcs = _board.getPiecesWithType(Board.Type.EXIT);

            if (exitPcs.size() != 1)
                return false;

            ArrayList<Piece> dragonPcs = _board.getPiecesWithType(Board.Type.DRAGON);

            if (dragonPcs.size() == 0)
                return false;

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    //
    //  Mouse Implementation
    //

    public void mousePressed(MouseEvent e) {
        Point clickPoint = e.getPoint();

        int xUnit = _window.x / _board.getWidth();
        int yUnit = _window.y / _board.getHeight();

        int xPoint = (int) Math.floor(clickPoint.x / xUnit);
        int yPoint = (int) Math.floor(clickPoint.y / yUnit);

        System.out.println("Pressed (" + xPoint + ", " + yPoint + ").");

        try {
            changePieceAtCoordinate(new Coordinate(xPoint, yPoint));
        } catch (Exception exc) {
            System.out.println("Unable to change piece.");
        }
    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseClicked(MouseEvent e) {

    }
}
