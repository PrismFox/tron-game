package de.haw.vsp.tron.controller.playercontrol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class PlayerControllerFactory {

    @Autowired
    private PlayerControllerOnboardingImpl playerControllerOnboarding;

    @Autowired
    private PlayerControllerMovementImpl playerControllerMovement;

    public IPlayerController createPlayerController(String impl) {
        switch(impl) {
            case "onboarding":
                return playerControllerOnboarding;
            case "movement": return playerControllerMovement;
            case "disabled": return new PlayerControllerDisabledImpl();
            default: throw new IllegalArgumentException("Implementation '" + impl + "' is not supported.");
        }
    }
}
