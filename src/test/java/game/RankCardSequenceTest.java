package game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("On the RankCardSequence class")
class RankCardSequenceTest implements CardSequenceListener {
	
	private final CardSequenceListener listener = this; 
	
	@Nested
	@DisplayName("the constructor")
	class ConstructorTest {
		
		@Test
		@DisplayName("when parsing a null listener")
		void testNullListener() {
			assertThrows(NullPointerException.class,
					() -> new RankCardSequence(null),
					() -> "should throw a NullPointerException"
					);
		}
		
		@Test
		@DisplayName("when passing an existing listener")
		void testNonNullListener() {
			assertDoesNotThrow(
					() -> new RankCardSequence(listener),
					() -> "should not throw any exceptions whatsoever"
					);
		}
		
	}
	
	@Nested
	@DisplayName("the equals method")
	class EqualTest {
		
		@Test
		@DisplayName("when comparing two empty sequences")
		void testEmptySequencesEqual() {
			CardSequence firstSequence = new RankCardSequence(listener);
			CardSequence secondSequence = new RankCardSequence(listener);
			assertAll(
					"should return true",
					() -> assertTrue(firstSequence.equals(secondSequence)),
					() -> assertTrue(secondSequence.equals(firstSequence)),
					() -> assertTrue(firstSequence.equals(firstSequence))
					);
		}
		
		@Test
		@DisplayName("when comparing a sequence with null")
		void testNullSequenceEqual() {
			CardSequence firstSequence = new RankCardSequence(listener);
			assertThrows(NullPointerException.class, 
					() -> firstSequence.equals(null),
					() -> "should throw a NullPointerException");
		}
		
		@Test
		@DisplayName("when comparing two existing sequences")
		void testNonNullSequenceEqual() {
			CardSequence firstSequence = new RankCardSequence(listener);
			assertDoesNotThrow(
					() -> firstSequence.equals(firstSequence),
					() -> "should not throw any exceptions whatsoever");
		}
		
		@Test
		@DisplayName("when comparing two sequences with the same card (one each)")
		void testSequencesWithSameCardEqual() {
			CardSequence firstSequence = new RankCardSequence(listener);
			CardSequence secondSequence = new RankCardSequence(listener);
			firstSequence.addCard(new Card(CardRank.ACE, CardSuit.SPADES));
			secondSequence.addCard(new Card(CardRank.ACE, CardSuit.SPADES));
			assertAll(
					"should return true",
					() -> assertTrue(firstSequence.equals(secondSequence)),
					() -> assertTrue(secondSequence.equals(firstSequence))
					);
		}
		
		@Test
		@DisplayName("when comparing two sequences with different cards (one each)")
		void testSequencesWithDifferentCardsEqual() {
			CardSequence firstSequence = new RankCardSequence(listener);
			CardSequence secondSequence = new RankCardSequence(listener);
			firstSequence.addCard(new Card(CardRank.ACE, CardSuit.SPADES));
			secondSequence.addCard(new Card(CardRank.TWO, CardSuit.SPADES));
			assertAll(
					"should return false",
					() -> assertFalse(firstSequence.equals(secondSequence)),
					() -> assertFalse(secondSequence.equals(firstSequence))
					);
		}
		
		@Test
		@DisplayName("when comparing two sequences with the same cards (multiple)")
		void testSequencesWithSameMultipleCardsEqual() {
			CardSequence firstSequence = new RankCardSequence(listener);
			CardSequence secondSequence = new RankCardSequence(listener);
			firstSequence.addCard(new Card(CardRank.ACE, CardSuit.SPADES));
			firstSequence.addCard(new Card(CardRank.TWO, CardSuit.SPADES));
			firstSequence.addCard(new Card(CardRank.THREE, CardSuit.SPADES));
			firstSequence.addCard(new Card(CardRank.FOUR, CardSuit.SPADES));
			firstSequence.addCard(new Card(CardRank.FIVE, CardSuit.SPADES));
			secondSequence.addCard(new Card(CardRank.ACE, CardSuit.SPADES));
			secondSequence.addCard(new Card(CardRank.TWO, CardSuit.SPADES));
			secondSequence.addCard(new Card(CardRank.THREE, CardSuit.SPADES));
			secondSequence.addCard(new Card(CardRank.FOUR, CardSuit.SPADES));
			secondSequence.addCard(new Card(CardRank.FIVE, CardSuit.SPADES));
			assertAll(
					"should return true",
					() -> assertTrue(firstSequence.equals(secondSequence)),
					() -> assertTrue(secondSequence.equals(firstSequence))
					);
		}
		
		@Test
		@DisplayName("when comparing two sequences with different cards (multiple)")
		void testSequencesWithDifferentMultipleCardsEqual() {
			CardSequence firstSequence = new RankCardSequence(listener);
			CardSequence secondSequence = new RankCardSequence(listener);
			firstSequence.addCard(new Card(CardRank.ACE, CardSuit.SPADES));
			assertAll(
					"should return false",
					() -> assertFalse(firstSequence.equals(secondSequence)),
					() -> assertFalse(secondSequence.equals(firstSequence))
					);
			secondSequence.addCard(new Card(CardRank.ACE, CardSuit.SPADES));
			firstSequence.addCard(new Card(CardRank.TWO, CardSuit.SPADES));
			assertAll(
					"should return false",
					() -> assertFalse(firstSequence.equals(secondSequence)),
					() -> assertFalse(secondSequence.equals(firstSequence))
					);
			secondSequence.addCard(new Card(CardRank.TWO, CardSuit.SPADES));
			firstSequence.addCard(new Card(CardRank.THREE, CardSuit.SPADES));
			assertAll(
					"should return false",
					() -> assertFalse(firstSequence.equals(secondSequence)),
					() -> assertFalse(secondSequence.equals(firstSequence))
					);
			
		}
		
		// TODO: Compare card sequences of different types
		// on a separate test file called CardSequenceTest
		
	}

	public void addCardSequence(CardSequence cardSequence) {
		
	}

}
