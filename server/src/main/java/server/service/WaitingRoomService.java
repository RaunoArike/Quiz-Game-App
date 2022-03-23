package server.service;

/**
 * Waiting room management service
 */
public interface WaitingRoomService {
	/**
	 * Check if the given player by their name is in the waiting room
	 *
	 * @param playerName The name of the player
	 * @return true if they are in the waiting room, false otherwise
	 */
	boolean isInWaitingRoom(String playerName);

	/**
	 * Puts the player in the waiting room if not already there
	 *
	 * @param playerName The name of the player
	 * @return the player id
	 */
	int joinWaitingRoom(String playerName);

	/**
	 * The game of the given waiting room is started by one of the players
	 *
	 * @return returns the player id starting the game
	 */
	Object startMultiplayerGame();

	/**
	 * Resets the waiting room
	 */
	void resetWaitingRoom();
}
