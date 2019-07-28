package game.card.sequence;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import game.card.*;
import game.card.sequence.types.*;

@DisplayName("On the CardSequence class")
class CardSequenceTest {

	@Nested
	@DisplayName("The equals method")
	class ComparingDifferentTypesTest {
		
		@Test
		@DisplayName("when comparing sequences of different types with many cards but one in common")
		void testSuitAndRankTriple() {
			
			CardSequence rankSequence = new CardSequenceBuilder()
					.setType(() -> new RankCardSequenceType())
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
					.addCard(new Card(CardRank.THREE, CardSuit.SPADES))
					.build();
			
			CardSequence suitSequence = new CardSequenceBuilder()
					.setType(() -> new SuitCardSequenceType())
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.ACE, CardSuit.HEARTS))
					.addCard(new Card(CardRank.ACE, CardSuit.DIAMONDS))
					.build();
			
			assertNotEquals(rankSequence, suitSequence,
					() -> "should return false");
			
		}
		
		@Test
		@DisplayName("when comparing sequences of different types with the same card")
		void testSuitAndRankSingleEqual() {
			
			CardSequence rankSequence = new CardSequenceBuilder()
					.setType(() -> new RankCardSequenceType())
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.allowInstability(true)
					.build();
			
			CardSequence suitSequence = new CardSequenceBuilder()
					.setType(() -> new SuitCardSequenceType())
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.allowInstability(true)
					.build();
			
			assertEquals(rankSequence, suitSequence,
					() -> "should return true");
			
		}
		
		@Nested
		@DisplayName("when comparing sequences with the same card")
		class SingleDifferentCardsDifferentTypes {
			
			@Test
			@DisplayName("which is of same rank and different suit")
			void testSameRankDifferentSuit() {
				
				CardSequence rankSequence = new CardSequenceBuilder()
						.setType(() -> new RankCardSequenceType())
						.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
						.allowInstability(true)
						.build();
				
				CardSequence suitSequence = new CardSequenceBuilder()
						.setType(() -> new SuitCardSequenceType())
						.addCard(new Card(CardRank.ACE, CardSuit.HEARTS))
						.allowInstability(true)
						.build();
				
				assertNotEquals(rankSequence, suitSequence,
						() -> "should return false");
				
			}
			
			@Test
			@DisplayName("which is of different rank and same suit")
			void testDifferentRankSameSuit() {
				
				CardSequence rankSequence = new CardSequenceBuilder()
						.setType(() -> new RankCardSequenceType())
						.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
						.allowInstability(true)
						.build();
				
				CardSequence suitSequence = new CardSequenceBuilder()
						.setType(() -> new SuitCardSequenceType())
						.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
						.allowInstability(true)
						.build();
				
				assertNotEquals(rankSequence, suitSequence,
						() -> "should return false");
				
			}

			@Test
			@DisplayName("which is of different rank and different suit")
			void testDifferentRankAndSuit() {
				
				CardSequence rankSequence = new CardSequenceBuilder()
						.setType(() -> new RankCardSequenceType())
						.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
						.allowInstability(true)
						.build();
				
				CardSequence suitSequence = new CardSequenceBuilder()
						.setType(() -> new SuitCardSequenceType())
						.addCard(new Card(CardRank.TWO, CardSuit.HEARTS))
						.allowInstability(true)
						.build();
				
				assertNotEquals(rankSequence, suitSequence,
						() -> "should return false");
				
			}
		}
		
	}

}
