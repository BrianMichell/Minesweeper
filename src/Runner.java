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

	private static Tile[][] space = new Tile[10][10];
	private static JButton[][] grid = new JButton[10][10];
	private static boolean[][] flag = new boolean[10][10];
	
	private static boolean kaboom=false;

	private static final Color ONE = new Color(0, 0, 200);
	private static final Color TWO = new Color(0, 150, 0);
	private static final Color THREE = new Color(255, 0, 0);
	private static final Color FOUR = new Color(20, 0, 120);
	private static final Color FIVE = new Color(120, 0, 20);
	private static final Color SIX = new Color(60, 200, 155);
	private static final Color SEVEN = new Color(255, 255, 255);
	private static final Color EIGHT = new Color(125, 125, 125);

	public static void main(String[] args) {

		GridLayout layout = new GridLayout(10, 10);
		Border margin = new EmptyBorder(10, 10, 10, 10);
		JFrame jf = new JFrame("Minesweeper");
		BriFrame frame = new BriFrame(jf, layout);
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
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
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
									kaboom=true;
								}
						 }
					 }
				});
			}
		}

		frame.getFrame().setSize(d);

	}

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

	private static int reveal(int x, int y) {
		//TODO Make it show the first ring of numbers other than 0
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
	
	private static boolean hasBlankNear(int x, int y) {
		if(x>0 && x<9 && y>0 && y<9) // Middle of board
			if(isZero(text(x-1,y-1)) || isZero(text(x,y-1)) || isZero(text(x+1,y-1)) || isZero(text(x-1,y)) || isZero(text(x+1,y)) || isZero(text(x-1,y+1)) || isZero(text(x,y+1)) || isZero(text(x+1,y+1)))
				return true;
		if(x==0 && y>0 && y<9) // Far Left side of board
			if(isZero(text(x,y-1)) || isZero(text(x+1,y+1)) || isZero(text(x+1,y-1)) || isZero(text(x+1,y)) || isZero(text(x,y+1)))
				return true;
		if(x==9 && y>0 && y<9) // Far right side of board
			if(isZero(text(x,y-1)) || isZero(text(x,y+1)) || isZero(text(x-1,y-1)) || isZero(text(x-1,y)) || isZero(text(x-1,y+1)))
				return true;
		if(x>0 && x<9 && y==0) // Top of board
			if(isZero(text(x-1,y)) || isZero(text(x+1,y)) || isZero(text(x-1,y+1)) || isZero(text(x,y+1)) || isZero(text(x+1,y+1)))
				return true;
		if(x>0 && x<9 && y==9) // Bottom of board
			if(isZero(text(x-1,y)) || isZero(text(x+1,y)) || isZero(text(x-1,y-1)) || isZero(text(x,y-1)) || isZero(text(x+1,y-1)))
				return true;
		if(x==0 && y==0) // Top left corner
			if(isZero(text(x+1,y)) || isZero(text(x+1,y+1)) || isZero(text(x,y+1)))
				return true;
		if(x==0 && y==9) // Bottom left corner
			if(isZero(text(x+1,y)) || isZero(text(x+1,y-1)) || isZero(text(x,y-1)))
				return true;
		if(x==9 && y==0) // Top right corner
			if(isZero(text(x-1,y)) || isZero(text(x-1,y+1)) || isZero(text(x,y+1)))
				return true;
		if(x==9 && y==9) // Bottom right corner
			if(isZero(text(x-1,y)) || isZero(text(x,y-1)) || isZero(text(x-1,y-1)))
				return true;
		return false;
	}
	
	private static String text(int x, int y) {
		try {
			return (grid[x][y].getText());
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	private static boolean isZero(String s) {
		return s.equals("0");
	}

}
