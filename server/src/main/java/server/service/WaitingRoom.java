package server.service;


public interface WaitingRoom {
	boolean isInWaitingRoom(String playerName);

	void joinWaitingRoom(String palyerName);

	boolean isInProgress();

	void startMultiplayerGame(String playerName);
}
