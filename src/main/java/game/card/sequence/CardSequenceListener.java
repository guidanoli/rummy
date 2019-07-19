package game.card.sequence;

import game.card.Card;

public interface CardSequenceListener {

	public void cardSequenceAdded(CardSequence cardSequence);
	
	public void cardSequenceRemoved(CardSequence cardSequence);
	
	public void cardRemovedFromSequence(Card card);
	
}
