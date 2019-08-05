package game.table;

import java.util.Iterator;
import java.util.LinkedList;

import game.card.Card;
import game.sequence.CardSequence;
import game.sequence.CardSequenceListener;

/**
 * 
 * <p>A card sequence table is an abstraction of a collection of card sequences.
 * It handles new card sequences being created or being removed (when empty)
 * through the {@link CardSequenceListener} interface.
 * 
 * <p>One can access the card sequence by simply iterating through them as
 * any iterators:
 * <p>{@code for(CardSequence sequence : cardSequenceTable) { ... }}
 * 
 * <p>Also, one can add or remove card sequences through the
 * {@link #addSequence(CardSequence)} and {@link #removeSequence(CardSequence)}
 * methods.
 * 
 * <p>Or even clear the whole table with the {@link #clearTable()} method.
 * 
 * @author guidanoli
 * @see CardSequence
 *
 */
public class CardSequenceTable implements Iterable<CardSequence> {

	/**
	 * The list that contains all of the card sequences currently
	 * in the table.
	 */
	private LinkedList<CardSequence> cardSequenceList = new LinkedList<CardSequence>();
	
	/**
	 * The listener that handles events beyond the scope of the
	 * card sequence table (e.g. card being removed)
	 */
	private CardSequenceTableListener tableListener;
		
	/**
	 * The listener that handles events within the scope of the
	 * card sequence table (e.g. sequences being added/removed)
	 */
	private CardSequenceListener thisListener = new CardSequenceListener() {
		
		public void cardSequenceIsEmpty(CardSequence cardSequence) {
			// if a card sequence is empty, it must be removed
			CardSequenceTable.this.removeSequence(cardSequence);
		}
		
		public void cardSequenceAdded(CardSequence cardSequence) {
			// if a new card sequence is created, it must be added
			CardSequenceTable.this.addSequence(cardSequence);
		}
		
		public void cardRemovedFromSequence(Card card) {
			// delegates the event to the CardSequenceTableListener
			CardSequenceTable.this.tableListener.cardRemoved(card);
		}
		
	};
	
	/**
	 * Constructs a card sequence table
	 * @param tableListener - observer that will capture events that
	 * cannot be handled by the card sequence table class
	 * @see CardSequenceTableListener
	 */
	public CardSequenceTable(CardSequenceTableListener tableListener) {
		this.tableListener = tableListener;
	}
	
	/**
	 * Tries to add a card sequence to the table.
	 * @param sequence - card sequence to be added
	 * @return {@code true} if card sequence could be added,
	 * or {@code false} if else.
	 */
	public boolean addSequence(CardSequence sequence) {
		if (!sequence.isStable()) return false; // no unstable card sequences
		for(CardSequence seq : this) if(seq == sequence) return false; // no duplicates
		sequence.addListener(thisListener);
		cardSequenceList.add(sequence);
		return true;
	}
	
	/**
	 * Tries to remove a card sequence from the table.
	 * @param sequence - card sequence to be removed
	 * @return {@code true} if card sequence could be removed,
	 * or {@code false} if else.
	 */
	public boolean removeSequence(CardSequence sequence) {
		boolean exists = cardSequenceList.contains(sequence);
		if(!exists) return false; // cannot remove an inexistent card sequence
		cardSequenceList.remove(sequence);
		return true;
	}
	
	/**
	 * Clears the card sequence table from all card sequences.
	 */
	public void clearTable() {
		cardSequenceList = new LinkedList<CardSequence>();
	}
	
	/**
	 * @return number of card sequences
	 */
	public int size() {
		return cardSequenceList.size();
	}
	
	/**
	 * @return iterator that iterates through all of the card
	 * sequences in the table, which can also be done by the
	 * {@code for} notation:
	 * <p>{@code for(CardSequence sequence : cardSequenceTable) {...}}
	 */
	public Iterator<CardSequence> iterator() {
		return cardSequenceList.iterator();
	}
	
}
