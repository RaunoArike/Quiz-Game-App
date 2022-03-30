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

	/**
	 * Exits the waiting room.
	 */
	void exitWaitingRoom();

	/**
	 * Starts a multiplayer game, the player must already be in a waiting room.
	 */
	void startMultiGame();

	/**
	 * Submits answer to the current question.
	 * @param answer answer; Integer or Float depending on the question type
	 * 0, 1 or 2 for choice questions, float answer for open-ended questions
	 */
	void answerQuestion(Number answer);
}
