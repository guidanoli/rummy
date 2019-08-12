package game.sequence.types;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;

import game.card.Card;
import game.card.CardRank;
import game.card.CardSuit;
import game.sequence.CardSequence;
import game.sequence.CardSequenceBuilder;
import game.sequence.CardSequenceListener;
import game.sequence.types.SuitCardSequenceType;

@DisplayName("On the SuitCardSequenceType class")
class SuitCardSequenceTest implements CardSequenceListener {

	private CardSequenceListener listener;
	private final ArrayList<CardSequence> addedSequencesQueue = new ArrayList<CardSequence>();
	private final ArrayList<CardSequence> emptySequencesQueue = new ArrayList<CardSequence>();
	private final ArrayList<Card> removedCardsQueue = new ArrayList<Card>();
	
	CardSequenceBuilder newSequenceBuilder(CardSequenceListener listener) {
		return new CardSequenceBuilder()
				.setType(() -> new SuitCardSequenceType())
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
						.setType(() -> new SuitCardSequenceType())
						.addCard(new Card(CardRank.ACE, CardSuit.SPADES));
				assertThrows(IllegalArgumentException.class,
						() -> builder.build());
			}
			
			@Test
			@DisplayName("when has two cards")
			void testTwoCards() {
				CardSequenceBuilder builder = new CardSequenceBuilder()
						.setType(() -> new SuitCardSequenceType())
						.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
						.addCard(new Card(CardRank.ACE, CardSuit.HEARTS));
				assertThrows(IllegalArgumentException.class,
						() -> builder.build());
			}
			
			@Test
			@DisplayName("when has three cards with one repetition")
			void testThreeDistantCards() {
				CardSequenceBuilder builder = new CardSequenceBuilder()
						.setType(() -> new SuitCardSequenceType())
						.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
						.addCard(new Card(CardRank.ACE, CardSuit.HEARTS))
						.addCard(new Card(CardRank.ACE, CardSuit.SPADES));
				assertThrows(IllegalArgumentException.class,
						() -> builder.build());
			}
			
			@Test
			@DisplayName("when has three cards with different suits but different ranks")
			void testThreeAlignedCardsDifferentSuits() {
				CardSequenceBuilder builder = new CardSequenceBuilder()
						.setType(() -> new SuitCardSequenceType())
						.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
						.addCard(new Card(CardRank.TWO, CardSuit.HEARTS))
						.addCard(new Card(CardRank.THREE, CardSuit.CLUBS));
				assertThrows(IllegalArgumentException.class,
						() -> builder.build());
			}
			
			@Test
			@DisplayName("when trying to add in rank order")
			void testAddingInRankOrder() {
				CardSequenceBuilder builder = new CardSequenceBuilder()
						.setType(() -> new SuitCardSequenceType())
						.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
						.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
						.addCard(new Card(CardRank.THREE, CardSuit.SPADES));
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
						.setType(() -> new SuitCardSequenceType())
						.addCard(new Card(CardRank.ACE, CardSuit.SPADES));
				assertDoesNotThrow(() -> builder.build());
			}
			
			@Test
			@DisplayName("when type is defined after the addition of card")
			void testUnstableOneCardSwap() {
				CardSequenceBuilder builder = new CardSequenceBuilder()
						.allowInstability(true)
						.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
						.setType(() -> new SuitCardSequenceType());
				assertDoesNotThrow(() -> builder.build(),
						() -> "and should build naturally");
				CardSequenceBuilder otherBuilder = new CardSequenceBuilder()
						.allowInstability(true)
						.setType(() -> new SuitCardSequenceType())
						.addCard(new Card(CardRank.ACE, CardSuit.SPADES));
				assertEquals(builder.build(),otherBuilder.build(),
						() -> "and should equal sequence if builder methods were swapped");
			}
			
