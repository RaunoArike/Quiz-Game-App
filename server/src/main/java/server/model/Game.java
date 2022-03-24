package server.model;

import commons.model.Question;
import org.springframework.lang.Nullable;

import java.util.*;

public class Game {
	public static final int QUESTIONS_PER_GAME = 20;
	public static final int LEADERBOARD_DISPLAY_FREQUENCY = 5;
	public static final int TIMER_DELAY = 100;

	private final int gameId;

	private final Map<Integer, Player> players = new HashMap<>(); // Maps playerId to Player
	private int questionNumber = -1;
	private long questionStartTime = 0;
	private Question currentQuestion;

	public Game(int gameId) {
		this.gameId = gameId;
	}

	public Game(List<Player> listOfPlayers, int gameId) {
		this(gameId);
		for (Player player : listOfPlayers) {
			players.put(player.getPlayerId(), player);
		}
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

	public List<Player> getPlayers() {
		return new ArrayList<>(players.values());
	}

	public boolean isLastQuestion() {
		return questionNumber == QUESTIONS_PER_GAME - 1;
	}

	public boolean isIntermediateLeaderboardNext() {
		if (questionNumber == 0) return false;
		if (isLastQuestion()) return false;
		return ((questionNumber + 1) % LEADERBOARD_DISPLAY_FREQUENCY) == 0;
	}

	public boolean isFirstQuestion() {
		return questionNumber == 0;
	}

	public int getQuestionNumber() {
		return questionNumber;
	}

	public Question getCurrentQuestion() {
		return currentQuestion;
	}

	public void startTimer(long currentTime) {
		this.questionStartTime = currentTime + TIMER_DELAY;
	}

	public long getStartTime() {
		return this.questionStartTime;
	}
}
