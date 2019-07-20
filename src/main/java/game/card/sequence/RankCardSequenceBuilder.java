package game.card.sequence;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import game.card.Card;

public class RankCardSequenceBuilder implements CardSequenceBuilder<RankCardSequence> {

	private Set<CardSequenceListener> listeners = new HashSet<CardSequenceListener>();
	private LinkedList<Card> cards = new LinkedList<Card>();

	public CardSequenceBuilder<RankCardSequence> addListener(CardSequenceListener listener) {
		listeners.add(listener);
		return this;
	}

	public CardSequenceBuilder<RankCardSequence> addCard(Card card) {
		cards.add(card);
		return this;
	}

	public RankCardSequence build() throws InvalidCardSequenceException {
		RankCardSequence sequence = new RankCardSequence(listeners);
		for( Card card : cards ) sequence.addCard(card);
		if( !sequence.isStable() ) throw new InvalidCardSequenceException();
		return sequence;
	}

}
