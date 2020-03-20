package source;

import java.awt.Color;
import java.awt.Graphics;

public class Grid {
	private final int BLOCKSIZE = 25;
	private final int GRIDHEIGHT = 24, GRIDWIDTH = 10;
	private final int indentY = 25 * 2;
	private final int indentX = 25;
	private final int borderY = 25 * 24 + indentY;

	private final int borderX = 25 * 10 + indentX;
	
	private int[][] board = new int[GRIDHEIGHT][GRIDWIDTH];
	
	public Grid() {
		// TODO Auto-generated constructor stub
	}
	void createGrids(Graphics g){
		// bikin grid blocks
		// horizontal
		g.setColor(Color.DARK_GRAY);
		for (int i = 0; i <= GRIDHEIGHT - 4; i++) {
			g.drawLine(indentX, i * BLOCKSIZE + (indentY + (BLOCKSIZE * 4)), GRIDWIDTH * BLOCKSIZE + BLOCKSIZE,
					i * BLOCKSIZE + (indentY + (BLOCKSIZE * 4)));
		}
		// vertical
		for (int i = 0; i <= GRIDWIDTH; i++) {
			g.drawLine(i * BLOCKSIZE + BLOCKSIZE, (indentY + (BLOCKSIZE * 4)), i * BLOCKSIZE + BLOCKSIZE,
					(GRIDHEIGHT - 4) * BLOCKSIZE + (indentY + (BLOCKSIZE * 4)));
		}
	}
	public int getBLOCKSIZE() {
		return BLOCKSIZE;
	}
	public int getGRIDHEIGHT() {
		return GRIDHEIGHT;
	}
	public int getGRIDWIDTH() {
		return GRIDWIDTH;
	}
	public int getIndentY() {
		return indentY;
	}
	public int getIndentX() {
		return indentX;
	}
	public int getBorderY() {
		return borderY;
	}
	public int getBorderX() {
		return borderX;
	}
	public int[][] getBoard() {
		return board;
	}
	
}
