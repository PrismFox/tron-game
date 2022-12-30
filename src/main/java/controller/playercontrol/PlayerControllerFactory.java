package controller.playercontrol;

public class PlayerControllerFactory {
    public static IPlayerController createPlayerController(String impl) {
        switch(impl) {
            case "onboarding": return new PlayerControllerOnboardingImpl();
            case "movement": return new PlayerControllerMovementImpl();
            default: throw new IllegalArgumentException("Implementation '" + impl + "' is not supported.");
        }
    }
}
