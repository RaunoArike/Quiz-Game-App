package server.service;

import commons.clientmessage.QuestionAnswerMessage;
import commons.model.LeaderboardEntry;
import commons.servermessage.IntermediateLeaderboardMessage;
import commons.servermessage.QuestionMessage;
import commons.servermessage.ScoreMessage;
import org.springframework.stereotype.Service;
import server.api.OutgoingController;
import server.model.Game;
import server.model.Player;

import java.util.*;

@Service
public class GameServiceImpl implements GameService {
	private final QuestionService questionService;
	private final OutgoingController outgoingController;
	private final PlayerService playerService;
	private final LeaderboardService leaderboardService;
	private final TimerService timerService;

	private final Map<Integer, Game> games = new HashMap<>(); // Maps gameId to Game
	private final Map<Integer, Integer> players = new HashMap<>(); // Maps playerId to gameId

	private static final long QUESTION_DELAY = 3000;

	private int nextGameId = 0;

	private static final int NUMBER_OF_ENTRIES_INTERMEDIATE_LEADERBOARD = 10;

	public GameServiceImpl(QuestionService questionService, OutgoingController outgoingController,
			PlayerService playerService, LeaderboardService leaderboardService, TimerService timerService) {
		this.questionService = questionService;
		this.outgoingController = outgoingController;
		this.playerService = playerService;
		this.leaderboardService = leaderboardService;
		this.timerService = timerService;
	}

	/**
	 * start single-player game
	 */
	@Override
	public void startSinglePlayerGame(int playerId, String userName) {
		var player = new Player(userName, playerId);

		var gameId = nextGameId++;
		var game = new Game(gameId);
		game.addPlayer(playerId, player);

		players.put(playerId, gameId);
		games.put(gameId, game);

		startNewQuestion(game);
	}

	/**
	 * start multi-player game
	 */
	@Override
	public void startMultiPlayerGame(List<Player> listOfPlayers) {
		var newGameId = nextGameId++;
		Game newGame = new Game(listOfPlayers, newGameId);
		games.put(newGameId, newGame);
		startNewQuestion(newGame);
	}

	/**
	 * Generic submitAnswer method, calls either single- or multi-player method
	 */
	@Override
	public void submitAnswer(int playerId, QuestionAnswerMessage answer) {
		var game = getPlayerGame(playerId);
		if (game == null) throw new RuntimeException("Game not found");
		if (game.isSinglePlayer()) {
			submitAnswerSinglePlayer(playerId, answer);
		} else {
			submitAnswerMultiPlayer(playerId, answer);
		}
	}

	/**
	 * Single-player submitAnswer method
	 */
	public void submitAnswerSinglePlayer(int playerId, QuestionAnswerMessage answer) {

		var game = getPlayerGame(playerId);
		if (game == null) throw new RuntimeException("Game not found");

		var player = game.getPlayer(playerId);
		if (player == null) throw new RuntimeException("Player not found");

		long timePassed = timerService.getTime() - game.getStartTime();
		var currentQuestion = game.getCurrentQuestion();

		var scoreDelta = questionService.calculateScore(currentQuestion, answer.getAnswer(),
				timePassed);
		player.incrementScore(scoreDelta);

		outgoingController.sendScore(new ScoreMessage(scoreDelta, player.getScore()), List.of(playerId));

		if (!game.isLastQuestion()) {
			startNewQuestion(game);
		} else {
			outgoingController.sendEndOfGame(game.getPlayerIds());
			leaderboardService.addToLeaderboard(new LeaderboardEntry(player.getName(), player.getScore()));
			cleanUpGame(game);
		}
	}

	/**
	 * Multi-player submitAnswer method
	 */
	public void submitAnswerMultiPlayer(int playerId, QuestionAnswerMessage answer) {
		//TO DO
	}


	/**
	 * Generic method for both single and multi player games
	 * @param game game for which new question is to be generated
	 */
	private void startNewQuestion(Game game) {
		if (game.isFirstQuestion()) {
			newQuestion(game);
		} else {
			timerService.scheduleTimer(game.getGameId(), QUESTION_DELAY, () -> newQuestion(game));
		}
	}

	/**
	 * Helper method of startNewQuestion
	 * @param game
	 */
	private void newQuestion(Game game) {
		var gameId = game.getGameId();
		var question = questionService.generateQuestion(gameId);
		game.startNewQuestion(question);
		outgoingController.sendQuestion(new QuestionMessage(question, game.getQuestionNumber()),
				game.getPlayerIds());
		game.startTimer(timerService.getTime());
	}

	/**
	 * Multiplayer leaderboard method
	 */
	@Override
	public void showIntermediateLeaderboard(Game game) {
		List<Player> players = game.getPlayers();
		List<LeaderboardEntry> listOfEntries = new ArrayList<LeaderboardEntry>();
		for (Player p : players) {
			listOfEntries.add(new LeaderboardEntry(p.getName(), p.getScore()));
		}
		List<LeaderboardEntry> leaderboard = listOfEntries.stream()
			.sorted(Comparator.<LeaderboardEntry>comparingInt(entry -> entry.score()).reversed())
			.limit(NUMBER_OF_ENTRIES_INTERMEDIATE_LEADERBOARD)
			.toList();
		IntermediateLeaderboardMessage message = new IntermediateLeaderboardMessage(leaderboard);
		outgoingController.sendIntermediateLeaderboard(message, game.getPlayerIds());
	}

	/**
	 * Generic cleanup method
	 * @param game
	 */
	private void cleanUpGame(Game game) {
		games.remove(game.getGameId());
		game.getPlayerIds().forEach(players::remove);
	}

	/**
	 * Generic get method - returns which game a player is in
	 * @param playerId
	 * @return
	 */
	private Game getPlayerGame(int playerId) {
		var gameId = players.get(playerId);
		if (gameId == null) return null;
		return games.get(gameId);
	}
}
