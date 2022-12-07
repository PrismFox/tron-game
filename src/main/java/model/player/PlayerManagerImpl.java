package model.player;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Service
public class PlayerManagerImpl implements PlayerManager {

    private List<Player> players;

    @Override
    public Map<String, Integer> getPlayerMappings() {
        Map<String, Integer>
    }

    @Override
    public void setPlayerMove(int playerId, int direction) {

    }

    @Override
    public List<Player> getLivingPlayers() {
        List<Player> livingPlayers = new ArrayList<>();

        for (Player player: players) {
            if (player.isAlive())
                livingPlayers.add(player);
        }

        return livingPlayers;
    }

    @Override
    public void notifyCollisions() {

    }

    @Override
    public void commitPlayerMoves() {


    }
}
