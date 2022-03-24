package server.model;

import commons.model.Question;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
	public static final int QUESTIONS_PER_GAME = 20;
	public static final int LEADERBOARD_DISPLAY_FREQUENCY = 5;

	private final int gameId;

	private final Map<Integer, Player> players = new HashMap<>(); // Maps playerId to Player
	private int questionNumber = -1;
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
		return questionNumber % (LEADERBOARD_DISPLAY_FREQUENCY - 1) == 0;
	}

	public int getQuestionNumber() {
		return questionNumber;
	}

	public Question getCurrentQuestion() {
		return currentQuestion;
	}
}
