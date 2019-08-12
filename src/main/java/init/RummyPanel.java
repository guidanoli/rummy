package init;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class RummyPanel extends JPanel {
	
	public RummyPanel(RummyDisposeListener disposeListener) {
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		// Add launch buttons 
		
		for ( RummyDialogs dialog : RummyDialogs.values() ) {
			JButton launchBtn = new JButton(dialog.getTitle());
			launchBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dialog.getListener().launchFrame();
					if ( dialog.closesParentFrame() ) {
						disposeListener.disposeFrame();
					}
				}
			});
			add(Box.createRigidArea(new Dimension(0,2)));
			launchBtn.setAlignmentX(CENTER_ALIGNMENT);
			add(launchBtn);
			add(Box.createRigidArea(new Dimension(0,2)));
		}
		
	}

}
