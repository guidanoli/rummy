package game.table;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
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
import game.sequence.types.RankCardSequenceType;

@DisplayName("On the CardSequenceTable class")
class CardSequenceTableTest implements CardSequenceTableListener {

	private final ArrayList<Card> removedCardsQueue = new ArrayList<Card>();
	private CardSequenceTable table;
	
	@BeforeEach
	void init() {
		removedCardsQueue.clear();
		table = new CardSequenceTable(this);
	}

	@Nested
	@DisplayName("the addSequence method")
	class AddSequenceTest {
		
		@RepeatedTest(name = "when missing {currentRepetition} card(s) to stability", value = 3)
		@DisplayName("when adding an unstable sequence")
		void testUnstableSequence(RepetitionInfo info) {
			int n = 3 - info.getCurrentRepetition();
			CardSequenceBuilder builder = new CardSequenceBuilder()
					.setType(() -> new RankCardSequenceType())
					.allowInstability(true);
			CardRank [] ranks = CardRank.values();
			for (int i = 0; i < n; i++) builder.addCard(new Card(ranks[i], CardSuit.SPADES));
			CardSequence sequence = builder.build();
			assertFalse(table.addSequence(sequence),
					() -> "should return false");
			assertTrue(table.isEmpty(),
					() -> "should leave the table empty");
			assertTrue(removedCardsQueue.isEmpty(),
					() -> "should not notify that a card has been removed");
		}
		
		@Test
		@DisplayName("when adding a stable sequence")
		void testStableSequence() {
			CardSequenceBuilder builder = new CardSequenceBuilder()
					.setType(() -> new RankCardSequenceType());
			CardRank [] ranks = CardRank.values();
			for (int i = 0; i < 3; i++) builder.addCard(new Card(ranks[i], CardSuit.SPADES));
			CardSequence sequence = builder.build();
			assertTrue(table.addSequence(sequence),
					() -> "should return true");
			assertEquals(1, table.size(),
					() -> "should make the table have one sequence");
			assertTrue(removedCardsQueue.isEmpty(),
					() -> "should not notify that a card has been removed");
		}
		
		@Test
		@DisplayName("when adding a sequence that already is listened by the table")
		void testDuplicateListener() {
			CardSequenceBuilder builder = new CardSequenceBuilder()
					.setType(() -> new RankCardSequenceType());
			CardRank [] ranks = CardRank.values();
			for (int i = 0; i < 3; i++) builder.addCard(new Card(ranks[i], CardSuit.SPADES));
			CardSequence sequence = builder.build();
			table.addSequence(sequence);
			table.removeSequence(sequence); // sequence still hears table
			assertTrue(table.addSequence(sequence),
					() -> "should still allow sequence being added after removed");
			assertTrue(removedCardsQueue.isEmpty(),
					() -> "should not notify that cards has been removed before the removal of the first card");
			Card cardToRemove = new Card(ranks[0], CardSuit.SPADES);
			assertTrue(sequence.removeCard(cardToRemove),
					() -> "should allow the removal of the first card of the sequence");
			assertEquals(1, removedCardsQueue.size(),
					() -> "should notify that a single card has been removed");
			Card removedCard = removedCardsQueue.remove(0);
			assertEquals(cardToRemove, removedCard,
					() -> "should notify that the removed card is indeed the first card");
		}
		
		@Test
		@DisplayName("when adding the same sequence twice")
		void testSameSequence() {
			CardSequenceBuilder builder = new CardSequenceBuilder()
					.setType(() -> new RankCardSequenceType());
			CardRank [] ranks = CardRank.values();
			for (int i = 0; i < 3; i++) builder.addCard(new Card(ranks[i], CardSuit.SPADES));
			CardSequence sequence = builder.build();
			assertTrue(table.addSequence(sequence),
					() -> "should return true the first time");
			assertFalse(table.addSequence(sequence),
					() -> "should return false the second time");
			assertEquals(1, table.size(),
					() -> "should make the table have one sequence only");
		}
		
