package server.service;

import org.springframework.stereotype.Service;
import server.api.OutgoingController;
import server.model.Game;
import server.model.Player;

import java.util.ArrayList;
import java.util.List;

@Service
public class WaitingRoomServiceImpl implements WaitingRoomService {
	private final List<Player> listOfPlayers;
	private final OutgoingController outgoingController;
	private int playersInWaitingRoom;
	private Game currentGame;
	public WaitingRoomServiceImpl(OutgoingController outgoingController) {
		this.outgoingController = outgoingController;
		listOfPlayers = new ArrayList<>();
	}

	@Override
	public boolean isInWaitingRoom(String playerName) {
		return listOfPlayers.contains(new Player(playerName, 0));
	}

	@Override
	public int joinWaitingRoom(String playerName) {
		/*
		 * If the player isn't already in the waiting room,
		 * we add them to the map of players to the current waiting room
		 * * and to the list of players.
		 */
		if (!isInWaitingRoom(playerName)) {
			playersInWaitingRoom++;
			Player currentPlayer = new Player(playerName, 0);
			listOfPlayers.add(currentPlayer);
		}
		return 0;
	}

	@Override
	public Object startMultiplayerGame() {
		for (Player player : listOfPlayers) {
			var currentPlayer = player;
			var playerGame = currentGame;
			currentGame.addPlayer(currentPlayer.getPlayerId(), currentPlayer);
			startNewQuestion(currentGame);
		}
		return currentGame;
	}

	@Override
	public void resetWaitingRoom() {
		//clears the list of players whilst resetting the number of players
		playersInWaitingRoom = 0;
		listOfPlayers.clear();
	}

	@Override
	public void startNewQuestion(Game game) {
		var noQuestions = 0;
		var currentQuestion = game.getCurrentQuestion();
		while (currentQuestion != null
			&& noQuestions <= game.QUESTIONS_PER_GAME) {
			startNewQuestion(game);
			noQuestions++;
		}
	}
}
