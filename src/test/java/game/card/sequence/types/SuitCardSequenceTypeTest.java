package game.card.sequence.types;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;

import game.card.Card;
import game.card.CardRank;
import game.card.CardSuit;
import game.card.sequence.CardSequence;
import game.card.sequence.CardSequenceBuilder;
import game.card.sequence.CardSequenceListener;

@DisplayName("On the SuitCardSequence class")
class SuitCardSequenceTypeTest implements CardSequenceListener {

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
				for(int i = 0; i < len; i++) {
					for(int j = 0; j < len; j++) {
						for(int k = 0; k < len; k++) {
							for(int l = 0; l < len; l++) {
								if( i != j && k != i && k != j && l != i && l != j && l != k ) {
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
				for(int i = 1; i < sequences.size(); i++) {
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
				
		@RepeatedTest(name = "Adding {currentRepetition} cards", value = 4)
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
			for(int i = 0; i < n; i++) {
				final int j = i;
				assertTrue(sequence.addCard(cards[i]),
						() -> "should add "+cards[j].toString()+" successfully to the sequence");
			}
			assertEquals(n, sequence.size(),
					() -> "it should add "+n+" cards exactly");
			Iterator<Card> iterator = sequence.iterator();
			for(int i = 0; i < n; i++) {
				assertTrue(iterator.hasNext(),
						() -> "it should add "+n+" cards exactly");
				final Card curr = iterator.next(); 
				for(int j = 0; j < n; j++) {
					if( cards[j].equals(curr) ) {
						assertFalse(present[j],
								() -> "should not have duplicates" );
						present[j] = true;
					}
				}
			}
			assertFalse(iterator.hasNext(),
					() -> "it should add "+n+" cards exactly");
			for(int i = 0; i < present.length; i++) {
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
