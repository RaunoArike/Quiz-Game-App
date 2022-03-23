package server.api;

import commons.servermessage.QuestionMessage;
import commons.servermessage.ScoreMessage;
import commons.servermessage.WaitingRoomStateMessage;
import org.aspectj.bridge.Message;
import server.model.Player;

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
	void sendScore(ScoreMessage message, List<Integer> players);

	/**
	 * Sends the client the waiting room state
	 * @param message The message that has to be sent
	 * @param listOfPlayers The list of players that has to be passed as parameter
	 */
	void sendWaitingRoomState(WaitingRoomStateMessage message, List<Integer> listOfPlayers);
}
