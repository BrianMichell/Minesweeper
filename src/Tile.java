
public class Tile {
	
	private boolean bomb;
	private int near;
	
	public Tile() {
		bomb=false;
		near=0;
	}
	
	public boolean getBomb() {
		return this.bomb;
	}
	
	public void setBomb() {
		bomb=true;
	}
	
	public void tickNear() {
		near++;
	}
	
	public String toString() {
		if(bomb) {
			return "B";
		}
		return Integer.toString(near);
	}

}
