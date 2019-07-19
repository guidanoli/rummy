package game.card.sequence;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import game.card.Card;

/**
 * 
 * <p>A card sequence is any collection of cards with a predetermined
 * set of rules that validate its state/stability. Cards can be added
 * or removed from the sequence whenever possible.
 * 
 * <p>The criteria that determines whether two cards are sequential
 * to one another, or if one card can be added to or removed from the
 * sequence is entirely depended on the construction of the card
 * sequence object. One cannot change its behaviour but by constructing
 * a new card sequence of the appropriate type.
 * 
 * @author guidanoli
 *
 */
public abstract class CardSequence {
	
	protected CardSequenceListener cardSequenceListener;
	
	/* Implemented methods */
	
	/**
	 * Constructs a card sequence object
	 * @param cardSequenceListener - card sequence listener that will
	 * receive call backs whenever a new card sequence is created or
	 * destroyed.
	 * @throws NullPointerException if listener is {@code null}
	 */
	public CardSequence(CardSequenceListener cardSequenceListener) {
		this.cardSequenceListener = Objects.requireNonNull(cardSequenceListener,
				() -> "Null cardSequenceListener is not accepted");
	}
	
	/**
	 * Adds a card to the sequence if possible
	 * @param card - card to be added
	 * @return {@code true} if card was added and
	 * {@code false} if card could not be added
	 * @throws NullPointerException if card is {@code null}
	 */
	public final boolean addCard(Card card) {
		boolean added = false;
		card = Objects.requireNonNull(card, 
				() -> "Cannot add null card");
		if( added = canAdd(card) ) add(card);
		return added;
	}
	
	/**
	 * Removes a card to the sequence if possible
	 * @param card - card to be removed
	 * @return {@code true} if card was removed and
	 * {@code false} if card could not be removed
	 * @throws NullPointerException if card is {@code null}
	 */
	public final boolean removeCard(Card card) {
		boolean removed = false;
		card = Objects.requireNonNull(card, 
				() -> "Cannot remove null card");
		if( removed = canRemove(card) ) remove(card);
		return removed;
	}
	
	/**
	 * @return set of all cards of the sequence that can be removed
	 */
	public final Set<Card> getRemovableCards() {
		Set<Card> removableCards = new HashSet<Card>();
		Iterator<Card> iterator = getSequenceIterator();
		while( iterator.hasNext() ) {
			Card card = iterator.next();
			if( canRemove(card) ) {
				removableCards.add(card);
			}
		}
		return removableCards;
	}
	
	/**
	 * Checks whether two card sequences contain the same cards, in order
	 * @param anotherCardSequence - another card sequence
	 * @return {@code true} if both have the same cards in order
	 */
	public final boolean equals(CardSequence anotherCardSequence) {
		Iterator<Card> iterator = getSequenceIterator();
		anotherCardSequence = Objects.requireNonNull(anotherCardSequence,
				() -> "Cannot iterate over sequence because it is null");
		Iterator<Card> anotherIterator = anotherCardSequence.getSequenceIterator();
		while( iterator.hasNext() ) {
			if( !anotherIterator.hasNext() ||
				!anotherIterator.next().equals(iterator.next()) ) {
				return false;
			}
		}
		return !anotherIterator.hasNext();
	}
	
	/**
	 * @return {@code true} if card sequence is stable, that is,
	 * has 3 or more cards and does not present an invalid state
	 */
	public final boolean isStable() {
		return hasValidState() && size() >= 3;
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
	
	protected final int size() {
		int size = 0;
		Iterator<Card> iterator = getSequenceIterator();
		while( iterator.hasNext() ) {
			iterator.next();
			size++;
		}
		return size;
	}
	
	/* Abstract methods */
	
	protected abstract boolean canAdd(Card card);
	protected abstract void add(Card card);
	protected abstract boolean canRemove(Card card);
	protected abstract void remove(Card card);
	protected abstract Iterator<Card> getSequenceIterator();
	protected abstract boolean areSequential(Card first, Card second);
	
}
