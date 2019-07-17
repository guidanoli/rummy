package game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("On the RankCardSequence class")
class RankCardSequenceTest implements CardSequenceListener {
	
	private CardSequenceListener listener;
	private RankCardSequence sequence;
	
	@BeforeEach
	void init() {
		listener = this;
		sequence = new RankCardSequence(listener);
	}
	
	@AfterEach
	void end() {
		assertTrue(sequence.hasValidState(),
				() -> "the sequence should have a valid state");
	}
	
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

	@Nested
	@DisplayName("the addCard method")
	class AddCardTest implements CardSequenceListener {
		
		private final ArrayList<CardSequence> addedSequencesQueue = new ArrayList<CardSequence>();
		private final ArrayList<CardSequence> removedSequencesQueue = new ArrayList<CardSequence>();
		
		@BeforeEach
		void init() {
			addedSequencesQueue.clear();
			removedSequencesQueue.clear();
		}
		
		@Test
		@DisplayName("when adding a null card")
		void testNullCard() {
			assertThrows(NullPointerException.class,
					() -> sequence.addCard(null),
					() -> "should throw a NullPointerException");
		}
		
		@Test
		@DisplayName("when adding a non-null card")
		void testNonNullCard() {
			assertDoesNotThrow(
					() -> sequence.add(new Card(CardRank.ACE, CardSuit.SPADES)),
					() -> "should not throw any exception whatsoever");
		}
		
		@Test
		@DisplayName("when adding one card")
		void testAddOneCard() {
			Card [] cards = { 
					new Card(CardRank.ACE, CardSuit.SPADES)
			};
			for(int i = 0; i < cards.length; i++) {
				assertTrue(sequence.addCard(cards[i]),
						() -> "adds successfully to the sequence");
			}
			Iterator<Card> iterator = sequence.getSequenceIterator();
			for(int i = 0; i < cards.length; i++) {
				assertTrue(iterator.hasNext(),
						() -> "it should add three cards exactly");
				assertTrue(cards[i].equals(iterator.next()),
						() -> "the cards should be found on the iterator");
			}
		}
		
		@Test
		@DisplayName("when adding two cards")
		void testAddTwoCards() {
			Card [] cards = { 
					new Card(CardRank.ACE, CardSuit.SPADES),
					new Card(CardRank.TWO, CardSuit.SPADES)
			};
			for(int i = 0; i < cards.length; i++) {
				assertTrue(sequence.addCard(cards[i]),
						() -> "adds successfully to the sequence");
			}
			Iterator<Card> iterator = sequence.getSequenceIterator();
			for(int i = 0; i < cards.length; i++) {
				assertTrue(iterator.hasNext(),
						() -> "it should add three cards exactly");
				assertTrue(cards[i].equals(iterator.next()),
						() -> "the cards should be found on the iterator");
			}
		}
		
		@Test
		@DisplayName("when adding three cards")
		void testAddThreeCards() {
			Card [] cards = { 
					new Card(CardRank.ACE, CardSuit.SPADES),
					new Card(CardRank.TWO, CardSuit.SPADES),
					new Card(CardRank.THREE, CardSuit.SPADES)
			};
			for(int i = 0; i < cards.length; i++) {
				assertTrue(sequence.addCard(cards[i]),
						() -> "adds successfully to the sequence");
			}
			Iterator<Card> iterator = sequence.getSequenceIterator();
			for(int i = 0; i < cards.length; i++) {
				assertTrue(iterator.hasNext(),
						() -> "it should add three cards exactly");
				assertTrue(cards[i].equals(iterator.next()),
						() -> "the cards should be found on the iterator");
			}
		}
		
		@Test
		@DisplayName("when adding three cards in reverse order")
		void testAddThreeCardsInReverse() {
			Card [] cards = { 
					new Card(CardRank.ACE, CardSuit.SPADES),
					new Card(CardRank.TWO, CardSuit.SPADES),
					new Card(CardRank.THREE, CardSuit.SPADES)
			};
			for(int i = cards.length - 1; i >= 0 ; i--) {
				assertTrue(sequence.addCard(cards[i]),
						() -> "adds successfully to the sequence");
			}
			Iterator<Card> iterator = sequence.getSequenceIterator();
			for(int i = 0; i < cards.length; i++) {
				assertTrue(iterator.hasNext(),
						() -> "it should add three cards exactly");
				assertTrue(cards[i].equals(iterator.next()),
						() -> "the cards should be found on the iterator");
			}
			assertFalse(iterator.hasNext(),
					() -> "it should add three cards exactly");
		}
		
