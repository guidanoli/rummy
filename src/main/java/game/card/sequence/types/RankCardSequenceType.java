package game.card.sequence.types;

import java.util.*;

import game.card.Card;
import game.card.sequence.CardSequenceBuilder;

public class RankCardSequenceType implements CardSequenceType {
	
	private LinkedList<Card> sequence = new LinkedList<Card>();
		
	public CardSequenceBuilder add(Card card) {
		if( sequence.isEmpty() ) {
			sequence.add(card);
		} else {
			Card firstCard = sequence.getFirst();
			int index = card.compareRanks(firstCard);
			int size = sequence.size();
			if( index <= 0 ) {
				sequence.addFirst(card);
			} else if( index >= size ) {
				sequence.addLast(card);
			} else {
				CardSequenceBuilder builder = null;
				if( canSplit(index) ) {
					builder = split(index);
				}
				sequence.add(card);
				return builder;
			}
		}
		return null;
	}
	
	public boolean canAdd(Card card) {
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

	public CardSequenceBuilder remove(Card card) {
		Card firstCard = sequence.getFirst();
		int index = card.compareRanks(firstCard);
		CardSequenceBuilder builder = null;
		if( canSplit(index) ) {
			builder = split(index+1);
		}
		sequence.remove(card);
		return builder;
	}
	
	public boolean canRemove(Card card) {
		return sequence.contains(card);
	}

	public CardSequenceBuilder split(int index) {
		int size = size();
		CardSequenceBuilder builder = new CardSequenceBuilder()
				.setType(new RankCardSequenceType());
		for( int i = index; i < size; i++ ) {
			Card removedCard = sequence.remove(index);
			builder.addCard(removedCard);
		}
		return builder;
	}

	public boolean canSplit(int index) {
		return index > 0 && index < size();
	}
	
	public Iterator<Card> getSequenceIterator() {
		return sequence.iterator();
	}

	public boolean areSequential(Card first, Card second) {
		return first.compareRanks(second) == -1;
	}

	@Override
	public int size() {
		return sequence.size();
	}
	
}
