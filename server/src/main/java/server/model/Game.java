package server.model;

import commons.model.Question;
import org.springframework.lang.Nullable;

import java.util.*;

public class Game {
	public static final int QUESTIONS_PER_GAME = 20;

	private final int gameId;

	private final Map<Integer, Player> players = new HashMap<>(); // Maps playerId to Player
	private int questionNumber = -1;
	private long questionStartTime = 0;
	private Question currentQuestion;

	public Game(int gameId) {
		this.gameId = gameId;
	}

	public void addPlayer(int playerId, Player player) {
		players.put(playerId, player);
	}

	public void startNewQuestion(Question question) {
		questionNumber++;
		currentQuestion = question;
	}

	public int getGameId() {
		return gameId;
	}

	@Nullable
	public Player getPlayer(int playerId) {
		return players.get(playerId);
	}

	public List<Integer> getPlayerIds() {
		return new ArrayList<>(players.keySet());
	}

	public boolean isLastQuestion() {
		return questionNumber == QUESTIONS_PER_GAME - 1;
	}

	public int getQuestionNumber() {
		return questionNumber;
	}

	public Question getCurrentQuestion() {
		return currentQuestion;
	}

	public void startTimer(long currentTime) {
		this.questionStartTime = currentTime + 100;
	}

	public long getStartTime() {
		return this.questionStartTime;
	}
}
