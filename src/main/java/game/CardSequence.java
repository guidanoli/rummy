package game;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public abstract class CardSequence {
	
	protected CardSequenceListener cardSequenceListener;
	
	/* Implemented methods */
	
	public CardSequence(CardSequenceListener cardSequenceListener) {
		this.cardSequenceListener = Objects.requireNonNull(cardSequenceListener,
				() -> "Null cardSequenceListener is not accepted");
	}
	
	public final boolean addCard(Card card) {
		boolean added = false;
		card = Objects.requireNonNull(card, 
				() -> "Cannot add null card");
		if( added = canAdd(card) ) {
			add(card);
//			cardSequenceListener.addedCard...
		}
		return added;
	}
	
	public final boolean removeCard(Card card) {
		boolean removed = false;
		card = Objects.requireNonNull(card, 
				() -> "Cannot remove null card");
		if( removed = canRemove(card) ) {
			remove(card);
//			cardSequenceListener.removedCard..
		}
		return removed;
	}
	
	public final Set<Card> getRemovableCards() {
		Set<Card> removableCards = new HashSet<Card>();
		Iterator<Card> iterator = Objects.requireNonNull(getSequenceIterator(),
				() -> "Cannot iterate over sequence because iterator is null");
		while( iterator.hasNext() ) {
			Card card = iterator.next();
			if( canRemove(card) ) {
				removableCards.add(card);
			}
		}
		return removableCards;
	}
	
	public final boolean equals(CardSequence anotherCardSequence) {
		Iterator<Card> iterator = Objects.requireNonNull(getSequenceIterator(),
				() -> "Cannot iterate over sequence because iterator is null");
		anotherCardSequence = Objects.requireNonNull(anotherCardSequence,
				() -> "Cannot iterate over sequence because it is null");
		Iterator<Card> anotherIterator = Objects.requireNonNull(anotherCardSequence.getSequenceIterator(),
				() -> "Cannot iterate over sequence because iterator is null");
		while( iterator.hasNext() ) {
			if( !anotherIterator.hasNext() ||
				!anotherIterator.next().equals(iterator.next()) ) {
				return false;
			}
		}
		return !anotherIterator.hasNext();
	}
	
	protected final boolean hasValidState() {
		Iterator<Card> iterator = getSequenceIterator();
		Card prev = null, curr = null;
		while( iterator.hasNext() ) {
			curr = iterator.next();
			if( curr == null || (prev != null && !areSequential(prev,curr)) ) return false;
			prev = curr;
		}
		return true;
	}
	
	/* Abstract methods */
	
	protected abstract boolean canAdd(Card card);
	protected abstract void add(Card card);
	protected abstract boolean canRemove(Card card);
	protected abstract void remove(Card card);
	protected abstract Iterator<Card> getSequenceIterator();
	protected abstract boolean areSequential(Card first, Card second);
	
}
