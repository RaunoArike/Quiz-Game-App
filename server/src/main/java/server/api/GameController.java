package server.api;

import commons.clientmessage.QuestionAnswerMessage;
import commons.clientmessage.SinglePlayerGameStartMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import server.service.GameService;

import java.security.Principal;

@Controller
@RequestMapping
public class GameController {
	private final GameService service;
	private final ConnectionRegistry connectionRegistry;

	public GameController(GameService service, ConnectionRegistry connectionRegistry) {
		this.service = service;
		this.connectionRegistry = connectionRegistry;
	}

	@MessageMapping("/start-single-player")
	public void startSPGame(@Payload SinglePlayerGameStartMessage startMessage, Principal principal) {
		int playerId = connectionRegistry.createPlayerIdForConnectionId(principal.getName());
		service.startSinglePlayerGame(playerId, startMessage.username());
	}

	@MessageMapping("/submit-answer")
	public void submitAnswer(@Payload QuestionAnswerMessage answerMessage, Principal principal) {
		int playerId = connectionRegistry.getPlayerIdByConnectionId(principal.getName());
		service.submitAnswer(playerId, answerMessage);
	}
}
