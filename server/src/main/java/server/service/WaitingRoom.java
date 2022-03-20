package server.service;


public interface WaitingRoom {
	/**
	 *Check if the given player by their name is in the waiting room
	 * @param playerName The name of the player
	 * @return true if they are in the waiting room, false otherwise
	 */
	boolean isInWaitingRoom(String playerName);

	/**
	 *Puts the player in the waiting room if not already
	 * @param playerName The name of the player
	 */
	void joinWaitingRoom(String playerName);

	/**
	 * The game of the given waiting room is started by one of the players
	 * @param playerName The name of one player from the waiting room
	 */
	void startMultiplayerGame(String playerName);
}
