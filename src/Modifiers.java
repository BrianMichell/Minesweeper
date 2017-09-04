import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JTextArea;

import com.github.BrianMichell.Utils.BriFrame;
import com.github.BrianMichell.Utils.BriRegex;

public class Modifiers {
	
	private static BriFrame frame = new BriFrame();
	private static JTextArea x;
	private static JTextArea y;
	private static JTextArea bombs;
	private static boolean done=false;
	
	public static void main(String[] args) {
	
		x = new JTextArea();
		y = new JTextArea();
		bombs = new JTextArea();
		JButton accept = new JButton("Accept");
		frame.getFrame().add(x);
		frame.getFrame().add(y);
		frame.getFrame().add(bombs);
		frame.addButton(accept);
		
		accept.addActionListener((e)->{
			set();
			Runner r = new Runner();
			r.run();
			frame.getFrame().setVisible(false);
		});
		Dimension d = new Dimension(75,75);
		frame.getFrame().setSize(d);
		
	}
	
	private static void set() {
		int xv=3;
		int yv=3;
		int bombsv=3;
		BriRegex reg = new BriRegex();
		if(!x.getText().equals("")) {
			xv=Integer.parseInt(reg.find("[0-9]{1,}", x.getText()).get(0));
		}
		if(!y.getText().equals("")) {
			yv=Integer.parseInt(reg.find("[0-9]{1,}", y.getText()).get(0));
		}
		if(!bombs.getText().equals("")) {
			bombsv=Integer.parseInt(reg.find("[0-9]{1,}", bombs.getText()).get(0));
		}
		Runner.setVals(xv,yv,bombsv);
	}
	
	public static boolean isDone() {
		return done;
	}

}
