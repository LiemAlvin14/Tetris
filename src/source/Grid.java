package source;

import java.awt.Color;
import java.awt.Graphics;

public class Grid {
	public final int BLOCKSIZE = 25;
	public final int GRIDHEIGHT = 24, GRIDWIDTH = 10;
	public final int indentY = 25 * 2;
	public final int indentX = 25;
	public final int borderY = 25 * 24 + indentY;

	public final int borderX = 25 * 10 + indentX;
	
	public int[][] board = new int[GRIDHEIGHT][GRIDWIDTH];
	
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
}
