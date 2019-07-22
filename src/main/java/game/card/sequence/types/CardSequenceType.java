package game.card.sequence.types;

import java.util.Iterator;
import game.card.Card;
import game.card.sequence.CardSequenceBuilder;

/**
 * 
 * <p>A card sequence type holds the core logic behind
 * the adding, removing and splitting of cards. Thus,
 * it needs to monitor internally the cards in any way
 * as may convey: Sets, Lists...
 * 
 * <p>It also dictates whether a card can be added or
 * removed, and whether the sequence can be split at
 * a particular position.
 * 
 * @author guidanoli
 *
 */
public interface CardSequenceType {
	
	/**
	 * @param card - card in question
	 * @return {@code true} if card can be added in the sequence 
	 * or {@code false} if else.
	 */
	public boolean canAdd(Card card);
	
	/**
	 * @param card - card in question
	 * @return one of the two:
	 * <ul>
	 * <li>card sequence builder - if a sequence was created
	 * (with all the cards in it already)</li>
	 * <li>{@code null} - if no sequence was created</li>
	 * </ul>
	 */
	public CardSequenceBuilder add(Card card);
	
	/**
	 * @param card - card in question
	 * @return {@code true} if card can be removed from the sequence 
	 * or {@code false} if else.
	 */
	public boolean canRemove(Card card);
	
	/**
	 * @param card - card in question
	 * @return one of the two:
	 * <ul>
	 * <li>card sequence builder - if a sequence was created
	 * (with all the cards in it already)</li>
	 * <li>{@code null} - if no sequence was created</li>
	 * </ul>
	 */
	public CardSequenceBuilder remove(Card card);
	
	/**
	 * @param index - index at which the sequence will be split up
	 * @return {@code true} if the sequence can be split at the index
	 * or {@code false} if else.
	 */
	public boolean canSplit(int index);
	
	/**
	 * @param index - index at which the sequence will be split up
	 * @return sequence created (starting from the card in index
	 * specified by the parameter)
	 */
	public CardSequenceBuilder split(int index);
	
	/**
	 * @return iterator that iterates through all the sequence's cards
	 */
	public Iterator<Card> getSequenceIterator();
	
	/**
	 * Checks whether two cards are sequential to one another
	 * @param first - first card
	 * @param second - second card
	 * @return {@code true} if the first card comes right before
	 * the second card, or {@code false} if else.
	 */
	public boolean areSequential(Card first, Card second);
	
	/**
	 * @return number of cards in the sequence
	 * Can be overwritten if there is a faster way to compute
	 */
	public default int size() {
		int size = 0;
		Iterator<Card> iterator = getSequenceIterator();
		while( iterator.hasNext() ) {
			iterator.next();
			size++;
		}
		return size;
	}
	
}