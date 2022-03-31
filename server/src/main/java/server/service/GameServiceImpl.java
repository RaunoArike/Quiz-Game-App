package server.service;

import commons.clientmessage.QuestionAnswerMessage;
import commons.clientmessage.SendJokerMessage;
import commons.model.JokerType;
import commons.model.LeaderboardEntry;
import commons.model.Question;
import commons.servermessage.*;
import org.springframework.stereotype.Service;
import server.api.OutgoingController;
import server.model.Game;
import server.model.Player;

import java.util.*;

@Service
public class GameServiceImpl implements GameService {
	private final QuestionService questionService;
	private final OutgoingController outgoingController;
	private final LeaderboardService leaderboardService;
	private final TimerService timerService;

	private final Map<Integer, Game> games = new HashMap<>(); // Maps gameId to Game
	private final Map<Integer, Integer> players = new HashMap<>(); // Maps playerId to gameId

	private int nextGameId = 0;

	private boolean doublePoints = false;

	private final double TIME_JOKER_EFFECT = 0.75;

	private static final int NUMBER_OF_ENTRIES_INTERMEDIATE_LEADERBOARD = 10;

	public GameServiceImpl(QuestionService questionService, OutgoingController outgoingController,
			LeaderboardService leaderboardService, TimerService timerService) {
		this.questionService = questionService;
		this.outgoingController = outgoingController;
		this.leaderboardService = leaderboardService;
		this.timerService = timerService;
	}

	/**
	 * Start a new single-player game.
	 *
	 * @param playerId
	 * @param userName
	 */
	@Override
	public void startSinglePlayerGame(int playerId, String userName) {
		var player = new Player(userName, playerId);
		player.init();
		var gameId = nextGameId++;
		var game = new Game(gameId);
		game.addPlayer(playerId, player);

		players.put(playerId, gameId);
		games.put(gameId, game);

		startNewQuestion(game, Game.QUESTION_DELAY);
	}

	/**
	 * Start a new multi-player game.
	 *
	 * @param listOfPlayers list of players participating in the new game that is being started
	 */
	@Override
	public void startMultiPlayerGame(List<Player> listOfPlayers) {
		var newGameId = nextGameId++;
		Game game = new Game(listOfPlayers, newGameId);
		games.put(newGameId, game);
		for (Player p : listOfPlayers) {
			players.put(p.getPlayerId(), newGameId);
			p.init();
		}
		continueMultiPlayerGame(game);
	}

	private void continueMultiPlayerGame(Game game) {
		//boolean extraDelayForLeaderboard = false;
		if (game.isIntermediateLeaderboardNext()) {
			showIntermediateLeaderboard(game);
			//extraDelayForLeaderboard = true;
		}
		if (!game.isLastQuestion()) {
			List<Player> playersinGame = game.getPlayers();
			for (Player p : playersinGame) {
				p.setLatestAnswer(null); //this case is specially handled when updating score
				p.setTimeTakenToAnswer((long) 0);
			}
			if (!game.isIntermediateLeaderboardNext()) {
				startNewQuestion(game, Game.QUESTION_DELAY);
			} else {
				startNewQuestion(game, Game.LEADERBOARD_DELAY);
			}
		} else {
			List<Integer> playersInGame = game.getPlayerIds();
			outgoingController.sendEndOfGame(new EndOfGameMessage(), playersInGame);
			showIntermediateLeaderboard(game);
			cleanUpGame(game);
		}
	}

