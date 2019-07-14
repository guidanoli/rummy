package game;

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
	
	private int rank;
	private String name;
	
	CardRank(int rank, String name) {
		this.rank = rank;
		this.name = name;
	}
	
	public int getIndex() { return rank; }
	public String toString() { return name; }
	public boolean equals(CardRank cardRank) {
		assert cardRank != null;
		return compare(cardRank) == 0;
	}
	public boolean isNeighbor(CardRank cardRank) {
		assert cardRank != null;
		return Math.abs(getIndex()-cardRank.getIndex()) == 1;
	}
	public int compare(CardRank anotherCardRank) {
		assert anotherCardRank != null;
		return getIndex() - anotherCardRank.getIndex();
	}
	
}
