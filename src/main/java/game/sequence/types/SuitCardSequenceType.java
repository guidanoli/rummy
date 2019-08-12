package game.sequence.types;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import game.card.Card;
import game.sequence.CardSequenceBuilder;

/**
 * 
 * <p>A suit card sequence is represented by the set
 * of all cards of same rank. By the word "set",
 * it is implied that no duplicates are allowed.
 * 
 * <p>This definition is quite simpler than the one
 * of {@link RankCardSequenceType} since there is
 * no order established between the cards, making it
 * far easier to implement in the form of a set, and
 * not of a list.
 * 
 * @author guidanoli
 *
 */
public class SuitCardSequenceType implements CardSequenceType, Comparator<Card> {

	private SortedSet<Card> sequence = new TreeSet<Card>(this);
	
	public boolean canAdd(Card card) {
		if (sequence.isEmpty()) return true;
		if (sequence.contains(card)) return false;
		Card anyCard = getAnyCard(); // any card from sequence
		return card.equalRanks(anyCard);
	}

	/**
	 * @return any card of the sequence
	 * @throws NoSuchElementException when empty
	 */
	private Card getAnyCard() {
		return sequence.iterator().next();
	}
	
	public CardSequenceBuilder add(Card card) {
		sequence.add(card);
		return null;
	}

	public boolean addCardSet(Set<Card> cardSet) {
		Iterator<Card> iterator = cardSet.iterator();
		while( iterator.hasNext() ) {
			Card card = iterator.next();
			if ( !canAdd(card) ) { return false; }
			add(card);
		}
		return true;
	}

	public boolean canRemove(Card card) {
		return sequence.contains(card);
	}

	public CardSequenceBuilder remove(Card card) {
		sequence.remove(card);
		return null;
	}

	public boolean canSplit(int index) {
		return false; // suit sequences can't be split
		// if you want, you can just remove any card from it
	}

	public CardSequenceBuilder split(int index) {
		return null; // will never be called
	}

	public Iterator<Card> getSequenceIterator() {
		return sequence.iterator();
	}

	public int size() {
		return sequence.size();
	}

	public int compare(Card first, Card second) {
		return first.compare(second);
	}

}
