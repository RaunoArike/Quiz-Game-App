package client.service;

import commons.servermessage.ErrorMessage;
import commons.servermessage.QuestionMessage;
import commons.servermessage.ScoreMessage;
import commons.servermessage.WaitingRoomStateMessage;

/**
 * Service for communication with server
 */
public interface ServerService {
	/**
	 * Listener for incoming server messages
	 */
	interface ServerListener {
		/**
		 * Called when new question starts
		 * @param message message with new question details
		 */
		void onQuestion(QuestionMessage message);

		/**
		 * Called when question ends
		 * @param message message with player's score
		 */
		void onScore(ScoreMessage message);

		/**
		 * Called when waiting room state is updated
		 * @param message message with waiting room details
		 */
		void onWaitingRoomState(WaitingRoomStateMessage message);

		/**
		 * Called when the endofGame message is received from the server
		 */
		void onEndOfGame();

		/**
		 * Called when error occurs at the server side
		 * @param message message with error details
		 */
		void onError(ErrorMessage message);
	}

	/**
	 * Connects to the server.
	 * @param url server url
	 * @return true if connection successful, false otherwise
	 */
	boolean connectToServer(String url);

	/**
	 * Start a single player game.
	 * Can only be called if connected to server.
	 * @param username username
	 */
	void startSingleGame(String username);

	/**
	 * Joins the waiting room.
	 * Can only be called if connected to server.
	 * @param username username
	 */
	void joinWaitingRoom(String username);

	/**
	 * Starts a multiplayer game, the player must already be in a waiting room.
	 * Can only be called if connected to server.
	 */
	void startMultiGame();

	/**
	 * Submits answer to the current question.
	 * Can only be called if connected to server.
	 * @param answer answer; Integer or Float depending on the question type
	 * 0, 1 or 2 for choice questions, float answer for open-ended questions
	 */
	void answerQuestion(Number answer);

	/**
	 * Registers a listener for server messages.
	 * @param serverListener listener
	 */
	void registerListener(ServerListener serverListener);
}
