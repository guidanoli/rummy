package game.card;

import java.util.Objects;

/**
 * 
 * <p>A card rank is an ordered attribute of a card,
 * with which you can sort cards of the same suit
 * always in the same way. They can be translated to
 * a sequence of discreet indexes ranging from 1 to 13,
 * but also as a collection of unique strings for each
 * card rank.
 * 
 * <p><b>Disclaimer<b>: Contrary to <u>real</u> game rules, the {@link #ACE}
 * will not be able to be put right after the {@link #KING} in a sequence,
 * thus will always reside before the {@link #TWO}.
 * 
 * @author guidanoli
 *
 */
public enum CardRank {

	ACE(1, "Ace"),
	TWO(2, "Two"),
	THREE(3, "Three"),
	FOUR(4, "Four"),
	FIVE(5, "Five"),
	SIX(6, "Six"),
	SEVEN(7, "Seven"),
	EIGHT(8, "Eight"),
	NINE(9, "Nine"),
	TEN(10, "Ten"),
	JACK(11, "Jack"),
	QUEEN(12, "Queen"),
	KING(13, "King");
	
	private int index;
	private String name;
	
	CardRank(int index, String name) {
		this.index = index;
		this.name = Objects.requireNonNull(name,
				() -> "Null name is not accepted");
	}
	
	/**
	 * @return index of card in a sequence, being
	 * {@link #ACE} the lowest and {@link #KING}
	 * the highest values.
	 */
	public int getIndex() { return index; }
	
	/**
	 * @return string that identifies the card rank in text.
	 * Each card rank has its unique name.
	 */
	public String toString() { return name; }
	
	/**
	 * Checks if two card ranks have the same index,
	 * and, thus, in other words, are the same.
	 * @param anotherCardRank - another card rank
	 * @return {@code true} if both are equal
	 * @see #compare(CardRank)
	 */
	public boolean equals(CardRank anotherCardRank) {
		assert anotherCardRank != null;
		return compare(anotherCardRank) == 0;
	}
	
	/**
	 * Checks if two card ranks have a difference of
	 * their indexes of one. That is, if they are
	 * adjacent to one another in a rank sequence.
	 * @param anotherCardRank - another card rank
	 * @return {@code true} if both are neighbours
	 * @see #compare(CardRank)
	 */
	public boolean isNeighbour(CardRank anotherCardRank) {
		assert anotherCardRank != null;
		return Math.abs(compare(anotherCardRank)) == 1;
	}
	
	/**
	 * Compare the indexes of two cards in a rank sequence.
	 * @param anotherCardRank - another card rank
	 * @return {@code index of this - index of the other card}
	 * <p>Thus, if the value above is:
	 * <li>{@code zero}, the card ranks are the same.
	 * <li>+-{@code one}, the card ranks are neighbours.
	 * @see #equals(CardRank)
	 * @see #isNeighbour(CardRank)
	 */
	public int compare(CardRank anotherCardRank) {
		assert anotherCardRank != null;
		return getIndex() - anotherCardRank.getIndex();
	}
	
}
