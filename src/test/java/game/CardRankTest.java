package game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("On the CardRank class")
class CardRankTest {

	private static final CardRank [] cardRanks = CardRank.values();
		
	@Nested
	@DisplayName("the toString method")
	class toStringTest {
		
		@Test
		@DisplayName("for one card rank")
		void testCardNames() {
			assertAll(
					"should return its name properly",
					() -> CardRank.ACE.toString().equals("Ace"),
					() -> CardRank.TWO.toString().equals("Two"),
					() -> CardRank.THREE.toString().equals("Three"),
					() -> CardRank.FOUR.toString().equals("Four"),
					() -> CardRank.FIVE.toString().equals("Five"),
					() -> CardRank.SIX.toString().equals("Six"),
					() -> CardRank.SEVEN.toString().equals("Seven"),
					() -> CardRank.EIGHT.toString().equals("Eight"),
					() -> CardRank.NINE.toString().equals("Nine"),
					() -> CardRank.TEN.toString().equals("Ten"),
					() -> CardRank.JACK.toString().equals("Jack"),
					() -> CardRank.QUEEN.toString().equals("Queen"),
					() -> CardRank.KING.toString().equals("King")
					);
		}
		
		@Test
		@DisplayName("for any two different card ranks")
		void testCardNamesAreUnique() {
			for( int i = 0 ; i < cardRanks.length; i++ ) {
				for( int j = i + 1 ; j < cardRanks.length; j++ ) {
					assertNotEquals(cardRanks[j].toString(), cardRanks[i].toString(),
							() -> "should output different strings");
				}
			}
		}
		
	}
	
	@Nested
	@DisplayName("the getRank method")
	class getRankTest {
		
		@Test
		@DisplayName("for any two different card ranks")
		void testCardRanksAreUnique() {
			for( int i = 0 ; i < cardRanks.length; i++ ) {
				for( int j = i + 1 ; j < cardRanks.length; j++ ) {
					assertNotEquals(cardRanks[j].getRank(), cardRanks[i].getRank(),
							() -> "should output different rank identifiers");
				}
			}
		}
		
	}
	
	@Nested
	@DisplayName("the equals method")
	class equalsTest {

		@Test
		@DisplayName("for the same card rank")
		void testEqualsItself() {
			for( CardRank cardRank : cardRanks ) {
				assertTrue(cardRank.equals(cardRank),
						() -> "should return true");
			}
		}
		
		@Test
		@DisplayName("for any two different card ranks")
		void testEqualsAnother() {
			for( int i = 0 ; i < cardRanks.length - 1; i++ ) {
				assertFalse(cardRanks[i].equals(cardRanks[i+1]),
						() -> "should return false");
			}
		}
		
	}
	
	@Nested
	@DisplayName("the isNeighbor method")
	class isNeighborTest {
		
		@Test
		@DisplayName("for the same card rank")
		void testIsItsOwnNeighbor() {
			for( CardRank cardRank : cardRanks ) {
				assertFalse(cardRank.isNeighbor(cardRank),
						() -> "should return false");
			}
		}
		
		@Test
		@DisplayName("for any two adjacent card ranks")
		void testIsAnothersNeighbor() {
			for( int i = 0 ; i < cardRanks.length - 1; i++ ) {
				assertTrue(cardRanks[i].isNeighbor(cardRanks[i+1]),
						() -> "should return true");
			}
		}
		
		@Test
		@DisplayName("for the first and last card ranks")
		void testNoWrapNeighbor() {
			assertFalse(cardRanks[0].isNeighbor(cardRanks[cardRanks.length-1]),
					() -> "should return false");
		}
		
		@Test
		@DisplayName("for any two card ranks")
		void testIsBidirectional() {
			for( int i = 0 ; i < cardRanks.length; i++ ) {
				for( int j = 0 ; j < cardRanks.length; j++ ) {
					assertEquals( cardRanks[i].isNeighbor(cardRanks[j]),
							cardRanks[j].isNeighbor(cardRanks[i]),
							() -> "should have the same return value both ways"
					);
				}
			}
		}
		
	}
	
	@Test
	@DisplayName("the compare method")
	void testCompare() {
		for( int i = 0 ; i < cardRanks.length; i++ ) {
			for( int j = 0 ; j < cardRanks.length; j++ ) {
				assertEquals( cardRanks[i].compare(cardRanks[j]), i - j,
						() -> "should return the difference between two cards");
			}
		}
	}
	
}
