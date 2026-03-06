import java.util.List;
import java.util.Scanner;

public class GameRunner {
    public static void main(String[] args) throws InterruptedException {
        PlayerManager pm = new PlayerManager();
        pm.loadPlayers();

        Deck deck = new Deck();
        deck.makeDeck();
        deck.shuffle();
        displayShuffle();

        System.out.println(printLogo());

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
                System.out.println("Your current chip count: " + currentPlayer.printChipCount());
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

            if (deck.needsReshuffle()) {
                deck.makeDeck();
                deck.shuffle();
                displayShuffle();
            }

            Card dealersCard[] = new Card[] {
                    deck.dealCard(), deck.dealCard()
            };
            Card playersCard[] = new Card[] {
                    deck.dealCard(), deck.dealCard()
            };

            // System.out.println("Your cards: " + displayCard(playersCard[0]) + ", " +
            // displayCard(playersCard[1]) + " --- Count: "
            // + (deck.getCardValue(playersCard[0]) + deck.getCardValue(playersCard[1])));
            // System.out.println(
            // "Dealer's visible card: " + displayCard(dealersCard[0]) + " --- Count: " +
            // deck.getCardValue(dealersCard[0]));

            int blackjackResult = hasBlackjack(playersCard, dealersCard, deck);

            boolean hitting = true;

            switch (blackjackResult) {
                case 0 -> {
                    System.out.println(Color.YELLOW.getCode() + "Both you and the dealer have blackjack. It's a push!"
                            + Color.RESET.getCode());
                    System.out.println("Dealer's hand: " + dealersCard[0] + ", " + dealersCard[1]);
                    System.out.println("Your hand: " + playersCard[0] + ", " + playersCard[1]);
                    hitting = false;
                }
                case -1 -> {
                    System.out.println(
                            Color.RED.getCode() + "Dealer has blackjack sorry you lose" + Color.RESET.getCode());
                    System.out.println("Dealer's hand: " + dealersCard[0] + ", " + dealersCard[1]);
                    System.out.println("Your hand: " + playersCard[0] + ", " + playersCard[1]);
                    currentPlayer.loseChips(wager);
                    hitting = false;
                    pm.savePlayers();
                }
                case 1 -> {
                    System.out.println(
                            Color.GREEN.getCode() + "You have blackjack congrats you win" + Color.RESET.getCode());
                    System.out.println("Dealer's hand: " + dealersCard[0] + ", " + dealersCard[1]);
                    System.out.println("Your hand: " + playersCard[0] + ", " + playersCard[1]);
                    currentPlayer.winChips((int) (wager * 1.5));
                    hitting = false;
                    pm.savePlayers();
                }
            }

            boolean playerBusted = false;
            int count = 0;

            while (hitting) {
                System.out.println("------------------------------");
                count = calculateHandValue(playersCard, deck);

                System.out.print("Your hand: ");
                System.out.println(" --- Count: " + count);
                for (Card card : playersCard) {
                    System.out.print(displayCard(card));
                }

                System.out.println("Dealer's hand:  --- Count: " + deck.getCardValue(dealersCard[0]));
                System.out.println(displayCard(dealersCard[0]));

                if (count == 21) {
                    System.out.println(
                            Color.CYAN.getCode() + "You hit 21! Good luck against the dealer!" + Color.RESET.getCode());
                    currentPlayer.winChips(wager);
                    pm.savePlayers();
                    hitting = false;
                    break;
                }

                if (count > 21) {
                    System.out.println(Color.RED.getCode() + "You busted with a total of " + count + ". You lose."
                            + Color.RESET.getCode());
                    currentPlayer.loseChips(wager);
                    pm.savePlayers();
                    hitting = false;
                    playerBusted = true;
                    break;
                }

                System.out.println("What would you like to do? (hit/stand/double): ");
                String actionInput = scanner.nextLine().trim().toLowerCase();

                if (actionInput.toLowerCase().equals("double")) {
                    if (playersCard.length != 2) {
                        System.out.println(
                                Color.YELLOW.getCode() + "You can only double down on your first move (with 2 cards)."
                                        + Color.RESET.getCode());
                        continue;
                    }

                    if (currentPlayer.getChipCount() < wager * 2) {
                        System.out.println(Color.YELLOW.getCode() + "You don't have enough chips to double down."
                                + Color.RESET.getCode());
                        continue;
                    }

                    wager *= 2;

                    Card newCard = deck.dealCard();
                    playersCard = java.util.Arrays.copyOf(playersCard, playersCard.length + 1);
                    playersCard[playersCard.length - 1] = newCard;

                    System.out.println("You doubled down and drew: " + newCard);

                    count = calculateHandValue(playersCard, deck);

                    if (count > 21) {
                        System.out.println(Color.RED.getCode() + "You busted with " + count + ". You lose."
                                + Color.RESET.getCode());
                        currentPlayer.loseChips(wager);
                        pm.savePlayers();
                        playerBusted = true;
                    }

                    hitting = false;

                } else if (actionInput.toLowerCase().equals("hit")) {
                    Card newCard = deck.dealCard();
                    playersCard = java.util.Arrays.copyOf(playersCard, playersCard.length + 1);
                    playersCard[playersCard.length - 1] = newCard;
                    System.out.println("You drew: " + newCard);
                } else if (actionInput.toLowerCase().equals("stand")) {
                    hitting = false;
                } else {
                    System.out.println(Color.YELLOW.getCode()
                            + "Invalid input. Please enter 'hit', 'stand', or 'double'. " + Color.RESET.getCode());
                }
            }

            System.out.println("------------------------------");

            int dealerCount = 0;

            for (Card card : dealersCard) {
                dealerCount += deck.getCardValue(card);
            }

            if (!playerBusted && hasBlackjack(dealersCard, playersCard, deck) == 2) {
                while (dealerCount <= 16) {
                    Card newCard = deck.dealCard();
                    dealersCard = java.util.Arrays.copyOf(dealersCard, dealersCard.length + 1);
                    dealersCard[dealersCard.length - 1] = newCard;
                    dealerCount += deck.getCardValue(newCard);
                }
            }

            System.out.println("Dealer's final hand: ");
            for (Card card : dealersCard) {
                System.out.println(displayCard(card));
            }
            System.out.println("Count: " + dealerCount);

            if (!playerBusted && hasBlackjack(dealersCard, playersCard, deck) == 2) {
                if (dealerCount > 21) {
                    System.out.println(Color.MAGENTA.getCode() + "Dealer busted with a total of " + dealerCount
                            + ". You win!" + Color.RESET.getCode());
                    currentPlayer.winChips(wager);
                } else if (dealerCount > count) {
                    System.out.println(Color.RED.getCode() + "Dealer wins with a total of " + dealerCount
                            + " against your " + count + "." + Color.RESET.getCode());
                    currentPlayer.loseChips(wager);
                } else if (dealerCount < count) {
                    System.out.println(Color.GREEN.getCode() + "You win with a total of " + count
                            + " against the dealer's " + dealerCount + "!" + Color.RESET.getCode());
                    currentPlayer.winChips(wager);
                } else {
                    System.out.println(Color.CYAN.getCode() + "It's a push with both you and the dealer at " + count
                            + "." + Color.RESET.getCode());
                }
            }

            pm.savePlayers();
        }
        scanner.close();
    }

    /**
     * Calculates the total value of a hand of cards, accounting for Aces as 1 or
     * 11.
     * 
     * @param hand Array of Card objects representing the player's hand
     * @param deck Deck object used to get the value of each card
     * @return Total value of the hand, with Aces counted as 1 or 11 to maximize the
     *         hand's value without busting
     */
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

    /**
     * Determines if a hand of two cards is a blackjack (an Ace and a 10-value
     * card).
     * 
     * @param hand Array of Card objects representing the hand to check for
     *             blackjack
     * @return true if the hand is a blackjack, false otherwise
     */
    public static boolean isBlackjack(Card[] hand) {
        if (hand.length != 2)
            return false;

        boolean hasAce = false;
        boolean hasTenValue = false;

        for (Card card : hand) {
            if (card.getRank().equals("Ace")) {
                hasAce = true;
            }
            if (card.getRank().equals("10") ||
                    card.getRank().equals("Jack") ||
                    card.getRank().equals("Queen") ||
                    card.getRank().equals("King")) {
                hasTenValue = true;
            }
        }

        return hasAce && hasTenValue;
    }

    /**
     * Determines the outcome of a hand based on whether the player and dealer have
     * 
     * @param playeCards  Array of Card objects representing the player's hand
     * @param dealerCards Array of Card objects representing the dealer's hand
     * @param deck        Deck object used to calculate card values if needed
     * @return 0 if both player and dealer have blackjack (push), 1 if player has
     *         blackjack
     */
    public static int hasBlackjack(Card[] playeCards, Card[] dealerCards, Deck deck) {
        boolean playerHasBlackjack = isBlackjack(playeCards);
        boolean dealerHasBlackjack = isBlackjack(dealerCards);

        if (playerHasBlackjack && dealerHasBlackjack) {
            return 0;
        } else if (playerHasBlackjack) {
            return 1;
        } else if (dealerHasBlackjack) {
            return -1;
        } else {
            return 2;
        }
    }

    /**
     * Returns a string representation of the game's logo to be displayed at the
     * start
     * 
     * @return String containing the ASCII art logo of the game
     */
    public static String printLogo() {
        char[] logo = new char[1000];
        logo = """
                 $$$$$$$\\  $$\\                     $$\\         $$$$$\\                     $$\\
                 $$  __$$\\ $$ |                    $$ |        \\__$$ |                    $$ |
                 $$ |  $$ |$$ | $$$$$$\\   $$$$$$$\\ $$ |  $$\\      $$ | $$$$$$\\   $$$$$$$\\ $$ |  $$\\
                 $$$$$$$\\ |$$ | \\____$$\\ $$  _____|$$ | $$  |     $$ | \\____$$\\ $$  _____|$$ | $$  |
                 $$  __$$\\ $$ | $$$$$$$ |$$ /      $$$$$$  /$$\\   $$ | $$$$$$$ |$$ /      $$$$$$  /
                 $$ |  $$ |$$ |$$  __$$ |$$ |      $$  _$$< $$ |  $$ |$$  __$$ |$$ |      $$  _$$<
                 $$$$$$$  |$$ |\\$$$$$$$ |\\$$$$$$$\\ $$ | \\$$\\$$$$$$  |\\$$$$$$$ |\\$$$$$$$\\ $$ | \\$$\\
                 \\_______/ \\__| \\_______| \\_______|\\__|  \\__|\\______/  \\_______| \\_______|\\__|  \\__|
                """.toCharArray();
        StringBuilder result = new StringBuilder();
        for (char c : logo) {
            if (c == '$') {
                result.append(Color.GREEN.getCode() + c + Color.RESET.getCode());
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Displays a shuffling animation in the console by printing "Shuffling deck"
     * followed by three dots with a delay between each dot.
     * 
     * @throws InterruptedException if the thread is interrupted while sleeping
     */
    public static void displayShuffle() throws InterruptedException {
        System.out.print("Shuffling deck");
        for (int i = 0; i < 3; i++) {
            Thread.sleep(500);
            System.out.print(".");
        }
        System.out.println("\n");
    }

    /**
     * Nicely display the value of the card in ASCII characters
     * 
     * @param card The card to have displayed
     * @return The full string of the card on display
     */
    public static String displayCard(Card card) {
        StringBuilder sb = new StringBuilder();
        String temp = "";

        sb.append(" _______\n");
        temp = (card.getRank() == "Jack"
                || card.getRank() == "King") ? ("|" + card.getRank() + "   |\n")
                        : (card.getRank() == "Queen") ? ("|" + card.getRank() + "  |\n")
                                : (card.getRank() == "Ace") ? ("|" + card.getRank() + "    |\n")
                                        : (card.getRank() == "10") ? ("|" + card.getRank() + "     |\n")
                                                : ("|" + card.getRank() + "      |\n");
        sb.append(temp);

        sb.append("|       |\n");
        sb.append("|   .   |\n");
        sb.append("|       |\n");

        temp = (card.getRank() == "Jack"
                || card.getRank() == "King") ? ("|___" + card.getRank() + "|\n")
                        : (card.getRank() == "Queen") ? ("|__" + card.getRank() + "|\n")
                                : (card.getRank() == "Ace") ? ("|____" + card.getRank() + "|\n")
                                        : (card.getRank() == "10") ? ("|_____" + card.getRank() + "|\n")
                                                : ("|______" + card.getRank() + "|\n");
        sb.append(temp);

        return sb.toString();
    }

    /**
     * Command to clear console based on OS.
     * If the os is windows, use a series of commands to clear.
     * If the os is linux, use the base clear command.
     */
    @SuppressWarnings("deprecation")
    public static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (final Exception e) {
            System.out.println(e);
        }

    }
}
