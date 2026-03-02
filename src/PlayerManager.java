import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Manages loading and saving player data to a JSON file using Gson.
 */
public class PlayerManager {
    private List<Player> players;
    private Gson gson = new Gson();
    private String filename = "data/players.json";

    /**
     * Loads the list of players from the JSON file. 
     */
    public void loadPlayers() {
        try (FileReader reader = new FileReader(filename)) {
            Type playerListType = new TypeToken<List<Player>>() {
            }.getType();
            players = gson.fromJson(reader, playerListType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the current list of players to the JSON file.
     */
    public void savePlayers() {
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(players, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the list of players.
     * 
     * @return List of Player objects
     */
    public List<Player> getPlayers() {
        return players;
    }
}