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
		System.out.println("Question sent");
	}

	@Override
	public void sendScore(ScoreMessage message, List<Integer> players) {
		send(message, players, "score");
		System.out.println("Score sent");
	}

	@Override
	public void sendEndOfGame(EndOfGameMessage message, List<Integer> players) {
		send(message, players, "end-of-game");
		System.out.println("End of game sent");
	}

	@Override
	public void sendWaitingRoomState(WaitingRoomStateMessage message, List<Integer> listOfPlayers) {
		send(message, listOfPlayers, "waiting-room-state");
		System.out.println("Waiting room state sent");
	}

	@Override
	public void sendIntermediateLeaderboard(IntermediateLeaderboardMessage message, List<Integer> listOfPlayers) {
		send(message, listOfPlayers, "intermediate-leaderboard");
		System.out.println("Intermediate leaderboard sent");
	}

	@Override
	public void sendError(ErrorMessage message, List<Integer> players) {
		send(message, players, "error");
	}

	@Override
	public void sendTimeReduced(ReduceTimePlayedMessage message, List<Integer> players) {
		send(message, players, "reduce-time-played");
		System.out.println("ReduceTimePlayed Message sent");
	}
}
