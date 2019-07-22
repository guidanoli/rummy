package game.card.sequence;

/**
 * 
 * <p>Used for calling a method of each listener in a set
 * Will be instantiated anonymously as a lambda:
 * <p>{@code (listener) -> listener.method(...)}
 * 
 * @author guidanoli
 * @see CardSequence
 * @see CardSequenceListener
 * 
 */
public interface CardSequenceCaller {

	/**
	 * <p>Calls each listener in the listener set as a lambda:
	 * <p>{@code (listener) -> listener.method(...)}
	 * @param listener - listener to be called
	 * @see CardSequence
	 * @see CardSequenceListener
	 */
	public void call(CardSequenceListener listener);
	
}
