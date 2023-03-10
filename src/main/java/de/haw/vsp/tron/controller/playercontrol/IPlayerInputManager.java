package de.haw.vsp.tron.controller.playercontrol;

import java.util.List;

import de.haw.vsp.tron.middleware.annotation.AsyncCall;
import de.haw.vsp.tron.middleware.annotation.Prefix;
import de.haw.vsp.tron.middleware.annotation.RemoteImplementation;
import de.haw.vsp.tron.middleware.annotation.RemoteInterface;

@RemoteInterface
public interface IPlayerInputManager {
    @AsyncCall
    public void onKeyPress(@Prefix String key);

    public String[] getValidKeys();
}