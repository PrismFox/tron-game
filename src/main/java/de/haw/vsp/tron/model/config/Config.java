package de.haw.vsp.tron.model.config;

import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Component
public class Config implements IConfig {

    private final static String ENVVAR_NAME = "TRON_CONFIG";
    private final static String DEFAULT_PATH = "config.properties";

    private Map<Integer, List<String>> playerMappings = new HashMap<>();
    private int lobbyTimerDuration = 30;
    private int playerCount = 6;
    private int[] boardSize = new int[]{16, 16};
    private int cellSize = 5;
    private int gameTick = 1000;

    public Config() {
        String configPath = System.getenv().getOrDefault(ENVVAR_NAME, DEFAULT_PATH);
        loadConfigFile(configPath);
    }

    private void loadConfigFile(String path) {
        lobbyTimerDuration = 30;
        playerCount = 6;
        boardSize = new int[]{16, 16};
        cellSize = 5;
        gameTick=1000;

        Properties prop = new Properties();
        try (FileInputStream input = new FileInputStream(path)) {
            prop.load(input);
            this.lobbyTimerDuration = Integer.parseInt(prop.getProperty("lobbyTimer", "30"));
            this.playerCount = Integer.parseInt(prop.getProperty("playerCount", "6"));
            this.gameTick = Integer.parseInt(prop.getProperty("gameTick", "1000"));
            String boardValue = prop.getProperty("boardSize", "16, 16");
            String[] boardArray = boardValue.split(",");
            if (boardArray.length != 2) {
                //TODO: handle error
            }
            this.boardSize = Arrays.asList(boardArray).stream().map(c -> Integer.parseInt(c.strip())).mapToInt(c -> c).toArray();
            for (int i = 1; i <= this.playerCount; i++) {
                String keys = prop.getProperty(String.format("player%dKeys", i));
                if (keys == null) {
                    //TODO: handle error
                }
                List<String> keyList = Arrays.asList(keys.split(",")).stream().map(k -> k.strip()).collect(Collectors.toList());
                if (keyList.size() != 1) {
                    //TODO: handle error
                }
                this.playerMappings.put(i, keyList);
            }


        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Map<Integer, List<String>> getPlayerMappings() {
        return this.playerMappings;
    }

    @Override
    public int getLobbyTimerDuration() {
        return this.lobbyTimerDuration;
    }

    @Override
    public int getPlayerCount() {
        return this.playerCount;
    }

    @Override
    public int[] getBoardSize() {
        return this.boardSize;
    }

    @Override
    public int getCellSize() {
        return this.cellSize;
    }

    @Override
    public int getGameTick() {
        return this.gameTick;
    }

    @Override
    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }
}
