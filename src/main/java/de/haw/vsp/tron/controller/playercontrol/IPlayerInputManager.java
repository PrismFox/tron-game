package de.haw.vsp.tron.controller.playercontrol;

import java.util.List;

import de.haw.vsp.tron.middleware.annotation.Prefix;
import de.haw.vsp.tron.middleware.annotation.RemoteImplementation;

@RemoteImplementation
public interface IPlayerInputManager {
    public void onKeyPress(@Prefix String key);

    public List<String> getValidKeys();
}