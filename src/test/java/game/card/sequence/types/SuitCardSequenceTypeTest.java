package game.card.sequence.types;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import game.card.Card;
import game.card.CardRank;
import game.card.CardSuit;
import game.card.sequence.CardSequence;
import game.card.sequence.CardSequenceBuilder;
import game.card.sequence.CardSequenceListener;

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
