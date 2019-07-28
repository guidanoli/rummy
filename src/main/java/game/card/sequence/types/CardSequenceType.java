package game.card.sequence.types;

import java.util.Iterator;
import java.util.Set;

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
	 * <p>Tries to add all the cards in a set. Can leave the
	 * sequence in an unstable state as long as it returns
	 * false so that a new card sequence be supplied.
	 * <p>Minimum size does not need to be considered since
	 * the Builder already deals with it formerly.
	 * @param cardSet - set of cards to be added
	 * @return {@code true} if it could add all of the cards and 
	 * {@code false} otherwise.
	 */
	public boolean addCardSet(Set<Card> cardSet);
	
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
	 * <p>An iterator later used to compare two sequences. Thus, any
	 * implementation has to ensure that a sequence always outputs
	 * the same iterator. Thus, if the cards are arranged in a set,
	 * an arbitrary order/comparator has to be established.
	 * @return iterator that iterates through all the sequence's cards
	 */
	public Iterator<Card> getSequenceIterator();
		
	/**
	 * @return number of cards in the sequence
	 */
	public int size();
	
}