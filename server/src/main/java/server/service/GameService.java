package server.service;

import commons.clientmessage.QuestionAnswerMessage;

/**
 * Game management service
 */
public interface GameService {
	/**
	 * Starts a single-player game.
	 *
	 * @param playerId player id, used to identify player throughout the game
	 * @param userName name of the player, will be used for leaderboard
	 */
	void startSinglePlayerGame(int playerId, String userName);

	/**
	 * Submits answer to the current question.
	 *
	 * @param playerId player id
	 * @param answer submitted answer
	 */
	void submitAnswer(int playerId, QuestionAnswerMessage answer);
}
