package server.api;

import commons.clientmessage.QuestionAnswerMessage;
import commons.clientmessage.SinglePlayerGameStartMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import server.service.GameService;
import server.service.WaitingRoomService;

import java.util.Map;

@Controller
@RequestMapping
public class GameController {
	private final GameService gameService;
	private final WaitingRoomService waitingRoomService;
	public GameController(GameService gameService, WaitingRoomService waitingRoomService) {
		this.gameService = gameService;
		this.waitingRoomService = waitingRoomService;
	}

	@MessageMapping("/start-single-player")
	public void startSPGame(@Payload SinglePlayerGameStartMessage startMessage, SimpMessageHeaderAccessor headerAcc) {
		Map<String, Object> attrs = headerAcc.getSessionAttributes();
		attrs.put("player", gameService.startSinglePlayerGame(startMessage.getUsername()));
	}
	@MessageMapping("/start-multiplayer-player")
	public void startMPGame(SimpMessageHeaderAccessor headerAcc) {
		Map<String, Object> attrs = headerAcc.getSessionAttributes();
		attrs.put("player", waitingRoomService.startMultiplayerGame());
	}
	@MessageMapping("/submit-answer")
	public void submitAnswer(@Payload QuestionAnswerMessage answerMessage, SimpMessageHeaderAccessor headerAccessor) {
		Map<String, Object> attrs = headerAccessor.getSessionAttributes();
		int playerId = (Integer) attrs.get("player");
		gameService.submitAnswer(playerId, answerMessage);
	}
}