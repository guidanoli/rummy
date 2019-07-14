package game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("On the CardSuit class")
class CardSuitTest {
	
	private static final CardSuit [] cardSuits = CardSuit.values();
	
	@Nested
	@DisplayName("the toString method")
	class ToStringTest {
		
		@Test
		@DisplayName("for any card suit")
		void testCardSuitsNames() {
			String [] expectedNames = {"Hearts", "Spades", "Diamonds", "Clubs"};
			String [] actualNames = new String[cardSuits.length];
			for( int i = 0 ; i < cardSuits.length; i++ ) actualNames[i] = cardSuits[i].toString();
			assertArrayEquals(expectedNames, actualNames,
					() -> "should return its proper name");
		}

		@Test
		@DisplayName("for any two different card suits")
		void testCardRanksNamesAreUnique() {
			for( int i = 0 ; i < cardSuits.length; i++ ) {
				for( int j = i + 1 ; j < cardSuits.length; j++ ) {
					assertNotEquals(cardSuits[j].toString(), cardSuits[i].toString(),
							() -> "should output different strings");
				}
			}
		}
		
	}
	
	@Nested
	@DisplayName("the getIndex method")
	class GetRankTest {
		
		@Test
		@DisplayName("for any two different card suits")
		void testCardRanksAreUnique() {
			for( int i = 0 ; i < cardSuits.length; i++ ) {
				for( int j = i + 1 ; j < cardSuits.length; j++ ) {
					assertNotEquals(cardSuits[j].getIndex(), cardSuits[i].getIndex(),
							() -> "should output different suit identifiers");
				}
			}
		}
		
	}
	
	@Nested
	@DisplayName("the equals method")
	class EqualsTest {

		@Test
		@DisplayName("for the same card suit")
		void testEqualsItself() {
			for( CardSuit cardSuit : cardSuits ) {
				assertTrue(cardSuit.equals(cardSuit),
						() -> "should return true");
			}
		}
		
		@Test
		@DisplayName("for any two different card suits")
		void testEqualsAnother() {
			for( int i = 0 ; i < cardSuits.length - 1; i++ ) {
				assertFalse(cardSuits[i].equals(cardSuits[i+1]),
						() -> "should return false");
			}
		}
		
	}
	
}
