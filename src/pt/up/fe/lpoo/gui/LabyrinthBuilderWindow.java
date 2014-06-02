/**
 * Labyrinth
 *
 * Created by Eduardo Almeida and Joao Almeida.
 */

package pt.up.fe.lpoo.gui;

import pt.up.fe.lpoo.logic.GameDataManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class LabyrinthBuilderWindow {
    private JFrame _frame;

    private LabyrinthBuilder _lb;

    /**
     * Creates and spawns a new labyrinth builder window.
     *
     * @param x The new labyrinth width.
     * @param y The new labyrinth height.
     */

    public void spawnNewBuilder(int x, int y) {
        _frame = new JFrame("Labyrinth Game View");
        _frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        _frame.setPreferredSize(new Dimension(50 * x, 50 * y + 100));
        _frame.getContentPane().setLayout(new BorderLayout());

        _lb = new LabyrinthBuilder(x, y);

        _frame.getContentPane().add(_lb, BorderLayout.CENTER);

        _frame.setFocusable(false);

        JPanel newPanel = new JPanel(new FlowLayout());

        newPanel.setFocusable(false);

        JButton validateButton = new JButton("Validate Board");

        validateButton.setPreferredSize(new Dimension(160, 35));
        validateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (_lb.validateBoard())
                    JOptionPane.showMessageDialog(null, "Validation Successful!", "Your board is valid.", JOptionPane.INFORMATION_MESSAGE);
                else
                    JOptionPane.showMessageDialog(null, "Validation Failed!", "Your board is invalid.", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        validateButton.setFocusable(false);

        newPanel.add(validateButton);

        JButton saveButton = new JButton("Save Level As...");
        saveButton.setPreferredSize(new Dimension(160, 35));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!_lb.validateBoard())
                    JOptionPane.showMessageDialog(null, "Your board didn't pass validation. You may save the level anyway, but it will probably not work.", "Oops.", JOptionPane.INFORMATION_MESSAGE);

                final FileNameExtensionFilter extension = new FileNameExtensionFilter("Labyrinth Game State (*.lpoo)", "lpoo");

                final JFileChooser saveChooser = new JFileChooser();

                saveChooser.setAcceptAllFileFilterUsed(false);
                saveChooser.setMultiSelectionEnabled(false);

                saveChooser.setFileFilter(extension);

                int returnVal = saveChooser.showSaveDialog(_frame);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        String path = saveChooser.getSelectedFile().getAbsolutePath();

                        if (!path.contains(".lpoo"))
                            path += ".lpoo";

                        GameDataManager.saveGameState(_lb.getBoard(), path);
                    } catch (Exception exc) {
                        System.out.println("Err!");
                    }
                } else {
                    System.out.println("Open command cancelled by user.");
                }
            }
        });

        saveButton.setFocusable(false);

        newPanel.add(saveButton);

        JButton exitButton = new JButton("Exit Builder");
        exitButton.setPreferredSize(new Dimension(160, 35));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _frame.dispatchEvent(new WindowEvent(_frame, WindowEvent.WINDOW_CLOSING));
            }
        });

        exitButton.setFocusable(false);

        newPanel.add(exitButton);

        _frame.getContentPane().add(newPanel, BorderLayout.SOUTH);

        _frame.pack();
        _frame.setVisible(true);
    }
}
