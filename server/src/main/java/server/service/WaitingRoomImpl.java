package server.service;

import lombok.Getter;
import lombok.Setter;
import server.api.OutgoingController;
import server.model.Game;
import server.model.Player;

import java.util.HashMap;
import java.util.Map;

public class WaitingRoomImpl implements WaitingRoom {
	@Getter
	@Setter
	private Map<Integer, Player> listOfPlayers = new HashMap<>();
	private final Map<Integer, Game> listOfGames = new HashMap<>();

	private String gameId;
	private OutgoingController outgoingController;
	@Override
	public boolean isInWaitingRoom(Player player) {
		return false;
	}

	@Override
	public void addPlayer(Player player) {

	}

	@Override
	public boolean isInProgress() {
		return false;
	}
}
