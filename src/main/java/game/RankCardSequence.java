package game;

import java.util.*;

public class RankCardSequence extends CardSequence {
	
	private final LinkedList<Card> sequence = new LinkedList<Card>();
	
	public RankCardSequence(CardSequenceListener cardSequenceListener) {
		super(cardSequenceListener);
	}

	/**
	 * Overwrites {@link CardSequence#getRemovableCards()} because
	 * it can be optimised for {@link RankCardSequence}. For the
	 * {@link LinkedList} implementation, the {@link LinkedList#get(int)}
	 * method has time complexity of O(n), so using a proper index
	 * much more efficient.
	 */
	public Set<Card> getRemovableCards() {
		Set<Card> removableCards = new HashSet<Card>();
		Iterator<Card> iterator = removableCards.iterator();
		int index = 0, size = sequence.size();
		// sequences of size 3 don't have removable cards
		if( size > 3 ) {
			while( iterator.hasNext() ) {
				Card card = iterator.next();
				if( index > 2 && index < size - 3 ||
					index == 0 || index == size - 1 ) {
					removableCards.add(card);
					// either from the beginning
					// or from the end
					// or from the middle
				}
				index++;
			}
		}
		return removableCards;
	}
	
	protected void add(Card card) {
		sequence.add(card);
	}
	
	protected boolean canAdd(Card card) {
		if( sequence.isEmpty() ) return true;
		Card first = sequence.getFirst(); 
		if( !first.equalSuits(card) ) return false;
		Card last = sequence.getLast();
		if( first.compareRanks(card) < -1 && 
			last.compareRanks(card) > 1 ) {
			return true;
		}
		if( !sequence.contains(card) && // contains(Object) makes use of equals
			(first.isNeighbor(card) || last.isNeighbor(card)) ) {
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
					index == 0 ||
					index == size - 1;
		}
		return false;
	}

}
