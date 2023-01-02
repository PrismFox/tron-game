package de.haw.vsp.tron.controller.playercontrol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlayerControllerFactory {

    @Autowired
    private static PlayerControllerOnboardingImpl playerControllerOnboarding;

    @Autowired
    private static PlayerControllerMovementImpl playerControllerMovement;

    public static IPlayerController createPlayerController(String impl) {
        switch(impl) {
            case "onboarding": return playerControllerOnboarding;
            case "movement": return playerControllerMovement;
            default: throw new IllegalArgumentException("Implementation '" + impl + "' is not supported.");
        }
    }
}
