package server.service;

import commons.clientmessage.QuestionAnswerMessage;

/**
 * Game management service
 */
public interface GameService {
	/**
	 * Starts a single-player game
	 * @param userName name of the player, will be used for leaderboard
	 * @return created player id, used to identify player throughout the game
	 */
	int startSinglePlayerGame(String userName);

	/**
	 * Submits answer to the current question
	 * @param playerId player id
	 * @param answer submitted answer
	 */
	void submitAnswer(int playerId, QuestionAnswerMessage answer);
}
