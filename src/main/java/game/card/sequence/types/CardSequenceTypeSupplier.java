package game.card.sequence.types;

import game.card.sequence.CardSequenceBuilder;

/**
 * 
 * <p>A card sequence type supplier is a simple
 * interface that allows the card sequence
 * builder build many types without having
 * to ask for a new object to the client.
 * 
 * <p>The client can even specify through this
 * lambda the constructor parameters etc.
 * The lambda would then look something like:
 * 
 * <p>{@code () -> new MyCardSequnceType(...)}
 * 
 * @author guidanoli
 * @see CardSequenceType
 * @see CardSequenceBuilder
 * 
 */
public interface CardSequenceTypeSupplier {

	/**
	 * The lambda function itself
	 * @return a new instance of card sequence type
	 */
	public CardSequenceType supply();
	
}
