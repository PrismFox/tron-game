package model.config;

import java.util.Map;

public class Config implements IConfig{
    @Override
    public Map<String, Integer> getPlayerMappings() {
        return null;
    }

    @Override
    public int getLobbyTimerDuration() {
        return 0;
    }

    @Override
    public int getPlayerCount() {
        return 0;
    }
}
