package game;

import java.util.*;

public class RankCardSequence extends CardSequence {
	
	private final LinkedList<Card> sequence = new LinkedList<Card>();
	
	public RankCardSequence(CardSequenceListener cardSequenceListener) {
		super(cardSequenceListener);
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
					index == 0 ||
					index == size - 1;
		}
		return false;
	}

	protected Iterator<Card> getSequenceIterator() {
		return sequence.iterator();
	}

}
