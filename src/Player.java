/**
 * Represents a player in the blackjack game, including their name and chip
 * count.
 */
public class Player {
    private String name;
    private int chipCount;

    /**
     * Constructor for Player class that initializes the player's name and chip
     * count.
     * 
     * @param name      Name
     * @param chipCount Initial chip count for the player
     */
    public Player(String name, int chipCount) {
        this.name = name;
        this.chipCount = chipCount;
    }

    /**
     * Overloaded constructor that initializes the player's name and sets the
     * default chip count to 10000.
     * 
     * @param name Name of the player
     */
    public Player(String name) {
        this(name, 10000);
    }

    /**
     * Returns the player's name.
     * 
     * @return Player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the player's current chip count.
     * 
     * @return Player's chip count
     */
    public int getChipCount() {
        return chipCount;
    }

    /**
     * Formats the player's chip count for display. If the chip count is 1000 or
     * more but less than 1 million, it formats it as "XK". If it's 1 million or
     * more, it formats it as "XM". Otherwise, it returns the chip count as a
     * string.
     * 
     * @return Formatted chip count string
     */
    public String printChipCount() {
        if (chipCount >= 1000 && chipCount < 1000000) {
            return String.format("%dK", chipCount / 1000);
        } else if (chipCount >= 1000000) {
            return String.format("%dM", chipCount / 1000000);
        } else {
            return String.valueOf(chipCount);
        }
    }

    /**
     * Sets the player's chip count to a specific value.
     * 
     * @param chipCount New chip count to set for the player
     */
    public void setChipCount(int chipCount) {
        this.chipCount = chipCount;
    }

    /**
     * Increases the player's chip count by a specified amount when they win chips.
     * 
     * @param amount Amount of chips to add to the player's chip count
     */
    public void winChips(int amount) {
        this.chipCount += amount;
    }

    /**
     * Decreases the player's chip count by a specified amount when they lose chips.
     * 
     * @param amount Amount of chips to subtract from the player's chip count
     */
    public void loseChips(int amount) {
        this.chipCount -= amount;
    }
}