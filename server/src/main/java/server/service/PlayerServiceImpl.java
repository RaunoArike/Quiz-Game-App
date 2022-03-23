package server.service;

import org.springframework.stereotype.Service;
import server.model.Player;

@Service
public class PlayerServiceImpl implements PlayerService {
	private int nextPlayerId = 0;
	@Override
	public Player generatePlayer(String playerName) {
		var playerId = nextPlayerId++;
		var player = new Player(playerName, playerId);
		return player;
	}

}