public class Deck {
    private Card[] cards;
    @SuppressWarnings("unused")
    private int top;

    @SuppressWarnings("unused")
    public Deck() {
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
    }

    public void makeDeck() {
        cards = new Card[52];
        int index = 0;
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};

        for (String suit : suits) {
            for (String rank : ranks) {
                cards[index++] = new Card(suit, rank);
            }
        }
        top = 0;
    }

    public void shuffle() {
        for (int i = cards.length - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            Card temp = cards[i];
            cards[i] = cards[j];
            cards[j] = temp;
        }
        top = 0;
    }

    public String printShuffledDeck() {
        StringBuilder sb = new StringBuilder();
        for (Card card : cards) {
            sb.append(card.toString()).append("\n");
        }
        return sb.toString();
    }

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
    
    public Card dealCard() {
        return cards[top++];
    }
}
