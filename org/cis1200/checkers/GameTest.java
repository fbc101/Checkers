package org.cis1200.checkers;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


public class GameTest {
    private Checkers ch;

    @BeforeEach
    public void setUp() {
        ch = new Checkers();
        ch.changeGameFile("org/cis1200/checkers/files/gameStateTEST3.csv");
        ch.reset();
    }

    private void emptyBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ch.setCell(i, j, 0);
            }
        }
    }

    private boolean moveRed(int r, int c) {
        ch.setCell(r, c, 1); 
        return ch.move(r, c, r - 1, c + 1);
    }

    private boolean moveBlack(int r, int c) {
        ch.setCell(r, c, 2); 
        return ch.move(r, c, r + 1, c + 1);
    }


    @Test
    public void testCanMoveNoOneInFront() {
        assertTrue(ch.canMove(2, 1));
        assertTrue(ch.canMove(2, 3));
        assertTrue(ch.canMove(2, 5));
        assertTrue(ch.canMove(5, 2));
        assertTrue(ch.canMove(5, 4));
        assertTrue(ch.canMove(5, 6));
    }

    @Test
    public void testCanMoveEdge() {
        assertTrue(ch.canMove(2, 7));
        assertTrue(ch.canMove(5, 0));
    }

    @Test
    public void testCanNOTMove() {
        assertFalse(ch.canMove(0, 1));
        assertFalse(ch.canMove(0, 3));
        assertFalse(ch.canMove(1, 2));
        assertFalse(ch.canMove(1, 4));
        assertFalse(ch.canMove(6, 1));
        assertFalse(ch.canMove(6, 3));
        assertFalse(ch.canMove(7, 0));
        assertFalse(ch.canMove(7, 6));
    }

    @Test
    public void testCanNotMoveEnemyOnSides() {
        emptyBoard();
        ch.setCell(5, 2, 1); // red soldier
        ch.setCell(4, 1, 2); // black soldier
        ch.setCell(4, 3, 2); // black soldier
        assertFalse(ch.canMove(5, 2));
        assertTrue(ch.canMove(4, 1));
        assertTrue(ch.canMove(4, 3));
    }

    @Test
    public void testCanMoveAndEnemiesUnder() {
        emptyBoard();
        ch.setCell(5, 2, 1); // red soldier
        ch.setCell(6, 1, 2); // black soldier
        ch.setCell(6, 3, 2); // black soldier
        assertTrue(ch.canMove(5, 2));
        assertTrue(ch.canMove(6, 1));
        assertTrue(ch.canMove(6, 3));
    }

    @Test
    public void testCanNOTAttackNoNeighbors() {
        assertFalse(ch.canAttack(2, 1));
        assertFalse(ch.canAttack(2, 3));
        assertFalse(ch.canAttack(2, 5));

        assertFalse(ch.canAttack(5, 2));
        assertFalse(ch.canAttack(5, 4));
        assertFalse(ch.canAttack(5, 6));
    }

    @Test
    public void testCanNOTAttackEdgeNoNeighbors() {
        assertFalse(ch.canAttack(2, 7));
        assertFalse(ch.canAttack(5, 0));
    }

    @Test
    public void testCanNOTAttackAllies() {
        assertFalse(ch.canAttack(1, 2));
        assertFalse(ch.canAttack(1, 4));
        assertFalse(ch.canAttack(1, 6));
        assertFalse(ch.canAttack(6, 1));
        assertFalse(ch.canAttack(6, 3));
        assertFalse(ch.canAttack(6, 5));
    }

    @Test
    public void testRedCanNOTAttackEnemiesUnder() {
        emptyBoard();
        ch.setCell(5, 2, 1); 
        ch.setCell(6, 1, 2); 
        ch.setCell(6, 3, 2); 
        assertFalse(ch.canAttack(5, 2));
    }

    @Test
    public void testBlackCanNOTAttackEnemiesAbove() {
        emptyBoard();
        ch.setCell(2, 3, 2); 
        ch.setCell(1, 2, 1); 
        ch.setCell(1, 4, 1); 
        assertFalse(ch.canAttack(2, 3));
    }

    @Test
    public void testRedCanAttackEnemies() {
        emptyBoard();
        ch.setCell(5, 2, 1); 
        ch.setCell(4, 1, 2); 
        assertTrue(ch.canAttack(5, 2)); // left side
        ch.setCell(4, 1, 0); 
        ch.setCell(4, 3, 2); 
        assertTrue(ch.canAttack(5, 2)); // right side
        ch.setCell(4, 1, 2); 
        assertTrue(ch.canAttack(5, 2)); // both sides
    }

    @Test
    public void testBlackCanAttackEnemies() {
        emptyBoard();
        ch.setCell(2, 3, 2); 
        ch.setCell(3, 2, 1); 
        assertTrue(ch.canAttack(2, 3)); // left side
        ch.setCell(3, 2, 0); 
        ch.setCell(3, 4, 1); 
        assertTrue(ch.canAttack(2, 3)); // right side
        ch.setCell(3, 2, 1); 
        assertTrue(ch.canAttack(2, 3)); // both side
    }

    @Test
    public void testBlackCanAttackWITHEnemyAndAllyNeighbors() {
        emptyBoard();
        ch.setCell(1, 4, 2); 
        ch.setCell(2, 5, 1);
        ch.setCell(2, 3, 2); 
        assertTrue(ch.canAttack(1, 4)); 
    }

    @Test
    public void testRedCanNOTAttackEnemiesBlockedPath() {
        emptyBoard();
        ch.setCell(5, 2, 1); 
        ch.setCell(4, 1, 2); 
        ch.setCell(3, 0, 2); 
        assertFalse(ch.canAttack(5, 2)); // left side
        ch.setCell(4, 1, 0); 
        ch.setCell(4, 3, 2); 
        ch.setCell(3, 4, 2); 
        assertFalse(ch.canAttack(5, 2)); // right side
        ch.setCell(4, 1, 2); 
        assertFalse(ch.canAttack(5, 2)); // both side
    }

    @Test
    public void testBlackCanNOTAttackEnemiesBlockedPath() {
        emptyBoard();
        ch.setCell(2, 3, 2); 
        ch.setCell(3, 2, 1); 
        ch.setCell(4, 1, 1);
        assertFalse(ch.canAttack(2, 3)); // left side
        ch.setCell(3, 2, 0); 
        ch.setCell(3, 4, 1); 
        ch.setCell(4, 5, 1);
        assertFalse(ch.canAttack(2, 3)); // right side
        ch.setCell(3, 2, 1); 
        assertFalse(ch.canAttack(2, 3)); // both side
    }

    @Test
    public void testREDCanNOTAttackEdges() {
        emptyBoard();
        ch.setCell(1, 6, 1); 
        ch.setCell(0, 7, 2); 
        assertFalse(ch.canAttack(1, 6)); 
        ch.setCell(1, 6, 0); 
        ch.setCell(0, 7, 1); 
        assertFalse(ch.canAttack(0, 7)); //didnt implement king yet
        ch.setCell(2, 1, 1); 
        ch.setCell(1, 0, 2); 
        assertFalse(ch.canAttack(2, 1)); 
    }

    @Test
    public void testBLACKCanNOTAttackEdges() {
        emptyBoard();
        ch.setCell(6, 5, 2); 
        ch.setCell(7, 6, 1); 
        assertFalse(ch.canAttack(6, 5)); 
        ch.setCell(3, 6, 2); 
        ch.setCell(4, 7, 1); 
        assertFalse(ch.canAttack(3, 6)); 
        ch.setCell(4, 1, 2); 
        ch.setCell(5, 0, 1); 
        assertFalse(ch.canAttack(4, 1)); 
    }

    @Test
    public void testREDCanAttackOnEdges() {
        emptyBoard();
        ch.setCell(6, 7, 1); 
        ch.setCell(5, 6, 2); 
        assertTrue(ch.canAttack(6, 7)); 
        ch.setCell(2, 7, 1); 
        ch.setCell(1, 6, 2); 
        assertTrue(ch.canAttack(2, 7)); 
        ch.setCell(5, 0, 1); 
        ch.setCell(4, 1, 2); 
        assertTrue(ch.canAttack(5, 0)); 
    }

    @Test
    public void testBLACKCanAttackOnEdges() {
        emptyBoard();
        ch.setCell(1, 0, 2); 
        ch.setCell(2, 1, 1); 
        assertTrue(ch.canAttack(1, 0)); 
        ch.setCell(0, 7, 2); 
        ch.setCell(1, 6, 1); 
        assertTrue(ch.canAttack(0, 7)); 
        ch.setCell(3, 6, 2); 
        ch.setCell(4, 5, 1); 
        assertTrue(ch.canAttack(3, 6)); 
    }

    @Test
    public void testRedMoveDiagonally() {
        emptyBoard();
        ch.setCell(5, 2, 1); 
        assertTrue(ch.move(5, 2, 4, 3));
        assertEquals(0, ch.getCell(5, 2));
        assertEquals(1, ch.getCell(4, 3));
    }

    @Test
    public void testRedDoesNOTMoveDOWNDiagonally() {
        emptyBoard();
        ch.setCell(5, 2, 1); 
        assertFalse(ch.move(5, 2, 6, 1));
        assertEquals(1, ch.getCell(5, 2));
        assertEquals(0, ch.getCell(6, 1));
    }

    @Test
    public void testRedMoveCannotMoveIfNotTheirTurn() {
        emptyBoard();
        ch.setCell(5, 2, 1); 
        assertTrue(ch.move(5, 2, 4, 3));
        assertEquals(0, ch.getCell(5, 2));
        assertEquals(1, ch.getCell(4, 3));
        assertFalse(ch.move(4, 3, 3, 2));
        assertEquals(1, ch.getCell(4, 3));
        assertEquals(0, ch.getCell(3, 2));
    }

    @Test
    public void testRedMovesNotDiagonally() {
        emptyBoard();
        ch.setCell(5, 2, 1); 
        assertFalse(ch.move(5, 2, 4, 2));
        assertEquals(1, ch.getCell(5, 2));
        assertEquals(0, ch.getCell(4, 2));
    }

    @Test
    public void testRedDoesNotMoveFarAway() {
        emptyBoard();
        ch.setCell(5, 2, 1); 
        assertFalse(ch.move(5, 2, 0, 0));
        assertEquals(1, ch.getCell(5, 2));
        assertEquals(0, ch.getCell(0, 0));
    }


    @Test
    public void testBlackMoveDiagonally() {
        emptyBoard();
        moveRed(7, 0);
        ch.setCell(2, 3, 2); 
        assertTrue(ch.move(2, 3, 3, 2));
        assertEquals(0, ch.getCell(2, 3));
        assertEquals(2, ch.getCell(3, 2));
    }

    @Test
    public void testBlackDoesNOTMoveDOWNDiagonally() {
        emptyBoard();
        ch.setCell(2, 3, 2); 
        assertFalse(ch.move(2, 3, 3, 3));
        assertEquals(2, ch.getCell(2, 3));
        assertEquals(0, ch.getCell(3, 3));
    }

    @Test
    public void testBlackMoveCannotMoveIfNotTheirTurn() {
        emptyBoard();
        moveRed(7, 0);
        ch.setCell(2, 3, 2); 
        assertTrue(ch.move(2, 3, 3, 2));
        assertEquals(0, ch.getCell(2, 3));
        assertEquals(2, ch.getCell(3, 2));
        assertFalse(ch.move(3, 2, 4, 3)); //cannot move consecutively
        assertEquals(2, ch.getCell(3, 2));
        assertEquals(0, ch.getCell(4, 3));
    }

    @Test
    public void testBlackMovesNotDiagonally() {
        emptyBoard();
        ch.setCell(2, 3, 2); 
        assertFalse(ch.move(2, 3, 3, 3));
        assertEquals(2, ch.getCell(2, 3));
        assertEquals(0, ch.getCell(3, 3));
    }

    @Test
    public void testBlackDoesNotMoveFarAway() {
        emptyBoard();
        ch.setCell(2, 3, 2); 
        assertFalse(ch.move(2, 3, 7, 6));
        assertEquals(2, ch.getCell(2, 3));
        assertEquals(0, ch.getCell(7, 6));
    }

    @Test
    public void testRedAttackDiagonallyRight() {
        emptyBoard();
        ch.setCell(5, 2, 1); 
        ch.setCell(4, 3, 2); 
        assertEquals(1, ch.getCell(5, 2));
        assertEquals(2, ch.getCell(4, 3));
        assertTrue(ch.canAttack(5, 2));
        assertTrue(ch.attack(5, 2, 4, 3));
        assertEquals(0, ch.getCell(5, 2));
        assertEquals(0, ch.getCell(4, 3));
        assertEquals(1, ch.getCell(3, 4));
    }

    @Test
    public void testRedAttackDiagonallLeft() {
        emptyBoard();
        ch.setCell(5, 2, 1); 
        ch.setCell(4, 1, 2); 
        assertEquals(1, ch.getCell(5, 2));
        assertEquals(2, ch.getCell(4, 1));
        assertTrue(ch.canAttack(5, 2));
        assertTrue(ch.attack(5, 2, 4, 1));
        assertEquals(0, ch.getCell(5, 2));
        assertEquals(0, ch.getCell(4, 1));
        assertEquals(1, ch.getCell(3, 0));
    }

    @Test
    public void testRedAttackEmptyBlock() {
        emptyBoard();
        ch.setCell(5, 2, 1); 
        assertFalse(ch.canAttack(5, 2));
        assertFalse(ch.attack(5, 2, 4, 1));
        assertEquals(1, ch.getCell(5, 2));
        assertEquals(0, ch.getCell(3, 0)); //shouldnt move
        assertFalse(ch.attack(5, 2, 4, 3));
        assertEquals(0, ch.getCell(3, 4));
    }

    @Test
    public void testRedCANTMoveIfCanAttack() {
        emptyBoard();
        ch.setCell(5, 2, 1); 
        ch.setCell(4, 3, 2); 
        assertTrue(ch.canAttack(5, 2));
        assertFalse(ch.move(5, 2, 4, 5));
    }

    @Test
    public void testRedCANTMoveOtherPieceIfOnePieceCanAttack() {
        emptyBoard();
        ch.setCell(5, 2, 1); 
        ch.setCell(4, 3, 2);
        ch.setCell(5, 6, 1); 
        assertTrue(ch.canAttack(5, 2));
        assertFalse(ch.move(5, 6, 4, 5));
    }

    @Test
    public void testRedAttackEnemyUnder() {
        emptyBoard();
        ch.setCell(5, 2, 1); 
        ch.setCell(6, 1, 2); 
        assertFalse(ch.canAttack(5, 2));
        assertFalse(ch.attack(5, 2, 6, 1));
        assertEquals(1, ch.getCell(5, 2));
        assertEquals(2, ch.getCell(6, 1));
        assertEquals(0, ch.getCell(7, 0)); //shouldnt move
    }

    @Test
    public void testRedAttackEnemyOnEdge() {
        emptyBoard();
        ch.setCell(1, 6, 1); 
        ch.setCell(0, 5, 2); 
        assertFalse(ch.canAttack(1, 6));
        assertFalse(ch.attack(1, 6, 0, 5));
        assertEquals(1, ch.getCell(1, 6));
        assertEquals(2, ch.getCell(0, 5));
        ch.setCell(4, 1, 1); 
        ch.setCell(3, 0, 2);
        assertFalse(ch.canAttack(4, 1));
        assertFalse(ch.attack(4, 1, 3, 0));
    }

    @Test
    public void testRedConsecutiveAttacks() {
        emptyBoard();
        ch.setCell(5, 2, 1);
        ch.setCell(4, 3, 2);
        ch.setCell(2, 5, 2);
        assertTrue(ch.canAttack(5, 2));
        assertFalse(ch.canAttack(3, 4));
        assertTrue(ch.attack(5, 2, 4, 3));
        assertEquals(0, ch.getCell(5, 2));
        assertEquals(0, ch.getCell(4, 3));
        assertEquals(1, ch.getCell(3, 4));
        assertFalse(moveBlack(0, 1)); // still not black's turn
        assertTrue(ch.canAttack(3, 4));
        assertTrue(ch.attack(3, 4, 2, 5));
        assertEquals(0, ch.getCell(3, 4));
        assertEquals(0, ch.getCell(2, 5));
        assertEquals(1, ch.getCell(1, 6));
    }

    @Test
    public void testRedConsecutiveAttacksCANTMOVE() {
        emptyBoard();
        ch.setCell(5, 2, 1);
        ch.setCell(4, 3, 2);
        ch.setCell(2, 5, 2);
        assertTrue(ch.canAttack(5, 2));
        assertTrue(ch.attack(5, 2, 4, 3));
        assertEquals(0, ch.getCell(5, 2));
        assertEquals(0, ch.getCell(4, 3));
        assertEquals(1, ch.getCell(3, 4));
        assertFalse(moveBlack(0, 1)); // still not black's turn
        assertTrue(ch.canAttack(3, 4));
        assertFalse(ch.move(3, 4, 2, 3)); // cant move if can attack
        assertTrue(ch.attack(3, 4, 2, 5));
        assertEquals(0, ch.getCell(3, 4));
        assertEquals(0, ch.getCell(2, 5));
        assertEquals(1, ch.getCell(1, 6));
        assertFalse(ch.canAttack(1, 6));
        assertFalse(ch.move(1, 6, 0, 5)); //finished attacking, no longer his turn
    }

    @Test
    public void testRedTwoSoldierCANTattackAtTheSameTime() {
        emptyBoard();
        ch.setCell(5, 2, 1);
        ch.setCell(5, 4, 1);
        ch.setCell(4, 3, 2);
        ch.setCell(4, 5, 2);
        assertTrue(ch.canAttack(5, 2));
        assertTrue(ch.canAttack(5, 4));
        assertTrue(ch.attack(5, 4, 4, 5));
        assertFalse(ch.attack(5, 2, 4, 3)); //cant attack consecutively with diff soldiers
        assertFalse(moveBlack(1, 0)); // BLACK's turn, has to attack
        assertTrue(ch.attack(4, 3, 5, 2));
    }

    @Test
    public void testBlackAttackDiagonallyRight() {
        emptyBoard();
        moveRed(7, 0);
        ch.setCell(2, 3, 2);
        ch.setCell(3, 4, 1); 
        assertEquals(2, ch.getCell(2, 3));
        assertEquals(1, ch.getCell(3, 4));
        assertTrue(ch.canAttack(2, 3));
        assertTrue(ch.attack(2, 3, 3, 4));
        assertEquals(0, ch.getCell(2, 3));
        assertEquals(0, ch.getCell(3, 4));
        assertEquals(2, ch.getCell(4, 5));
    }

    @Test
    public void testBlackAttackDiagonallLeft() {
        emptyBoard();
        moveRed(7, 0);
        ch.setCell(2, 3, 2); 
        ch.setCell(3, 2, 1); 
        assertEquals(2, ch.getCell(2, 3));
        assertEquals(1, ch.getCell(3, 2));
        assertTrue(ch.canAttack(2, 3));
        assertTrue(ch.attack(2, 3, 3, 2));
        assertEquals(0, ch.getCell(2, 3));
        assertEquals(0, ch.getCell(3, 2));
        assertEquals(2, ch.getCell(4, 1));
    }

    @Test
    public void testBlackAttackEmptyBlock() {
        emptyBoard();
        moveRed(7, 0);
        ch.setCell(2, 3, 2); 
        assertFalse(ch.canAttack(2, 3));
        assertFalse(ch.attack(2, 3, 3, 4));
        assertEquals(2, ch.getCell(2, 3));
        assertEquals(0, ch.getCell(4, 5)); //shouldnt move
        assertFalse(ch.attack(2, 3, 3, 2));
        assertEquals(0, ch.getCell(4, 1));
    }

    @Test
    public void testBlackAttackEnemyAbove() {
        emptyBoard();
        moveRed(7, 0);
        ch.setCell(2, 3, 2); 
        ch.setCell(1, 4, 1); 
        assertFalse(ch.canAttack(2, 3));
        assertFalse(ch.attack(2, 3, 1, 4));
        assertEquals(2, ch.getCell(2, 3));
        assertEquals(1, ch.getCell(1, 4));
        assertEquals(0, ch.getCell(0, 5)); //shouldnt move
    }

    @Test
    public void testBlackAttackEnemyOnEdge() {
        emptyBoard();
        moveRed(7, 0);
        ch.setCell(6, 3, 2); 
        ch.setCell(7, 4, 1); 
        assertFalse(ch.canAttack(6, 3));
        assertFalse(ch.attack(6, 3, 7, 4));
        assertEquals(2, ch.getCell(6, 3));
        assertEquals(1, ch.getCell(7, 4));
        ch.setCell(5, 6, 2); 
        ch.setCell(6, 7, 1);
        assertFalse(ch.canAttack(5, 6));
        assertFalse(ch.attack(5, 6, 6, 7));
    }

    @Test
    public void testBlackConsecutiveAttacks() {
        emptyBoard();
        moveRed(7, 0);
        ch.setCell(0, 1, 2);
        ch.setCell(1, 2, 1);
        ch.setCell(3, 4, 1);
        ch.setCell(5, 4, 1);
        assertTrue(ch.canAttack(0, 1));
        assertTrue(ch.attack(0, 1, 1, 2));
        assertEquals(2, ch.getCell(2, 3));
        assertTrue(ch.canAttack(2, 3));
        assertFalse(moveRed(6, 1)); //not red's turn yet
        assertTrue(ch.attack(2, 3, 3, 4));
        assertEquals(2, ch.getCell(4, 5));
        assertTrue(ch.canAttack(4, 5));
        assertFalse(moveRed(6, 1)); //not red's turn yet
        assertTrue(ch.attack(4, 5, 5, 4));
        assertEquals(2, ch.getCell(6, 3));
        assertFalse(ch.canAttack(6, 3));
        assertTrue(moveRed(6, 1));
    }

    @Test
    public void testBlackConsecutiveAttacksCANTMOVE() {
        emptyBoard();
        moveRed(7, 0);
        ch.setCell(0, 5, 2);
        ch.setCell(1, 4, 1);
        ch.setCell(3, 4, 1);
        assertTrue(ch.canAttack(0, 5));
        assertFalse(ch.move(0, 5, 1, 6)); //cant move if can attack
        assertTrue(ch.attack(0, 5, 1, 4));
        assertEquals(0, ch.getCell(0, 5));
        assertEquals(0, ch.getCell(1, 4));
        assertEquals(2, ch.getCell(2, 3));
        assertFalse(moveRed(7, 0)); // still not red's turn
        assertTrue(ch.canAttack(2, 3));
        assertFalse(ch.move(2, 3, 3, 2)); // cant move if can attack
        assertTrue(ch.attack(2, 3, 3, 4));
        assertEquals(0, ch.getCell(3, 4));
        assertEquals(0, ch.getCell(2, 3));
        assertEquals(2, ch.getCell(4, 5));
        assertFalse(ch.canAttack(4, 5));
        assertFalse(ch.move(4, 5, 5, 6)); //finished attacking, no longer his turn
        assertTrue(moveRed(6, 1));
    }

    @Test
    public void testBlackTwoSoldiersCANTattackAtTheSameTime() {
        emptyBoard();
        moveRed(7, 0);
        ch.setCell(2, 1, 2);
        ch.setCell(2, 3, 2);
        ch.setCell(3, 2, 1);
        ch.setCell(3, 4, 1);
        assertTrue(ch.canAttack(2, 1));
        assertTrue(ch.canAttack(2, 3));
        assertTrue(ch.attack(2, 1, 3, 2));
        assertFalse(ch.attack(2, 3, 3, 4)); //cant attack consecutively with diff soldiers
        assertFalse(moveRed(6, 1)); // RED's turn, has to attack
        assertTrue(ch.attack(3, 4, 2, 3));
    }

    @Test
    public void testBlackIfSoldierCanAttackCANTMoveOtherSoldier() {
        emptyBoard();
        moveRed(7, 0);
        ch.setCell(3, 2, 2);
        ch.setCell(2, 3, 2);
        ch.setCell(4, 3, 1);
        ch.setCell(4, 1, 1);
        ch.setCell(5, 0, 1);
        assertTrue(ch.canAttack(3, 2));
        assertFalse(ch.move(2, 3, 3, 4)); 
    }

    @Test
    public void testRedturnKing() {
        emptyBoard();
        ch.setCell(1, 2, 1);
        ch.move(1, 2, 0, 1);
        assertEquals(7, ch.getCell(0, 1));
    }

    @Test
    public void testBlackturnKing() {
        emptyBoard();
        moveRed(7, 0);
        ch.setCell(6, 5, 2);
        ch.move(6, 5, 7, 6);
        assertEquals(8, ch.getCell(7, 6));
    }

    @Test
    public void testRedKingCanMoveDown() {
        emptyBoard();
        moveRed(1, 0);
        assertEquals(7, ch.getCell(0, 1));
        assertTrue(ch.canMove(0, 1));
        moveBlack(0, 5);
        assertTrue(ch.move(0, 1, 1, 2));
    }

    @Test
    public void testBlackKingCanMoveUp() {
        emptyBoard();
        moveRed(7, 0);
        moveBlack(6, 5);
        assertEquals(8, ch.getCell(7, 6));
        assertTrue(ch.canMove(7, 6));
        moveRed(6, 1);
        assertTrue(ch.move(7, 6, 6, 7));
    }

    @Test
    public void testBlackSoldierAttackRedKing() {
        emptyBoard();
        ch.setCell(3, 4, 7);
        moveRed(7, 0);
        ch.setCell(2, 5, 2);
        assertTrue(ch.attack(2, 5, 3, 4));
    }

    @Test
    public void testRedKingConsecutiveAttacksBackAndForward() {
        emptyBoard();
        ch.setCell(4, 5, 7);
        ch.setCell(3, 4, 2);
        ch.setCell(3, 2, 2);
        ch.setCell(5, 2, 2);
        assertTrue(ch.attack(4, 5, 3, 4));
        assertFalse(moveBlack(1, 0));
        assertTrue(ch.attack(2, 3, 3, 2));
        assertFalse(moveBlack(1, 0));
        assertTrue(ch.attack(4, 1, 5, 2));
    }

    @Test
    public void testGameOverRedWins() {
        emptyBoard();
        ch.setNumRed(1);
        ch.setNumBlack(1);
        ch.setCell(4, 5, 1);
        ch.setCell(3, 4, 2);
        assertTrue(ch.attack(4, 5, 3, 4));
        assertEquals(1, ch.checkWinner());
    }

    @Test
    public void testGameOverBlackWins() {
        emptyBoard();
        ch.setCurrPlayer(false);
        ch.setNumRed(1);
        ch.setNumBlack(1);
        ch.setCell(4, 5, 1);
        ch.setCell(3, 4, 2);
        assertTrue(ch.attack(3, 4, 4, 5));
        assertEquals(2, ch.checkWinner());
    }

    @Test
    public void testGameOverTIE() {
        emptyBoard();
        ch.setNumRed(8);
        ch.setNumBlack(8);
        for (int i = 1; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                if (j % 2 == 0) {
                    if (i == 1) {
                        ch.setCell(i, j, 2);
                    } else if (i == 3) {
                        ch.setCell(i, j, 1);
                    }
                } else if (j % 2 == 1) {
                    if (i == 2) {
                        ch.setCell(i, j, 2);
                    } else if (i == 4) {
                        ch.setCell(i, j, 1);
                    }
                }
            }
        }
        assertFalse(ch.attack(3, 4, 2, 3));
        assertEquals(3, ch.checkWinner());
    }
}
