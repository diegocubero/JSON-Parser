import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuakeLogParserTest {

    private QuakeLogParser parser;

    @BeforeEach
    public void setUp() throws IOException {
        String sampleLogContent = """
                    InitGame: ...
                    Kill: player1 killed player2
                    Kill: player1 killed player3
                    Kill: player3 killed player1
                    InitGame: ...
                    Kill: player2 killed player4
                """;

        // Write the content to a temporary file.
        Path tempFile = Files.createTempFile("quake3_", ".log");
        Files.write(tempFile, sampleLogContent.getBytes());

        parser = new QuakeLogParser(tempFile.toString());
    }

    @Test
    public void testTotalGames() {
        List<QuakeLogParser.Game> games = parser.parse();
        assertEquals(2, games.size(), "There should be 2 games in the sample log");
    }

    @Test
    public void testPlayerKillsInFirstGame() {
        List<QuakeLogParser.Game> games = parser.parse();
        assertEquals(2, games.get(0).kills.get("player1").intValue(), "player1 should have 2 kills in the first game");
        assertEquals(1, games.get(0).kills.get("player3").intValue(), "player3 should have 1 kill in the first game");
    }

    @Test
    public void testTotalKillsInSecondGame() {
        List<QuakeLogParser.Game> games = parser.parse();
        assertEquals(1, games.get(1).totalKills, "There should be 1 kill in the second game");
    }
}