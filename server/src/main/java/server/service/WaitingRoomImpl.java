package server.service;

import lombok.Getter;
import lombok.Setter;
import server.api.OutgoingController;
import server.model.Game;

import java.util.HashMap;
import java.util.Map;

public class WaitingRoomImpl implements WaitingRoom {
	@Getter
	@Setter
	//Maps the playerId to the waiting room they belong
	private Map<String, WaitingRoom> listOfPlayers = new HashMap<>();
	// gameIds to game
	private final Map<Integer, Game> games = new HashMap<>();
	private final Map<Integer, Integer> playersToGame = new HashMap<>();
	private int playersInWaitingRoom;
	private Game currentGame;
	private String gameId;
	private final OutgoingController outgoingController;
	public WaitingRoomImpl(OutgoingController outgoingController) {
		this.outgoingController = outgoingController;
	}

	@Override
	public boolean isInWaitingRoom(String playerName) {
		return false;
	}

	@Override
	public void joinWaitingRoom(String playerName) {

	}

	@Override
	public boolean isInProgress() {
		return false;
	}

	@Override
	public void startMultiplayerGame(String playerName) {

	}
}
