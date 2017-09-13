import javax.swing.JButton;
import javax.swing.JTextField;

import com.github.BrianMichell.Utils.BriFrame;

public class Modifiers {
	
	public Modifiers() {
		
	}
	
	public Modifiers(int x, int y) {
		BriFrame frame = new BriFrame();
		JTextField xField = new JTextField(x);
		JTextField yField = new JTextField(y);
		JButton accept = new JButton("Accept");
		accept.addActionListener((e)->{
			//Close the current frame, game frame, and open a new frame
		});
	}

}
