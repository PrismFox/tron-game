package de.haw.vsp.tron.controller.playercontrol;

import java.util.List;

public interface IPlayerInputManager {
    public void onKeyPress(String key);

    public List<String> getValidKeys();
}