		@Test
		@DisplayName("when adding the identical (but not equal) sequences twice")
		void testIdenticalSequences() {
			CardSequenceBuilder builder = new CardSequenceBuilder()
					.setType(() -> new RankCardSequenceType());
			CardRank [] ranks = CardRank.values();
			for (int i = 0; i < 3; i++) builder.addCard(new Card(ranks[i], CardSuit.SPADES));
			CardSequence firstSequence = builder.build();
			CardSequence secondSequence = builder.build();
			assertTrue(table.addSequence(firstSequence),
					() -> "should return true the first time");
			assertTrue(table.addSequence(secondSequence),
					() -> "should return true the second time");
			assertEquals(2, table.size(),
					() -> "should make the table have two sequences");
		}
		
	}
	
	@Nested
	@DisplayName("the removeSequence method")
	class RemoveSequenceTest {
		
		@Test
		@DisplayName("when removing a sequence in the table")
		void testContains() {
			CardSequence sequence = new CardSequenceBuilder()
					.setType(() -> new RankCardSequenceType())
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
					.addCard(new Card(CardRank.THREE, CardSuit.SPADES))
					.build();
			assertTrue(table.addSequence(sequence),
					() -> "should add a valid sequence first");
			assertEquals(1, table.size(),
					() -> "should increase the table size by one");
			CardSequence sameSequence = new CardSequenceBuilder()
					.setType(() -> new RankCardSequenceType())
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
					.addCard(new Card(CardRank.THREE, CardSuit.SPADES))
					.build();
			assertEquals(sameSequence, sequence,
					() -> "both the sequence inserted and the one to be removed should equal");
			assertTrue(table.removeSequence(sameSequence),
					() -> "should remove the sequence by an identical other");
			assertTrue(table.isEmpty(),
					() -> "should empty the table after the removal");
			assertTrue(removedCardsQueue.isEmpty(),
					() -> "should not notify that a card has been removed");
		}
		
		@Test
		@DisplayName("when removing a sequence that is not in the table")
		void testDoesNotContain() {
			CardSequence sequence = new CardSequenceBuilder()
					.setType(() -> new RankCardSequenceType())
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
					.addCard(new Card(CardRank.THREE, CardSuit.SPADES))
					.build();
			assertFalse(table.removeSequence(sequence),
					() -> "should return false");
			assertTrue(removedCardsQueue.isEmpty(),
					() -> "should not notify that a card has been removed");
		}
		
		@Test
		@DisplayName("when removing a sequence that has been added twice before")
		void testRemoveIdenticals() {
			Supplier<CardSequence> sequenceSupplier = () -> new CardSequenceBuilder()
					.setType(() -> new RankCardSequenceType())
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
					.addCard(new Card(CardRank.THREE, CardSuit.SPADES))
					.build();
			for (int i = 0; i < 2; i++) table.addSequence(sequenceSupplier.get());
			assertEquals(2, table.size(),
					() -> "should be able to add two identical sequences");
			assertTrue(table.removeSequence(sequenceSupplier.get()),
					() -> "should be able to remove a sequence");
			assertEquals(1, table.size(),
					() -> "should remove only one of the sequences, not both");
			assertTrue(removedCardsQueue.isEmpty(),
					() -> "should not notify that a card has been removed");
		}
	}
	
	@Nested
	@DisplayName("the clearTable method")
	class ClearTableTest {
		
		@Test
		@DisplayName("when clearing a table without card sequences")
		void testEmpty() {
			assertTrue(table.isEmpty(),
					() -> "the table should be empty by default");
			table.clearTable();
			assertTrue(table.isEmpty(),
					() -> "should leave the table still empty");
			assertTrue(removedCardsQueue.isEmpty(),
					() -> "should not notify that a card has been removed");
		}
		
		@RepeatedTest(name = "{currentRepetition} card sequence(s)", value = 3)
		@DisplayName("when clearing a table with cards sequences")
		void testNotEmpty(RepetitionInfo info) {
			int n = info.getCurrentRepetition();
			Supplier<CardSequenceBuilder> builderSupplier = () -> new CardSequenceBuilder()
					.setType(() -> new RankCardSequenceType());
			CardSuit suit = CardSuit.SPADES;
			CardRank [] ranks = CardRank.values();
			for (int i = 0; i < n; i++) {
				CardSequenceBuilder builder = builderSupplier.get();
				for (int j = 0; j < 3; j++) builder.addCard(new Card(ranks[i+j], suit));
				table.addSequence(builder.build());
			}
			table.clearTable();
			assertTrue(table.isEmpty(),
					() -> "should empty the table");
			assertTrue(removedCardsQueue.isEmpty(),
					() -> "should not notify that a card has been removed");
		}
		
	}
	
