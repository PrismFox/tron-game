package controller.playercontrol;

import java.util.List;

import middleware.annotation.RemoteInterface;

@RemoteInterface
public interface IPlayerController {
    public void onKeyPress(String key);

    public List<String> getValidKeys();
}
