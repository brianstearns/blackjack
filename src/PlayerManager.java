import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.io.FileReader;
import java.io.FileWriter;

public class PlayerManager {
    private List<Player> players;
    private Gson gson = new Gson();
    private String filename = "data/players.json";

    public void loadPlayers() {
        try (FileReader reader = new FileReader(filename)) {
            Type playerListType = new TypeToken<List<Player>>() {
            }.getType();
            players = gson.fromJson(reader, playerListType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savePlayers() {
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(players, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Player> getPlayers() {
        return players;
    }
}