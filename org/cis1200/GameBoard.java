package org.cis1200;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.cis1200.checkers.Checkers;

/*
 * INVARIANTS:
 * In order to capture an enemy, select first the ally soldier
 * doing the capturing, then click on the cell where the enemy soldier is.
 */

@SuppressWarnings("serial")
public class GameBoard extends JPanel {
    class Cell extends JPanel {
        private int soldier;
        private final int row;
        private final int col;
        private static int[] currSoldier = {0, 0, 0};


        public Cell(int r, int c, int a, Color bckground) {
            this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            this.soldier = a;
            this.setBackground(bckground);
            this.row = r;
            this.col = c;
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (soldier == 0 && currSoldier[2] != 0) {
                        ch.move(currSoldier[0], currSoldier[1], getRow(), getCol());
                        resetCurrSoldier();
                    } else if ((soldier == 1 || soldier == 7) && 
                            (currSoldier[2] == 2 || currSoldier[2] == 8)) {
                        ch.attack(currSoldier[0], currSoldier[1], getRow(), getCol());
                        resetCurrSoldier();
                    } else if ((soldier == 2 || soldier == 8) && 
                            (currSoldier[2] == 1 || currSoldier[2] == 7)) {
                        ch.attack(currSoldier[0], currSoldier[1], getRow(), getCol());
                        resetCurrSoldier();
                    } else if (soldier != 0 && (currSoldier[2] == 0)) {
                        currSoldier[0] = getRow();
                        currSoldier[1] = getCol();
                        currSoldier[2] = getSoldier();
                        return;
                    } else if (((soldier == 1 || soldier == 7) &&
                            (currSoldier[2] == 1 || currSoldier[2] == 7)) ||
                            ((soldier == 2 || soldier == 8) &&
                                    (currSoldier[2] == 2 || currSoldier[2] == 8))) {
                        currSoldier[0] = getRow();
                        currSoldier[1] = getCol();
                        currSoldier[2] = getSoldier();
                        return;
                    }
                    updateStatus();
                }
            });
        }

        private int getRow() {
            return row;
        }

        private int getCol() {
            return col;
        }

        private int getSoldier() {
            return soldier;
        }

        private void resetCurrSoldier() {
            currSoldier[0] = 0;
            currSoldier[1] = 0;
            currSoldier[2] = 0;
        }

        /*
         * draw helper
         */
        private void drawImage(BufferedImage img, String imgFile, Graphics g) {
            try {
                if (img == null) {
                    img = ImageIO.read(new File(imgFile));
                    g.drawImage(img, 7, 7, 60, 60, null);
                }
            } catch (IOException e) {
                System.out.println("Internal Error:" + e.getMessage());
            }
        }

        public void draw(Graphics g) {
            BufferedImage img = null;
            this.soldier = ch.getCell(row, col);
            if (getRow() == currSoldier[0] &&
                    getCol() == currSoldier[1] &&
                    soldier == currSoldier[2] && 
                    currSoldier[2] != 0) {
                g.setColor(Color.GREEN);
                g.drawOval(7, 7, 60, 60);
            }
            if (soldier == 1) {
                String imgFile = "org/cis1200/checkers/files/redSoldier.png";
                drawImage(img, imgFile, g);
            } else if (soldier == 7) {
                String imgFile = "org/cis1200/checkers/files/redKing.png";
                drawImage(img, imgFile, g);
            } else if (soldier == 2) {
                String imgFile = "org/cis1200/checkers/files/blackSoldier.png";
                drawImage(img, imgFile, g);
            } else if (soldier == 8) {
                String imgFile = "org/cis1200/checkers/files/blackKing.png";
                drawImage(img, imgFile, g);
            } else if (soldier == 0) {
                g.setColor(getBackground());
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            draw(g);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(50, 50);
        }

    }

    private Checkers ch; //game
    public static final int BOARD_WIDTH = 600;
    public static final int BOARD_HEIGHT = 600;
    private JLabel status;

    public GameBoard(JLabel statusInit) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setLayout(new GridLayout(8, 8));
        setFocusable(true);
        ch = new Checkers();
        status = statusInit;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                makeCells(i, j, ch.getCell(i, j));
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        repaint();
    }

    /*
     * helper. Adds the cells to the grid/board
     */
    private void makeCells(int r, int c, int val) {
        if (c % 2 == 0 && r % 2 == 1) {
            this.add(new Cell(r, c, val, Color.BLACK));
        } else if (c % 2 == 1 && r % 2 == 0) {
            this.add(new Cell(r, c, val, Color.BLACK));
        } else {
            this.add(new Cell(r, c, 0, Color.RED));
        }
    }

    /**
     * Size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }

    public void reset() {
        ch.reset();
        updateStatus();
        requestFocusInWindow();
    }

    public void load() {
        ch.readFromFile();
        updateStatus();
        requestFocusInWindow();
    }

    public void save() {
        ch.saveGame();
        requestFocusInWindow();
    }

    public void undo() {
        ch.undo();
        updateStatus();
        requestFocusInWindow();
    }


    private void updateStatus() {
        if (ch.getCurrentPlayer()) {
            status.setText("Player 1's Turn " + " | Red Pieces: " + ch.getNumRed() + 
                    " | Black Pieces: " + ch.getNumBlack());
        } else {
            status.setText("Player 2's Turn " + " | Red Pieces: " + ch.getNumRed() + 
                    " | Black Pieces: " + ch.getNumBlack());
        }

        int winner = ch.checkWinner();
        if (winner == 1) {
            status.setText("Player 1 wins!!!");
            JOptionPane.showMessageDialog(this, 
                    "Player 1 Wins!!! Do you want to play again?\n"
                            + "Press Start New Game or Continue Game", "Game Over", 1);
        } else if (winner == 2) {
            status.setText("Player 2 wins!!!");
            JOptionPane.showMessageDialog(this, 
                    "Player 2 Wins!!! Do you want to play again?\n"
                            + "Press Start New Game or Continue Game", "Game Over", 1);
        } else if (winner == 3) {
            status.setText("It's a tie.");
            JOptionPane.showMessageDialog(this, 
                    "It's a tie!! Do you want to play again?\n"
                            + "Press Start New Game or Continue Game", "Game Over", 1);
        }
    }
}
