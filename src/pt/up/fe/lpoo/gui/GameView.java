/**
 * Labyrinth
 *
 * Created by Eduardo Almeida and Joao Almeida.
 */

package pt.up.fe.lpoo.gui;

import pt.up.fe.lpoo.logic.Board;
import pt.up.fe.lpoo.logic.BoardGenerator;
import pt.up.fe.lpoo.logic.Coordinate;
import pt.up.fe.lpoo.logic.GameDataManager;
import pt.up.fe.lpoo.logic.piece.Piece;
import pt.up.fe.lpoo.logic.piece.itemizable.Dragon;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class GameView {
    static private JFrame frame;

    static private GamePanel gp;

    static private Board brd;

    static private int boardX = 10;
    static private int boardY = 10;
    static private int dragonsNr = 1;
    static private boolean dragonsSleep = true;

    static private String upBinding = "up";
    static private String leftBinding = "left";
    static private String downBinding = "down";
    static private String rightBinding = "right";
    static private String eagleBinding = "e";

    /**
     * The entry point for the GUI part of the application.
     *
     * @param  args  The arguments passed to the application by the OS.
     */

    public static void main(String[] args) {
        try {
            brd = new Board();

            BoardGenerator gen = new BoardGenerator(10, 10, 1);

            ArrayList<Piece> ooBoard = gen.generateBoard();

            for (Piece pc : ooBoard)
                pc.setBoard(brd);

            brd.setBoardPieces(ooBoard);
            brd.setWidth(10);
            brd.setHeight(10);

            Dragon drag = (Dragon) brd.getPiecesWithType(Board.Type.DRAGON).get(0);

            drag.setBehavior(Dragon.Behavior.NO_SLEEP);

            frame = new JFrame("Labyrinth Game View");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setPreferredSize(new Dimension(500, 600));
            frame.getContentPane().setLayout(new BorderLayout());

            gp = new GamePanel(brd, new Coordinate(500, 500));

            frame.getContentPane().add(gp, BorderLayout.CENTER);

            frame.setFocusable(false);

            JPanel newPanel = new JPanel(new FlowLayout());

            newPanel.setFocusable(false);

            JButton restartGameButton = new JButton("Restart");
            restartGameButton.setPreferredSize(new Dimension(100, 35));
            restartGameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    newGame();
                }
            });

            restartGameButton.setFocusable(false);

            newPanel.add(restartGameButton);

            JButton settingsButton = new JButton("Settings");
            settingsButton.setPreferredSize(new Dimension(100, 35));
            settingsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    labyrinthConfigurationDialog();
                }
            });

            settingsButton.setFocusable(false);

            newPanel.add(settingsButton);

            final FileNameExtensionFilter extension = new FileNameExtensionFilter("Labyrinth Game State (*.lpoo)", "lpoo");

            JButton loadButton = new JButton("Load");
            loadButton.setPreferredSize(new Dimension(75, 35));
            loadButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    final JFileChooser loadChooser = new JFileChooser();

                    loadChooser.setAcceptAllFileFilterUsed(false);
                    loadChooser.setMultiSelectionEnabled(false);

                    loadChooser.setFileFilter(extension);

                    int returnVal = loadChooser.showOpenDialog(frame);

                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = loadChooser.getSelectedFile();
                        System.out.println("Opening: " + file.getName() + ".");

                        try {
                            loadGame(GameDataManager.loadGameState(file.getAbsolutePath()));
                        } catch (Exception exc) {

                        }
                    } else {
                        System.out.println("Open command cancelled by user.");
                    }
                }
            });

            loadButton.setFocusable(false);

            newPanel.add(loadButton);

            JButton saveButton = new JButton("Save");
            saveButton.setPreferredSize(new Dimension(75, 35));
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    final JFileChooser saveChooser = new JFileChooser();

                    saveChooser.setAcceptAllFileFilterUsed(false);
                    saveChooser.setMultiSelectionEnabled(false);

                    saveChooser.setFileFilter(extension);

                    int returnVal = saveChooser.showSaveDialog(frame);

                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        try {
                            String path = saveChooser.getSelectedFile().getAbsolutePath();

                            if (!path.contains(".lpoo"))
                                path += ".lpoo";

                            GameDataManager.saveGameState(brd, path);
                        } catch (Exception exc) {
                            System.out.println("Err!");
                        }
                    } else {
                        System.out.println("Save command cancelled by user.");
                    }
                }
            });

            saveButton.setFocusable(false);

            newPanel.add(saveButton);

            JButton lbButton = new JButton("Level Builder");
            lbButton.setPreferredSize(new Dimension(100, 35));
            lbButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    levelBuilderDialog();
                }
            });

            lbButton.setFocusable(false);

            newPanel.add(lbButton);

            frame.getContentPane().add(newPanel, BorderLayout.SOUTH);

            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {

        }
    }

    /**
     * Spawns a new game.
     */

    public static void newGame() {
        try {
            Board newBoard = new Board();

            BoardGenerator gen = new BoardGenerator(boardX, boardY, dragonsNr);

            ArrayList<Piece> ooBoard = gen.generateBoard();

            for (Piece pc : ooBoard)
                pc.setBoard(newBoard);

            newBoard.setBoardPieces(ooBoard);
            newBoard.setWidth(boardX);
            newBoard.setHeight(boardY);

            ArrayList<Piece> drag = newBoard.getPiecesWithType(Board.Type.DRAGON);

            for (Piece d : drag)
                ((Dragon) d).setBehavior(dragonsSleep ? Dragon.Behavior.SLEEP : Dragon.Behavior.NO_SLEEP);

            frame.getContentPane().remove(gp);

            gp = new GamePanel(newBoard, new Coordinate(50 * boardX, 50 * boardY));

            frame.getContentPane().add(gp, BorderLayout.CENTER);

            frame.repaint();
            frame.revalidate();

            gp.setFocusable(true);

            frame.setSize(50 * boardX, 50 * boardY + 100);
        } catch (Exception exc) {

        }
    }

    /**
     * Loads a previously saved game.
     *
     * @param theBoard  The board to load.
     */

    public static void loadGame(Board theBoard) {
        try {
            brd = theBoard;

            frame.getContentPane().remove(gp);

            gp = new GamePanel(theBoard, new Coordinate(50 * theBoard.getWidth(), 50 * theBoard.getHeight()));

            frame.getContentPane().add(gp, BorderLayout.CENTER);

            frame.repaint();
            frame.revalidate();

            gp.setFocusable(true);

            frame.setSize(50 * theBoard.getWidth(), 50 * theBoard.getHeight() + 100);
        } catch (Exception exc) {

        }
    }

    /**
     * Spawns a level builder dialog box.
     */

    public static void levelBuilderDialog() {
        JTextField xField = new JTextField(Integer.toString(boardX));
        JTextField yField = new JTextField(Integer.toString(boardY));

        JPanel myPanel = new JPanel();

        myPanel.setLayout(new GridLayout(4, 2));

        myPanel.add(new JLabel("New Board Settings"));
        myPanel.add(Box.createHorizontalStrut(1));

        myPanel.add(Box.createHorizontalStrut(1));
        myPanel.add(Box.createHorizontalStrut(1));

        myPanel.add(new JLabel("Board X:"));
        myPanel.add(xField);

        myPanel.add(new JLabel("Board Y:"));
        myPanel.add(yField);

        int result = JOptionPane.showConfirmDialog(null, myPanel, "Labyrinth Configuration", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            if (Integer.parseInt(xField.getText()) < 5 || Integer.parseInt(yField.getText()) < 5) {
                JOptionPane.showMessageDialog(null, "Validation Error: Both the X and Y axis sizes of the board need to be equal to or bigger than 5.", "Board Error", JOptionPane.INFORMATION_MESSAGE);

                return;
            }

            LabyrinthBuilderWindow lb = new LabyrinthBuilderWindow();

            lb.spawnNewBuilder(Integer.parseInt(xField.getText()), Integer.parseInt(yField.getText()));
        }
    }

    /**
     * Spawns a labyrinth configuration dialog box.
     */

    public static void labyrinthConfigurationDialog() {
        JTextField xField = new JTextField(Integer.toString(boardX));
        JTextField yField = new JTextField(Integer.toString(boardY));

        JTextField dragField = new JTextField(Integer.toString(dragonsNr));

        JCheckBox shouldSleep = new JCheckBox("Yes", dragonsSleep);

        JTextField upKeyBinding = new JTextField(upBinding);
        JTextField downKeyBinding = new JTextField(downBinding);
        JTextField leftKeyBinding = new JTextField(leftBinding);
        JTextField rightKeyBinding = new JTextField(rightBinding);
        JTextField eagleKeyBinding = new JTextField(eagleBinding);

        JPanel myPanel = new JPanel();

        myPanel.setLayout(new GridLayout(14, 2));

        myPanel.add(new JLabel("Board Settings"));
        myPanel.add(Box.createHorizontalStrut(1));

        myPanel.add(Box.createHorizontalStrut(1));
        myPanel.add(Box.createHorizontalStrut(1));

        myPanel.add(new JLabel("Board X:"));
        myPanel.add(xField);

        myPanel.add(new JLabel("Board Y:"));
        myPanel.add(yField);

        myPanel.add(new JLabel("Dragons Nr:"));
        myPanel.add(dragField);

        myPanel.add(new JLabel("Dragons Sleep?"));
        myPanel.add(shouldSleep);

        myPanel.add(Box.createHorizontalStrut(1));
        myPanel.add(Box.createHorizontalStrut(1));

        myPanel.add(new JLabel("Key Bindings"));
        myPanel.add(Box.createHorizontalStrut(1));

        myPanel.add(Box.createHorizontalStrut(1));
        myPanel.add(Box.createHorizontalStrut(1));

        myPanel.add(new JLabel("Up:"));
        myPanel.add(upKeyBinding);

        myPanel.add(new JLabel("Left:"));
        myPanel.add(leftKeyBinding);

        myPanel.add(new JLabel("Down:"));
        myPanel.add(downKeyBinding);

        myPanel.add(new JLabel("Right:"));
        myPanel.add(rightKeyBinding);

        myPanel.add(new JLabel("Eagle:"));
        myPanel.add(eagleKeyBinding);

        int result = JOptionPane.showConfirmDialog(null, myPanel, "Labyrinth Configuration", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            if (Integer.parseInt(xField.getText()) < 5 || Integer.parseInt(yField.getText()) < 5) {
                JOptionPane.showMessageDialog(null, "Validation Error: Both the X and Y axis sizes of the board need to be equal to or bigger than 5.", "Board Error", JOptionPane.INFORMATION_MESSAGE);

                return;
            }

            boardX = Integer.parseInt(xField.getText());
            boardY = Integer.parseInt(yField.getText());
            dragonsNr = Integer.parseInt(dragField.getText());
            dragonsSleep = shouldSleep.isSelected();

            upBinding = upKeyBinding.getText();
            leftBinding = leftKeyBinding.getText();
            downBinding = downKeyBinding.getText();
            rightBinding = rightKeyBinding.getText();
            eagleBinding = eagleKeyBinding.getText();

            gp.setKeyBindings(upBinding, leftBinding, downBinding, rightBinding, eagleBinding);
        }
    }
}