	@Nested
	@DisplayName("the size method")
	class SizeTest {
		
		@Test
		@DisplayName("on an empty table")
		void testEmpty() {
			assertEquals(0, table.size(),
					() -> "should return zero");
		}
		
		@Test
		@DisplayName("on a table with variant number of card sequences")
		void testBoyantSize() {
			Supplier<CardSequenceBuilder> builderSupplier = () -> new CardSequenceBuilder()
					.setType(() -> new RankCardSequenceType());
			CardSuit suit = CardSuit.SPADES;
			CardRank [] ranks = CardRank.values();
			ArrayList<Integer> order = new ArrayList<Integer>();
			for (int i = 0; i < 5; i++) order.add(i);
			int tableSize = 0;
			for (int i : order) {
				CardSequenceBuilder builder = builderSupplier.get();
				for (int j = 0; j < 3; j++) builder.addCard(new Card(ranks[i+j], suit));
				table.addSequence(builder.build());
				tableSize += 1;
				assertEquals(tableSize, table.size(),
						() -> "should return the accurate size when adding");
			}
			Collections.shuffle(order); // randomise removal order
			for (int i : order) {
				CardSequenceBuilder builder = builderSupplier.get();
				for (int j = 0; j < 3; j++) builder.addCard(new Card(ranks[i+j], suit));
				table.removeSequence(builder.build());
				tableSize -= 1;
				assertEquals(tableSize, table.size(),
						() -> "should return the accurate size when removing");
			}
		}
				
	}
	
	@Nested
	@DisplayName("the cardSequenceIsEmpty listener method")
	class EmptySequenceListenerTest {
		
		@Test
		@DisplayName("when the table has a single sequence")
		void testSingleSequence() {
			Supplier<CardSequence> sequenceSupplier = () -> new CardSequenceBuilder()
					.setType(() -> new RankCardSequenceType())
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
					.addCard(new Card(CardRank.THREE, CardSuit.SPADES))
					.build();
			CardSequence sequence = sequenceSupplier.get();
			table.addSequence(sequence);
			assertEquals(1, table.size(),
					() -> "should have one sequence before removing cards");
			for (CardSequence cs : table) sequence = cs;
			CardRank [] ranks = CardRank.values();
			for (int i = 0; i < 3; i++) {
				sequence.removeCard(new Card(ranks[i], CardSuit.SPADES));
			}
			assertEquals(0, table.size(),
					() -> "should leave the table empty");
			assertEquals(3, removedCardsQueue.size(),
					() -> "should be notified of each card removed");
			CardSequenceBuilder rebuilder = new CardSequenceBuilder()
					.setType(() -> new RankCardSequenceType());
			for (Card card : removedCardsQueue) {
				rebuilder.addCard(card);
			}
			CardSequence rebuiltSequence = rebuilder.build();
			CardSequence expectedSequence = sequenceSupplier.get();
			assertEquals(expectedSequence, rebuiltSequence,
					() -> "should pop the right cards through the listener");
		}
		
		@Test
		@DisplayName("when the table has many sequences")
		void testMultipleSequence() {
			Supplier<CardSequence> sequenceSupplier = () -> new CardSequenceBuilder()
					.setType(() -> new RankCardSequenceType())
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
					.addCard(new Card(CardRank.THREE, CardSuit.SPADES))
					.build();
			for (int i = 0; i < 2; i++) table.addSequence(sequenceSupplier.get());
			assertEquals(2, table.size(),
					() -> "should have two sequences before removing cards");
			CardSequence sequence = null;
			for (CardSequence cs : table) sequence = cs; // the latter
			CardRank [] ranks = CardRank.values();
			for (int i = 0; i < 3; i++) {
				sequence.removeCard(new Card(ranks[i], CardSuit.SPADES));
			}
			assertEquals(1, table.size(),
					() -> "should reduce the table size by one");
			assertEquals(3, removedCardsQueue.size(),
					() -> "should be notified of each card removed");
			CardSequenceBuilder rebuilder = new CardSequenceBuilder()
					.setType(() -> new RankCardSequenceType());
			for (Card card : removedCardsQueue) {
				rebuilder.addCard(card);
			}
			CardSequence rebuiltSequence = rebuilder.build();
			CardSequence expectedSequence = sequenceSupplier.get();
			assertEquals(expectedSequence, rebuiltSequence,
					() -> "should pop the right cards through the listener");
		}
		
	}
	
