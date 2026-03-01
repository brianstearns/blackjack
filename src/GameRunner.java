import java.util.List;
import java.util.Scanner;

public class GameRunner {
    public static void main(String[] args) {
        PlayerManager pm = new PlayerManager();
        pm.loadPlayers();

        Deck deck = new Deck();
        deck.makeDeck();
        deck.shuffle();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your name: ");
        String nameInput = scanner.nextLine().trim();

        Player currentPlayer = null;
        List<Player> players = pm.getPlayers();

        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(nameInput.toLowerCase())) {
                currentPlayer = p;
                break;
            }
        }

        if (currentPlayer == null) {
            currentPlayer = new Player(nameInput.toLowerCase());
            players.add(currentPlayer);
            pm.savePlayers();
            System.out.println("New player created: " + nameInput + " with 10,000 chips");
        } else {
            System.out
                    .println("Welcome back, " + currentPlayer.getName() + "! Chips: " + currentPlayer.printChipCount());
        }

        System.out.println("Current chip count: " + currentPlayer.printChipCount());

        boolean gameRunning = true;
        while (gameRunning) {
            System.out.print("Do you want to play a hand? (yes/no): ");
            String playInput = scanner.nextLine().trim().toLowerCase();
            if (playInput.equals("yes") || playInput.equals("y")) {
                System.out.println("-----------------------\nStarting a new hand...");
            } else if (playInput.equals("no") || playInput.equals("n")) {
                gameRunning = false;
                System.out.println("Thanks for playing! Final chip count: " + currentPlayer.printChipCount());
                continue;
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                continue;
            }

            int wager = 0;
            while (true) {
                System.out.print("Enter your wager: ");
                String wagerInput = scanner.nextLine().trim();
                try {
                    wager = Integer.parseInt(wagerInput);
                    if (wager > currentPlayer.getChipCount()) {
                        System.out.println("You cannot wager more than your current chip count. Try again.");
                    } else if (wager <= 0) {
                        System.out.println("Wager must be a positive number. Try again.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a numeric value for the wager.");
                }
            }

            System.out.println("------------------------------");

            Card dealersCard[] = new Card[] {
                    deck.dealCard(), deck.dealCard()
            };
            Card playersCard[] = new Card[] {
                    deck.dealCard(), deck.dealCard()
            };

            System.out.println("Your cards: " + playersCard[0] + ", " + playersCard[1] + " --- Count: "
                    + (deck.getCardValue(playersCard[0]) + deck.getCardValue(playersCard[1])));
            System.out.println(
                    "Dealer's visible card: " + dealersCard[0] + " --- Count: " + deck.getCardValue(dealersCard[0]));

            if ((dealersCard[0].getRank().equals("Ace")
                    && (dealersCard[0].getRank().equals("10") || dealersCard[0].getRank().equals("Jack")
                            || dealersCard[0].getRank().equals("Queen") || dealersCard[0].getRank().equals("King")))
                    &&
                    (playersCard[0].getRank().equals("Ace") && (playersCard[0].getRank().equals("10")
                            || playersCard[0].getRank().equals("Jack") || playersCard[0].getRank().equals("Queen")
                            || playersCard[0].getRank().equals("King")))) {
                System.out.println("Both you and the dealer have blackjack. It's a push!");
                System.out.println("Dealer's hand: " + dealersCard[0] + ", " + dealersCard[1]);
                System.out.println("Your hand: " + playersCard[0] + ", " + playersCard[1]);
                continue;
            }

            if (dealersCard[0].getRank().equals("Ace")
                    && (dealersCard[0].getRank().equals("10") || dealersCard[0].getRank().equals("Jack")
                            || dealersCard[0].getRank().equals("Queen") || dealersCard[0].getRank().equals("King"))) {
                System.out.println("Dealer has blackjack sorry you lose");
                System.out.println("Dealer's hand: " + dealersCard[0] + ", " + dealersCard[1]);
                System.out.println("Your hand: " + playersCard[0] + ", " + playersCard[1]);
                currentPlayer.loseChips(wager);
                pm.savePlayers();
                continue;
            }

            if (playersCard[0].getRank().equals("Ace")
                    && (playersCard[0].getRank().equals("10") || playersCard[0].getRank().equals("Jack")
                            || playersCard[0].getRank().equals("Queen") || playersCard[0].getRank().equals("King"))) {
                System.out.println("You have blackjack congrats you win");
                System.out.println("Dealer's hand: " + dealersCard[0] + ", " + dealersCard[1]);
                System.out.println("Your hand: " + playersCard[0] + ", " + playersCard[1]);
                currentPlayer.winChips((int) (wager * 1.5));
                pm.savePlayers();
                continue;
            }

            boolean hitting = true;
            boolean playerBusted = false;
            int count = 0;

            while (hitting) {
                System.out.println("------------------------------");
                count = calculateHandValue(playersCard, deck);

                System.out.print("Your hand: ");
                System.out.println(" --- Count: " + count);
                for (Card card : playersCard) {
                    System.out.print("  " + card + "\n");
                }

                System.out.println("Dealer's hand:  --- Count: " + deck.getCardValue(dealersCard[0]));
                System.out.println("  " + dealersCard[0]);

                if (count == 21) {
                    System.out.println("You hit 21! Good luck against the dealer!");
                    currentPlayer.winChips(wager);
                    pm.savePlayers();
                    hitting = false;
                    break;
                }

                if (count > 21) {
                    System.out.println("You busted with a total of " + count + ". You lose.");
                    currentPlayer.loseChips(wager);
                    pm.savePlayers();
                    hitting = false;
                    playerBusted = true;
                    break;
                }

                System.out.println("What would you like to do? (hit/stand): ");
                String actionInput = scanner.nextLine().trim().toLowerCase();

                if (actionInput.equals("hit")) {
                    Card newCard = deck.dealCard();
                    playersCard = java.util.Arrays.copyOf(playersCard, playersCard.length + 1);
                    playersCard[playersCard.length - 1] = newCard;
                    System.out.println("You drew: " + newCard);
                } else if (actionInput.equals("stand")) {
                    hitting = false;
                } else {
                    System.out.println("Invalid input. Please enter 'hit' or 'stand'.");
                }
            }

            int dealerCount = 0;

            for (Card card : dealersCard) {
                dealerCount += deck.getCardValue(card);
            }

            if (!playerBusted) {
                while (dealerCount <= 16) {
                    Card newCard = deck.dealCard();
                    dealersCard = java.util.Arrays.copyOf(dealersCard, dealersCard.length + 1);
                    dealersCard[dealersCard.length - 1] = newCard;
                    dealerCount += deck.getCardValue(newCard);
                }
            }

            System.out.println("Dealer's final hand: ");
            for (Card card : dealersCard) {
                System.out.println("\n   " + card);
            }
            System.out.println("Count: " + dealerCount);

            if (dealerCount > 21) {
                System.out.println("Dealer busted with a total of " + dealerCount + ". You win!");
                currentPlayer.winChips(wager);
            } else if (dealerCount > count) {
                System.out.println("Dealer wins with a total of " + dealerCount + " against your " + count + ".");
                currentPlayer.loseChips(wager);
            } else if (dealerCount < count) {
                System.out.println("You win with a total of " + count + " against the dealer's " + dealerCount + "!");
                currentPlayer.winChips(wager);
            } else {
                System.out.println("It's a push with both you and the dealer at " + count + ".");
            }

            pm.savePlayers();
        }
        scanner.close();
    }

    public static int calculateHandValue(Card[] hand, Deck deck) {
        int total = 0;
        int aceCount = 0;

        for (Card card : hand) {
            int val = deck.getCardValue(card);
            total += val;
            if (card.getRank().equals("Ace")) {
                aceCount++;
            }
        }

        while (total > 21 && aceCount > 0) {
            total -= 10;
            aceCount--;
        }

        return total;
    }
}
