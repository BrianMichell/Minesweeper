import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.github.BrianMichell.Utils.BriFrame;

public class Game {
	
	private static Tile[][] space = new Tile[10][10];
	private static JButton[][] grid = new JButton[10][10];
	private static boolean[][] flag = new boolean[10][10];
	
	private static boolean startTimer=false;
	private static boolean kaboom=false;
	private static boolean winner=false;
	
	private static final double bil=Math.pow(10, 9);
	private static long initTime=0;
	
	private static JLabel time;
	private static JLabel minesLeft;
	
	public Game() {
		this(10,10);
	}
	
	public Game(int xVal, int yVal) {
		GridLayout layout = new GridLayout(1, 10);
		Border margin = new EmptyBorder(10, 10, 10, 10);
		JFrame jf = new JFrame("Minesweeper");
		JPanel utils = new JPanel();
		BriFrame frame = new BriFrame(jf, new GridLayout(11,1));
		time = new JLabel();
		minesLeft=new JLabel();
		time.setText("0");
		minesLeft.setText("10");
		utils.add(time);
		utils.add(minesLeft);
		frame.getFrame().add(utils);
		Dimension d = new Dimension(700,700);
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu();
		// TODO Add the menu bar to allow user to change size/difficulty

		// Creates a blank board
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				space[x][y] = new Tile();
			}
		}

		// Generates bombs on the field.
		Random rand = new Random();
		int bombs = 10;
		while (bombs > 0) {
			int x = rand.nextInt(10);
			int y = rand.nextInt(10);
			if (!space[x][y].getBomb()) {
				space[x][y].setBomb();
				bombs--;
			}
		}

		// Correctly labels the spaces on the field
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				circle(x, y);
			}
		}

		// Generates the board
		Colors c = new Colors();
		for (int x = 0; x < 10; x++) {
			JPanel panel = new JPanel();
			panel.setLayout(layout);
			for (int y = 0; y < 10; y++) {
				String place = space[x][y].toString();
				JButton b = new JButton();
				b.setBorder(margin);
				b.setForeground(c.setColor(place));
				panel.add(b);
				grid[x][y]=b;
				final int xf = x;
				final int yf = y;
				b.addMouseListener(new MouseAdapter(){ // Gets left and right clicks
					 public void mouseClicked(MouseEvent e) {
						 if(!startTimer) {
							 startTimer=true;
							 initTime=System.nanoTime();
						 }
						 ImageIcon image;
						 if(SwingUtilities.isRightMouseButton(e) && text(xf,yf).equals("") && !kaboom && !winner) {
							 if(!flag[xf][yf]) {
								 image=new ImageIcon("flag.jpg");
								 int tmp=Integer.parseInt(minesLeft.getText());
								 if(tmp>0)
									 tmp--;
								 minesLeft.setText(Integer.toString(tmp));
							 } else {
								 image=new ImageIcon("");
								 int tmp=Integer.parseInt(minesLeft.getText());
								 tmp++;
								 minesLeft.setText(Integer.toString(tmp));
							 }
							 flag[xf][yf]=!flag[xf][yf];
							 b.setIcon(image);
						 } else if(SwingUtilities.isLeftMouseButton(e) && !kaboom && !flag[xf][yf] && !winner) {
							 String placef = space[xf][yf].toString();
								reveal(xf, yf);
								if(!placef.equals("B")) {
									b.setText(placef);
								} else {
									image = new ImageIcon("bomb.jpg");
									b.setIcon(image);
									revealMines();
									kaboom=true;
								}
						 }
						 win();
					 }
				});
			}
			frame.getFrame().add(panel);
		}

		frame.getFrame().setSize(d);
		
		new Thread(()->{
			while(!winner && !kaboom) {
				if(startTimer) {
					getTime();
				}
				try {
					Thread.sleep(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(winner) {
				time.setText(time.getText()+" Winner");
			} else {
				time.setText(time.getText()+" Looser");
			}
		}).start();


	}
	
	/**
	 * Finds all the bombs on the field and adds the numbers to the Tile as needed
	 * @param xCent The x coordinate of the Tile you are checking
	 * @param yCent The y coordinate of the Tile you are checking
	 */
	public static void circle(int xCent, int yCent) {
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				try {
					if (space[xCent + x][yCent + y].getBomb()) {
						space[xCent][yCent].tickNear();
					}
				} catch (IndexOutOfBoundsException e) {
					// This is a lazy way to make sure the numbers are always on the board
				}
			}
		}
	}
	
	/**
	 * Reveals every 0 on the board
	 * @param x The x coordinate of the Tile
	 * @param y The y coordinate of the Tile
	 * @return Nothing. This is a void recursive method
	 */
	private static int reveal(int x, int y) {
		if(space[x][y].toString().equals("0") && grid[x][y].getText().equals("") || hasBlankNear(x,y) && text(x,y).equals("")) {
			if(x>0 && y>0 && x<9 && y<9) { // Middle of the board
				grid[x][y].setText(space[x][y].toString());
				return reveal(x-1,y-1)+reveal(x,y-1)+reveal(x+1,y-1)+reveal(x-1,y)+reveal(x+1,y)+reveal(x-1,y+1)+reveal(x,y+1)+reveal(x+1,y+1);
			}
			if(x==0 && y>0 && y<9) { // Far left side of board
				grid[x][y].setText(space[x][y].toString());
				return reveal(x,y-1)+reveal(x+1,y+1)+reveal(x+1,y)+reveal(x+1,y-1)+reveal(x,y+1);
			}
			if(x==9 && y>0 && y<9) { // Far right side of board
				grid[x][y].setText(space[x][y].toString());
				return reveal(x,y-1)+reveal(x,y+1)+reveal(x-1,y-1)+reveal(x-1,y)+reveal(x-1,y+1);
			}
			if(x>0 && x<9 && y==0) { // Top of board
				grid[x][y].setText(space[x][y].toString());
				return reveal(x-1,y)+reveal(x+1,y)+reveal(x-1,y+1)+reveal(x,y+1)+reveal(x+1,y+1);
			}
			if(x>0 && x<9 && y==9) { // Bottom of board
				grid[x][y].setText(space[x][y].toString());
				return reveal(x-1,y)+reveal(x+1,y)+reveal(x-1,y-1)+reveal(x,y-1)+reveal(x+1,y-1);
			}
			if(x==0 && y==0) { // Top left corner
				grid[x][y].setText(space[x][y].toString());
				return reveal(x+1,y)+reveal(x+1,y+1)+reveal(x,y+1);
			}
			if(x==0 && y==9) { // Bottom left corner
				grid[x][y].setText(space[x][y].toString());
				return reveal(x+1,y)+reveal(x+1,y-1)+reveal(x,y-1);
			}
			if(x==9 && y==0) { // Top right corner
				grid[x][y].setText(space[x][y].toString());
				return reveal(x-1,y)+reveal(x-1,y+1)+reveal(x,y+1);
			}
			if(x==9 && y==9) { // Bottom right corner
				grid[x][y].setText(space[x][y].toString());
				return reveal(x-1,y)+reveal(x,y-1)+reveal(x-1,y-1);
			}
		}
		return 0;
	}
	
	/**
	 * Reveals all the numbers next to a 0
	 * @param x The x coordinate of the Tile
	 * @param y The y coordinate of the Tile
	 * @return true if the tile should be revealed and is on the board
	 */
	private static boolean hasBlankNear(int x, int y) {
		for(int xl=-1; xl<2; xl++) {
			for(int yl=-1; yl<2; yl++) {
				if(isZero(text(x+xl,y+yl))) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Looks at the text displayed on the button. 
	 * It will catch IndexOutOfBoundsExceptions and return "".
	 * I removed the printing of the stack trace to eliminate any printed errors.
	 * @param x The x coordinate of the Button
	 * @param y The y coordinate of the Button
	 * @return The text displayed on the button. If it is out of bounds it will return ""
	 */
	private static String text(int x, int y) {
		try {
			return (grid[x][y].getText());
		} catch (IndexOutOfBoundsException e) {
			return "";
		}
	}
	
	/**
	 * Simplifies checking if a String (normally from the button) is zero.
	 * @param s The string (from the button)
	 * @return if the string is a 0
	 */
	private static boolean isZero(String s) {
		return s.equals("0");
	}
	
	/**
	 * Tests to see if the player has won the game.
	 * @return true if there are no blank spaces or bombs clicked, false otherwise
	 */
	private static boolean win() {
		int left = Integer.parseInt(minesLeft.getText());
		for(int x=0; x<10; x++) {
			for(int y=0; y<10; y++) {
				if(grid[x][y].getText().equals("") && !space[x][y].toString().equals("B") && left>0) {
					System.out.println(grid[x][y].getText().equals("") +" "+ !space[x][y].toString().equals("B") +" "+ (left>0));
					return false;
				}
			}
		}
		System.out.println("Winner"); // Print the win to the console for debugging
		winner=true;
		return true;
	}
	
	/**
	 * If a bomb is clicked, all the mines on the field will be shown.
	 */
	private static void revealMines() {
		for(int x=0; x<10; x++) {
			for(int y=0; y<10; y++) {
				if(space[x][y].toString().equals("B")) {
					ImageIcon image = new ImageIcon("bomb.jpg");
					grid[x][y].setIcon(image);
				}
			}
		}
	}
	
	/**
	 * Get the change in time from the first reveal on the field to the current time.
	 * @return the time in seconds that has elapsed.
	 */
	private static int getTime() {
		long current = System.nanoTime();
		int t = (int)(Math.round((current-initTime)/bil));
		time.setText(Integer.toString(t));
		return (int)(Math.round((current-initTime)/bil));
	}


}