	/**
	 * Generic submitAnswer method, calls either single- or multi-player method.
	 *
	 * @param playerId player who submits the answer
	 * @param answer message containing the answer
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
	 * Single-player submitAnswer method.
	 *
	 * @param playerId player who submits the answer
	 * @param answer message containing the answer
	 */
	private void submitAnswerSinglePlayer(int playerId, QuestionAnswerMessage answer) {

		var game = getPlayerGame(playerId);
		if (game == null) throw new RuntimeException("Game not found");

		var player = game.getPlayer(playerId);
		if (player == null) throw new RuntimeException("Player not found");

		if (game.isQuestionFinished()) return;
		game.markCurrentQuestionAsFinished();

		long timePassed = timerService.getTime() - game.getStartTime();
		var currentQuestion = game.getCurrentQuestion();

		var scoreDelta = questionService.calculateScore(currentQuestion, answer.getAnswer(),
				timePassed, doublePoints);
		doublePoints = false;
		player.incrementScore(scoreDelta);

		outgoingController.sendScore(new ScoreMessage(scoreDelta, player.getScore(), -1), List.of(playerId));

		if (!game.isLastQuestion()) {
			startNewQuestion(game, Game.QUESTION_DELAY);
		} else {
			outgoingController.sendEndOfGame(new EndOfGameMessage(), game.getPlayerIds());
			leaderboardService.addToLeaderboard(new LeaderboardEntry(player.getName(), player.getScore()));
			cleanUpGame(game);
		}
	}

	/**
	 * Multi-player submitAnswer method.
	 *
	 * @param playerId player who submits the answer
	 * @param answer message containing the answer
	 */
	private void submitAnswerMultiPlayer(int playerId, QuestionAnswerMessage answer) {
		var game = getPlayerGame(playerId);
		if (game == null) throw new RuntimeException("Game not found");

		var player = game.getPlayer(playerId);
		if (player == null) throw new RuntimeException("Player not found");

		player.setLatestAnswer(answer.getAnswer());
		long timePassed = timerService.getTime() - game.getStartTime();
		player.setTimeTakenToAnswer(timePassed);
	}

	/**
	 * Generic jokerPlayed method, calls either single- or multi-player method
	 * @param playerId player who uses the joker
	 * @param jokerMessage message containing which joker is played
	 */
	@Override
	public void jokerPlayed(int playerId, SendJokerMessage jokerMessage) {
		var game = getPlayerGame(playerId);
		if (game == null) throw new RuntimeException("Game not found");
		if (game.isSinglePlayer()) {
			jokerPlayedSinglePlayer(playerId, jokerMessage);
		} else {
			jokerPlayedMultiPlayer(playerId, jokerMessage);
		}
	}

	/**
	 * Single player jokerPlayed method
	 * @param playerId player who uses the joker
	 * @param jokerMessage message containing which joker is played
	 */
	private void jokerPlayedSinglePlayer(int playerId, SendJokerMessage jokerMessage) {
		var game = getPlayerGame(playerId);
		if (game == null) throw new RuntimeException("Game not found");

		var player = game.getPlayer(playerId);
		if (player == null) throw new RuntimeException("Player not found");

		if (jokerMessage.getJokerType() == JokerType.DOUBLE_POINTS) {
			player.setJokerAvailability(JokerType.DOUBLE_POINTS, false);
			doublePoints = true;
		}

		if (jokerMessage.getJokerType() == JokerType.ELIMINATE_MC_OPTION) {
			player.setJokerAvailability(JokerType.ELIMINATE_MC_OPTION, false);
		}

	}

	/**
	 * Multi-player jokerPlayed method
	 * @param playerId player who use the joker
	 * @param jokerMessage message containing which joker is used
	 */
	private void jokerPlayedMultiPlayer(int playerId, SendJokerMessage jokerMessage) {
		var game = getPlayerGame(playerId);
		if (game == null) throw new RuntimeException("Game not found");

		var player = game.getPlayer(playerId);
		if (player == null) throw new RuntimeException("Player not found");

		if (jokerMessage.getJokerType() == JokerType.DOUBLE_POINTS) {
			player.setJokerAvailability(JokerType.DOUBLE_POINTS, false);
			doublePoints = true;
		}

		if (jokerMessage.getJokerType() == JokerType.ELIMINATE_MC_OPTION) {
			player.setJokerAvailability(JokerType.ELIMINATE_MC_OPTION, false);
		}

		if (jokerMessage.getJokerType() == JokerType.REDUCE_TIME) {
			player.setJokerAvailability(JokerType.REDUCE_TIME, false);

			long currentTimeLefts = timerService.getRemainingTime(game.getGameId());
			long timeReduced = (long) (currentTimeLefts * TIME_JOKER_EFFECT);
			timerService.rescheduleTimer(game.getGameId(), timeReduced);

			outgoingController.sendTimeReduced(new ReduceTimePlayedMessage(timeReduced), game.getPlayerIds());
		}

	}

