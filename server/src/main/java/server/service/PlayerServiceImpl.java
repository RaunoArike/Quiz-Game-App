package server.service;

import org.springframework.stereotype.Service;
import server.model.Player;

@Service
public class PlayerServiceImpl implements PlayerService {

	@Override
	public Player playerGeneration(String playerName) {
		Player player = new Player(playerName, 0);
		return player;
	}
}