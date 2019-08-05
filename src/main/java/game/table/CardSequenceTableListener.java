package game.table;

import game.card.Card;

/**
 * 
 * A card sequence listener handles events that cannot be
 * handled by the {@link CardSequenceTable} class.
 * 
 * @author guidanoli
 * @see CardSequenceTable
 *
 */
public interface CardSequenceTableListener {

	/**
	 * A card has been removed from a sequence
	 * @param card - removed card
	 */
	public void cardRemoved(Card card);
	
}
