package org.game.checkers;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.game.GameBoard;


public class RunCheckers implements Runnable {
    public void run() {
        final JFrame frame = new JFrame("Checkers");
        frame.setLocation(950, 0);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        final JLabel status = new JLabel("Please select one");
        //game
        GameBoard grid = new GameBoard(status);

        //INFORMATIONAL SCREEN
        InfoScreen info = new InfoScreen();
        //Instructions for pop-up window
        String instructions = "CHECKERS! capture all enemy soldiers or lose all of yours\n"
                + "Press Start New Game to start a new game from zero."
                + "This will delete any previous saved game\n"
                + "Press Continue to continue a previous saved game\n"
                + "Press Save to save a game\n"
                + "Press Undo to undo a movement\n"
                + "\n"
                + "Rules:\n"
                + "Soldiers can ONLY move in one"
                + "direction (Red up, Black down), one cell diagonally\n"
                + "Soldiers can ONLY capture in one "
                + "direction (Red up, Black down), one cell diagonally\n"
                + "Soldiers become Kings when they reach the "
                + "furthest row of the direction they are\n"
                + "going.\n"
                + "Kings can move and capture both forward/backwards "
                + "diagonally within a one-cell range\n"
                + "If player can capture an enemy soldier, the "
                + "player MUST capture (can choose which\n"
                + "soldier can capture if multiple soldiers are able to)\n"
                + "\n"
                + "Controls:\n"
                + "MOVE: click on ally soldier, then click on empty cell\n"
                + "CAPTURE: click on soldier doing the capturing, then\n"
                + "click on the cell where the enemy is.\n"
                + "CONSECUTIVE CAPTURES: click ally soldier, then the \n"
                + "enemy soldier per capture.\n"
                + "\n"
                + "***NEW FEATURE: DIVIDE and CONQUER - Player is able to "
                + "either do consecutive\n"
                + "captures with one piece, or choose to split the attacks "
                + "by initiating with the soldier who \n"
                + "could do the consecutive attacks, and then attack "
                + "with the other ally piece.";

        //CONTROl BUTTONS
        JButton newGame = new JButton("Start New Game");
        JButton load = new JButton("Continue Game");
        JButton save = new JButton("Save");
        JButton quit = new JButton("Quit");
        JButton undo = new JButton("Undo move");
        JButton instr = new JButton("Instructions");

        JPanel controlButtons = new JPanel();
        controlButtons.setLayout(new GridLayout(3, 2));
        controlButtons.add(newGame);
        controlButtons.add(load);
        controlButtons.add(instr);
        controlButtons.add(quit);
        frame.add(controlButtons, BorderLayout.NORTH);
        frame.add(info, BorderLayout.CENTER);

        newGame.addActionListener(e -> {
            grid.reset();
            frame.add(grid, BorderLayout.CENTER);
            frame.remove(info);
            controlButtons.add(save);
            controlButtons.add(undo);
        });

        load.addActionListener(e -> {
            grid.load();
            frame.add(grid, BorderLayout.CENTER);
            frame.remove(info);
            controlButtons.add(save);
            controlButtons.add(undo);
        });

        save.addActionListener(e -> {
            grid.save();
        });

        undo.addActionListener(e -> {
            grid.undo();
        });

        instr.addActionListener(e -> 
            JOptionPane.showMessageDialog(null, instructions, "Instructions", 1));

        quit.addActionListener(e -> System.exit(0));

        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        status_panel.add(status);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @SuppressWarnings("serial")
    class InfoScreen extends JPanel {
        private static BufferedImage img;
        public static final String IMG_FILE = "org/game/checkers/files/frontPage.png";

        public InfoScreen() {
            try {
                if (img == null) {
                    img = ImageIO.read(new File(IMG_FILE));
                }
            } catch (IOException e) {
                System.out.println("Internal Error:" + e.getMessage());
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, 600, 600, null);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(600, 600);
        }
    }
}
