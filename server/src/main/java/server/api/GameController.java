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

	@MessageMapping("/start-single-player")
	public void startSPGame(@Payload SinglePlayerGameStartMessage startMessage, Principal principal) {
		int playerId = connectionRegistry.createPlayerIdForConnectionId(principal.getName());
		gameService.startSinglePlayerGame(playerId, startMessage.username());
	}

	@MessageMapping("/start-multiplayer-player")
	public void startMPGame() throws NullPointerException {
		waitingRoomService.startMultiplayerGame();
	}

	@MessageMapping("/join-waiting-room")
	public void joinWaitingRoom(@Payload WaitingRoomJoinMessage waitingRoomJoinMessage, Principal principal) {
		int playerId = connectionRegistry.createPlayerIdForConnectionId(principal.getName());
		waitingRoomService.joinWaitingRoom(waitingRoomJoinMessage.username(), playerId);
	}

	@MessageMapping("/submit-answer")
	public void submitAnswer(@Payload QuestionAnswerMessage answerMessage, Principal principal) {
		int playerId = connectionRegistry.getPlayerIdByConnectionId(principal.getName());
		gameService.submitAnswer(playerId, answerMessage);
	}
}
