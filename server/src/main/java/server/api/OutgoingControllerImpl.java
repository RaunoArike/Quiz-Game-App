package server.api;

import commons.servermessage.ScoreMessage;
import commons.servermessage.IntermediateLeaderboardMessage;
import commons.servermessage.QuestionMessage;
import commons.servermessage.WaitingRoomStateMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutgoingControllerImpl implements OutgoingController {
	private final SimpMessagingTemplate template;
	private final ConnectionRegistry connectionRegistry;

	public OutgoingControllerImpl(SimpMessagingTemplate template, ConnectionRegistry connectionRegistry) {
		this.template = template;
		this.connectionRegistry = connectionRegistry;
	}

	private void send(Object message, List<Integer> players, String destination) {
		for (int playerId : players) {
			String connectionId = connectionRegistry.getConnectionIdByPlayerId(playerId);
			if (connectionId == null) continue;
			template.convertAndSendToUser(connectionId, "/queue/" + destination, message);
		}
	}

	public void sendQuestion(QuestionMessage message, List<Integer> players) {
		send(message, players, "question");
	}

	public void sendScore(ScoreMessage message, List<Integer> players) {
		send(message, players, "score");
	}

	public void sendWaitingRoomState(WaitingRoomStateMessage message, List<Integer> listOfPlayers) {
		String destination = new String("waiting-room-state");
		send(message, listOfPlayers, destination);
	}

	public void sendIntermediateLeaderboard(IntermediateLeaderboardMessage message, List<Integer> listOfPlayers) {
		send(message, listOfPlayers, "intermediate-leaderboard");
	}
}
