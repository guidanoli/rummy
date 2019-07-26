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
	private final ArrayList<CardSequence> addedSequencesQueue = new ArrayList<CardSequence>();
	private final ArrayList<CardSequence> emptySequencesQueue = new ArrayList<CardSequence>();
	private final ArrayList<Card> removedCardsQueue = new ArrayList<Card>();
	
	CardSequenceBuilder newSequenceBuilder(CardSequenceListener listener) {
		return new CardSequenceBuilder()
				.setType(() -> new RankCardSequenceType())
				.addListener(listener);
	}
	
	@BeforeEach
	void init() {
		listener = this;
	}
		
	@Nested
	@DisplayName("the builder")
	class BuilderTest {
		
		@Nested
		@DisplayName("throws IllegalArgumentException")
		class throwsException {
			
			@Test
			@DisplayName("when has one card")
			void testOneCard() {
				CardSequenceBuilder builder = new CardSequenceBuilder()
						.setType(() -> new RankCardSequenceType())
						.addCard(new Card(CardRank.ACE, CardSuit.SPADES));
				assertThrows(IllegalArgumentException.class,
						() -> builder.build());
			}
			
			@Test
			@DisplayName("when has two cards")
			void testTwoCards() {
				CardSequenceBuilder builder = new CardSequenceBuilder()
						.setType(() -> new RankCardSequenceType())
						.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
						.addCard(new Card(CardRank.TWO, CardSuit.SPADES));
				assertThrows(IllegalArgumentException.class,
						() -> builder.build());
			}
			
			@Test
			@DisplayName("when has three distant cards")
			void testThreeDistantCards() {
				CardSequenceBuilder builder = new CardSequenceBuilder()
						.setType(() -> new RankCardSequenceType())
						.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
						.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
						.addCard(new Card(CardRank.FOUR, CardSuit.SPADES));
				assertThrows(IllegalArgumentException.class,
						() -> builder.build());
			}
			
			@Test
			@DisplayName("when has three cards with sequential ranks but different suits")
			void testThreeAlignedCardsDifferentSuits() {
				CardSequenceBuilder builder = new CardSequenceBuilder()
						.setType(() -> new RankCardSequenceType())
						.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
						.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
						.addCard(new Card(CardRank.THREE, CardSuit.HEARTS));
				assertThrows(IllegalArgumentException.class,
						() -> builder.build());
			}
			
		}
		
		@Nested
		@DisplayName("does not throw IllegalArgumentException")
		class DoesNotThrowException {
			
			@Test
			@DisplayName("when instability is allowed and one card is added")
			void testUnstableOneCard() {
				CardSequenceBuilder builder = new CardSequenceBuilder()
						.allowInstability(true)
						.setType(() -> new RankCardSequenceType())
						.addCard(new Card(CardRank.ACE, CardSuit.SPADES));
				assertDoesNotThrow(() -> builder.build());
			}
			
			@Test
			@DisplayName("when type is defined after the addition of card")
			void testUnstableOneCardSwap() {
				CardSequenceBuilder builder = new CardSequenceBuilder()
						.allowInstability(true)
						.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
						.setType(() -> new RankCardSequenceType());
				assertDoesNotThrow(() -> builder.build(),
						() -> "and should build naturally");
				CardSequenceBuilder otherBuilder = new CardSequenceBuilder()
						.allowInstability(true)
						.setType(() -> new RankCardSequenceType())
						.addCard(new Card(CardRank.ACE, CardSuit.SPADES));
				assertEquals(builder.build(),otherBuilder.build(),
						() -> "and should equal sequence if builder methods were swapped");
			}
			
			@Test
			@DisplayName("when adding cards not in order")
			void testAddCardsNotInOrder() {
				CardSequenceBuilder builder = new CardSequenceBuilder()
						.setType(() -> new RankCardSequenceType())
						.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
						.addCard(new Card(CardRank.FIVE, CardSuit.SPADES))
						.addCard(new Card(CardRank.THREE, CardSuit.SPADES))
						.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
						.addCard(new Card(CardRank.FOUR, CardSuit.SPADES))
						.addCard(new Card(CardRank.SIX, CardSuit.SPADES));
				assertDoesNotThrow(() -> builder.build(),
						() -> "and should build naturally");
			}
			
		}
	
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
		@DisplayName("when comparing a sequence to another object")
		void testObjectEqual() {
			CardSequence sequence = newSequenceBuilder(listener)
					.allowInstability(true)
					.build();
			Object o = new Object();
			assertFalse(sequence.equals(o),
					() -> "should return false");
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
					.build();
			CardSequence secondSequence = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
					.addCard(new Card(CardRank.THREE, CardSuit.SPADES))
					.addCard(new Card(CardRank.FOUR, CardSuit.SPADES))
					.addCard(new Card(CardRank.FIVE, CardSuit.SPADES))
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
		
	}

	@Nested
	@DisplayName("the addCard method")
	class AddCardTest {
		
		private CardSequenceBuilder builder;
		
		@BeforeEach
		void init() {
			addedSequencesQueue.clear();
			emptySequencesQueue.clear();
			removedCardsQueue.clear();
			builder = newSequenceBuilder(listener);
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
			assertEquals(0, emptySequencesQueue.size(),
					() -> "should not call removeCardSequence before adding 8th card");
			assertTrue(sequence.addCard(new Card(CardRank.THREE, CardSuit.SPADES)),
					() -> "should let the 8th card be added in the middle");
			assertEquals(1, addedSequencesQueue.size(),
					() -> "should call addCardSequence once after adding 8th card");
			assertEquals(0, emptySequencesQueue.size(),
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
			assertEquals(0, emptySequencesQueue.size(),
					() -> "should not call removeCardSequence after adding 9th card");
			cardSequenceCreated = addedSequencesQueue.get(0);
			assertEquals(cardSequenceCreated.size(),biggerSequence.size(),
					() -> "Card sequences should be of same size");			
		}
		
		@Test
		@DisplayName("when adding an illegal card in the middle of a sequence")
		void testAddingIllegalCardInMiddle() {
			CardSequence sequence = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
					.addCard(new Card(CardRank.THREE, CardSuit.SPADES))
					.build();
			assertAll(
					"should return false",
					() -> assertFalse(sequence.addCard(new Card(CardRank.TWO, CardSuit.HEARTS))),
					() -> assertFalse(sequence.addCard(new Card(CardRank.KING, CardSuit.HEARTS))),
					() -> assertFalse(sequence.addCard(new Card(CardRank.KING, CardSuit.SPADES)))
					);
		}
				
	}
	
	@Nested
	@DisplayName("the removeCard method")
	class RemoveCardTest {
				
		@Test
		@DisplayName("when removing a card from an empty sequence")
		void testEmptySequence() {
			CardSequence sequence = newSequenceBuilder(listener)
					.allowInstability(true)
					.build();
			assertFalse(sequence.removeCard(new Card(CardRank.ACE, CardSuit.SPADES)),
					() -> "should return false");
			assertEquals(0, sequence.size(),
					() -> "should not change the sequence size");
			assertEquals(0, removedCardsQueue.size(),
					() -> "should not notify that a card has been removed");
		}
		
		@Test
		@DisplayName("when removing the only card from a sequence")
		void testSingleCard() {
			CardSequence sequence = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.allowInstability(true)
					.build();
			assertTrue(sequence.removeCard(new Card(CardRank.ACE, CardSuit.SPADES)),
					() -> "should return true");
			assertEquals(0, sequence.size(),
					() -> "should empty the sequence");
			assertEquals(1, removedCardsQueue.size(),
					() -> "should notify that the card has been removed");
		}
		

		@Test
		@DisplayName("when trying to remove a card that's not in a sequence")
		void testSingleWrongCard() {
			CardSequence sequence = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
					.addCard(new Card(CardRank.THREE, CardSuit.SPADES))
					.addCard(new Card(CardRank.FOUR, CardSuit.SPADES))
					.build();
			int initialSize = sequence.size();
			assertAll(
					"should return false",
					() -> assertFalse(sequence.removeCard(new Card(CardRank.TWO, CardSuit.HEARTS))),	// has rank but not suit
					() -> assertFalse(sequence.removeCard(new Card(CardRank.ACE, CardSuit.SPADES))),	// has suit but not rank (before)
					() -> assertFalse(sequence.removeCard(new Card(CardRank.FIVE, CardSuit.SPADES))),	// has suit but not rank (after)
					() -> assertFalse(sequence.removeCard(new Card(CardRank.ACE, CardSuit.HEARTS))) 	// has neither rank or suit
			);
			assertEquals(initialSize, sequence.size(),
					() -> "should not remove any cards");
			assertEquals(0, removedCardsQueue.size(),
					() -> "should not notify that a card has been removed");
		}
		
		@Test
		@DisplayName("when removing the first card from a triple")
		void testFirstFromTriple() {
			CardSequence sequence = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
					.addCard(new Card(CardRank.THREE, CardSuit.SPADES))
					.build();
			assertTrue(sequence.removeCard(new Card(CardRank.ACE, CardSuit.SPADES)),
					() -> "should return true");
			assertEquals(2, sequence.size(),
					() -> "should reduce the sequence size by one");
			assertEquals(1, removedCardsQueue.size(),
					() -> "should notify that a card has been removed");
			assertEquals(0, addedSequencesQueue.size(),
					() -> "should not notify that a sequence has been added");
			CardSequence expected = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
					.addCard(new Card(CardRank.THREE, CardSuit.SPADES))
					.allowInstability(true)
					.build();
			assertEquals(expected, sequence,
					() -> "should shift cards to the left");
		}
		
		@Test
		@DisplayName("when removing the middle card from a triple")
		void testMiddleFromTriple() {
			CardSequence sequence = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
					.addCard(new Card(CardRank.THREE, CardSuit.SPADES))
					.build();
			assertTrue(sequence.removeCard(new Card(CardRank.TWO, CardSuit.SPADES)),
					() -> "should return true");
			assertEquals(1, sequence.size(),
					() -> "should leave only the left cards");
			assertEquals(1, removedCardsQueue.size(),
					() -> "should notify that a card has been removed");
			assertEquals(1, addedSequencesQueue.size(),
					() -> "should notify that a sequence has been added");
			CardSequence expected = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.allowInstability(true)
					.build();
			assertEquals(expected, sequence,
					() -> "should keep only the cards to the left of the removed card");
			CardSequence newSequence = addedSequencesQueue.get(0);
			expected = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.THREE, CardSuit.SPADES))
					.allowInstability(true)
					.build();
			assertEquals(expected, newSequence,
					() -> "should create a new sequence with the cards to the right of the removed card");
			Card removedCard = removedCardsQueue.get(0);
			assertEquals(new Card(CardRank.TWO, CardSuit.SPADES), removedCard,
					() -> "should remove the middle card correctly");
		}
		
		@Test
		@DisplayName("when removing the last card from a triple")
		void testLastFromTriple() {
			CardSequence sequence = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
					.addCard(new Card(CardRank.THREE, CardSuit.SPADES))
					.build();
			assertTrue(sequence.removeCard(new Card(CardRank.THREE, CardSuit.SPADES)),
					() -> "should return true");
			assertEquals(2, sequence.size(),
					() -> "should reduce the sequence size by one");
			assertEquals(1, removedCardsQueue.size(),
					() -> "should notify that a card has been removed");
			assertEquals(0, addedSequencesQueue.size(),
					() -> "should not notify that a sequence has been added");
			CardSequence expected = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
					.allowInstability(true)
					.build();
			assertEquals(expected, sequence,
					() -> "should keep cards as is but without last card");
		}
		
		@Test
		@DisplayName("when partinioning twice a 5-card sequence")
		void testDoublePartition() {
			CardSequence sequence = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
					.addCard(new Card(CardRank.THREE, CardSuit.SPADES))
					.addCard(new Card(CardRank.FOUR, CardSuit.SPADES))
					.addCard(new Card(CardRank.FIVE, CardSuit.SPADES))
					.build();
			assertTrue(sequence.removeCard(new Card(CardRank.TWO, CardSuit.SPADES)),
					() -> "should allow the removal of the second card");
			assertEquals(1, sequence.size(),
					() -> "should render the main sequence down to one card");
			assertEquals(1, removedCardsQueue.size(),
					() -> "should notify that a card has been removed");
			assertEquals(new Card(CardRank.TWO, CardSuit.SPADES), removedCardsQueue.remove(0),
					() -> "should output in the removed card queue the card just removed");
			assertEquals(1, addedSequencesQueue.size(),
					() -> "should notify that a card sequence has been added");
			CardSequence newSequence = addedSequencesQueue.remove(0);
			CardSequence expected = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.THREE, CardSuit.SPADES))
					.addCard(new Card(CardRank.FOUR, CardSuit.SPADES))
					.addCard(new Card(CardRank.FIVE, CardSuit.SPADES))
					.build();
			assertEquals(expected, newSequence,
					() -> "should output in the added sequences the sequence after the removed card");
			assertTrue(newSequence.removeCard(new Card(CardRank.FOUR, CardSuit.SPADES)),
					() -> "should allow the removal of the fourth card");
			assertEquals(1, newSequence.size(),
					() -> "should render the new sequence down to one card");
			assertEquals(1, removedCardsQueue.size(),
					() -> "should notify that a card has been removed");
			assertEquals(new Card(CardRank.FOUR, CardSuit.SPADES), removedCardsQueue.remove(0),
					() -> "should output in the removed card queue the card just removed");
			assertEquals(1, addedSequencesQueue.size(),
					() -> "should notify that a card sequence has been added");
			CardSequence anotherSequence = addedSequencesQueue.remove(0);
			CardSequence anotherExcepted = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.FIVE, CardSuit.SPADES))
					.allowInstability(true)
					.build();
			assertEquals(anotherExcepted, anotherSequence,
					() -> "should output in the added sequences the sequence after the removed card");
		}
		
	}
	
	/* Listener methods */
	
	public void cardSequenceAdded(CardSequence cardSequence) {
		assertNotNull(cardSequence,
				() -> "Card sequence added should not be null");
		addedSequencesQueue.add(cardSequence);
	}

	public void cardSequenceIsEmpty(CardSequence cardSequence) {
		assertNotNull(cardSequence,
				() -> "Card sequence removed should not be null");
		emptySequencesQueue.add(cardSequence);
	}

	public void cardRemovedFromSequence(Card card) {
		assertNotNull(card,
				() -> "Card removed should not be null");
		removedCardsQueue.add(card);
	}

}
