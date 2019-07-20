package game.card.sequence.types;

import java.util.*;

import game.card.Card;
import game.card.sequence.CardSequenceListener;
import game.card.sequence.GenericCardSequence;

public class RankCardSequence extends GenericCardSequence {
	
	private LinkedList<Card> sequence = new LinkedList<Card>();
		
	public RankCardSequence() { }
	public RankCardSequence( CardSequenceListener listener ) { super(listener); }
	public RankCardSequence( Set<CardSequenceListener> listeners ) { super(listeners); }
	
	protected void add(Card card) {
		if( sequence.isEmpty() ) {
			sequence.add(card);
			return;
		}
		Card firstCard = sequence.getFirst();
		int index = card.compareRanks(firstCard);
		int size = sequence.size();
		if( index <= 0 ) {
			sequence.addFirst(card);
		} else if( index >= size ) {
			sequence.addLast(card);
		} else {
			split(index);
			sequence.add(card);
		}
	}
	
	protected boolean canAdd(Card card) {
		if( sequence.isEmpty() ) return true;
		Card first = sequence.getFirst(); 
		if( !first.equalSuits(card) ) return false;
		Card last = sequence.getLast();
		if( first.compareRanks(card) < -1 && 
			last.compareRanks(card) > 1 ) {
			return true; // card is in the middle
		}
		if( !sequence.contains(card) && // contains(Object) makes use of equals
			(first.isNeighbour(card) || last.isNeighbour(card)) ) {
			return true; // card is in one of the corners
		}
		return false;
	}

	protected void remove(Card card) {
		Card firstCard = sequence.getFirst();
		int index = card.compareRanks(firstCard);
		split(index+1);
		sequence.remove(card);
		callListener((listener) -> listener.cardRemovedFromSequence(card));
	}
	
	protected boolean canRemove(Card card) {
		return sequence.contains(card);
	}

	protected Iterator<Card> getSequenceIterator() {
		return sequence.iterator();
	}

	protected boolean areSequential(Card first, Card second) {
		return first.compareRanks(second) == -1;
	}

	/**
	 * <p>Splits card sequence into two. One that
	 * ranges from indexes 0 to index-1 and another
	 * that ranges from indexes index until the end.
	 * <p><b>Observation:</b> One cannot split a single-card sequence.
	 * @param index - index of first card of rightmost
	 * card sequence (starting from 0)
	 * @return {@code true} if split was successful
	 */
	public final boolean split(int index) {
		int size = size();
		if( index <= 0 || index >= size ) return false;
		RankCardSequence newSequence = new RankCardSequence();
		newSequence.addListeners(this);
		for( int i = index; i < size; i++ ) {
			Card removedCard = sequence.remove(index);
			newSequence.add(removedCard);
		}
		callListener((listener) -> listener.cardSequenceAdded(newSequence));
		return true;
	}
	
}
