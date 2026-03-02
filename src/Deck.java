/**
 * Represents a standard deck of 52 playing cards, providing methods to create,
 * shuffle, and deal cards, as well as calculate card values and determine when
 * a reshuffle is needed.
 */
public class Deck {
    private Card[] cards;
    @SuppressWarnings("unused")
    private int top;

    /**
     * Constructor for Deck class that initializes the suits and ranks arrays. The
     * actual deck of cards is created using the makeDeck method, which populates
     * the cards array with Card objects representing each combination of suit and
     * rank.
     */
    @SuppressWarnings("unused")
    public Deck() {
        String[] suits = { "Hearts", "Diamonds", "Clubs", "Spades" };
        String[] ranks = { "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King" };
    }

    /**
     * Creates a standard deck of 52 playing cards.
     */
    public void makeDeck() {
        cards = new Card[52];
        int index = 0;
        String[] suits = { "Hearts", "Diamonds", "Clubs", "Spades" };
        String[] ranks = { "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King" };

        for (String suit : suits) {
            for (String rank : ranks) {
                cards[index++] = new Card(suit, rank);
            }
        }
        top = 0;
    }

    /**
     * Returns the number of cards in the deck.
     * 
     * @return Size of the deck (number of cards)
     */
    public int getSize() {
        return cards.length;
    }

    /**
     * Shuffles the deck of cards using the Fisher-Yates algorithm, which ensures a
     * uniform random shuffle. After shuffling, the top index is reset to 0.
     */
    public void shuffle() {
        for (int i = cards.length - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            Card temp = cards[i];
            cards[i] = cards[j];
            cards[j] = temp;
        }
        top = 0;
    }

    /**
     * Returns a string representation of the shuffled deck, with each card on a new
     * line.
     * 
     * @return String representation of the shuffled deck of cards
     */
    public String printShuffledDeck() {
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) {
            sb.append(card.toString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Calculates the value of a given card based on its rank. Aces are worth 11
     * points, face cards (Jack, Queen, King) are worth 10 points, and numbered
     * cards are worth their face value.
     * 
     * @param card Card object for which to calculate the value
     * @return Integer value of the card based on its rank
     */
    public int getCardValue(Card card) {
        String rank = card.getRank();
        if (rank.equals("Ace")) {
            return 11;
        } else if (rank.equals("Jack") || rank.equals("Queen") || rank.equals("King")) {
            return 10;
        } else {
            return Integer.parseInt(rank);
        }
    }

    /**
     * Deals a card from the top of the deck and increments the top index to point
     * to
     * the next card in the deck.
     * 
     * @return Card object representing the dealt card from the top of the deck
     */
    public Card dealCard() {
        return cards[top++];
    }

    /**
     * Determines if the deck needs to be reshuffled based on the number of cards
     * left.
     * 
     * @return true if the deck needs reshuffling (15 or fewer cards left)
     */
    public boolean needsReshuffle() {
        return top >= cards.length - 15;
    }
}
