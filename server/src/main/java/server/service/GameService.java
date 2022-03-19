package server.service;

import commons.clientmessage.QuestionAnswerMessage;
import server.model.Game;

/**
 * Game management service
 */
public interface GameService {
	/**
	 * Starts a single-player game
	 *
	 * @param userName name of the player, will be used for leaderboard
	 * @return created player id, used to identify player throughout the game
	 */
	int startSinglePlayerGame(String userName);

	/**
	 * Submits answer to the current question
	 *
	 * @param playerId player id
	 * @param answer submitted answer
	 */
	void submitAnswer(int playerId, QuestionAnswerMessage answer);

	/**
	 * Starts a multiplayer game given the waiting room
	 * @param userName name of the player, to be used in the leaderboard
	 * @return created player id to be identified in the game
	 */
	int startMultiPlayerGame(String userName);

	void startNewQuestion(Game game);
}
