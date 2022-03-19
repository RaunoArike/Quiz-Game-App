package server.service;

import server.model.Player;


public interface WaitingRoom {
	/**
     *  *  * Checks if the player is already in the game
     * @param player PLayer in the game
     * @return true if so, false otherwise
     */
	boolean isInWaitingRoom(Player player);

	/**
     * Adds the player to the game if not already
     * @param player Player in the game
     */
	void addPlayer(Player player);

	/**
     * Check if the waiting room has a game in progress
     * @return true if so, false otherwise
     */
	boolean isInProgress();
}
