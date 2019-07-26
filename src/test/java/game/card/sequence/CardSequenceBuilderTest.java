package game.card.sequence;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import game.card.Card;
import game.card.CardRank;
import game.card.CardSuit;
import game.card.sequence.types.RankCardSequenceType;

class CardSequenceBuilderTest {

	@Nested
	@DisplayName("the builder")
	class BuilderTest {
		
		@Nested
		@DisplayName("throws IllegalArgumentException")
		class throwsException {
			
			@Test
			@DisplayName("when instability is not allowed and type is not defined")
			void testStableUndefinedType() {
				CardSequenceBuilder builder = new CardSequenceBuilder();
				assertThrows(IllegalArgumentException.class,
						() -> builder.build());
			}
			
			@Test
			@DisplayName("when instability is allowed and type is not defined")
			void testUnstableUndefinedType() {
				CardSequenceBuilder builder = new CardSequenceBuilder()
						.allowInstability(true);
				assertThrows(IllegalArgumentException.class,
						() -> builder.build());
			}
			
			@Test
			@DisplayName("when does not have cards")
			void testNoCard() {
				CardSequenceBuilder builder = new CardSequenceBuilder()
						.setType(() -> new RankCardSequenceType());
				assertThrows(IllegalArgumentException.class,
						() -> builder.build());
			}
						
		}
		
		@Nested
		@DisplayName("does not throw IllegalArgumentException")
		class DoesNotThrowException {
			
			@Test
			@DisplayName("when instability is allowed and type is defined")
			void testUnstableEmpty() {
				CardSequenceBuilder builder = new CardSequenceBuilder()
						.allowInstability(true)
						.setType(() -> new RankCardSequenceType());
				assertDoesNotThrow(() -> builder.build());
			}
			
		}

		@Test
		@DisplayName("when building many sequences from the same builder")
		void testMultipleBuildsAreEqual() {
			CardSequenceBuilder builder = new CardSequenceBuilder()
					.setType(() -> new RankCardSequenceType())
					.addCard(new Card(CardRank.ACE, CardSuit.SPADES))
					.addCard(new Card(CardRank.TWO, CardSuit.SPADES))
					.addCard(new Card(CardRank.THREE, CardSuit.SPADES));
			assertEquals(builder.build(), builder.build(),
					() -> "should build equal sequences");
		}
			
	}

}
