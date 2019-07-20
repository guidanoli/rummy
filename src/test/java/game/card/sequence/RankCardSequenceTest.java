package game.card.sequence;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import game.card.Card;
import game.card.CardRank;
import game.card.CardSuit;
import game.card.sequence.CardSequence;
import game.card.sequence.CardSequenceListener;
import game.card.sequence.RankCardSequence;

@DisplayName("On the RankCardSequence class")
class RankCardSequenceTest implements CardSequenceListener {
	
	private CardSequenceListener listener;
	private RankCardSequence sequence;
	
	@BeforeEach
	void init() {
		listener = this;
		sequence = new RankCardSequence();
		sequence.addListener(listener);
	}
	
	@AfterEach
	void end() {
		assertTrue(sequence.hasValidState(),
				() -> "the sequence should have a valid state");
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
						() -> "it should add one card exactly");
				assertTrue(cards[i].equals(iterator.next()),
						() -> "the card should be found on the iterator");
			}
		}
		
		@Test
		@DisplayName("when adding two cards in crescent order")
		void testAddTwoCardsCrescent() {
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
						() -> "it should add two cards exactly");
				assertTrue(cards[i].equals(iterator.next()),
						() -> "the cards should be found on the iterator");
			}
		}
		
		@Test
		@DisplayName("when adding two cards in decrescent order")
		void testAddTwoCardsDecrescent() {
			Card [] cards = {
					new Card(CardRank.TWO, CardSuit.SPADES),
					new Card(CardRank.ACE, CardSuit.SPADES)
			};
			for(int i = 0; i < cards.length; i++) {
				assertTrue(sequence.addCard(cards[i]),
						() -> "adds successfully to the sequence");
			}
			Iterator<Card> iterator = sequence.getSequenceIterator();
			for(int i = cards.length - 1; i >= 0; i--) {
				assertTrue(iterator.hasNext(),
						() -> "it should add two cards exactly");
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
		@DisplayName("when adding nine cards and alternating endings")
		void testAddingAndAlternatingEnds() {
			Card [] cards = {
					new Card(CardRank.ACE, CardSuit.SPADES),
					new Card(CardRank.TWO, CardSuit.SPADES),
					new Card(CardRank.THREE, CardSuit.SPADES),
					new Card(CardRank.FOUR, CardSuit.SPADES),
					new Card(CardRank.FIVE, CardSuit.SPADES),
					new Card(CardRank.SIX, CardSuit.SPADES),
					new Card(CardRank.SEVEN, CardSuit.SPADES),
					new Card(CardRank.EIGHT, CardSuit.SPADES),
					new Card(CardRank.NINE, CardSuit.SPADES)
			};
			int [] order = {4,3,5,2,6,1,7,0,8};
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
		@DisplayName("when partitioning the sequence twice")
		void testDoublePartition() {
			sequence = new RankCardSequence(this);
			Card [] cards = {
					new Card(CardRank.ACE, CardSuit.SPADES),
					new Card(CardRank.TWO, CardSuit.SPADES),
					new Card(CardRank.THREE, CardSuit.SPADES),
					new Card(CardRank.FOUR, CardSuit.SPADES),
					new Card(CardRank.FIVE, CardSuit.SPADES),
					new Card(CardRank.SIX, CardSuit.SPADES),
					new Card(CardRank.SEVEN, CardSuit.SPADES)
			};
			for(int i = 0; i < cards.length; i++) sequence.addCard(cards[i]);
			assertEquals(cards.length, sequence.size(),
					() -> "should add all cards");
			assertEquals(0, addedSequencesQueue.size(),
					() -> "should not call addCardSequence before adding 8th card");
			assertEquals(0, removedSequencesQueue.size(),
					() -> "should not call removeCardSequence before adding 8th card");
			assertTrue(sequence.addCard(new Card(CardRank.THREE, CardSuit.SPADES)),
					() -> "should let the 8th card be added in the middle");
			assertEquals(1, addedSequencesQueue.size(),
					() -> "should call addCardSequence once after adding 8th card");
			assertEquals(0, removedSequencesQueue.size(),
					() -> "should not call removeCardSequence after adding 8th card");
			CardSequence cardSequenceCreated = addedSequencesQueue.get(0);

			boolean isNewSequenceSmaller = cardSequenceCreated.size() == 3;
			CardSequence smallerSequence = isNewSequenceSmaller ? cardSequenceCreated : sequence;
			CardSequence biggerSequence = isNewSequenceSmaller ? sequence : cardSequenceCreated;
			assertEquals(3, smallerSequence.size(),
					() -> "should create a sequence with 5 cards after adding 8th card");
			assertEquals(5, biggerSequence.size(),
					() -> "should create a sequence with 3 cards after adding 8th card");
			assertTrue(smallerSequence.hasValidState(),
					() -> "should create card sequences with a valid state");
			assertTrue(biggerSequence.hasValidState(),
					() -> "should create card sequences with a valid state");
			
			addedSequencesQueue.clear();
			assertTrue(biggerSequence.addCard(new Card(CardRank.FIVE, CardSuit.SPADES)),
					() -> "should let the 9th card be added in the middle of the larger sequence");
			assertEquals(1, addedSequencesQueue.size(),
					() -> "should call addCardSequence once after adding 9th card");
			assertEquals(0, removedSequencesQueue.size(),
					() -> "should not call removeCardSequence after adding 9th card");
			cardSequenceCreated = addedSequencesQueue.get(0);
			assertEquals(cardSequenceCreated.size(),biggerSequence.size(),
					() -> "Card sequences should be of same size");			
		}
		
		public void cardSequenceAdded(CardSequence cardSequence) {
			assertNotNull(cardSequence,
					() -> "Card sequence added should not be null");
			addedSequencesQueue.add(cardSequence);
		}

		public void cardSequenceRemoved(CardSequence cardSequence) {
			assertNotNull(cardSequence,
					() -> "Card sequence removed should not be null");
			removedSequencesQueue.add(cardSequence);
		}

		public void cardRemovedFromSequence(Card card) {
			
		}
		
	}
	
	@Nested
	@DisplayName("the removeCard method")
	class RemoveCardTest {
		
		
		
	}
	
	public void cardSequenceAdded(CardSequence cardSequence) {
		
	}

	public void cardSequenceRemoved(CardSequence cardSequence) {
		
	}

	public void cardRemovedFromSequence(Card card) {
		
	}

}
