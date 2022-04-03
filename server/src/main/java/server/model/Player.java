package server.model;

import commons.model.JokerType;
import lombok.Getter;

import java.util.HashMap;


@Getter
public class Player {
	private final String name;
	private final int playerId;
	private Number latestAnswer;
	private long timeTakenToAnswer;
	private int score = 0;
	private HashMap<JokerType, Boolean> jokerAvailability = new HashMap<>();

	public void init() {
			jokerAvailability.put(JokerType.REDUCE_TIME, true);
			jokerAvailability.put(JokerType.DOUBLE_POINTS, true);
			jokerAvailability.put(JokerType.ELIMINATE_MC_OPTION, true);

	}

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

	public void setLatestAnswer(Number answer) {
		this.latestAnswer = answer;
	}

	public void setTimeTakenToAnswer(Long time) {
		this.timeTakenToAnswer = time;
	}

	public void setJokerAvailability(JokerType type, boolean isAvailabe) {
		jokerAvailability.put(type, isAvailabe);
	}
}
