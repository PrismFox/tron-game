package model.config;

import java.util.List;
import java.util.Map;

public class Config implements IConfig{
    @Override
    public Map<Integer, List<String>> getPlayerMappings() {
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

    @Override
    public int[] getBoardSize(){
        return new int[]{16,16};
    }

    public int getCellSize(){
        return 5;
    }
}
