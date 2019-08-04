package game.card;

import java.util.Objects;

import game.sequence.CardSequence;

/**
 * <p>A card always handles two informations: its <b>suit</b>
 * and its <b>rank</b>. If two cards share the same informations,
 * they are, by definition, the same card.
 * 
 * <p>These informations are by no means alterable, but can be
 * consulted at any time through the methods displayed by this class.
 * 
 * @see CardRank
 * @see CardSuit
 * 
 * @author guidanoli
 *
 */
public class Card {

	private CardRank rank;
	private CardSuit suit;
	
	/* auxiliary constants */
	private final static int numOfSuits = CardSuit.values().length;
	
	/**
	 * Constructs a card object
	 * @param rank - card rank
	 * @param suit - card suit
	 */
	public Card(CardRank rank, CardSuit suit) {
		this.rank = rank;
		this.suit = suit;
	}
	
	/**
	 * @return card's rank
	 * @see CardRank
	 */
	public CardRank getRank() { return rank; }
	
	/**
	 * @return card's suit
	 * @see CardSuit
	 */
	public CardSuit getSuit() { return suit; }
	
	/**
	 * @return string that identifies the card in text.
	 * Each card has its unique name.
	 * @see CardRank#toString()
	 * @see CardSuit#toString()
	 */
	public String toString() {
		return 	String.format( "%s of %s",
					getRank().toString(),
					getSuit().toString()
				);
	}
	
	/**
	 * Compare cards' suits in quantitative means
	 * @param anotherCard - another card
	 * @return arbitrary comparison between suits for
	 * sorting reasons only
	 */
	public int compareSuits(Card anotherCard) {
		return getSuit().compareTo(anotherCard.getSuit());
	}
	
	/**
	 * Compare cards' ranks in quantitative means
	 * @param anotherCard - another card
	 * @return {@code rank of this card - rank of the other card}
	 * @see CardRank#compare(CardRank)
	 */
	public int compareRanks(Card anotherCard) {
		return getRank().compare(anotherCard.getRank());
	}
	
	/**
	 * Checks whether two cards share the same suit
	 * @param anotherCard - another card
	 * @return {@code true} if they share the same suit
	 * @see CardSuit#equals(CardSuit)
	 */
	public boolean equalSuits(Card anotherCard) {
		return getSuit().equals(anotherCard.getSuit());
	}
	
	/**
	 * Checks whether two cards share the same rank
	 * @param anotherCard - another card
	 * @return {@code true} if they share the same rank
	 * @see CardRank#equals(CardRank)
	 */
	public boolean equalRanks(Card anotherCard) {
		return getRank().equals(anotherCard.getRank());
	}
	
	/**
	 * Checks whether two cards share the same information
	 * @param o - another card
	 * @return {@code true} if they share the same information
	 * @see #equalRanks(Card)
	 * @see #equalSuits(Card)
	 */
	@Override
	public boolean equals(Object o) {
		if( o instanceof Card ) {
			Card card = (Card) o;
			return equalRanks(card) && equalSuits(card);
		}
		return false;
	}
	
	/**
	 * Checks whether two cards are neighbours
	 * @param anotherCard - another card
	 * @return {@code true} if they can be found side by side
	 * on a card sequence
	 * @see CardSequence
	 */
	public boolean isNeighbour(Card anotherCard) {
		return	(getSuit().equals(anotherCard.getSuit()) &&
				getRank().isNeighbour(anotherCard.getRank()))
				||
				(getRank().equals(anotherCard.getRank()) &&
				!getSuit().equals(anotherCard.getSuit()));
	}
	
	/**
	 * Creates a hash for the card concerning its suit and rank
	 * @return hash code
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getRank(), getSuit());
	}
	
	/**
	 * <p>Compares two cards such that for every card {@code c}, the following always holds:
	 * <ul><li>For every card {@code e}, only when {@code e == c}, {@code compare(e) == 0}.</li>
	 * <li>For every card {@code f} and {@code g}, {@code compare(f) == compare(g)} only if {@code f==g}.</li></ul>
	 * @param anotherCard - another card
	 * @return the delta concerning suit and rank between the two cards
	 */
	public int compare(Card anotherCard) {
		return compareSuits(anotherCard) + numOfSuits * compareRanks(anotherCard);
	}
	
}
