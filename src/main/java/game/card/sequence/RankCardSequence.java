package game.card.sequence;

import java.util.*;

import game.card.Card;

public class RankCardSequence extends CardSequence {
	
	private LinkedList<Card> sequence = new LinkedList<Card>();
	
	public RankCardSequence(CardSequenceListener cardSequenceListener) {
		super(cardSequenceListener);
	}
	
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
			sequence.add(index, card);
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
		sequence.remove(card);
	}
	
	protected boolean canRemove(Card card) {
		int index = sequence.indexOf(card), size = sequence.size();
		if( size > 3 ) {
			return 	(index > 2 && index < size - 3) ||
					index == 0 || index == size - 1;
		}
		return false;
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
		if( index <= 0 || index >= size() ) return false;
		int i = 0;
		Iterator<Card> iterator = getSequenceIterator();
		LinkedList<Card> left = new LinkedList<Card>(),
						right = new LinkedList<Card>();
		while( iterator.hasNext() ) {
			Card card = iterator.next();
			if( i < index ) {
				left.add(card);
			} else {
				right.add(card);
			}
			i++;
		}
		sequence = left;
		RankCardSequence newSequence = new RankCardSequence(cardSequenceListener);
		newSequence.sequence = right;
		cardSequenceListener.addCardSequence(newSequence);
		return true;
	}
	
}
