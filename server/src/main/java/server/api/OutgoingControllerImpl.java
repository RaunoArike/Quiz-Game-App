package server.api;

import commons.servermessage.*;
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

	@Override
	public void sendQuestion(QuestionMessage message, List<Integer> players) {
		send(message, players, "question");
	}

	@Override
	public void sendScore(ScoreMessage message, List<Integer> players) {
		send(message, players, "score");
	}

	@Override
	public void sendEndOfGame(List<Integer> players) {
		send(new Object(), players, "end-of-game");
	}

	@Override
	public void sendWaitingRoomState(WaitingRoomStateMessage message, List<Integer> listOfPlayers) {
		send(message, listOfPlayers, "waiting-room-state");
	}

	@Override
	public void sendIntermediateLeaderboard(IntermediateLeaderboardMessage message, List<Integer> listOfPlayers) {
		send(message, listOfPlayers, "intermediate-leaderboard");
	}

	@Override
	public void sendError(ErrorMessage message, List<Integer> players) {
		send(message, players, "error");
	}
}
