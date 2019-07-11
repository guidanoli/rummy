package game;

public class Card {

	private CardRank rank;
	private CardSuit suit;
	
	public Card(CardRank rank, CardSuit suit) {
		this.rank = rank;
		this.suit = suit;
	}
	
	public CardRank getRank() { return rank; }
	public CardSuit getSuit() { return suit; }
	public String toString() {
		return 	String.format( "%s of %s",
					getRank().toString(),
					getSuit().toString()
				);
	}
	public boolean equals(Card card) {
		return 	getRank().equals(card.getRank()) &&
				getSuit().equals(card.getSuit());
	}
	public boolean isNeighbor(Card card) {
		return	(getSuit().equals(card.getSuit()) &&
				getRank().isNeighbor(card.getRank()))
				||
				(getRank().equals(card.getRank()) &&
				!getSuit().equals(card.getSuit()));
	}
	
}
