package server.model;

import lombok.Getter;

@Getter
public class Player {
	private final String name;

	private int score = 0;

	public Player(String name) {
		this.name = name;
	}

	public void incrementScore(int scoreDelta) {
		score += scoreDelta;
	}
}
