package game.card.sequence;

import game.card.Card;

/**
 * 
 * The listener notifies whenever an internal event has occurred
 * and may help the class that uses the {@link CardSequence} class
 * to handle new or empty sequences.
 * 
 * @author guidanoli
 *
 */
public interface CardSequenceListener {

	/**
	 * A new card sequence was created 
	 * @param cardSequence - new card sequence
	 */
	public void cardSequenceAdded(CardSequence cardSequence);
	
	/**
	 * The card sequence is now empty
	 * @param cardSequence - empty card sequence
	 */
	public void cardSequenceIsEmpty(CardSequence cardSequence);
	
	/**
	 * A card has been removed from a sequence
	 * @param card - card removed from sequence
	 */
	public void cardRemovedFromSequence(Card card);
	
}
