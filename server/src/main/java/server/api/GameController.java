package server.api;

import commons.clientmessage.QuestionAnswerMessage;
import commons.clientmessage.SinglePlayerGameStartMessage;
import commons.clientmessage.WaitingRoomJoinMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import server.service.GameService;
import server.service.WaitingRoomService;

import java.security.Principal;

/**
 * A controller for client to server communication during the game
 */
@Controller
@RequestMapping
public class GameController {
	private final GameService gameService;
	private final WaitingRoomService waitingRoomService;
	private final ConnectionRegistry connectionRegistry;

	public GameController(GameService gameService, WaitingRoomService waitingRoomService,
		ConnectionRegistry connectionRegistry) {
		this.gameService = gameService;
		this.waitingRoomService = waitingRoomService;
		this.connectionRegistry = connectionRegistry;
	}

	/**
	 * Starts a new single-player game.
	 *
	 * @param startMessage message to the server containing the username of the player
	 * @param principal contains the connection id
	 */
	@MessageMapping("/start-single-player")
	public void startSPGame(@Payload SinglePlayerGameStartMessage startMessage, Principal principal) {
		int playerId = connectionRegistry.createPlayerIdForConnectionId(principal.getName());
		gameService.startSinglePlayerGame(playerId, startMessage.username());
	}

	@MessageMapping("/start-multi-player")
	public void startMPGame() throws NullPointerException {
		waitingRoomService.startMultiplayerGame();
	}

	/**
	 * Adds the player to the waiting room.
	 *
	 * @param waitingRoomJoinMessage A message containing the player's username
	 * @param principal contains the connection id
	 */
	@MessageMapping("/join-waiting-room")
	public void joinWaitingRoom(@Payload WaitingRoomJoinMessage waitingRoomJoinMessage, Principal principal) {
		int playerId = connectionRegistry.createPlayerIdForConnectionId(principal.getName());
		waitingRoomService.joinWaitingRoom(waitingRoomJoinMessage.username(), playerId);
	}

	/**
	 * Submits the player's answer to the server.
	 *
	 * @param answerMessage a message containing the player's answer
	 * @param principal contains the connection id
	 */
	@MessageMapping("/submit-answer")
	public void submitAnswer(@Payload QuestionAnswerMessage answerMessage, Principal principal) {
		int playerId = connectionRegistry.getPlayerIdByConnectionId(principal.getName());
		gameService.submitAnswer(playerId, answerMessage);
	}
}
