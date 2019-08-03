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
	@DisplayName("the compareSuits method")
	class CompareSuitsTest {
		
		@Test
		@DisplayName("when comparing two cards")
		void testCompareAllSuits() {
			CardSuit firstCardSuit = CardSuit.HEARTS;
			for( CardSuit secondCardSuit : CardSuit.values() ) {
				Card firstCard = new Card(CardRank.ACE,firstCardSuit);
				Card secondCard = new Card(CardRank.TWO,secondCardSuit);
				if( !firstCard.equalSuits(secondCard) ) {
					assertNotEquals( 0,
							firstCard.compareSuits(secondCard),
							() -> "should return a value different from zero for different suits");
				} else {
					assertEquals( 0,
							firstCard.compareSuits(secondCard),
							() -> "should return zero for the same suit");
				}
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

		@Test
		@DisplayName("when comparing a card and another object")
		void testNotEqual() {
			Card card = new Card(CardRank.ACE, CardSuit.SPADES);
			Object o = new Object();
			assertFalse(card.equals(o), () -> "should return false");
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
	
	@Nested
	@DisplayName("the hashCode method")
	class HashCodeTest {
		
		@Test
		@DisplayName("when hashing the same card")
		void testHashSame() {		
			for( CardRank rank : CardRank.values() ) {
				for( CardSuit suit : CardSuit.values() ) {
					Card card = new Card(rank,suit);
					Card otherCard = new Card(rank,suit);
					assertEquals( card.hashCode(),
							otherCard.hashCode(),
							() -> "should return the same value" );
				}
			}
		}
		
		@Test
		@DisplayName("when hashing different cards")
		void testHashDifferent() {
			CardRank [] ranks = CardRank.values();
			CardSuit [] suits = CardSuit.values();
			for( int i = 0 ; i < ranks.length; i++ ) {
				for( int j = 0 ; j < suits.length; j++ ) {
					Card firstCard = new Card(ranks[i], suits[j]);
					for( int k = i+1; k < ranks.length; k++ ) {
						for( int l = j+1; l < suits.length; l++ ) {
							Card secondCard = new Card(ranks[k], suits[l]);
							assertNotEquals(firstCard.hashCode(), secondCard.hashCode(),
									() -> "should return different values");
						}
					}
				}
			}
		}
				
	}
	
	@Nested
	@DisplayName("the compare method")
	class CompareTest {
		
		@Test
		@DisplayName("when comparing the same card")
		void testCompareSame() {
			for( CardRank rank : CardRank.values() ) {
				for( CardSuit suit : CardSuit.values() ) {
					Card card = new Card(rank,suit);
					Card otherCard = new Card(rank,suit);
					assertEquals(0, card.compare(otherCard),
							() -> "should return 0");
				}
			}
		}
		
		@Test
		@DisplayName("when comparing different cards")
		void testCompareDifferent() {
			CardRank [] ranks = CardRank.values();
			CardSuit [] suits = CardSuit.values();
			for( int i = 0 ; i < ranks.length; i++ ) {
				for( int j = 0 ; j < suits.length; j++ ) {
					Card firstCard = new Card(ranks[i], suits[j]);
					for( int k = i+1; k < ranks.length; k++ ) {
						for( int l = j+1; l < suits.length; l++ ) {
							Card secondCard = new Card(ranks[k], suits[l]);
							assertNotEquals(0, firstCard.compare(secondCard),
									() -> "should not return 0");
						}
					}
				}
			}
		}
		
	}

}