	/**
	 * Sends a new question after a short delay.
	 *
	 * @param game game for which new question is to be sent
	 * @param questionDelay delay in milliseconds
	 */
	private void startNewQuestion(Game game, Long questionDelay) {
		if (game.isBeforeFirstQuestion()) {
			newQuestion(game);
		} else {
			timerService.scheduleTimer(game.getGameId(), questionDelay, () -> newQuestion(game));
		}
	}

	/**
	 * Sends a new question immediately.
	 *
	 * @param game game for which new question is to be sent
	 */
	private void newQuestion(Game game) {
		var gameId = game.getGameId();
		var question = questionService.generateQuestion(gameId);
		game.startNewQuestion(question);

		for (Player player: game.getPlayers()) {
			if (question instanceof Question.MultiChoiceQuestion) {
				var questionMessage = new QuestionMessage(question, game.getQuestionNumber(),
						player.getJokerAvailability().get(JokerType.REDUCE_TIME),
						player.getJokerAvailability().get(JokerType.DOUBLE_POINTS),
						player.getJokerAvailability().get(JokerType.ELIMINATE_MC_OPTION));

				outgoingController.sendQuestion(questionMessage, List.of(player.getPlayerId()));
			} else {
				var questionMessage = new QuestionMessage(question, game.getQuestionNumber(),
						player.getJokerAvailability().get(JokerType.REDUCE_TIME),
						player.getJokerAvailability().get(JokerType.DOUBLE_POINTS),
						false);

				outgoingController.sendQuestion(questionMessage, List.of(player.getPlayerId()));
			}

		}
		game.setQuestionStartTime(timerService.getTime());

		timerService.scheduleTimer(game.getGameId(), Game.QUESTION_DURATION, () -> scoreUpdate(game));
	}

	/**
	 * Updates the scores of all the players in a multi-player game when the timer of a question elapses
	 * based on the latest answer that they submitted.
	 *
	 * @param game
	 */
	private void scoreUpdate(Game game) {
		int numberOfPlayersScored = 0;
		Map<Integer, Integer> playerScores = new HashMap<>(); //maps each player to their scoreDelta

		//First loop - to calculate everyone's scores and store them, while counting how many have scored
		for (Player player : game.getPlayers()) {
			//if latestAnswer was null it represents that the player has not given any answer for this question
			var scoreDelta = 0;
			if (player.getLatestAnswer() != null) {
				scoreDelta = questionService.calculateScore(game.getCurrentQuestion(),
					player.getLatestAnswer(), player.getTimeTakenToAnswer(), doublePoints);
				doublePoints = false;
				player.incrementScore(scoreDelta);
				if (scoreDelta > 0) {
					numberOfPlayersScored++;
				}
			}
			playerScores.put(player.getPlayerId(), scoreDelta);
		}
		//Second loop - send each player their score along with the total number of people who have scored
		for (Player player : game.getPlayers()) {
			var scoreDelta = playerScores.get(player.getPlayerId());
			ScoreMessage message = new ScoreMessage(scoreDelta, player.getScore(), numberOfPlayersScored);
			outgoingController.sendScore(message, List.of(player.getPlayerId()));
		}
		numberOfPlayersScored = 0;
		continueMultiPlayerGame(game);
	}

	/**
	 * Multiplayer leaderboard method.
	 */
	public void showIntermediateLeaderboard(Game game) {
		List<Player> players = game.getPlayers();
		List<LeaderboardEntry> listOfEntries = new ArrayList<>();
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
	 * Generic cleanup method.
	 *
	 * @param game
	 */
	private void cleanUpGame(Game game) {
		games.remove(game.getGameId());
		game.getPlayerIds().forEach(players::remove);
	}

	/**
	 * Generic get method.
	 *
	 * @param playerId
	 * @return which game a player is in
	 */
	private Game getPlayerGame(int playerId) {
		var gameId = players.get(playerId);
		if (gameId == null) return null;
		return games.get(gameId);
	}
}
