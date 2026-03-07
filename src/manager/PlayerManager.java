package manager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import model.Player;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Manages loading and saving player data to a JSON file using Gson.
 */
public class PlayerManager {
    private List<Player> players = new ArrayList<>();
    private Gson gson = new Gson();
    private String filename = "data/players.json";

    /**
     * Loads the list of players from the JSON file.
     */
    public void loadPlayers() {

        File file = new File("data/players.json");

        if (!file.exists()) {
            System.out.println("players.json not found. Creating new file...");
            savePlayers();
            return;
        }

        try (FileReader reader = new FileReader(filename)) {
            Type playerListType = new TypeToken<List<Player>>() {
            }.getType();
            List<Player> loaded = gson.fromJson(reader, playerListType);
            players = (loaded != null) ? loaded : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            players = new ArrayList<>();
        }
    }

    /**
     * Saves the current list of players to the JSON file.
     */
    public void savePlayers() {
        try {
            File file = new File(filename);

            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(players, writer);
            }

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