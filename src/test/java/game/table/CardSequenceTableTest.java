package game.table;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

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
			assertEquals(0, table.size(),
					() -> "should leave the table empty");
			assertEquals(0, removedCardsQueue.size(),
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
			assertEquals(0, removedCardsQueue.size(),
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
			assertEquals(0, removedCardsQueue.size(),
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
	
	@Override
	public void cardRemoved(Card card) {
		assertNotNull(card,
				() -> "Card output by cardRemoved callback should not be null");
		removedCardsQueue.add(card);
	}
	
	
	
}
