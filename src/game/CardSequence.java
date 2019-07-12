package game;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class CardSequence {
	
	protected CardSequenceListener cardSequenceListener;
	
	public CardSequence(CardSequenceListener cardSequenceListener) {
		this.cardSequenceListener = cardSequenceListener;
	}
	
	public final boolean addCard(Card card) {
		boolean added;
		assert card != null;
		if( added = canAdd(card) ) {
			add(card);
//			cardSequenceListener.addedCard...
		}
		return added;
	}
	
	public final boolean removeCard(Card card) {
		boolean removed; 
		assert card != null;
		if( removed = canRemove(card) ) {
			remove(card);
//			cardSequenceListener.removedCard..
		}
		return removed;
	}
	
	public Set<Card> getRemovableCards() {
		Set<Card> removableCards = new HashSet<Card>();
		Iterator<Card> iterator = removableCards.iterator();
		while( iterator.hasNext() ) {
			Card card = iterator.next();
			if( canRemove(card) ) {
				removableCards.add(card);
			}
		}
		return removableCards;
	}
	
	protected abstract boolean canAdd(Card card);
	protected abstract void add(Card card);
	protected abstract boolean canRemove(Card card);
	protected abstract void remove(Card card);
	
}
