package client.service;

public interface MessageLogicService {
	/**
	 * Start a single player game.
	 * @param username username
	 */
	void startSingleGame(String username);

	/**
	 * Joins the waiting room.
	 * @param username username
	 */
	void joinWaitingRoom(String username);
}
