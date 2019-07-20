package game.card.sequence;

import game.card.Card;

/**
 * 
 * A card sequence builder helps build a card sequence without
 * having to bother to write the name of the object over and
 * over again, and, even more, validate the sequence upon building.
 * 
 * @author guidanoli
 *
 * @param <T> type of card sequence
 */
public interface CardSequenceBuilder<T extends GenericCardSequence> {

	public CardSequenceBuilder<T> addListener(CardSequenceListener listener); 
	public CardSequenceBuilder<T> addCard(Card card);
	public T build() throws InvalidCardSequenceException;
	
}
