package game;

public enum CardSuit {

	HEARTS(0, "Hearts"),
	SPADES(1, "Spades"),
	DIAMONDS(2, "Diamonds"),
	CLUBS(3, "Clubs");
	
	private int index;
	private String name;
	
	CardSuit(int index, String name) {
		this.index = index;
		this.name = name;
	}
	
	public int getIndex() { return index; }
	public String toString() { return name; }
	public boolean equals(CardSuit cardSuit) {
		return getIndex() == cardSuit.getIndex();
	}
	
}
