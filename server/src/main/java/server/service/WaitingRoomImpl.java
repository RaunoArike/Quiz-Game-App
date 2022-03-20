package server.service;

import lombok.Getter;
import lombok.Setter;
import server.api.OutgoingController;
import server.model.Game;
import server.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaitingRoomImpl implements WaitingRoom {
	@Getter
	@Setter
	//Maps the playerId to the waiting room they belong
	private Map<String, WaitingRoom> players = new HashMap<>();
	private final List<Player> listOfPlayers;
	private final OutgoingController outgoingController;
	private int playersInWaitingRoom;
	private Game currentGame;
	private Integer gameId;
	public WaitingRoomImpl(OutgoingController outgoingController) {
		this.outgoingController = outgoingController;
		listOfPlayers = new ArrayList<>();
	}

	@Override
	public boolean isInWaitingRoom(String playerName) {
		return players.containsKey(playerName) && listOfPlayers.contains(new Player(playerName));
	}

	@Override
	public void joinWaitingRoom(String playerName) {
		/*
		 * If the player isn't already in the waiting room,
		 * we add them to the map of players to the current waiting room
		 * * and to the list of players.
		 */
		if (!isInWaitingRoom(playerName)) {
			playersInWaitingRoom++;
			Player currentPlayer = new Player(playerName);
			players.put(playerName, this);
			listOfPlayers.add(new Player(playerName));
		}
	}

	@Override
	public void startMultiplayerGame(String playerName) {
		//TODO
	}
}
