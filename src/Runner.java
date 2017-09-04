import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import com.github.BrianMichell.Utils.BriFrame;

public class Runner {

	private static Tile[][] space;
	private static JButton[][] grid;
	private static boolean[][] flag;
	
	private static boolean kaboom=false;

	private static final Color ONE = new Color(0, 0, 200);
	private static final Color TWO = new Color(0, 150, 0);
	private static final Color THREE = new Color(255, 0, 0);
	private static final Color FOUR = new Color(20, 0, 120);
	private static final Color FIVE = new Color(120, 0, 20);
	private static final Color SIX = new Color(60, 200, 155);
	private static final Color SEVEN = new Color(255, 255, 255);
	private static final Color EIGHT = new Color(125, 125, 125);
	
	private static int xVal=10;
	private static int yVal=10;
	private static int bVal=10;
	
	public Runner() {
		
	}

	public static void run(){
				
		space = new Tile[xVal][yVal];
		grid = new JButton[xVal][yVal];
		flag = new boolean[xVal][yVal];

		GridLayout layout = new GridLayout(xVal, yVal);
		Border margin = new EmptyBorder(10, 10, 10, 10);
		JFrame jf = new JFrame("Minesweeper");
		BriFrame frame = new BriFrame(jf, layout);
		Dimension d = new Dimension(xVal*7,yVal*7);
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu();
		// TODO Add the menu bar to allow user to change size/difficulty

		// Creates a blank board
		for (int x = 0; x < xVal; x++) {
			for (int y = 0; y < yVal; y++) {
				space[x][y] = new Tile();
			}
		}

		// Generates bombs on the field.
		Random rand = new Random();
		int bombs = bVal;
		while (bombs > 0) {
			int x = rand.nextInt(xVal);
			int y = rand.nextInt(yVal);
			if (!space[x][y].getBomb()) {
				space[x][y].setBomb();
				bombs--;
			}
		}

		// Correctly labels the spaces on the field
		for (int x = 0; x < xVal; x++) {
			for (int y = 0; y < yVal; y++) {
				circle(x, y);
			}
		}

		// Generates the board
		for (int x = 0; x < xVal; x++) {
			for (int y = 0; y < yVal; y++) {
				String place = space[x][y].toString();
				JButton b = new JButton();
				b.setBorder(margin);
				b.setForeground(getColor(place));
				frame.addButton(b);
				grid[x][y]=b;
				final int xf = x;
				final int yf = y;
				b.addMouseListener(new MouseAdapter(){
					 public void mouseClicked(MouseEvent e) {
						 ImageIcon image;
						 if(SwingUtilities.isRightMouseButton(e) && text(xf,yf).equals("") && !kaboom) {
							 if(!flag[xf][yf]) {
								 image=new ImageIcon("flag.jpg");
							 } else {
								 image=new ImageIcon("");
							 }
							 flag[xf][yf]=!flag[xf][yf];
							 b.setIcon(image);
						 } else if(SwingUtilities.isLeftMouseButton(e) && !kaboom && !flag[xf][yf]) {
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
		}

		frame.getFrame().setSize(d);

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
	 * Colors the number to look more like the actual game of Minesweeper
	 * @param num The string version of the number
	 * @return The color that the number should be
	 */
	private static Color getColor(String num) {
		if (num.equals("1"))
			return ONE;
		if (num.equals("2"))
			return TWO;
		if (num.equals("3"))
			return THREE;
		if (num.equals("4"))
			return FOUR;
		if (num.equals("5"))
			return FIVE;
		if (num.equals("6"))
			return SIX;
		if (num.equals("7"))
			return SEVEN;
		if (num.equals("8"))
			return EIGHT;
		if (num.equals("0"))
			return new Color(0, 0, 0);
		return new Color(200, 200, 200);

	}

	/**
	 * Reveals every 0 on the board
	 * @param x The x coordinate of the Tile
	 * @param y The y coordinate of the Tile
	 * @return Nothing. This is a void recursive method
	 */
	private static int reveal(int x, int y) {
		if(space[x][y].toString().equals("0") && grid[x][y].getText().equals("") || hasBlankNear(x,y) && text(x,y).equals("")) {
			if(x>0 && y>0 && x<xVal-1 && y<yVal-1) { // Middle of the board
				grid[x][y].setText(space[x][y].toString());
				return reveal(x-1,y-1)+reveal(x,y-1)+reveal(x+1,y-1)+reveal(x-1,y)+reveal(x+1,y)+reveal(x-1,y+1)+reveal(x,y+1)+reveal(x+1,y+1);
			}
			if(x==0 && y>0 && y<yVal-1) { // Far left side of board
				grid[x][y].setText(space[x][y].toString());
				return reveal(x,y-1)+reveal(x+1,y+1)+reveal(x+1,y)+reveal(x+1,y-1)+reveal(x,y+1);
			}
			if(x==xVal-1 && y>0 && y<yVal-1) { // Far right side of board
				grid[x][y].setText(space[x][y].toString());
				return reveal(x,y-1)+reveal(x,y+1)+reveal(x-1,y-1)+reveal(x-1,y)+reveal(x-1,y+1);
			}
			if(x>0 && x<xVal-1 && y==0) { // Top of board
				grid[x][y].setText(space[x][y].toString());
				return reveal(x-1,y)+reveal(x+1,y)+reveal(x-1,y+1)+reveal(x,y+1)+reveal(x+1,y+1);
			}
			if(x>0 && x<xVal-1 && y==yVal-1) { // Bottom of board
				grid[x][y].setText(space[x][y].toString());
				return reveal(x-1,y)+reveal(x+1,y)+reveal(x-1,y-1)+reveal(x,y-1)+reveal(x+1,y-1);
			}
			if(x==0 && y==0) { // Top left corner
				grid[x][y].setText(space[x][y].toString());
				return reveal(x+1,y)+reveal(x+1,y+1)+reveal(x,y+1);
			}
			if(x==0 && y==yVal-1) { // Bottom left corner
				grid[x][y].setText(space[x][y].toString());
				return reveal(x+1,y)+reveal(x+1,y-1)+reveal(x,y-1);
			}
			if(x==xVal-1 && y==0) { // Top right corner
				grid[x][y].setText(space[x][y].toString());
				return reveal(x-1,y)+reveal(x-1,y+1)+reveal(x,y+1);
			}
			if(x==xVal-1 && y==yVal-1) { // Bottom right corner
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
	
	private static boolean win() {
		for(int x=0; x<xVal; x++) {
			for(int y=0; y<yVal; y++) {
				if(grid[x][y].getText().equals("") && !space[x][y].toString().equals("B")) {
					return false;
				}
			}
		}
		System.out.println("Winner");
		//TODO Stop the timer
		return true;
	}
	
	private static void revealMines() {
		for(int x=0; x<xVal; x++) {
			for(int y=0; y<yVal; y++) {
				if(space[x][y].toString().equals("B")) {
					ImageIcon image = new ImageIcon("bomb.jpg");
					grid[x][y].setIcon(image);
				}
			}
		}
	}
	
	public static void setVals(int x, int y, int bombs) {
		xVal=x;
		yVal=y;
		bVal=bombs;
	}

}