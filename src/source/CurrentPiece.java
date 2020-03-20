package source;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class CurrentPiece extends Piece {

	private int normalSpeed = 1000, speedDown = 100, currentSpeed;
	private long time, lastTime;

	private boolean collisionY = false, collisionX = false, spacePressed = false;

	public CurrentPiece(BufferedImage block, int[][] coords, Board board, int color) {
		super(block, coords, board, color);
		currentSpeed = normalSpeed;
		time = 0;
		lastTime = System.currentTimeMillis();

		cX = board.grid.getIndentX() + (board.grid.getGRIDWIDTH() / 2 - (coords[0].length / 2)) * board.grid.getBLOCKSIZE();
		cY = board.grid.getIndentY() + 4 * board.grid.getBLOCKSIZE() - coords.length * board.grid.getBLOCKSIZE();
	}

	@Override
	public void render(Graphics g) {
		for (int y = 0; y < coords.length; y++) {
			for (int x = 0; x < coords[y].length; x++) {
				if (coords[y][x] != 0)
					g.drawImage(block, x * board.grid.getBLOCKSIZE() + cX, y * board.grid.getBLOCKSIZE() + cY, null);

			}
		}
	}

	public void update() {
		time += System.currentTimeMillis() - lastTime;
		lastTime = System.currentTimeMillis();
		// System.out.println(board.grid.getBoard()[((cY - 70) / board.grid.getBLOCKSIZE()()) +
		// 1][((cX - 20) / board.grid.getBLOCKSIZE()())]);
		if (board.isShiftPressed()) {
			board.getPiece();
		}
		if (collisionY) {
			if (time > currentSpeed) {
				time = 0;
			}
			for (int row = 0; row < coords.length; row++)
				for (int col = 0; col < coords[row].length; col++)
					if (coords[row][col] != 0)
						board.grid.getBoard()[((cY - board.grid.getIndentY()) / board.grid.getBLOCKSIZE())
								+ row][((cX - board.grid.getIndentX()) / board.grid.getBLOCKSIZE()) + col] = color;
			checkLine();
			board.getPiece();
		}

		if (cX + dX >= board.grid.getIndentX() && cX + dX + coords[0].length * board.grid.getBLOCKSIZE() <= board.grid.getBorderX()) {
			for (int row = 0; row < coords.length; row++)
				for (int col = 0; col < coords[row].length; col++)
					if (coords[row][col] != 0) {
						if (board.grid.getBoard()[((cY - board.grid.getIndentY()) / board.grid.getBLOCKSIZE())
								+ row][((cX - board.grid.getIndentX()) / board.grid.getBLOCKSIZE()) + col
										+ (dX / board.grid.getBLOCKSIZE())] != 0)
							collisionX = true;
					}
			if (!collisionX)
				cX += dX;
		}
		if (cY + board.grid.getBLOCKSIZE() + (coords.length * board.grid.getBLOCKSIZE()) <= board.grid.getBorderY()) {
			for (int row = 0; row < coords.length; row++)
				for (int col = 0; col < coords[row].length; col++)
					if (coords[row][col] != 0) {
						if (board.grid.getBoard()[((cY - board.grid.getIndentY()) / board.grid.getBLOCKSIZE()) + row
								+ 1][((cX - board.grid.getIndentX()) / board.grid.getBLOCKSIZE()) + col] != 0) {
							collisionY = true;
							if (spacePressed)
								cY -= 1;
						}
					}
			if (spacePressed)
				currentSpeed = 1;
			if (time > currentSpeed) {
				cY += board.grid.getBLOCKSIZE();
				time = 0;
			}
		} else {
			collisionY = true;
		}
		dX = 0;
		collisionX = false;
	}

	// System.out.println(cX + " " + cY);

	public void rotate() {
		int[][] rotatedMatrix = null;

		rotatedMatrix = getTranspose(coords);
		rotatedMatrix = getReverseMatrix(rotatedMatrix);

		if (cX < board.grid.getIndentX() || cX + (rotatedMatrix[0].length * board.grid.getBLOCKSIZE()) > board.grid.getBorderX()
				|| cY + (rotatedMatrix.length * board.grid.getBLOCKSIZE()) < board.grid.getIndentY()
				|| cY + (rotatedMatrix.length * board.grid.getBLOCKSIZE()) > board.grid.getBorderY()) {
			while (cX + (rotatedMatrix[0].length * board.grid.getBLOCKSIZE()) > board.grid.getBorderX())
				cX -= board.grid.getBLOCKSIZE();

		}

		for (int row = 0; row < rotatedMatrix.length; row++)
			for (int col = 0; col < rotatedMatrix[row].length; col++)
				if (rotatedMatrix[row][col] != 0) {
					if (board.grid.getBoard()[((cY - board.grid.getIndentY()) / board.grid.getBLOCKSIZE()) + row
							+ 1][((cX - board.grid.getIndentX()) / board.grid.getBLOCKSIZE()) + col] != 0) {
						return;
					}
				}
		// System.out.println(rotatedMatrix.length + " " + rotatedMatrix[0].length);
		// System.out.println(cX + " " + cY);

		coords = rotatedMatrix;
		// System.out.println(coords[0].length * board.grid.getBLOCKSIZE()() + cX + " " + cY +
		// coords.length *
		// board.grid.getBLOCKSIZE()());

	}

	private int[][] getTranspose(int[][] matrix) {
		int[][] newMatrix = new int[matrix[0].length][matrix.length];

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				newMatrix[j][i] = matrix[i][j];
			}
		}
		return newMatrix;

	}

	private int[][] getReverseMatrix(int[][] matrix) {
		int middle = matrix.length / 2;

		for (int i = 0; i < middle; i++) {
			int[] m = matrix[i];
			matrix[i] = matrix[matrix.length - i - 1];
			matrix[matrix.length - i - 1] = m;
		}
		return matrix;

	}

	private void checkLine() {
		int temp = 0;
		int height = board.grid.getBoard().length - 1;
		for (int i = height; i > 0; i--) {
			int count = 0;
			for (int j = 0; j < board.grid.getBoard()[0].length; j++) {
				if (board.grid.getBoard()[i][j] != 0) {
					count++;
				}

				board.grid.getBoard()[height][j] = board.grid.getBoard()[i][j];
			}
			if (count < board.grid.getBoard()[0].length) {
				height--;
				temp++;
			}
		}
		board.setScore((23 - temp));
		System.out.println("Score : " + board.getScore() + " " + temp);
	}

	public void normalSpeed() {
		currentSpeed = normalSpeed;
	}

	public void speedDown() {
		currentSpeed = speedDown;
	}

	public void placeDown() {
		spacePressed = true;
	}

	public boolean isCollisionY() {
		return collisionY;
	}

	public boolean isSpacePressed() {
		return spacePressed;
	}

}
