package server.model;

import commons.clientmessage.QuestionAnswerMessage;
import commons.model.Question;
import org.springframework.lang.Nullable;

import java.util.*;

public class Game {
	public static final int QUESTIONS_PER_GAME = 20;
	public static final int LEADERBOARD_DISPLAY_FREQUENCY = 5;
	public static final int TIMER_DELAY = 100;
	public static final int QUESTION_DURATION = 20000;

	private final int gameId;

	private final Map<Integer, Player> players = new HashMap<>(); // Maps playerId to Player

	//Used in a multiplayer game to keep track of answer submissions and answering times of each player,
	//mapped to by playerId.
	public Map<Integer, QuestionAnswerMessage> answers = new HashMap<>();
	public Map<Integer, Long> times = new HashMap<>();

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

	public boolean isSinglePlayer() {
		return (getPlayerIds().size() <= 1);
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
