package game;

import java.util.*;

public class RankCardSequence extends CardSequence {
	
	private final LinkedList<Card> sequence = new LinkedList<Card>();
	
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
			sequence.add(index, card);
			RankCardSequence newSequence = new RankCardSequence(cardSequenceListener);
			for( int i = index; i < size; i++ ) {
				Card removedCard = sequence.remove(index+1);
				newSequence.add(removedCard);
			}
			cardSequenceListener.addCardSequence(newSequence);
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

}
