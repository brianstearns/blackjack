public class Player {
    private String name;
    private int chipCount;

    public Player(String name, int chipCount) {
        this.name = name;
        this.chipCount = chipCount;
    }

    public Player(String name) {
        this(name, 10000);
    }

    public String getName() {
        return name;
    }

    public int getChipCount() {
        return chipCount;
    }

    public String printChipCount() {
        if (chipCount >= 1000 && chipCount < 1000000) {
            return String.format("%dK", chipCount / 1000);
        } 
        else if (chipCount >= 1000000) {
            return String.format("%dM", chipCount / 1000000);
        }
        else {
            return String.valueOf(chipCount);
        }
    }

    public void setChipCount(int chipCount) {
        this.chipCount = chipCount;
    }

    public void winChips(int amount) {
        this.chipCount += amount;
    }

    public void loseChips(int amount) {
        this.chipCount -= amount;
    }
}