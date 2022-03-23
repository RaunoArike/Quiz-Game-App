package server.service;

import server.model.Player;

/**
 * Player management service
 */
public interface PlayerService {
	/**
	 * Generates a player with a given name.
	 *
	 * @param playerName The name of the player
	 * @return a player with the given name and a generated Id
	 */
	Player generatePlayer(String playerName);
}
