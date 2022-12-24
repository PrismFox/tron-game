package model.gameLogic;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.player.IPlayerManager;
import model.player.Player;
import model.player.PlayerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Data
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PlayerLogic implements IPlayerLogic {

    private final IPlayerManager playerManager;



    @Override
    public void killPlayer(int playerId) {
        playerManager.killPlayer(playerId);
    }

    public IPlayerManager getPlayerManager() {
        return playerManager;
    }

    @Override
    public int[] getWinnerStatus() {
        List<Player> livingPlayers = getPlayerManager().getLivingPlayers();
        int[] result = new int[2];

        if (livingPlayers.size() == 0) {

            result[1] = -1;

        }

        if (livingPlayers.size() == 1) {
            result[0] = 1;
            result[1] = livingPlayers.get(0).getId();
        }

        return result;
    }
}
