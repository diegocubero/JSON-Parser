import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class QuakeLogParser {
    private final String logFilePath;

    public QuakeLogParser(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    // In the Game class, Java data structures are created which is where the parsed information from the log file is stored.
    static class Game {
        int totalKills;
        List<String> players = new ArrayList<>();
        Map<String, Integer> kills = new HashMap<>();

        void addPlayer(String player) {
            if (!players.contains(player)) {
                players.add(player);
                kills.put(player, 0);
            }
        }

        void addKill(String killer) {
            totalKills++;
            kills.put(killer, kills.getOrDefault(killer, 0) + 1);
        }
    }

    public List<Game> parse() {
        List<Game> games = new ArrayList<>();
        Game currentGame = new Game();

        // The log file is read line by line. The lines are then parsed to extract the relevant information.
        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("InitGame")) {
                    currentGame = new Game();
                    games.add(currentGame);
                } else if (line.contains("Kill")) {
                    // Extract relevant information from the kill line
                    String[] parts = line.split(":");
                    String details = parts[parts.length - 1].trim();
                    String[] killDetails = details.split("\\s+");
                    String killer = killDetails[0];
                    String victim = killDetails[1];

                    currentGame.addPlayer(killer);
                    currentGame.addPlayer(victim);
                    currentGame.addKill(killer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return games;
    }

    public static void main(String[] args) {
        // Necessary variables and objects are defined and initialized.
        String logFilePath = "src/main/resources/qgames.log";
        QuakeLogParser parser = new QuakeLogParser(logFilePath);
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        List<Game> games = parser.parse();

        // The parsed data is converted into JSON format using the Gson library.
        // The resulting JSON file is organized in a per-game basis.
        Gson gson = new Gson();
        jsonWriter.setIndent("  "); // Use two spaces for indentation
        gson.toJson(games, new TypeToken<List<Game>>(){}.getType(), jsonWriter);
        // The JSON file is prettified to be readable and then printed in the console.
        String prettifiedJsonOutput = stringWriter.toString();
        System.out.println(prettifiedJsonOutput);
    }
}