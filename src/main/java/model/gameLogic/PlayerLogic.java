package model.gameLogic;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import model.player.IPlayerManager;
import model.player.PlayerManager;
import org.springframework.stereotype.Service;


@Slf4j
@Data
@Service
public class PlayerLogic implements IPlayerLogic {

    private PlayerManager playerManager;

    public PlayerLogic(){
        this.playerManager = new PlayerManager();
    }

    @Override
    public void killPlayer() {
        System.out.println("test");
    }

    public IPlayerManager getPlayerManager() {
        return playerManager;

    }
}
