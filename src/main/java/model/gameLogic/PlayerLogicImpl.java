package model.gameLogic;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.player.PlayerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Data
public class PlayerLogicImpl implements PlayerLogic{

    private final PlayerManager playerManager;

    @Override
    public void killPlayer() {
        System.out.println("test");
    }


}
