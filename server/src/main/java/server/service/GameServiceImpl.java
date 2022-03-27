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

	@Override
	public void generateNewQuestion(List<Player> listOfPlayers) {
		var newGameId = nextGameId++;
		Game newGame = new Game(listOfPlayers, newGameId);

		for (Player player : listOfPlayers) {
			players.put(player.getPlayerId(), newGameId);
		}
		games.put(newGameId, newGame);

		startNewQuestion(newGame);
	}

	@Override
	public void submitAnswer(int playerId, QuestionAnswerMessage answer) {
		var game = getPlayerGame(playerId);
		if (game == null) throw new RuntimeException("Game not found");

		var player = game.getPlayer(playerId);
		if (player == null) throw new RuntimeException("Player not found");

		if (game.isQuestionFinished()) return;
		game.markCurrentQuestionAsFinished();

		long timePassed = timerService.getTime() - game.getStartTime();
		var currentQuestion = game.getQuestion();

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


	private void startNewQuestion(Game game) {
		if (game.isBeforeFirstQuestion()) {
			newQuestion(game);
		} else {
			timerService.scheduleTimer(game.getGameId(), QUESTION_DELAY, () -> newQuestion(game));
		}
	}

	private void newQuestion(Game game) {
		var gameId = game.getGameId();
		var question = questionService.generateQuestion(gameId);
		game.startNewQuestion(question);
		outgoingController.sendQuestion(new QuestionMessage(question, game.getQuestionNumber()),
				game.getPlayerIds());
		game.startTimer(timerService.getTime());
	}

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

	private void cleanUpGame(Game game) {
		games.remove(game.getGameId());
		game.getPlayerIds().forEach(players::remove);
	}

	private Game getPlayerGame(int playerId) {
		var gameId = players.get(playerId);
		if (gameId == null) return null;
		return games.get(gameId);
	}
}
