package server.model;

import commons.model.Question;
import org.springframework.lang.Nullable;
import server.service.TimerService;

import java.util.*;

public class Game {
	public static final int QUESTIONS_PER_GAME = 20;

	private final int gameId;
	private final TimerService timerService;

	private final Map<Integer, Player> players = new HashMap<>(); // Maps playerId to Player
	private int questionNumber = -1;
	private Question currentQuestion;

	public Game(int gameId, TimerService timerService) {
		this.gameId = gameId;
		this.timerService = timerService;
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

	public TimerService getTimerService() { return timerService; }
}
