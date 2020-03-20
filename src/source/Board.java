package source;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements KeyListener {
	private BufferedImage blocks;
	private BufferedImage bg;
	private BufferedImage frame2;
	// gameover new
	private BufferedImage gO;
	private int score = 0;
	private String scoreString = "0";
	
	private Grid grid;
	
	private int curIDX;
	private int holdIDX = 7;
	private int nextIDX;

	private final Piece piece[] = new Piece[7];
	private CurrentPiece currentPiece; // current piece being positioned/played
	private NextPiece nextPiece;
	private HoldPiece holdPiece = null;
	private Timer timer;

	private final int FPS = 60;
	private final int delay = 1000 / 60;
	private boolean gameOver = false;
	private boolean shiftPressed = false; // apakah shift di tekan
	private boolean shiftPieceAvail = false; // cek apakah ada untuk diganti, ini baru ada setelah pertama shift
	private boolean playerShifted = false; // ck apakah selama main, shift udah pernah dipencet
	private boolean shifted = false; // cuman boleh sekali shift per piece

	private float h = (float) 0.53358333;
	private float s = (float) 0.5697;
	private float b = (float) 0.9569;
	
	private void initPicture(){
		try {
			bg = ImageIO.read(Board.class.getResource("/totoro.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// new gameOver
		try {
			gO = ImageIO.read(Board.class.getResource("/game_over.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			frame2 = ImageIO.read(Board.class.getResource("/totoroFrames.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < grid.board[row].length; col++) {
				grid.board[row][col] = 0;
			}
		}
		try {
			blocks = ImageIO.read(Board.class.getResource("/tetris_blocks_21.png")); // masukkin gambar
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void cropImages(){
		piece[0] = new Piece(blocks.getSubimage(0, 0, grid.BLOCKSIZE, grid.BLOCKSIZE), new int[][] { { 1, 1, 1, 1 } }, this, 1); // the
		// stick

		piece[1] = new Piece(blocks.getSubimage(1 * grid.BLOCKSIZE, 0, grid.BLOCKSIZE, grid.BLOCKSIZE),
		new int[][] { { 1, 1, 0 }, { 0, 1, 1 } }, this, 2); // the dog (left)
		
		piece[2] = new Piece(blocks.getSubimage(2 * grid.BLOCKSIZE, 0, grid.BLOCKSIZE, grid.BLOCKSIZE),
		new int[][] { { 0, 1, 1 }, { 1, 1, 0 } }, this, 3); // the dog (right)
		
		piece[3] = new Piece(blocks.getSubimage(3 * grid.BLOCKSIZE, 0, grid.BLOCKSIZE, grid.BLOCKSIZE),
		new int[][] { { 1, 0, 0 }, { 1, 1, 1 } }, this, 4); // the L (left)
		
		piece[4] = new Piece(blocks.getSubimage(4 * grid.BLOCKSIZE, 0, grid.BLOCKSIZE, grid.BLOCKSIZE),
		new int[][] { { 0, 0, 1 }, { 1, 1, 1 } }, this, 5); // the L (right)
		
		piece[5] = new Piece(blocks.getSubimage(5 * grid.BLOCKSIZE, 0, grid.BLOCKSIZE, grid.BLOCKSIZE),
		new int[][] { { 0, 1, 0 }, { 1, 1, 1 } }, this, 6); // the T
		
		piece[6] = new Piece(blocks.getSubimage(6 * grid.BLOCKSIZE, 0, grid.BLOCKSIZE, grid.BLOCKSIZE),
		new int[][] { { 1, 1 }, { 1, 1 } }, this, 7); // the Square

	}
	
	public Board() {
		setBackground(Color.getHSBColor(h, s, b));
		initPicture();

		timer = new Timer(delay, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				update();
				repaint();
			}
		});

		timer.start();

		// shapes of tetris block piece , crop subImage
		cropImages();
		curIDX = Helper.randomNum(0, 6);
		currentPiece = new CurrentPiece(piece[curIDX].getBlock(), piece[curIDX].getCoords().clone(), this,
				piece[curIDX].getColor()); // coba

		getNextPiece();
		// getPiece();
	}

	public void update() {
		if (gameOver) {
			timer.stop();

		}
		currentPiece.update();
	}

	
	
	private void createBG(Graphics g){
		g.drawImage(bg, 40, 320, null);
		g.setColor(Color.getHSBColor((float) 0.472, (float) 0.5, (float) 0.76));
		// g.fillRect(405, 70, 125, 188);
		g.setColor(new Color(255, 255, 255, 100));
		g.fillRect(grid.indentX, grid.indentY + 4 * grid.BLOCKSIZE, grid.GRIDWIDTH * grid.BLOCKSIZE, (grid.GRIDHEIGHT - 4) * grid.BLOCKSIZE);
	}
	
	private void createScore(Graphics g){
		g.setFont(new Font("Agent Red", Font.BOLD, 20));
		g.setColor(Color.BLACK);
		g.drawString("LINES CLEARED: " + scoreString, 50, 40);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		createBG(g);
		
		currentPiece.render(g);
		
		for (int row = 0; row < grid.board.length; row++)
			for (int col = 0; col < grid.board[0].length; col++)
				if (grid.board[row][col] != 0)
					g.drawImage(blocks.getSubimage((grid.board[row][col] - 1) * grid.BLOCKSIZE, 0, grid.BLOCKSIZE, grid.BLOCKSIZE),
							col * grid.BLOCKSIZE + grid.indentX, row * grid.BLOCKSIZE + grid.indentY, null);

		// gambar nextPiece
		nextPiece.render(g);
		// gambar holdPiece
		if (holdPiece != null && (shiftPieceAvail || playerShifted))
			holdPiece.render(g);

		// piece frame
		g.drawImage(frame2, 390, 65, null);

		createScore(g);

		grid.createGrids(g);

		// new gameover image
		if (gameOver) {
			// x,y,lebar,tinggi

			g.drawImage(gO, 10, 150, 280, 500, null);

		}
	}

	public void getNextPiece() {
		do {
			nextIDX = Helper.randomNum(0, 6);
		} while (nextIDX == curIDX);

		nextPiece = new NextPiece(piece[nextIDX].getBlock(), piece[nextIDX].getCoords(), this,
				piece[nextIDX].getColor());

	}
	
	private void firstShift(){
		shifted = false;
		currentPiece = new CurrentPiece(piece[nextIDX].getBlock(), piece[nextIDX].getCoords(), this,
				piece[nextIDX].getColor());
		getNextPiece();
	}
	
	private void nextShift(){
		shifted = true;
		curIDX = currentPiece.getColor() - 1;
		if (curIDX == holdIDX)
			currentPiece.setcY(grid.indentY + 4 * grid.BLOCKSIZE - currentPiece.getCoords().length * grid.BLOCKSIZE);
		else {
			int temp = holdIDX;
			holdIDX = curIDX;
			holdPiece = new HoldPiece(piece[holdIDX].getBlock(), piece[holdIDX].getCoords(), this,
					piece[holdIDX].getColor());

			if (shiftPieceAvail) {
				curIDX = temp;
				currentPiece = new CurrentPiece(piece[curIDX].getBlock(), piece[curIDX].getCoords(), this,
						piece[curIDX].getColor());
				holdIDX = holdPiece.getColor() - 1;
			} else if (!shiftPieceAvail) {
				curIDX = nextPiece.getColor() - 1;
				currentPiece = new CurrentPiece(piece[nextIDX].getBlock(), piece[nextIDX].getCoords(), this,
						piece[nextIDX].getColor());

				getNextPiece();
			}
			holdPiece.render(getGraphics());
		}
		shiftPieceAvail = true;
		shiftPressed = false;
	}
	
	public void getPiece() {
		if (shiftPressed && !shifted) {
			nextShift();
		} else if (!shiftPressed) {
			firstShift();
		}

		for (int i = 0; i < grid.GRIDWIDTH; i++)
			if (grid.board[3][i] != 0) {
				gameOver = true;
				break;
			}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			currentPiece.rotate();
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			currentPiece.speedDown();
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			currentPiece.setdX(-grid.BLOCKSIZE);
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			currentPiece.setdX(grid.BLOCKSIZE);
		}
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			if (!shifted) {
				shiftPressed = true;
			} else {
				playerShifted = true;
				shifted = true;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (gameOver) {
				Sound.stop();
				new MainMenuFrame();

			} else
				currentPiece.placeDown();
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			currentPiece.normalSpeed();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	
	public boolean getGameOver() {
		return gameOver;
	}
	

	public boolean isShiftPressed() {
		return shiftPressed;
	}

	public Piece getHoldPiece() {
		return holdPiece;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score += score;
		scoreString = "" + this.score;
		// System.out.println(scoreString);
	}

}
