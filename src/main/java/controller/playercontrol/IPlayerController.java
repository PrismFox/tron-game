package controller.playercontrol;

import java.util.List;

public interface IPlayerController {
    public void onKeyPress(String key);

    public List<String> getValidKeys();
}
