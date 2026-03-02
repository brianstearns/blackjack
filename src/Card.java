public class Card {
    private String suit;
    private String rank;

    /**
     * Constructor for Card class that initializes the suit and rank of the card.
     * 
     * @param suit The suit of the card (e.g., "Hearts", "Diamonds", "Clubs",
     *             "Spades")
     * @param rank The rank of the card (e.g., "Ace", "2", "3", ..., "10", "Jack",
     *             "Queen", "King")
     */
    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    /**
     * Returns the suit of the card, such as "Hearts", "Diamonds", "Clubs", or
     * "Spades".
     * 
     * @return Suit of the card as a string
     */
    public String getSuit() {
        return suit;
    }

    /**
     * Returns the rank of the card, such as "Ace", "2", "3", ..., "10", "Jack",
     * 
     * @return Rank of the card as a string
     */
    public String getRank() {
        return rank;
    }

    /**
     * Returns a string representation of the card in the format "Rank of Suit",
     * such as "Ace of Hearts" or "10 of Clubs". This method is used to display the
     * card information in a human-readable format when printing the card or the
     * deck.
     */
    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}