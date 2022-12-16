package model.gameLogic;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import model.player.PlayerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Data
@Service
public class PlayerLogic implements IPlayerLogic {

    @Autowired
    private PlayerManager playerManager;

    @Override
    public void killPlayer() {
        System.out.println("test");
    }

    public PlayerManager getPlayerManager() {
        return playerManager;

    }
}
