package game.card;

import java.util.Objects;

import game.card.sequence.GenericCardSequence;

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
	
	/**
	 * Constructs a card object
	 * @param rank - card rank
	 * @param suit - card suit
	 * @throws NullPointerException if one of the fields is {@code null}
	 */
	public Card(CardRank rank, CardSuit suit) {
		this.rank = Objects.requireNonNull(rank,
				() -> "Null rank is not accepted");
		this.suit = Objects.requireNonNull(suit,
				() -> "Null suit is not accepted");
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
	 * Compare cards' ranks in quantitative means
	 * @param anotherCard - another card
	 * @return {@code rank of this card - rank of the other card}
	 * @see CardRank#compare(CardRank)
	 * @throws NullPointerException if card provided is {@code null}
	 */
	public int compareRanks(Card anotherCard) {
		Objects.requireNonNull(anotherCard,
				() -> "Null card parameter is not accepted");
		return getRank().compare(anotherCard.getRank());
	}
	
	/**
	 * Checks whether two cards share the same suit
	 * @param anotherCard - another card
	 * @return {@code true} if they share the same suit
	 * @see CardSuit#equals(CardSuit)
	 * @throws NullPointerException if card provided is {@code null}
	 */
	public boolean equalSuits(Card anotherCard) {
		Objects.requireNonNull(anotherCard,
				() -> "Null card parameter is not accepted");
		return getSuit().equals(anotherCard.getSuit());
	}
	
	/**
	 * Checks whether two cards share the same rank
	 * @param anotherCard - another card
	 * @return {@code true} if they share the same rank
	 * @see CardRank#equals(CardRank)
	 * @throws NullPointerException if card provided is {@code null}
	 */
	public boolean equalRanks(Card anotherCard) {
		Objects.requireNonNull(anotherCard,
				() -> "Null card parameter is not accepted");
		return getRank().equals(anotherCard.getRank());
	}
	
	/**
	 * Checks whether two cards share the same information
	 * @param anotherCard - another card
	 * @return {@code true} if they share the same information
	 * @see #equalRanks(Card)
	 * @see #equalSuits(Card)
	 * @throws NullPointerException if card provided is {@code null}
	 */
	public boolean equals(Card anotherCard) {
		Objects.requireNonNull(anotherCard,
				() -> "Null card parameter is not accepted");
		return equalRanks(anotherCard) && equalSuits(anotherCard);
	}
	
	/**
	 * Checks whether two cards are neighbours
	 * @param anotherCard - another card
	 * @return {@code true} if they can be found side by side
	 * on a card sequence
	 * @see GenericCardSequence
	 * @throws NullPointerException if card provided is {@code null}
	 */
	public boolean isNeighbour(Card anotherCard) {
		Objects.requireNonNull(anotherCard,
				() -> "Null card parameter is not accepted");
		return	(getSuit().equals(anotherCard.getSuit()) &&
				getRank().isNeighbour(anotherCard.getRank()))
				||
				(getRank().equals(anotherCard.getRank()) &&
				!getSuit().equals(anotherCard.getSuit()));
	}
		
}
