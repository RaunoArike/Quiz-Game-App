package server.service;

import commons.servermessage.WaitingRoomStateMessage;
import org.springframework.stereotype.Service;
import server.api.OutgoingController;
import server.model.Player;

import java.util.ArrayList;
import java.util.List;

@Service
public class WaitingRoomServiceImpl implements WaitingRoomService {
	private final List<Player> listOfPlayers;
	private final OutgoingController outgoingController;
	private final GameService gameService;
	public WaitingRoomServiceImpl(OutgoingController outgoingController, GameService gameService) {
		this.outgoingController = outgoingController;
		listOfPlayers = new ArrayList<>();
		this.gameService = gameService;
	}

	@Override
	public boolean isInWaitingRoom(String playerName) {
		return listOfPlayers.contains(new Player(playerName, 0));
	}

	@Override
	public void joinWaitingRoom(String playerName, int playerId) {
		/*
		 * If the player isn't already in the waiting room,
		 * we add them to the map of players to the current waiting room
		 * * and to the list of players.
		 */
		if (!isInWaitingRoom(playerName)) {
			Player currentPlayer = new Player(playerName, playerId);
			listOfPlayers.add(currentPlayer);
			broadcastNotify();
		}
	}

	@Override
	public void startMultiplayerGame() {
		gameService.generateNewQuestion(listOfPlayers);
		resetWaitingRoom();
	}

	private void resetWaitingRoom() {
		//clears the list of players whilst resetting the number of players
		listOfPlayers.clear();
	}
	private void broadcastNotify() {
		List<Player>  tempListOfPlayers = new ArrayList<>();
		int i = 0;
		int numberOfDummies = listOfPlayers.size();
		while (i < numberOfDummies) {
			Player currentPlayer = listOfPlayers.get(i);
			tempListOfPlayers.add(currentPlayer);
			i++;
		}
		int numberOfPlayers = listOfPlayers.size();
		WaitingRoomStateMessage waitingRoomStateMessage = new WaitingRoomStateMessage(numberOfPlayers);
		if (numberOfPlayers != 0) {
			List<Integer> listOfPlayerIds = new ArrayList<>();
			for (Player player : listOfPlayers) {
				listOfPlayerIds.add(player.getPlayerId());
			}
			outgoingController.sendWaitingRoomState(waitingRoomStateMessage, listOfPlayerIds);
		}
	}

}
