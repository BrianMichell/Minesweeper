import java.awt.Color;

public class Colors {

	public final Color ONE = new Color(0, 0, 200);
	public final Color TWO = new Color(0, 150, 0);
	public final Color THREE = new Color(255, 0, 0);
	public final Color FOUR = new Color(20, 0, 120);
	public final Color FIVE = new Color(120, 0, 20);
	public final Color SIX = new Color(60, 200, 155);
	public final Color SEVEN = new Color(255, 255, 255);
	public final Color EIGHT = new Color(125, 125, 125);
	
	public Colors() {
		
	}
	
	/**
	 * Colors the number to look more like the actual game of Minesweeper
	 * @param num The string version of the number
	 * @return The color that the number should be
	 */
	public Color setColor(String num) {
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
	
}
