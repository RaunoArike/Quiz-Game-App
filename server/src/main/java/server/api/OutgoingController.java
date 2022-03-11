package server.api;

import commons.servermessage.CorrectAnswerMessage;
import commons.servermessage.QuestionMessage;

import java.util.List;

/**
 * Controller for messages from server to client
 */
public interface OutgoingController {
	/**
	 * Sends a new question to the client
	 * @param message - the message containing the question
	 * @param players - the players that receive the message
	 */
	void sendQuestion(QuestionMessage message, List<Integer> players);

	/**
	 * Sends the client's current score to the client
	 * @param message - the message containing the score
	 * @param players - the players that receive the message
	 */
	void sendScore(CorrectAnswerMessage message, List<Integer> players);
}
