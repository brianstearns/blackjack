import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Deck class.
 */
public class DeckTest {

    /**
     * Test that makeDeck creates a deck with 52 cards.
     */
    @Test
    void deckShouldHave52CardsAfterMakeDeck() {
        Deck deck = new Deck();
        deck.makeDeck();
        assertEquals(52, deck.getSize(), "Deck should have 52 cards");
    }

    /**
     * Test that shuffle does not change the number of cards in the deck.
     */
    @Test
    void shuffleShouldNotChangeCardCount() {
        Deck deck = new Deck();
        deck.makeDeck();
        deck.shuffle();
        assertEquals(52, deck.getSize(), "Shuffling should not change the number of cards");
    }

    /**
     * Test that dealCard returns a card and increments the top index.
     */
    @Test
    void dealCardShouldReturnFirstCardThenIncrementTop() {
        Deck deck = new Deck();
        deck.makeDeck();
        Card first = deck.dealCard();
        assertNotNull(first, "Dealt card should not be null");
        Card second = deck.dealCard();
        assertNotEquals(first, second, "Second card should not equal the first card");
    }

    /**
     * Test that getCardValue returns correct values for different ranks.
     */
    @Test
    void getCardValueShouldReturnCorrectValues() {
        Deck deck = new Deck();
        Card ace = new Card("Hearts", "Ace");
        Card king = new Card("Clubs", "King");
        Card five = new Card("Diamonds", "5");

        assertEquals(11, deck.getCardValue(ace));
        assertEquals(10, deck.getCardValue(king));
        assertEquals(5, deck.getCardValue(five));
    }

    /**
     * Test that needsReshuffle returns true when 15 cards are left and false
     * otherwise.
     */
    @Test
    void needsReshuffleReturnsTrueWhen15CardsLeft() {
        Deck deck = new Deck();
        deck.makeDeck();
        for (int i = 0; i < 37; i++) {
            deck.dealCard();
        }
        assertTrue(deck.needsReshuffle(), "Deck should need reshuffle when 15 cards left");
    }

    /**
     * Test that needsReshuffle returns false when more than 15 cards are left.
     */
    @Test
    void needsReshuffleReturnsFalseWhenMoreThan15CardsLeft() {
        Deck deck = new Deck();
        deck.makeDeck();
        for (int i = 0; i < 20; i++) {
            deck.dealCard();
        }
        assertFalse(deck.needsReshuffle(), "Deck should not need reshuffle yet");
    }
}