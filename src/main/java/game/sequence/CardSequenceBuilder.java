package game.sequence;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import game.card.Card;
import game.sequence.types.CardSequenceType;

/**
 * A card sequence builder is responsible for building 
 * stable card sequences and facilitating the object's
 * instantiation. This is an application of the Builder
 * Design Pattern from the GoF.
 * 
 * @author guidanoli
 *
 */
public class CardSequenceBuilder {
		
	private Supplier<CardSequenceType> sequenceTypeSupplier;
	private Set<CardSequenceListener> listeners;
	private Set<Card> cardSet;
	private boolean allowInstability;
	
	/**
	 * Constructs a card sequence builder
	 */
	public CardSequenceBuilder() { 
		listeners = new HashSet<CardSequenceListener>();
		cardSet = new HashSet<Card>();
		allowInstability = false;
	}
	
	/**
	 * Adds a listener to the listener set
	 * @param listener - card sequence listener
	 * @return this builder
	 */
	public CardSequenceBuilder addListener(CardSequenceListener listener) {
		listeners.add(listener);
		return this;
	}
	
	/**
	 * Adds each listener from the set to this builder's listener set
	 * @param listenerSet - set of card sequence listeners
	 * @return this builder
	 */
	public CardSequenceBuilder addListenerSet(Set<CardSequenceListener> listenerSet) {
		for ( CardSequenceListener listener : listenerSet ) {
			listeners.add(listener);
		}
		return this;
	}
	
	/**
	 * Adds a card after the last one added to the card list
	 * @param card - new card to be added
	 * @return this builder
	 */
	public CardSequenceBuilder addCard(Card card) {
		cardSet.add(card);
		return this;
	}
	
	/**
	 * Sets card sequence type through a supplier, which is an obligatory field
	 * @param typeSupplier - card sequence type supplier (lambda function)
	 * @return this builder
	 * @see CardSequenceType
	 * @see CardSequenceTypeSupplier
	 */
	public CardSequenceBuilder setType(Supplier<CardSequenceType> typeSupplier) {
		this.sequenceTypeSupplier = typeSupplier;
		return this;
	}
	
	/**
	 * Allow or not card sequence instability
	 * @param allow - whether to allow ({@code true}) or not ({@code true})
	 * @return this builder
	 */
	public CardSequenceBuilder allowInstability(boolean allow) {
		allowInstability = allow;
		return this;
	}
	
	/**
	 * Build card sequence according to attributes set by the builder methods
	 * @return card sequence
	 * @throws IllegalArgumentException - whenever a card sequence type is unspecified or
	 * the card sequence would be unstable and instability is not allowed 
	 */
	public CardSequence build() {
		if ( sequenceTypeSupplier == null ) throw new IllegalArgumentException("Undefined card sequence type");
		CardSequenceType sequenceType = sequenceTypeSupplier.get();
		CardSequence cardSequence = new CardSequence(sequenceType);
		if ( !sequenceType.addCardSet(cardSet) || (!allowInstability && !cardSequence.isStable()) ) {
			throw new IllegalArgumentException("Invalid card sequence");
		}
		for ( CardSequenceListener listener : listeners ) cardSequence.addListener(listener);
		return cardSequence;
	}
		
}
