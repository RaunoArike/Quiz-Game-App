package server.api;


import commons.servermessage.ScoreMessage;
import commons.servermessage.QuestionMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

public class OutgoingController {
    private final SimpMessagingTemplate template;

    public OutgoingController(SimpMessagingTemplate template) {
        this.template = template;
    }

    private void send(Object message, List<Integer> players, String destination) {
        for (int player : players) {
            template.convertAndSendToUser("" + player, "/topic/" + destination, message);
        }
    }

    public void sendQuestion(QuestionMessage message, List<Integer> players) {
        send(message, players, "question");
    }

    public void sendScore(ScoreMessage message, List<Integer> players) {
        send(message, players, "score");
    }
}