			@Test
			@DisplayName("when adding cards in any order whatsoever")
			void testAddCardsNotInOrder() {
				Card [] cards = {
						new Card(CardRank.ACE, CardSuit.SPADES),
						new Card(CardRank.ACE, CardSuit.HEARTS),
						new Card(CardRank.ACE, CardSuit.CLUBS),
						new Card(CardRank.ACE, CardSuit.DIAMONDS),
				};
				ArrayList<CardSequence> sequences = new ArrayList<CardSequence>();
				int len = cards.length;
				for (int i = 0; i < len; i++) {
					for (int j = 0; j < len; j++) {
						for (int k = 0; k < len; k++) {
							for (int l = 0; l < len; l++) {
								if ( i != j && k != i && k != j && l != i && l != j && l != k ) {
									sequences.add(newSequenceBuilder(listener)
											.addCard(cards[i]) // first card
											.addCard(cards[j]) // second card
											.addCard(cards[k]) // third card
											.addCard(cards[l]) // fourth card
											.build()); 
								}
							}
						}
					}
				}
				for (int i = 1; i < sequences.size(); i++) {
					CardSequence prev = sequences.get(i-1),
							curr = sequences.get(i);
					assertEquals(prev, curr,
							() -> "should result in the same sequence");
				}
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
					() -> assertEquals(firstSequence,secondSequence),
					() -> assertEquals(secondSequence,firstSequence),
					() -> assertEquals(firstSequence,firstSequence)
					);
		}
				
		@Test
		@DisplayName("when comparing a sequence to another object")
		void testObjectEqual() {
			CardSequence sequence = newSequenceBuilder(listener)
					.allowInstability(true)
					.build();
			Object o = new Object();
			assertNotEquals(sequence, o,
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
					() -> assertEquals(firstSequence,secondSequence),
					() -> assertEquals(secondSequence,firstSequence)
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
					() -> assertNotEquals(firstSequence,secondSequence),
					() -> assertNotEquals(secondSequence,firstSequence)
					);
		}
		
		@Test
		@DisplayName("when comparing two sequences with the same cards (multiple)")
		void testSequencesWithSameMultipleCardsEqual() {
			CardSequence firstSequence = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.ACE, CardSuit.HEARTS))
					.addCard(new Card(CardRank.ACE, CardSuit.DIAMONDS))
					.build();
			CardSequence secondSequence = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.ACE, CardSuit.HEARTS))
					.addCard(new Card(CardRank.ACE, CardSuit.DIAMONDS))
					.build();
			assertAll(
					"should return true",
					() -> assertEquals(firstSequence,secondSequence),
					() -> assertEquals(secondSequence,firstSequence)
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
					() -> assertNotEquals(firstSequence,secondSequence),
					() -> assertNotEquals(secondSequence,firstSequence)
					);
			secondSequence.addCard(new Card(CardRank.ACE, CardSuit.SPADES));
			firstSequence.addCard(new Card(CardRank.ACE, CardSuit.HEARTS));
			assertAll(
					"should return false",
					() -> assertNotEquals(firstSequence,secondSequence),
					() -> assertNotEquals(secondSequence,firstSequence)
					);
			secondSequence.addCard(new Card(CardRank.ACE, CardSuit.HEARTS));
			firstSequence.addCard(new Card(CardRank.ACE, CardSuit.DIAMONDS));
			assertAll(
					"should return false",
					() -> assertNotEquals(firstSequence,secondSequence),
					() -> assertNotEquals(secondSequence,firstSequence)
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
				
		@RepeatedTest(name = "when adding {currentRepetition} cards", value = 4)
		void testAddingCards(RepetitionInfo info) {
			final int n = info.getCurrentRepetition();
			final Card [] cards = { 
					new Card(CardRank.ACE, CardSuit.SPADES),
					new Card(CardRank.ACE, CardSuit.HEARTS),
					new Card(CardRank.ACE, CardSuit.CLUBS),
					new Card(CardRank.ACE, CardSuit.DIAMONDS)
			};
			boolean [] present = new boolean[n];
			CardSequence sequence = builder.allowInstability(true).build();
			for (int i = 0; i < n; i++) {
				final int j = i;
				assertTrue(sequence.addCard(cards[i]),
						() -> "should add "+cards[j].toString()+" successfully to the sequence");
			}
			assertEquals(n, sequence.size(),
					() -> "it should add "+n+" cards exactly");
			Iterator<Card> iterator = sequence.iterator();
			for (int i = 0; i < n; i++) {
				assertTrue(iterator.hasNext(),
						() -> "it should add "+n+" cards exactly");
				final Card curr = iterator.next(); 
				for (int j = 0; j < n; j++) {
					if ( cards[j].equals(curr) ) {
						assertFalse(present[j],
								() -> "should not have duplicates" );
						present[j] = true;
					}
				}
			}
			assertFalse(iterator.hasNext(),
					() -> "it should add "+n+" cards exactly");
			for (int i = 0; i < present.length; i++) {
				final Card curr = cards[i];
				assertTrue(present[i],
						() -> String.format("should contain card '%s'", curr.toString()));
			}
		}

		@Test
		@DisplayName("when adding an illegal card in a sequence")
		void testAddingIllegalCardInMiddle() {
			CardSequence sequence = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.ACE, CardSuit.HEARTS))
					.addCard(new Card(CardRank.ACE, CardSuit.CLUBS))
					.build();
			assertAll(
					"should return false",
					() -> assertFalse(sequence.addCard(new Card(CardRank.TWO, CardSuit.HEARTS))),
					() -> assertFalse(sequence.addCard(new Card(CardRank.TWO, CardSuit.DIAMONDS))),
					() -> assertFalse(sequence.addCard(new Card(CardRank.ACE, CardSuit.SPADES)))
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
			assertEquals(0, addedSequencesQueue.size(),
					() -> "should not notify that a sequence has been added");
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
			assertEquals(0, addedSequencesQueue.size(),
					() -> "should not notify that a sequence has been added");
		}
		

		@Test
		@DisplayName("when trying to remove a card that's not in a sequence")
		void testSingleWrongCard() {
			CardSequence sequence = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
					.addCard(new Card(CardRank.TWO, CardSuit.HEARTS))
					.addCard(new Card(CardRank.TWO, CardSuit.DIAMONDS))
					.build();
			int initialSize = sequence.size();
			assertAll(
					"should return false",
					() -> assertFalse(sequence.removeCard(new Card(CardRank.TWO, CardSuit.CLUBS)),
							() -> "when has rank but not suit"),
					() -> assertFalse(sequence.removeCard(new Card(CardRank.ACE, CardSuit.SPADES)),
							() -> "when has suit but not rank (before)"),
					() -> assertFalse(sequence.removeCard(new Card(CardRank.FIVE, CardSuit.SPADES)),
							() -> "when has suit but not rank (after)"),
					() -> assertFalse(sequence.removeCard(new Card(CardRank.ACE, CardSuit.CLUBS)),
							() -> "when has neither rank or suit")
			);
			assertEquals(initialSize, sequence.size(),
					() -> "should not remove any cards");
			assertEquals(0, removedCardsQueue.size(),
					() -> "should not notify that a card has been removed");
			assertEquals(0, addedSequencesQueue.size(),
					() -> "should not notify that a sequence has been added");
		}
		
		@Test
		@DisplayName("when removing a card from a triple")
		void testFromTriple() {
			CardSequence sequence = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.ACE, CardSuit.HEARTS))
					.addCard(new Card(CardRank.ACE, CardSuit.DIAMONDS))
					.build();
			int initialSize = sequence.size();
			Card removedCard = new Card(CardRank.ACE, CardSuit.SPADES);
			assertTrue(sequence.removeCard(removedCard),
					() -> "should return false");
			assertEquals(initialSize - 1, sequence.size(),
					() -> "should drop down the sequence size by one");
			assertEquals(1, removedCardsQueue.size(),
					() -> "should notify that a card has been removed");
			assertEquals(0, addedSequencesQueue.size(),
					() -> "should not notify that a sequence has been added");
			assertEquals(removedCard, removedCardsQueue.remove(0),
					() -> "should output in the removed card queue the card just removed");
			CardSequence expected = newSequenceBuilder(listener)
					.addCard(new Card(CardRank.ACE, CardSuit.HEARTS))
					.addCard(new Card(CardRank.ACE, CardSuit.DIAMONDS))
					.allowInstability(true)
					.build();
			assertEquals(expected, sequence,
					() -> "should render the sequence down to the two remaining cards");
		}
		
		@RepeatedTest(name = "when removing suit {currentRepetition}/{totalRepetitions} from complete suit sequence", value = 4)
		@DisplayName("when removing a card from a suit sequence of four")
		void testFromFour(RepetitionInfo info) {
			CardRank rank = CardRank.ACE;
			CardSequence sequence = newSequenceBuilder(listener)
					.addCard(new Card(rank, CardSuit.SPADES))
					.addCard(new Card(rank, CardSuit.HEARTS))
					.addCard(new Card(rank, CardSuit.CLUBS))
					.addCard(new Card(rank, CardSuit.DIAMONDS))
					.build();
			int suitId = info.getCurrentRepetition() - 1;
			CardSuit suit = CardSuit.values()[suitId];
			int initialSize = sequence.size();
			Card removedCard = new Card(rank, suit);
			assertTrue(sequence.removeCard(removedCard),
					() -> "should remove card successfully");
			assertEquals(initialSize - 1, sequence.size(),
					() -> "should drop down the sequence size by one");
			CardSequenceBuilder builder = newSequenceBuilder(listener);
			for (CardSuit cardSuit : CardSuit.values()) {
				if ( !cardSuit.equals(suit) ) {
					builder.addCard(new Card(rank, cardSuit));
				}
			}
			CardSequence expected = builder.build();
			assertEquals(expected, sequence,
					() -> "should leave the sequence with the rest of the cards");
			assertEquals(1, removedCardsQueue.size(),
					() -> "should notify that a card has been removed");
			assertEquals(0, addedSequencesQueue.size(),
					() -> "should not notify that a sequence has been added");
			assertEquals(removedCard, removedCardsQueue.remove(0),
					() -> "should output in the removed card queue the card just removed");
		}
		
	}
	
	@Nested
	@DisplayName("the split method")
	class SplitTest {
			
		Supplier<CardSequence> sequenceSupplier = () -> {
			CardSequenceBuilder sequenceBuilder = newSequenceBuilder(listener)
					.allowInstability(true);
			CardSuit [] suits = CardSuit.values();
			for (int i = 0; i < 4; i++) sequenceBuilder.addCard(new Card(CardRank.ACE, suits[i]));
			return sequenceBuilder.build();
		};
		
		@RepeatedTest(name = "the card #{currentRepetition}", value = 4)
		@DisplayName("when trying to split")
		void testAnyCard(RepetitionInfo info) {
			int n = info.getCurrentRepetition();
			CardSequence sequence = sequenceSupplier.get();
			assertFalse(sequence.split(n - 1),
					() -> "should return false");
			assertEquals(0, addedSequencesQueue.size(),
					() -> "should not notify that a sequence has been added");
			assertEquals(0, removedCardsQueue.size(),
					() -> "should not notify that a card has been removed");
			assertEquals(0, emptySequencesQueue.size(),
					() -> "should not notify that a sequence is empty");
			CardSequence expected = sequenceSupplier.get();
			assertEquals(expected, sequence,
					() -> "should not change the sequence whatsoever");
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
