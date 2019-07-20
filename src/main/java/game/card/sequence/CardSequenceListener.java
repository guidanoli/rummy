package game.card.sequence;

import game.card.Card;

public interface CardSequenceListener {

	public void cardSequenceAdded(GenericCardSequence cardSequence);
	
	public void cardSequenceRemoved(GenericCardSequence cardSequence);
	
	public void cardRemovedFromSequence(Card card);
	
}
