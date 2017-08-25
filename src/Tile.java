
public class Tile {
	
	private boolean bomb;
	private boolean revealed;
	private int near;
	
	public Tile() {
		bomb=false;
		revealed=false;
		near=0;
	}
	
	public boolean getBomb() {
		return this.bomb;
	}
	
	public void setBomb() {
		bomb=true;
	}
	
	public void setNear(int close) {
		near=close;
	}
	
	public void tickNear() {
		near++;
	}
	
	public void reveal() {
		revealed=true;
	}
	
	public boolean isRevealed() {
		return revealed;
	}
	
	public String toString() {
		if(bomb) {
			return "B";
		}
		return Integer.toString(near);
	}

}