	@Nested
	@DisplayName("the addSequence listener method")
	class NewSequenceListenerTest {
		
		@Test
		@DisplayName("when generating a stable sequence through splitting")
		void testSplittingStableSequences() {
			CardSequence sequence = new CardSequenceBuilder()
					.setType(() -> new RankCardSequenceType())
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
					.addCard(new Card(CardRank.THREE, CardSuit.SPADES))
					.addCard(new Card(CardRank.FOUR, CardSuit.SPADES))
					.addCard(new Card(CardRank.FIVE, CardSuit.SPADES))
					.addCard(new Card(CardRank.SIX, CardSuit.SPADES))
					.build();
			table.addSequence(sequence);
			for (CardSequence cs : table) sequence = cs;
			assertEquals(1, table.size(),
					() -> "the table should have one sequence before splitting");
			assertTrue(sequence.split(3),
					() -> "should allow the splitting");
			assertEquals(2, table.size(),
					() -> "the table should have two sequence after splitting");
			ArrayList<CardSequence> expectedSequences = new ArrayList<CardSequence>();
			expectedSequences.add(new CardSequenceBuilder()
					.setType(() -> new RankCardSequenceType())
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
					.addCard(new Card(CardRank.THREE, CardSuit.SPADES))
					.build());
			expectedSequences.add(new CardSequenceBuilder()
					.setType(() -> new RankCardSequenceType())
					.addCard(new Card(CardRank.FOUR, CardSuit.SPADES))
					.addCard(new Card(CardRank.FIVE, CardSuit.SPADES))
					.addCard(new Card(CardRank.SIX, CardSuit.SPADES))
					.build());
			for (CardSequence cs : table) {
				assertTrue(expectedSequences.contains(cs),
						() -> "should result in expected sequences");
				expectedSequences.remove(cs);
			}
		}
		
		@Test
		@DisplayName("when generating an unstable sequence through splitting")
		void testSplittingUntableSequences() {
			CardSequence sequence = new CardSequenceBuilder()
					.setType(() -> new RankCardSequenceType())
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
					.addCard(new Card(CardRank.THREE, CardSuit.SPADES))
					.addCard(new Card(CardRank.FOUR, CardSuit.SPADES))
					.addCard(new Card(CardRank.FIVE, CardSuit.SPADES))
					.build();
			table.addSequence(sequence);
			for (CardSequence cs : table) sequence = cs;
			assertEquals(1, table.size(),
					() -> "the table should have one sequence before splitting");
			assertTrue(sequence.split(3),
					() -> "should allow the splitting");
			assertEquals(2, table.size(),
					() -> "the table should have two sequence after splitting");
			ArrayList<CardSequence> expectedSequences = new ArrayList<CardSequence>();
			expectedSequences.add(new CardSequenceBuilder()
					.setType(() -> new RankCardSequenceType())
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
					.addCard(new Card(CardRank.THREE, CardSuit.SPADES))
					.build());
			expectedSequences.add(new CardSequenceBuilder()
					.setType(() -> new RankCardSequenceType())
					.addCard(new Card(CardRank.FOUR, CardSuit.SPADES))
					.addCard(new Card(CardRank.FIVE, CardSuit.SPADES))
					.allowInstability(true)
					.build());
			for (CardSequence cs : table) {
				assertTrue(expectedSequences.contains(cs),
						() -> "should result in expected sequences");
				expectedSequences.remove(cs);
			}
		}
		
	}
	
	@Override
	public void cardRemoved(Card card) {
		assertNotNull(card,
				() -> "Card output by cardRemoved callback should not be null");
		removedCardsQueue.add(card);
	}
	
	
	
}
