package org.hameister.javafx.puzzle;

import java.util.Random;

public class PuzzleModel {
	private PuzzleFieldStyle[][] board;
	private PuzzleFieldStyle[][] expectedBoard;
	private int boardSize;

	// Indices of the neighbor fields
	private int[] indexX = { 0, -1, 1, 0 };
	private int[] indexY = { 1, 0, 0, -1 };
	
	public PuzzleModel(int size) {
		board = new PuzzleFieldStyle[size][size];
		expectedBoard = new PuzzleFieldStyle[size][size];

		// Simplify the range checks
		boardSize = board.length;

		init();
	}

	private void init() {
		int colorCounter = 1;
		for (int x = 0; x < boardSize; x++) {
			for (int y = 0; y < boardSize; y++) {
				PuzzleFieldStyle color = new PuzzleFieldStyle("puzzle-field-style-no"+colorCounter++);
				expectedBoard[x][y] = color;
				board[x][y] = color;
			}
		}

		// One field is empty
		expectedBoard[boardSize-1][boardSize-1] = null;
		board[boardSize-1][boardSize-1] = null;
		
		shuffleBoard();
	}

	public Point moveToEmptyField(Point moveColoredFieldToPoint) {
		for (int i = 0; i < 4; i++) {
			if (moveColoredFieldToPoint.getX() + indexX[i] >= 0 && moveColoredFieldToPoint.getY() + indexY[i] >= 0 && moveColoredFieldToPoint.getX() + indexX[i] < boardSize && moveColoredFieldToPoint.getY() + indexY[i] < boardSize) {
				//Check if the field is empty (null)
				if (board[moveColoredFieldToPoint.getX() + indexX[i]][moveColoredFieldToPoint.getY() + indexY[i]] == null) {

					Point emptyFieldPos = new Point(moveColoredFieldToPoint.getX() + indexX[i], moveColoredFieldToPoint.getY() + indexY[i]);
					switchField(moveColoredFieldToPoint, emptyFieldPos);
					return emptyFieldPos;
				}
			}
		}
		return null;
	}

	/**
	 * Compare the colors of the rectangles. 
	 */
	public boolean areBoardsEqual() {
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board.length; y++) {
				PuzzleFieldStyle expectedRec = expectedBoard[x][y];
				PuzzleFieldStyle rect = board[x][y];
				if (rect!=expectedRec || !(expectedRec == rect || expectedRec.toString().equals(rect.toString()))) {
					return false;
				}
			}
		}

		return true;
	}

	private void shuffleBoard() {
		// Per definition this is the empty field in the initial state
		// see colorSet4: Last value is null
		Point emptyFieldPos = new Point(boardSize - 1, boardSize - 1);

		Random r = new Random(System.currentTimeMillis());

		for (int i = 0; i < 1000; i++) {
			int fieldToMove = r.nextInt(indexX.length);
			if (emptyFieldPos.getX() + indexX[fieldToMove] >= 0 && emptyFieldPos.getY() + indexY[fieldToMove] >= 0 && emptyFieldPos.getX() + indexX[fieldToMove] < boardSize && emptyFieldPos.getY() + indexY[fieldToMove] < boardSize) {
				Point colorFieldPos = new Point(emptyFieldPos.getX() + indexX[fieldToMove], emptyFieldPos.getY() + indexY[fieldToMove]);
				emptyFieldPos = switchField(colorFieldPos, emptyFieldPos);
			}
		}
	}

	private Point switchField(Point colorFieldPos, Point emptyFieldPos) {
		// Switch with one temp variable was possible, too. But this is 
		// better to understand.
		PuzzleFieldStyle coloredField = board[colorFieldPos.getX()][colorFieldPos.getY()];
		PuzzleFieldStyle emptyWhiteField = board[emptyFieldPos.getX()][emptyFieldPos.getY()];
		board[emptyFieldPos.getX()][emptyFieldPos.getY()] = coloredField;
		board[colorFieldPos.getX()][colorFieldPos.getY()] = emptyWhiteField;
		return new Point(colorFieldPos.getX(), colorFieldPos.getY());
	}

	public PuzzleFieldStyle getColorAt(int x, int y) {
		if (x < boardSize && x >= 0 && y < boardSize && y >= 0) {
			return board[x][y];
		}
		return null;
	}

	public PuzzleFieldStyle getExpectedColorAt(int x, int y) {
		if (x < boardSize && x >= 0 && y < boardSize && y >= 0) {
			return expectedBoard[x][y];
		}
		return null;
	}

	public void printBoards() {


		System.out.println("======================================================= Board");
		for (int x = 0; x < boardSize; x++) {
			for (int y = 0; y < boardSize; y++) {
				if (board[y][x] != null) {
					System.out.print("\t" + board[y][x].toString() + "\t");
				} else {
					System.out.print("\t\t\t\t");
				}
			}
			System.out.println();
		}

		System.out.println("======================================================= Expected");

		for (int x = 0; x < boardSize; x++) {
			for (int y = 0; y < boardSize; y++) {
				if (expectedBoard[y][x] != null) {
					System.out.print("\t" + expectedBoard[y][x].toString() + "\t");
				} else {
					System.out.print("\t\t\t");
				}
			}
			System.out.println();
		}
	}

}
