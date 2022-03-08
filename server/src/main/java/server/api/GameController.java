package server.api;

import commons.clientmessage.QuestionAnswerMessage;
import commons.clientmessage.SinglePlayerGameStartMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import server.service.GameService;

import java.util.Map;

@Controller
@RequestMapping
public class GameController {

    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    @MessageMapping("/start-single-player")
    public void startSinglePlayerGame(@Payload SinglePlayerGameStartMessage startMessage, SimpMessageHeaderAccessor headerAccessor) {
        Map<String, Object> attrs = headerAccessor.getSessionAttributes();
        attrs.put("player", service.startSinglePlayerGame(startMessage.getUsername()));
    }

    @MessageMapping("/score")
    public void getScore(@Payload QuestionAnswerMessage answerMessage, SimpMessageHeaderAccessor headerAccessor) {
        Map<String, Object> attrs = headerAccessor.getSessionAttributes();
        int playerId = (Integer) attrs.get("player");
        service.submitAnswer(playerId, answerMessage);
    }
}



