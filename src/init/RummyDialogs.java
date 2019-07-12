package init;

import javax.swing.JFrame;

import gui.FatalError;

public enum RummyDialogs {
	
	END("Exit", true, null);
	
	private String title;
	private boolean closeParent;
	private RummyLaunchListener listener;
	
	RummyDialogs( String title, boolean closeParent, Class<?> frameClass ) {
			
		// Initialises a new listener
		
		listener = new RummyLaunchListener() {
			
			public void launchFrame() {
				Object frameObj = null;
				if( frameClass != null ) {
					try {
						frameObj = frameClass.newInstance();
					} catch (Exception e) {
						FatalError.show(e);
					}
					assert frameObj instanceof JFrame;
					JFrame frame = (JFrame) frameObj;
					frame.setVisible(true);
				}
			}
			
		};
		
		// Sets static variables
		
		this.title = title;
		this.closeParent = closeParent;
		
	}
	
	public RummyLaunchListener getListener() { return listener; }
	public boolean closesParentFrame() { return closeParent; }
	public String getTitle() { return title; }
	
}
