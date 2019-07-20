package gui;
import java.awt.Component;
import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class FatalError {

	protected final static String ERROR_MSG_TITLE = "Error";
	protected final static String ERROR_LOG_TITLE = "Error log";
	protected final static String ERROR_GENERAL_MSG = "An unhandled error occurred.";
	
	/* Builder */
	
	public static class Builder {
		
		private Exception exception = null;
		private String message = null;
		private Component parent = null;
		private boolean exit = true;
		
		public Builder() {} // can instantiate directly
		public static Builder newInstance() { return new Builder(); } // or indirectly

		public Builder exception(Exception e) { this.exception = e; message = null; return this; } 
		public Builder message(String msg) { this.message = msg; exception = null; return this; }
		public Builder parent(Component parent) { this.parent = parent; return this; }
		public Builder exit(boolean exit) { this.exit = exit; return this; }	
		
		public void show() {
			if( exception != null ) { // exception
				FatalError.show(exception, parent, exit);
			} else if( message != null ) { // or message
				FatalError.show(message, parent, exit);
			} // or nothing
		}
		
	}
	
	/* Methods */
	
	/**
	 * Show a fatal error message on-screen
	 * @param msg - message
	 * @param parent - parent dialog
	 * @param exit - whether to exit the application afterwards
	 */
	public static void show (String msg, Component parent, boolean exit) {
		JOptionPane.showMessageDialog(parent, msg, ERROR_MSG_TITLE, JOptionPane.ERROR_MESSAGE);
		if( exit ) System.exit(0);
	}
	
	/**
	 * Show a fatal error message on-screen as a log
	 * @param msg - log message
	 * @param parent - parent dialog
	 * @param exit - whether to exit the application afterwards
	 */
	public static void showLog (String msg, Component parent, boolean exit) {
		JTextArea txt = new JTextArea(msg);
		JScrollPane sp = new JScrollPane();
		txt.setEditable(false);
		txt.setLineWrap(true);
		txt.setWrapStyleWord(true);
		sp.setViewportView(txt);
		sp.setPreferredSize(new Dimension(1000,200));
		JOptionPane.showMessageDialog(parent, sp, ERROR_LOG_TITLE, JOptionPane.ERROR_MESSAGE);
		if( exit ) System.exit(0);
	}
	
	/**
	 * Show a fatal error message on-screen
	 * @param e - exception
	 * @param parent - parent dialog
	 * @param exit - whether to exit the application afterwards
	 */
	public static void show (Exception e, Component parent, boolean exit) {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String encoding = "UTF-8";
	    try (PrintStream ps = new PrintStream(baos, true, encoding)) {
	        e.printStackTrace(ps);
	    } catch (UnsupportedEncodingException e1) {
			show(String.format("Could not encode error message because '%s' is not supported.", encoding),parent,true);
		}
	    String data = new String(baos.toByteArray(), StandardCharsets.UTF_8);
	    String [] options = {"OK","Show log"};
	    String msg = e.getMessage();
	    if( msg == null ) msg = ERROR_GENERAL_MSG;
	    int choice = JOptionPane.showOptionDialog(	parent,
				    								msg,
			    									ERROR_MSG_TITLE,
			    									JOptionPane.OK_CANCEL_OPTION,
			    									JOptionPane.ERROR_MESSAGE,
			    									null, options, options[0]);
	    if( choice == 1 )
	    	showLog(data,parent,false);
	    if( exit ) System.exit(0);
	}
		
	
}

