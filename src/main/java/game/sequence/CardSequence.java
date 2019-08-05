package game.sequence;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Consumer;

import game.card.Card;
import game.sequence.types.CardSequenceType;

/**
 * 
 * <p>A card sequence is any collection of cards with a predetermined
 * set of rules that validate its stability. Cards can be added
 * or removed from the sequence whenever possible.
 * 
 * <p>The criteria that determines whether two cards are sequential
 * to one another, or if one card can be added to or removed from the
 * sequence is entirely depended on the construction of the card
 * sequence object. One cannot change its behaviour but by constructing
 * a new card sequence of the appropriate type.
 * 
 * <p>A card sequence can have zero or more listeners that will be
 * notified whenever any of the actions specified in {@link CardSequenceListener}
 * occur. It's important to have listeners in order to handle new sequences
 * being created by splitting and empty sequences by removing.
 * 
 * <p>A card sequence can be iterated through by the {@code for} notation
 * since this class implements the {@link Iterable} interface.
 * 
 * @author guidanoli
 * @see Card
 * @see CardSequenceListener
 *
 */
public class CardSequence implements Iterable<Card> {
	
	private Set<CardSequenceListener> listeners = new HashSet<CardSequenceListener>();
	private CardSequenceType type;
	
	/**
	 * Constructs a card sequence object
	 * @see CardSequenceBuilder
	 */
	protected CardSequence(CardSequenceType type) {
		this.type = type;
	}
		
	/**
	 * Adds a listener to the card sequence listener set
	 * @param listener - card sequence listener
	 */
	public void addListener(CardSequenceListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Adds a card to the sequence, if possible
	 * @param card - card to be added
	 * @return {@code true} if card was added and
	 * {@code false} if card could not be added
	 */
	public boolean addCard(Card card) {
		boolean added = false;
		if( added = type.canAdd(card) ) {
			CardSequenceBuilder builder = type.add(card);
			if( builder != null ) buildNewSequence(builder);
			
		}
		return added;
	}
	
	/**
	 * Removes a card to the sequence if possible
	 * @param card - card to be removed
	 * @return {@code true} if card was removed and
	 * {@code false} if card could not be removed
	 */
	public boolean removeCard(Card card) {
		boolean removed = false;
		if( removed = type.canRemove(card) ) {
			CardSequenceBuilder builder = type.remove(card);
			if( builder != null ) buildNewSequence(builder);
			callListeners((listener) -> listener.cardRemovedFromSequence(card));
			if( size() == 0 ) callListeners((listener) -> listener.cardSequenceIsEmpty(this));
		}
		return removed;
	}
		
	/**
	 * @return set of all cards of the sequence that can be removed
	 */
	public Set<Card> getRemovableCards() {
		Set<Card> removableCards = new HashSet<Card>();
		Iterator<Card> iterator = type.getSequenceIterator();
		while( iterator.hasNext() ) {
			Card card = iterator.next();
			if( type.canRemove(card) ) {
				removableCards.add(card);
			}
		}
		return removableCards;
	}
	
	/**
	 * Splits sequence into two
	 * @param index - index of the first card of the second sequence
	 * @return {@code true} if sequence was split up and
	 * {@code false} if sequence could not be split up
	 */
	public boolean split(int index) {
		if( !type.canSplit(index) ) return false;
		CardSequenceBuilder builder = type.split(index); 
		if( builder != null ) buildNewSequence(builder);
		return true;
	}
	
	/**
	 * Checks whether two card sequences contain the same cards, in the same order
	 * @param o - another card sequence
	 * @return {@code true} if both have the same cards in the same order
	 */
	@Override
	public boolean equals(Object o) {
		if( o instanceof CardSequence ) {
			CardSequence anotherCardSequence = (CardSequence) o;
			Iterator<Card> iterator = type.getSequenceIterator();
			Iterator<Card> anotherIterator = anotherCardSequence.type.getSequenceIterator();
			while( iterator.hasNext() ) {
				if( !anotherIterator.hasNext() ||
					!anotherIterator.next().equals(iterator.next()) ) {
					return false;
				}
			}
			return !anotherIterator.hasNext();
		}
		return false;
	}
	
	/**
	 * @return {@code true} if card sequence is stable, that is,
	 * has 3 or more cards and does not present an invalid state
	 */
	public boolean isStable() {
		return size() >= 3;
	}
	
	/**
	 * @return number of cards in the sequence
	 */
	public int size() {
		return type.size();
	}
		
	/**
	 * @return iterator that iterates through all of the card
	 * in the sequence, which can also be done by the
	 * {@code for} notation:
	 * <p>{@code for(Card card: cardSequence) {...}}
	 */
	public Iterator<Card> iterator() {
		return type.getSequenceIterator();
	}
		
	/**
	 * Builds a new sequence according to builder object provided
	 * and notifies all of the current sequence listeners. Allows
	 * for unstable sequences.
	 * @param builder - card sequence builder object
	 */
	private void buildNewSequence(CardSequenceBuilder builder) {
		CardSequence newSequence = builder
				.addListenerSet(listeners)
				.allowInstability(true)
				.build();
		callListeners((listener) -> listener.cardSequenceAdded(newSequence));
	}

	/**
	 * Fires all listeners with a calling message
	 * @param caller - caller lambda
	 */
	private void callListeners(Consumer<CardSequenceListener> caller) {
		for( CardSequenceListener listener : listeners ) {
			caller.accept(listener);
		}
	}
	
	/**
	 * For debugging purposes, prints the cards of a sequence
	 * in the same order as laid out by the iterator.
	 * @return a textual representation of the sequence
	 */
	@Override
	public String toString() {
		StringJoiner cardStrings = new StringJoiner(", ");
		Iterator<Card> iterator = type.getSequenceIterator();
		while( iterator.hasNext() ) {
			Card curr = iterator.next();
			cardStrings.add(curr.toString());
		}
		return String.format("[%s]", cardStrings.toString());
	}
		
}
