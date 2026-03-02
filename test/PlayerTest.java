import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Player class.
 */
public class PlayerTest {

    /**
     * Test that a player created with only a name starts with the default chip
     * count of 10000.
     */
    @Test
    void playerStartsWithDefaultChipsIfNotSpecified() {
        Player p = new Player("Alice");
        assertEquals(10000, p.getChipCount());
    }

    /**
     * Test that a player created with a name and chip count has the correct chip
     * count.
     */
    @Test
    void winChipsIncreasesChipCount() {
        Player p = new Player("Bob", 5000);
        p.winChips(2000);
        assertEquals(7000, p.getChipCount());
    }

    /**
     * Test that loseChips decreases the chip count correctly.
     */
    @Test
    void loseChipsDecreasesChipCount() {
        Player p = new Player("Carol", 8000);
        p.loseChips(3000);
        assertEquals(5000, p.getChipCount());
    }

    /**
     * Test that printChipCount formats the chip count correctly for different
     * ranges.
     */
    @Test
    void printChipCountFormatsCorrectly() {
        Player p1 = new Player("Dan", 950);
        Player p2 = new Player("Eve", 15000);
        Player p3 = new Player("Frank", 2_500_000);

        assertEquals("950", p1.printChipCount());
        assertEquals("15K", p2.printChipCount());
        assertEquals("2M", p3.printChipCount());
    }
}