		@Test
		@DisplayName("when adding two equal cards in an empty sequence")
		void testAddTwoEqualCards() {
			Card [] cards = { 
					new Card(CardRank.ACE, CardSuit.SPADES),
					new Card(CardRank.ACE, CardSuit.SPADES)
			};
			assertTrue(sequence.addCard(cards[0]),
					() -> "should add the first card");
			assertFalse(sequence.addCard(cards[1]),
					() -> "should not add the second card");
			Iterator<Card> iterator = sequence.getSequenceIterator();
			assertTrue(iterator.hasNext(),
					() -> "it should add one card exactly");
			assertTrue(cards[0].equals(iterator.next()),
					() -> "the card should be found on the iterator");
			assertFalse(iterator.hasNext(),
					() -> "it should add one card exactly");
		}
		
		@Test
		@DisplayName("when adding a king and an ace")
		void testNoWrapAce() {
			Card [] cards = { 
					new Card(CardRank.KING, CardSuit.SPADES),
					new Card(CardRank.ACE, CardSuit.SPADES)
			};
			assertTrue(sequence.addCard(cards[0]),
					() -> "should add the king");
			assertFalse(sequence.addCard(cards[1]),
					() -> "should not add the ace");
			Iterator<Card> iterator = sequence.getSequenceIterator();
			assertTrue(iterator.hasNext(),
					() -> "it should add one card exactly");
			assertTrue(cards[0].equals(iterator.next()),
					() -> "the king should be found on the iterator");
			assertFalse(iterator.hasNext(),
					() -> "it should add one cards exactly");
		}
		
		@Test
		@DisplayName("when adding five cards and alternating endings")
		void testAddingAndAlternatingEnds() {
			Card [] cards = {
					new Card(CardRank.ACE, CardSuit.SPADES),
					new Card(CardRank.TWO, CardSuit.SPADES),
					new Card(CardRank.THREE, CardSuit.SPADES),
					new Card(CardRank.FOUR, CardSuit.SPADES),
					new Card(CardRank.FIVE, CardSuit.SPADES)
			};
			int [] order = {2,1,3,0,4};
			for(int i = 0; i < cards.length; i++) {
				assertTrue(sequence.addCard(cards[order[i]]),
						() -> "adds cards successfully to the sequence");
			}
			Iterator<Card> iterator = sequence.getSequenceIterator();
			for(int i = 0; i < cards.length; i++) {
				assertTrue(iterator.hasNext(),
						() -> "it should add all five cards exactly");
				assertTrue(cards[i].equals(iterator.next()),
						() -> "the cards should be found on the iterator");
			}
			assertFalse(iterator.hasNext(),
					() -> "it should add five cards exactly");
		}
		
		@Test
		@DisplayName("when adding a card in the middle of a 5+ card sequence")
		void testAddingInTheMiddle() {
			sequence = new RankCardSequence(this);
			Card [] cards = {
					new Card(CardRank.ACE, CardSuit.SPADES),
					new Card(CardRank.TWO, CardSuit.SPADES),
					new Card(CardRank.THREE, CardSuit.SPADES),
					new Card(CardRank.FOUR, CardSuit.SPADES),
					new Card(CardRank.FIVE, CardSuit.SPADES)
			};
			for(int i = 0; i < cards.length; i++) sequence.addCard(cards[i]);
			assertEquals(0, addedSequencesQueue.size(),
					() -> "should not call addCardSequence before adding 6th card");
			assertEquals(0, removedSequencesQueue.size(),
					() -> "should not call removeCardSequence before adding 6th card");
			assertTrue(sequence.addCard(new Card(CardRank.THREE, CardSuit.SPADES)),
					() -> "should let the 6th be added in the middle");
			assertEquals(1, addedSequencesQueue.size(),
					() -> "should call addCardSequence once after adding 6th card");
			assertEquals(0, removedSequencesQueue.size(),
					() -> "should not call removeCardSequence after adding 6th card");
			CardSequence cardSequenceCreated = addedSequencesQueue.get(0);
			assertTrue(cardSequenceCreated.hasValidState(),
					() -> "should create a card sequence with a valid state");
			Iterator<Card> 	newIterator = cardSequenceCreated.getSequenceIterator(),
							iterator = sequence.getSequenceIterator();
			int new_size = 0, size = 0;
			while( newIterator.hasNext() ) {
				new_size++; newIterator.next();
			}
			while( iterator.hasNext() ) {
				size++; iterator.next();
			}
			assertEquals(size, new_size,
					() -> "Card sequences should be of same size");
		}

		public void addCardSequence(CardSequence cardSequence) {
			addedSequencesQueue.add(cardSequence);
		}

		public void removeCardSequence(CardSequence cardSequence) {
			removedSequencesQueue.add(cardSequence);
		}
		
	}
	
	public void addCardSequence(CardSequence cardSequence) {
		
	}

	public void removeCardSequence(CardSequence cardSequence) {
		
	}

}
