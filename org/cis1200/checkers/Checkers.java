package org.cis1200.checkers;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * CIS 120 HW09 - Checkers Demo
 * Created by Franci Branda-Chen in Fall 2022.
 */

/*
 * INVARIANTS: 
 * Cannot capture enemy soldiers backwards 
 * (RED cannot capture below, BLACK cannot capture above)
 * Player is OBLIGATED to capture an enermy soldier if they are able to.
 * If the Player starts an attack with one soldier, they must continue 
 * with the same soldier until it can't attack anymore.
 * A soldier becomes KING when it reaches the furthest row on the enemy side
 * A KING can move one block back/forward diagonally.
 * A KING can attack back/forward diagonally.
 */
public class Checkers {

    private int[][] board;
    private boolean player1;
    private boolean gameOver;
    private int numBlack;
    private int numRed;

    private int moves;
    private Map<Integer, List<String>> history;
    private static String gameStateFile = "org/cis1200/checkers/files/gameState.csv";

    /*
     * Constructor
     */
    public Checkers() {
        readFromFile();
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        resetFile();
        readFromFile();
    }

    /*
     * READ FROM GAME STATE FILE
     */
    public void readFromFile() {
        FileEditorCheckers gameState = new FileEditorCheckers(gameStateFile);
        player1 = gameState.next().equals("TRUE");
        gameOver = gameState.next().equals("TRUE");
        numBlack = Integer.valueOf(gameState.next());
        numRed = Integer.valueOf(gameState.next());
        board = new int[8][];
        int count = 0;
        while (gameState.hasNext()) {
            board[count] = FileEditorCheckers.extractBoardRow(gameState.next());
            count++;
        }
        moves = 0;
        history = new TreeMap<>();
        history.put(moves, getCurrentFields());
    }

