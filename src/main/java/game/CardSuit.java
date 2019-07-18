package game;

import java.util.Objects;

/**
 * 
 * <p>A card suit is an unordered attribute of a card,
 * which divides a deck into four equally sized sets.
 * Some games may even account for the suit colour, which
 * is either black or red.
 * 
 * @author guidanoli
 *
 */
public enum CardSuit {

	HEARTS(0, "Hearts"),
	SPADES(1, "Spades"),
	DIAMONDS(2, "Diamonds"),
	CLUBS(3, "Clubs");
	
	private int index;
	private String name;
	
	CardSuit(int index, String name) {
		this.index = index;
		this.name = Objects.requireNonNull(name,
				() -> "Null name is not accepted");
	}
	
	/**
	 * @return index of card suit, which is
	 * arbitrary and does not propose any
	 * kind of privilege of one over the other.
	 * The suit index should be used for
	 * identification purposes only.
	 * The indexes go from 0 to 3.
	 */
	public int getIndex() { return index; }
	
	/**
	 * @return string that identifies the card suit in text.
	 * Each card suit has its unique name.
	 */
	public String toString() { return name; }
	
	/**
	 * Checks if two card suits have the same index,
	 * and, thus, in other words, are the same.
	 * @param anotherCardSuit - another card suit
	 * @return {@code true} if both are equal
	 * @see #getIndex()
	 */
	public boolean equals(CardSuit anotherCardSuit) {
		return getIndex() == anotherCardSuit.getIndex();
	}
	
}
