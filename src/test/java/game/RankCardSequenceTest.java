package game;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("On the RankCardSequence class")
class RankCardSequenceTest {
	
	@Nested
	@DisplayName("the constructor")
	class ConstructorTest {
		
		@Test
		@DisplayName("when parsing a null listener")
		void testNullListener() {
			assertThrows(NullPointerException.class,
					() -> new RankCardSequence(null),
					() -> "should throw a NullPointerException");
		}
		
		@Test
		@DisplayName("when passing an existing listener")
		void testNonNullListener() {
			assertDoesNotThrow(
					() -> new RankCardSequence(new CardSequenceListener() {
						public void addCardSequence(CardSequence cardSequence) {
							// nothing
						}
					}),
					() -> "should not throw any exceptions whatsoever");
		}
		
	}

}
