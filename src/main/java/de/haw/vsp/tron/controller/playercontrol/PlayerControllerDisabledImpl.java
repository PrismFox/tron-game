package de.haw.vsp.tron.controller.playercontrol;

import java.util.ArrayList;
import java.util.List;

public class PlayerControllerDisabledImpl implements IPlayerController{
    @Override
    public void onKeyPress(String key) {
        return;
    }

    @Override
    public List<String> getValidKeys() {
        return new ArrayList<>();
    }
}
