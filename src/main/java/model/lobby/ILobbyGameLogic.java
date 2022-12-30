package model.lobby;

public interface ILobbyGameLogic {

    public void endGame() throws InterruptedException;

    public void  createWinnerScreen() throws InterruptedException;
}
