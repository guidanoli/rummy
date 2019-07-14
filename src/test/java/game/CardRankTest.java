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
	class ToStringTest {
		
		@Test
		@DisplayName("for any card rank")
		void testCardRanksNames() {
			String [] expectedNames = {"Ace", "Two", "Three", "Four", "Five", "Six",
					"Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
			String [] actualNames = new String[cardRanks.length];
			for( int i = 0 ; i < cardRanks.length; i++ ) actualNames[i] = cardRanks[i].toString();
			assertArrayEquals(expectedNames, actualNames,
					() -> "should return its proper name");
		}
		
		@Test
		@DisplayName("for any two different card ranks")
		void testCardRanksNamesAreUnique() {
			for( int i = 0 ; i < cardRanks.length; i++ ) {
				for( int j = i + 1 ; j < cardRanks.length; j++ ) {
					assertNotEquals(cardRanks[j].toString(), cardRanks[i].toString(),
							() -> "should output different strings");
				}
			}
		}
		
	}
	
	@Nested
	@DisplayName("the getIndex method")
	class GetRankTest {
		
		@Test
		@DisplayName("for any two different card ranks")
		void testCardRanksAreUnique() {
			for( int i = 0 ; i < cardRanks.length; i++ ) {
				for( int j = i + 1 ; j < cardRanks.length; j++ ) {
					assertNotEquals(cardRanks[j].getIndex(), cardRanks[i].getIndex(),
							() -> "should output different rank identifiers");
				}
			}
		}
		
	}
	
	@Nested
	@DisplayName("the equals method")
	class EqualsTest {

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
	class IsNeighborTest {
		
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
		@DisplayName("for ace and king ranks")
		void testNoWrapAce() {
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
	
	@Nested
	@DisplayName("the compare method")
	class CompareTest {
		
		@Test
		@DisplayName("for any two card ranks")
		void testCompare() {
			for( int i = 0 ; i < cardRanks.length; i++ ) {
				for( int j = 0 ; j < cardRanks.length; j++ ) {
					assertEquals( cardRanks[i].compare(cardRanks[j]), i - j,
							() -> "should return the difference between them");
				}
			}
		}
		
	}
	
}
