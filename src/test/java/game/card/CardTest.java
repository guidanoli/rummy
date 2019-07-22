package game.card;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import game.card.Card;
import game.card.CardRank;
import game.card.CardSuit;

@DisplayName("On the Card class")
class CardTest {
		
	@Nested
	@DisplayName("the getRank method")
	class GetRankTest {
		
		@Test
		@DisplayName("when operating on a valid card")
		void testGetRank() {
			for( CardRank cardRank : CardRank.values() ) {
				assertEquals(new Card(cardRank,CardSuit.SPADES).getRank(), cardRank,
						() -> "should return its rank");
			}
		}
		
	}
	
	@Nested
	@DisplayName("the getSuit method")
	class GetSuitTest {
		
		@Test
		@DisplayName("when operating on a valid card")
		void testGetSuit() {
			for( CardSuit cardSuit : CardSuit.values() ) {
				assertEquals(new Card(CardRank.ACE, cardSuit).getSuit(), cardSuit,
						() -> "should return its suit");
			}
		}
		
	}
	
	@Nested
	@DisplayName("the toString method")
	class ToStringTest {
		
		@Test
		@DisplayName("should return a string which contains")
		void testConstains() {
			for( CardRank cardRank : CardRank.values() ) {
				for( CardSuit cardSuit : CardSuit.values() ) {
					Card card = new Card(cardRank, cardSuit);
					String cardString = card.toString(); 
					assertTrue(cardString.contains(cardRank.toString()),
							() -> "the card rank string");
					assertTrue(cardString.contains(cardSuit.toString()),
							() -> "the card suit string");
				}
			}
		}
		
	}
	
	@Nested
	@DisplayName("the compareRanks method")
	class CompareRanksTest {
		
		@Test
		@DisplayName("when comparing two cards of the same suit")
		void testCompareSameSuit() {
			CardRank firstCardRank = CardRank.SEVEN;
			for( CardRank secondCardRank : CardRank.values() ) {
				Card firstCard = new Card(firstCardRank,CardSuit.SPADES);
				Card secondCard = new Card(secondCardRank,CardSuit.SPADES);
				assertEquals(
						firstCard.compareRanks(secondCard),
						firstCardRank.compare(secondCardRank),
						() -> "should return their ranks difference");
			}
		}
		
		@Test
		@DisplayName("when comparing two cards of different suits")
		void testCompareDifferentSuit() {
			CardRank firstCardRank = CardRank.SEVEN;
			for( CardRank secondCardRank : CardRank.values() ) {
				Card firstCard = new Card(firstCardRank,CardSuit.SPADES);
				Card secondCard = new Card(secondCardRank,CardSuit.CLUBS);
				assertEquals(
						firstCard.compareRanks(secondCard),
						firstCardRank.compare(secondCardRank),
						() -> "should return their ranks difference");
			}
		}
		
	}
	
	@Nested
	@DisplayName("the equalSuits method")
	class EqualSuitsTest {
		
		@Test
		@DisplayName("when comparing two cards of the same suit")
		void testEqualSuits() {
			CardRank firstCardRank = CardRank.SEVEN;
			for( CardRank secondCardRank : CardRank.values() ) {
				Card firstCard = new Card(firstCardRank,CardSuit.SPADES);
				Card secondCard = new Card(secondCardRank,CardSuit.SPADES);
				assertTrue(
						firstCard.equalSuits(secondCard),
						() -> "should return true");
			}
		}

		@Test
		@DisplayName("when comparing two cards of different suits")
		void testDifferentSuits() {
			CardRank firstCardRank = CardRank.SEVEN;
			for( CardRank secondCardRank : CardRank.values() ) {
				Card firstCard = new Card(firstCardRank,CardSuit.SPADES);
				Card secondCard = new Card(secondCardRank,CardSuit.CLUBS);
				assertFalse(
						firstCard.equalSuits(secondCard),
						() -> "should return false");
			}
		}
		
	}
	
	@Nested
	@DisplayName("the equals method")
	class EqualsTest {
		
		@Test
		@DisplayName("when comparting two cards")
		void testEqual() {
			Card targetCard = new Card(CardRank.ACE, CardSuit.SPADES);
			assertFalse(targetCard.equals(new Card(CardRank.ACE, CardSuit.CLUBS)),
					() -> "of same ranks but different suits should return false");
			assertFalse(targetCard.equals(new Card(CardRank.TWO, CardSuit.SPADES)),
					() -> "of same suits but different ranks should return false");
			assertFalse(targetCard.equals(new Card(CardRank.TWO, CardSuit.CLUBS)),
					() -> "of different ranks and suits should return false");
			assertTrue(targetCard.equals(new Card(CardRank.ACE, CardSuit.SPADES)),
					() -> "of same ranks and suits should return true");
		}
		
	}
	
	@Nested
	@DisplayName("the isNeighbour method")
	class IsNeighbourTest {
		
		@Test
		@DisplayName("when comparing two cards of the same neighbourhood")
		void testNeighbourhood() {
			Card targetCard = new Card(CardRank.SEVEN, CardSuit.SPADES);
			assertAll(
					"should return true",
					() -> assertTrue(targetCard.isNeighbour(new Card(CardRank.SIX,CardSuit.SPADES))),
					() -> assertTrue(targetCard.isNeighbour(new Card(CardRank.EIGHT,CardSuit.SPADES))),
					() -> assertTrue(targetCard.isNeighbour(new Card(CardRank.SEVEN,CardSuit.CLUBS))),
					() -> assertTrue(targetCard.isNeighbour(new Card(CardRank.SEVEN,CardSuit.DIAMONDS))),
					() -> assertTrue(targetCard.isNeighbour(new Card(CardRank.SEVEN,CardSuit.HEARTS)))
					);
		}
		
		@Test
		@DisplayName("when comparing a card with itself")
		void testSameCard() {
			Card targetCard = new Card(CardRank.SEVEN, CardSuit.SPADES);
			assertFalse(targetCard.isNeighbour(targetCard),
					() -> "should return false");
		}
		
		@Test
		@DisplayName("when comparing ace with king of same suit")
		void testNoWrapAce() {
			assertFalse(new Card(CardRank.ACE, CardSuit.SPADES)
					.isNeighbour(new Card(CardRank.KING, CardSuit.SPADES)),
					() -> "should return false");
		}
		
	}
	

}
