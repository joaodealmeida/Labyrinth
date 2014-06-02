/**
 * Labyrinth
 *
 * Created by Eduardo Almeida and Joao Almeida.
 */

package pt.up.fe.lpoo.gui;

import pt.up.fe.lpoo.logic.Board;
import pt.up.fe.lpoo.logic.Coordinate;
import pt.up.fe.lpoo.logic.piece.Piece;
import pt.up.fe.lpoo.logic.piece.Wall;
import pt.up.fe.lpoo.logic.piece.itemizable.Blank;
import pt.up.fe.lpoo.logic.piece.itemizable.Dragon;
import pt.up.fe.lpoo.logic.piece.itemizable.Eagle;
import pt.up.fe.lpoo.logic.piece.itemizable.Hero;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class GamePanel extends JPanel implements KeyListener {
    public Coordinate _window = new Coordinate(500, 500);

    private Board _board;

    private String _upBinding = "up";
    private String _leftBinding = "left";
    private String _downBinding = "down";
    private String _rightBinding = "right";

    private String _eagleBinding = "e";

    private Boolean _presentedModalState = false;

    private Eagle _egl = null;

    private Blank _swordPiece = null;

    /**
     * Constructs a GamePanel object.
     *
     * @param  board  The board to assign the game panel with.
     * @param  windowSize  The size of the panel (in pixels).
     */

    public GamePanel(Board board, Coordinate windowSize) {
        //  _board = boardSize;

        _board = board;

        _window = windowSize;

        addKeyListener(this);

        setFocusable(true);
    }

    /**
     * Configures the key bindings for the GUI.
     *
     * @param  up  The "move up" key binding.
     * @param  left  The "move left" key binding.
     * @param  down  The "move down" key binding.
     * @param  right  The "move right" key binding.
     * @param  eagle  The "launch eagle" key binding.
     */

    public void setKeyBindings(String up, String left, String down, String right, String eagle) {
        _upBinding = up;
        _leftBinding = left;
        _downBinding = down;
        _rightBinding = right;
        _eagleBinding = eagle;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int sepX = _window.x / _board.getWidth();
        int sepY = _window.y / _board.getHeight();

        for (int i = 1; i <= _board.getWidth(); i++) {
            g.drawLine(sepX * i, 0, sepX * i, _window.y);
            g.drawLine(0, sepY * i, _window.x, sepY * i);
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
                        File hero = new File(((Hero) pc).getHasItem() ? "SuperTeemo.png" : "teemo.png");

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
                    } else if (pc instanceof Eagle) {
                        File img;

                        if (!((Eagle) pc).getOnGround())
                            img = new File(((Eagle) pc).getHasItem() ? "PidgeySword.png" : "pidgey.gif");
                        else
                            img = new File(((Eagle) pc).getHasItem() ? "HootHootSword.png" : "HootHoot.png");

                        try {
                            BufferedImage egl = ImageIO.read(img);

                            g.drawImage(egl, 0 + sepX * j, 0 + sepY * i, sepX, sepY, null);
                        } catch (Exception e) {

                        }
                    } else if (pc instanceof Dragon) {
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

        synchronized (this) {   //  For safety.
            if (!_presentedModalState) {
                if (_board.getGameState() == Board.State.WON)
                    JOptionPane.showMessageDialog(null, "Congratulations!", "You won!", JOptionPane.INFORMATION_MESSAGE);
                else if (_board.getGameState() == Board.State.LOST)
                    JOptionPane.showMessageDialog(null, "Sorry...", "You lost.", JOptionPane.INFORMATION_MESSAGE);
                else
                    return;

                _presentedModalState = true;
            }
        }
    }

    //
    //  Key Listener Implementation
    //

    public void keyTyped(KeyEvent e) {
        //  Do nothing.
    }

    public void keyPressed(KeyEvent e) {
        String keyText = java.awt.event.KeyEvent.getKeyText(e.getKeyCode());

        try {
            Hero hero = (Hero) _board.getPiecesWithType(Board.Type.HERO).get(0);

            if (keyText.equalsIgnoreCase(_upBinding)) {
                hero.move(Board.Direction.UP);
            } else if (keyText.equalsIgnoreCase(_leftBinding)) {
                hero.move(Board.Direction.LEFT);
            } else if (keyText.equalsIgnoreCase(_downBinding)) {
                hero.move(Board.Direction.DOWN);
            } else if (keyText.equalsIgnoreCase(_rightBinding)) {
                hero.move(Board.Direction.RIGHT);
            } else if (keyText.equalsIgnoreCase(_eagleBinding)) {
                if (_egl == null) {
                    Coordinate crd = hero.getCoordinate();

                    _egl = new Eagle(false, new Coordinate(crd.x, crd.y));

                    _board.setEagle(_egl);
                    _egl.setBoard(_board);

                    ArrayList<Piece> blankPieces = _board.getPiecesWithType(Board.Type.BLANK);

                    if (_swordPiece == null)
                        for (Piece pc : blankPieces)
                            if (((Blank) pc).getHasItem())
                                _swordPiece = (Blank) pc;
                }
            }
        } catch (Exception exc) {

        }

        try {
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
        } catch (Exception exc) {

        }

        try {
            _board.moveDragon();
        } catch (Exception exc) {

        }

        try {
            _board.checkDragon();
        } catch (Exception exc) {

        }

        try {
            _board.recheckGameState();
        } catch (Exception exc) {

        }

        repaint();
        revalidate();
    }

    public void keyReleased(KeyEvent e) {

    }
}