    /*
     * resets the game state file to its default starting mode
     */
    private void resetFile() {
        FileEditorCheckers gameState = new FileEditorCheckers(gameStateFile);
        List<String> fields = new LinkedList<>();
        fields.add("TRUE");
        fields.add("FALSE");
        fields.add("12");
        fields.add("12");
        board = new int[8][8];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (i < 3) {
                    if (j % 2 == 1 && i % 2 == 0) {
                        board[i][j] = 2; // 2 == black soldier
                    } else if (j % 2 == 0 && i % 2 == 1) {
                        board[i][j] = 2;

                    } 
                } else if (i >= 5) {
                    if (j % 2 == 1 && i % 2 == 0) {
                        board[i][j] = 1; // 1 == red soldier
                    } else if (j % 2 == 0 && i % 2 == 1) {
                        board[i][j] = 1;
                    }
                }     
            }
        }
        for (int i = 0; i < board.length; i++) {
            fields.add(FileEditorCheckers.turnRowToString(board[i]));
        }
        moves = 0;
        history = new TreeMap<>();
        history.put(moves, fields);
        gameState.writeStateToFile(fields, gameStateFile, false);
    }

    /*
     * Saves the current game state fields in the game state file
     */
    public void saveGame() {
        FileEditorCheckers gameState = new FileEditorCheckers(gameStateFile);
        List<String> fields = getCurrentFields();
        gameState.writeStateToFile(fields, gameStateFile, false);
    }

    /*
     * adds a move to the history
     */
    public void updateHistory() {
        moves++;
        history.put(moves, getCurrentFields());
    }

    /*
     * removes a move from the history and sets the game state
     * to the previous move
     */
    public void undo() {
        history.remove(moves); //removes the current state
        moves--; //moves down one previous state
        if (moves < 0) {
            moves = 0;
            history.put(moves, getCurrentFields());
            return;
        }
        List<String> fields = history.get(moves);
        Iterator<String> it = fields.iterator();
        player1 = it.next().equals("TRUE");
        gameOver = it.next().equals("TRUE");
        numBlack = Integer.valueOf(it.next());
        numRed = Integer.valueOf(it.next());
        int count = 0;
        while (it.hasNext()) {
            board[count] = FileEditorCheckers.extractBoardRow(it.next());
            count++;
        }
        history.put(moves, getCurrentFields());
    }

    /*
     * gets the current game state fields
     */
    private List<String> getCurrentFields() {
        List<String> fields = new LinkedList<>();
        if (player1) {
            fields.add("TRUE");
        } else {
            fields.add("FALSE");
        }
        if (gameOver) {
            fields.add("TRUE");
        } else {
            fields.add("FALSE");
        }
        fields.add(String.valueOf(numBlack));
        fields.add(String.valueOf(numRed));
        for (int i = 0; i < board.length; i++) {
            fields.add(FileEditorCheckers.turnRowToString(board[i]));
        }
        return fields;
    }

    /*
     * changes gameStateFile for testing purposes
     */
    public void changeGameFile(String filePath) {
        gameStateFile = filePath;
    }
    /*
     * setters for testing purposes
     */
    public void setNumRed(int a) {
        this.numRed = a;
    }

    public void setNumBlack(int a) {
        this.numBlack = a;
    }

    public void setCurrPlayer(boolean a) {
        this.player1 = a;
    }

    public int getNumRed() {
        return numRed;
    }

    public int getNumBlack() {
        return numBlack;
    }


    /*************************** GAME FUNCTIONALITY METHODS ***************************/

    /*
     * cell getter from board
     */
    public int getCell(int r, int c) {
        return board[r][c];
    }

    /*
     * sets a cell in the board into input val. Only used when an action is performed
     */
    public void setCell(int r, int c, int val) {
        board[r][c] = val;
        turnKing(r, c); // checks if a soldier can be turned into a king
    }

    public boolean getCurrentPlayer() {
        return player1;
    }

    /*
     * helper. Checks a cell's neighbor on top right
     * returns -1 if on edge.
     * returns 0 if empty cell
     * returns 1 if (red soldier) and 2 (black soldier)
     */
    private int neighborTopRight(int r, int c) {
        if (r == 0 || c == 7) {
            return -1;
        } else {
            return board[r - 1][c + 1];
        }
    }

    private int neighborTopLeft(int r, int c) {
        if (r == 0 || c == 0) {
            return -1;
        } else {
            return board[r - 1][c - 1];
        }
    }

    private int neighborBotRight(int r, int c) {
        if (r == 7 || c == 7) {
            return -1;
        } else {
            return board[r + 1][c + 1];
        }
    }

    private int neighborBotLeft(int r, int c) {
        if (r == 7 || c == 0) {
            return -1;
        } else {
            return board[r + 1][c - 1];
        }
    }


    /*
     * checks if a given soldier can move. Must be applied on a cell where it is
     * known that there is a soldier
     */
    public boolean canMove(int r, int c) {
        if (getCell(r, c) == 1) {
            return (neighborTopRight(r, c) == 0 || neighborTopLeft(r, c) == 0);
        } else if (getCell(r, c) == 2) {
            return (neighborBotRight(r, c) == 0 || neighborBotLeft(r, c) == 0);
        } else if (getCell(r, c) == 7 || getCell(r, c) == 8) {
            return (neighborTopRight(r, c) == 0 || neighborTopLeft(r, c) == 0 || 
                    neighborBotRight(r, c) == 0 || neighborBotLeft(r, c) == 0);
        }
        return false;
    }


    /*
     * helper. checks one block diagonal bound
     */
    public boolean boundCheck(int oldR, int oldC, int newR, int newC) {
        if (getCell(oldR, oldC) == 1) {
            return ((oldR - 1 == newR) &&
                    (oldC + 1 == newC || oldC - 1 == newC));
        } else if (getCell(oldR, oldC) == 2) {
            return ((oldR + 1 == newR) &&
                    (oldC + 1 == newC || oldC - 1 == newC));
        } else if (getCell(oldR, oldC) == 7 || getCell(oldR, oldC) == 8) {
            return ((oldR - 1 == newR || oldR + 1 == newR) && 
                    ((oldC + 1 == newC || oldC - 1 == newC)));
        }
        return false;
    }

    /*
     * checks if at least one soldier of either side can attack
     */
    public boolean canMoveOverall() {
        if (player1) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if ((j % 2 == 1 && i % 2 == 0) || (j % 2 == 0 && i % 2 == 1)) {
                        if ((getCell(i, j) == 1 || getCell(i, j) == 7) && canMove(i, j)) {
                            return true; 
                        }
                    } 
                }
            }
        } else {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if ((j % 2 == 1 && i % 2 == 0) || (j % 2 == 0 && i % 2 == 1)) {
                        if ((getCell(i, j) == 2 || getCell(i, j) == 8)  && canMove(i, j)) {
                            return true; 
                        }
                    } 
                }
            }
        }
        return false;
    }

    /*
     * checks if at least one soldier of either side can attack
     */
    public boolean canAttackOverall() {
        if (player1) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if ((j % 2 == 1 && i % 2 == 0) || (j % 2 == 0 && i % 2 == 1)) {
                        if ((getCell(i, j) == 1 || getCell(i, j) == 7) && canAttack(i, j)) {
                            return true; 
                        }
                    } 
                }
            }
        } else {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if ((j % 2 == 1 && i % 2 == 0) || (j % 2 == 0 && i % 2 == 1)) {
                        if ((getCell(i, j) == 2 || getCell(i, j) == 8) && canAttack(i, j)) {
                            return true;  
                        }
                    } 
                }
            }
        }
        return false;
    }

    /*
     * moves a soldier. Cannot move at least one soldier it is able to attack.
     */
    public boolean move(int oldR, int oldC, int newR, int newC) {
        if (!canMove(oldR, oldC) || gameOver) {
            return false;
        } else if (canAttackOverall()) {
            return false;
        }

        if (player1) {
            if (!boundCheck(oldR, oldC, newR, newC)) {
                return false;
            }
            if (getCell(oldR, oldC) == 1 && getCell(newR, newC) == 0) {
                setCell(oldR, oldC, 0);
                setCell(newR, newC, 1);
                player1 = false;
                turnKing(newR, newC);
                updateHistory();
                return true;
            } else if (getCell(oldR, oldC) == 7 && getCell(newR, newC) == 0) {
                setCell(oldR, oldC, 0);
                setCell(newR, newC, 7);
                player1 = false;
                updateHistory();
                return true;
            }
        } else {
            if (!boundCheck(oldR, oldC, newR, newC)) {
                return false;
            }
            if (getCell(oldR, oldC) == 2 && getCell(newR, newC) == 0) {
                setCell(oldR, oldC, 0);
                setCell(newR, newC, 2);
                player1 = true;
                updateHistory();
                turnKing(newR, newC);
                return true;
            } else if (getCell(oldR, oldC) == 8 && getCell(newR, newC) == 0) {
                setCell(oldR, oldC, 0);
                setCell(newR, newC, 8);
                player1 = true;
                updateHistory();
                return true;
            }
        }
        return false;
    }



    /*
     * attack helpers
     */
    private boolean canAttackTopRight(int r, int c, int target) {
        if (neighborTopRight(r, c) == target) {
            return neighborTopRight(r - 1, c + 1) == 0;
        }
        return false;
    }

    private boolean canAttackTopLeft(int r, int c, int target) {
        if (neighborTopLeft(r, c) == target) {
            return neighborTopLeft(r - 1, c - 1) == 0;
        }
        return false;
    }

    private boolean canAttackBotRight(int r, int c, int target) {
        if (neighborBotRight(r, c) == target) {
            return neighborBotRight(r + 1, c + 1) == 0;
        }
        return false;
    }

    private boolean canAttackBotLeft(int r, int c, int target) {
        if (neighborBotLeft(r, c) == target) {
            return neighborBotLeft(r + 1, c - 1) == 0;
        }
        return false;
    }

    private boolean attackRangeHelper(int r, int c, boolean fun1, boolean fun2) {
        if (c == 0 || c == 1) {
            return fun1;
        } else if (c == 6 || c == 7) {
            return fun2;
        } else {
            return (fun1 || fun2);
        }
    }

    /*
     * checks if a given soldier can attack. Must be applied on a cell where it is
     * known that there is a soldier
     */
    public boolean canAttack(int r, int c) {
        boolean canAtt = false;
        switch (getCell(r, c)) {
            case 1: 
                if (r <= 1) {
                    return false;
                }
                canAtt = attackRangeHelper(r, c, 
                        (canAttackTopRight(r, c, 2) || canAttackTopRight(r, c, 8)),
                        (canAttackTopLeft(r, c, 2) || canAttackTopLeft(r, c, 8)));
                break;
            case 2: 
                if (r >= 6) {
                    return false;
                }
                canAtt = attackRangeHelper(r, c, 
                        (canAttackBotRight(r, c, 1) || canAttackBotRight(r, c, 7)),
                        (canAttackBotLeft(r, c, 1) || canAttackBotLeft(r, c, 7)));
                break;
            case 7: 
                canAtt = attackRangeHelper(r, c, 
                        (canAttackTopRight(r, c, 2) || canAttackTopRight(r, c, 8) || 
                                canAttackBotRight(r, c, 2) || canAttackBotRight(r, c, 8)),
                        (canAttackTopLeft(r, c, 2) || canAttackTopLeft(r, c, 8) ||
                                canAttackBotLeft(r, c, 2) || canAttackBotLeft(r, c, 8)));
                break;    
            case 8:
                canAtt = attackRangeHelper(r, c, 
                        (canAttackTopRight(r, c, 1) || canAttackTopRight(r, c, 7) || 
                                canAttackBotRight(r, c, 1) || canAttackBotRight(r, c, 7)),
                        (canAttackTopLeft(r, c, 1) || canAttackTopLeft(r, c, 7) ||
                                canAttackBotLeft(r, c, 1) || canAttackBotLeft(r, c, 7)));
                break;
            default :
                break;
        }
        return canAtt;
    }
    
    /*
     * kills an enemy soldier and moves soldier to its new cell
     */
    private boolean kill(int oldR, int oldC, int enemyR, int enemyC) {
        if ((getCell(oldR, oldC) == 1 || getCell(oldR, oldC) == 7) &&
                (getCell(enemyR, enemyC) == 2 || getCell(enemyR, enemyC) == 8) && player1) {
            int soldier = getCell(oldR, oldC);
            setCell(oldR, oldC, 0);
            setCell(enemyR, enemyC, 0);
            if (oldC > enemyC) {
                enemyC--;
            } else {
                enemyC++;
            }
            if (oldR > enemyR) {
                enemyR--;
            } else {
                enemyR++;
            }
            setCell(enemyR, enemyC, soldier);
            numBlack--;
            player1 = canAttack(enemyR, enemyC); //if it can still attack, must keep attacking
            updateHistory();
            return true;
        } else if ((getCell(oldR, oldC) == 2 || getCell(oldR, oldC) == 8) &&
                (getCell(enemyR, enemyC) == 1 || getCell(enemyR, enemyC) == 7) && !player1) {
            int soldier = getCell(oldR, oldC);
            setCell(oldR, oldC, 0);
            setCell(enemyR, enemyC, 0);
            if (oldC > enemyC) {
                enemyC--;
            } else {
                enemyC++;
            }
            if (oldR > enemyR) {
                enemyR--;
            } else {
                enemyR++;
            }
            setCell(enemyR, enemyC, soldier);
            numRed--;
            player1 = !canAttack(enemyR, enemyC); // if black can keep attacking, NOT player1's turn
            updateHistory();
            return true;
        }
        return false;
    }

    /*
     * soldier attacks and moves to a new cell
     */
    public boolean attack(int oldR, int oldC, int enemyR, int enemyC) {
        if (!canAttack(oldR, oldC) || gameOver) {
            return false;
        }
        if (!boundCheck(oldR, oldC, enemyR, enemyC)) {
            return false;
        }
        if (kill(oldR, oldC, enemyR, enemyC)) {
            return true;
        }
        return false;
    }

    /*
     * turns soldier into King when they reach enemy furthest row
     */
    public boolean turnKing(int oldR, int oldC) {
        if (getCell(oldR, oldC) == 1 && oldR == 0) {
            setCell(oldR, oldC, 7);
            return true;
        } else if (getCell(oldR, oldC) == 2 && oldR == 7) {
            setCell(oldR, oldC, 8);
            return true;
        }
        return false;
    }

    /*
     * returns 1 if there are no black pieces (player one wins)
     * returns 2 if there are no red pieces (player two wins)
     * returns 3 if it's a draw (either player is unable to move/attack)
     * returns 0 ongoing
     */
    public int checkWinner() {
        if (numBlack == 0) {
            gameOver = true;
            return 1;
        } else if (numRed == 0) {
            gameOver = true;
            return 2;
        } else if (!canAttackOverall() && !canMoveOverall()) {
            gameOver = true;
            return 3;
        } else {
            return 0;
        }
    }

    public void printGameState() {
        if (player1) {
            System.out.println("\nRED's turn");
        } else {
            System.out.println("\nBLACK's turn");
        }
        System.out.println("---------" +
                "----------------------");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
                System.out.print(" | ");
            }
            System.out.println("\n---------" +
                    "----------------------");
        }
        System.out.println();
    }


    public static void main(String[] args) {
        Checkers ch = new Checkers();
        ch.printGameState();
        ch.move(5, 6, 4, 7);
        ch.printGameState();
        ch.move(4, 7, 3, 6); // try to move red again
        ch.printGameState();
        ch.move(2, 5, 3, 6);
        ch.printGameState();
        ch.move(5, 4, 4, 3); // try to move red instead of attack
        ch.printGameState();
        ch.attack(4, 7, 3, 6);
        ch.printGameState();
        ch.move(2, 1, 3, 0); // try to move black instead of attack
        ch.printGameState();
        ch.attack(1, 4, 2, 5);
        ch.printGameState();
    }
}
