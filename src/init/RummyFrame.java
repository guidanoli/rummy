package init;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * The {@link RummyFrame} class is the entry point of the program.
 * It creates the initial frame where buttons are laid out.
 * 
 * Each button launches a different program, and may or may not
 * close this frame.
 * 
 * @author guidanoli
 * @see RummyLaunchListener
 *
 */
@SuppressWarnings("serial")
public class RummyFrame extends JFrame implements RummyDisposeListener {
	
	public RummyFrame() {
		setTitle("Rummy");
		setMinimumSize(new Dimension(150,50));
		getContentPane().add(new RummyPanel(this));
		setLocationRelativeTo(null); // centres
		pack();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new RummyFrame().setVisible(true);
			}
		});
	}

	public void disposeFrame() {
		dispose();
	}

}
