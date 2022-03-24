package server.model;

import lombok.Getter;

@Getter
public class Player {
	private final String name;
	private final int playerId;
	private int score = 0;

	public Player(String name, int playerId, int score) {
		this.name = name;
		this.playerId = playerId;
		this.score = score;
	}

	public Player(String name, int playerId) {
		this.name = name;
		this.playerId = playerId;
	}

	public void incrementScore(int scoreDelta) {
		score += scoreDelta;
	}
}
