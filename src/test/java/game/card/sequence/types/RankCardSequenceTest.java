package game.card.sequence.types;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import game.card.Card;
import game.card.CardRank;
import game.card.CardSuit;
import game.card.sequence.CardSequence;
import game.card.sequence.CardSequenceBuilder;
import game.card.sequence.types.RankCardSequenceType;
import game.card.sequence.CardSequenceListener;

@DisplayName("On the RankCardSequence class")
class RankCardSequenceTest implements CardSequenceListener {
	
	private CardSequenceListener listener;
	
	CardSequenceBuilder newSequenceBuilder(CardSequenceListener listener) {
		return new CardSequenceBuilder()
				.setType(new RankCardSequenceType())
				.addListener(listener);
	}
	
	@BeforeEach
	void init() {
		listener = this;
	}
		
	@Nested
	@DisplayName("the equals method")
	class EqualTest {
		
		@Test
		@DisplayName("when comparing two empty sequences")
		void testEmptySequencesEqual() {
			CardSequence firstSequence = newSequenceBuilder(listener).allowInstability(true).build();
			CardSequence secondSequence = newSequenceBuilder(listener).allowInstability(true).build();
			assertAll(
					"should return true",
					() -> assertTrue(firstSequence.equals(secondSequence)),
					() -> assertTrue(secondSequence.equals(firstSequence)),
					() -> assertTrue(firstSequence.equals(firstSequence))
					);
		}
				
		@Test
		@DisplayName("when comparing two sequences with the same card (one each)")
		void testSequencesWithSameCardEqual() {
			CardSequence firstSequence = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.allowInstability(true)
					.build();
			CardSequence secondSequence = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.allowInstability(true)
					.build();
			assertAll(
					"should return true",
					() -> assertTrue(firstSequence.equals(secondSequence)),
					() -> assertTrue(secondSequence.equals(firstSequence))
					);
		}
		
		@Test
		@DisplayName("when comparing two sequences with different cards (one each)")
		void testSequencesWithDifferentCardsEqual() {
			CardSequence firstSequence = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.allowInstability(true)
					.build();
			CardSequence secondSequence = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
					.allowInstability(true)
					.build();
			assertAll(
					"should return false",
					() -> assertFalse(firstSequence.equals(secondSequence)),
					() -> assertFalse(secondSequence.equals(firstSequence))
					);
		}
		
		@Test
		@DisplayName("when comparing two sequences with the same cards (multiple)")
		void testSequencesWithSameMultipleCardsEqual() {
			CardSequence firstSequence = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
					.addCard(new Card(CardRank.THREE, CardSuit.SPADES))
					.addCard(new Card(CardRank.FOUR, CardSuit.SPADES))
					.addCard(new Card(CardRank.FIVE, CardSuit.SPADES))
					.allowInstability(true)
					.build();
			CardSequence secondSequence = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
					.addCard(new Card(CardRank.THREE, CardSuit.SPADES))
					.addCard(new Card(CardRank.FOUR, CardSuit.SPADES))
					.addCard(new Card(CardRank.FIVE, CardSuit.SPADES))
					.allowInstability(true)
					.build();
			assertAll(
					"should return true",
					() -> assertTrue(firstSequence.equals(secondSequence)),
					() -> assertTrue(secondSequence.equals(firstSequence))
					);
		}
		
		@Test
		@DisplayName("when comparing two sequences with different cards (multiple)")
		void testSequencesWithDifferentMultipleCardsEqual() {
			CardSequence firstSequence = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.allowInstability(true)
					.build();
			CardSequence secondSequence = newSequenceBuilder(listener)
					.allowInstability(true)
					.build();
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
		private CardSequenceBuilder builder;
		
		@BeforeEach
		void init() {
			addedSequencesQueue.clear();
			removedSequencesQueue.clear();
			builder = newSequenceBuilder(this);
		}
		
		@Test
		@DisplayName("when adding one card")
		void testAddOneCard() {
			Card [] cards = { 
					new Card(CardRank.ACE, CardSuit.SPADES)
			};
			CardSequence sequence = builder.allowInstability(true).build();
			for(int i = 0; i < cards.length; i++) {
				assertTrue(sequence.addCard(cards[i]),
						() -> "adds successfully to the sequence");
			}
			Iterator<Card> iterator = sequence.iterator();
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
			CardSequence sequence = builder.allowInstability(true).build();
			for(int i = 0; i < cards.length; i++) {
				assertTrue(sequence.addCard(cards[i]),
						() -> "adds successfully to the sequence");
			}
			Iterator<Card> iterator = sequence.iterator();
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
			CardSequence sequence = builder.allowInstability(true).build();
			for(int i = 0; i < cards.length; i++) {
				assertTrue(sequence.addCard(cards[i]),
						() -> "adds successfully to the sequence");
			}
			Iterator<Card> iterator = sequence.iterator();
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
			CardSequence sequence = builder.allowInstability(true).build();
			for(int i = 0; i < cards.length; i++) {
				assertTrue(sequence.addCard(cards[i]),
						() -> "adds successfully to the sequence");
			}
			Iterator<Card> iterator = sequence.iterator();
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
			CardSequence sequence = builder.allowInstability(true).build();
			for(int i = cards.length - 1; i >= 0 ; i--) {
				assertTrue(sequence.addCard(cards[i]),
						() -> "adds successfully to the sequence");
			}
			Iterator<Card> iterator = sequence.iterator();
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
			CardSequence sequence = builder.allowInstability(true).build();
			assertTrue(sequence.addCard(cards[0]),
					() -> "should add the first card");
			assertFalse(sequence.addCard(cards[1]),
					() -> "should not add the second card");
			Iterator<Card> iterator = sequence.iterator();
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
			CardSequence sequence = builder.allowInstability(true).build();
			assertTrue(sequence.addCard(cards[0]),
					() -> "should add the king");
			assertFalse(sequence.addCard(cards[1]),
					() -> "should not add the ace");
			Iterator<Card> iterator = sequence.iterator();
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
			CardSequence sequence = builder.allowInstability(true).build();
			for(int i = 0; i < cards.length; i++) {
				assertTrue(sequence.addCard(cards[order[i]]),
						() -> "adds cards successfully to the sequence");
			}
			Iterator<Card> iterator = sequence.iterator();
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
			Card [] cards = {
					new Card(CardRank.ACE, CardSuit.SPADES),
					new Card(CardRank.TWO, CardSuit.SPADES),
					new Card(CardRank.THREE, CardSuit.SPADES),
					new Card(CardRank.FOUR, CardSuit.SPADES),
					new Card(CardRank.FIVE, CardSuit.SPADES),
					new Card(CardRank.SIX, CardSuit.SPADES),
					new Card(CardRank.SEVEN, CardSuit.SPADES)
			};
			CardSequence sequence = builder.allowInstability(true).build();
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
			assertTrue(smallerSequence.isStable(),
					() -> "should create stable card sequences");
			assertTrue(biggerSequence.isStable(),
					() -> "should create stable card sequences");
			
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

		public void cardSequenceIsEmpty(CardSequence cardSequence) {
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

	public void cardSequenceIsEmpty(CardSequence cardSequence) {
		
	}

	public void cardRemovedFromSequence(Card card) {
		
	}

}