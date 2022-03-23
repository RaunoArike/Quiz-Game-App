package server.service;

import commons.clientmessage.QuestionAnswerMessage;
import commons.servermessage.QuestionMessage;
import commons.servermessage.ScoreMessage;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import server.api.OutgoingController;
import server.model.Game;
import server.model.Player;

import java.util.*;

@Service
@EnableScheduling
public class GameServiceImpl implements GameService {
	private final QuestionService questionService;
	private final OutgoingController outgoingController;
	private final TimerService timerService;

	private final Map<Integer, Game> games = new HashMap<>(); // Maps gameId to Game
	private final Map<Integer, Integer> players = new HashMap<>(); // Maps playerId to gameId

	private static final int QUESTION_DELAY = 0;

	private int nextGameId = 0;

	public GameServiceImpl(QuestionService questionService, OutgoingController outgoingController,
			TimerService timerService) {
		this.questionService = questionService;
		this.outgoingController = outgoingController;
		this.timerService = timerService;
	}

	@Override
	public void startSinglePlayerGame(int playerId, String userName) {
		var player = new Player(userName);

		var gameId = nextGameId++;
		var game = new Game(gameId);
		game.addPlayer(playerId, player);

		players.put(playerId, gameId);
		games.put(gameId, game);

		startNewQuestion(game);
	}

	@Override
	public void submitAnswer(int playerId, QuestionAnswerMessage answer) {
		var game = getPlayerGame(playerId);
		if (game == null) throw new RuntimeException("Game not found");

		var player = game.getPlayer(playerId);
		if (player == null) throw new RuntimeException("Player not found");

		long timePassed = timerService.getTime();
		var currentQuestion = game.getCurrentQuestion();

		var scoreDelta = questionService.calculateScore(currentQuestion, answer.getAnswer(),
				game.getStartTime() - timePassed);
		player.incrementScore(scoreDelta);

		outgoingController.sendScore(new ScoreMessage(scoreDelta, player.getScore()), List.of(playerId));

		if (!game.isLastQuestion()) startNewQuestion(game);
		else cleanUpGame(game);
	}

	private void startNewQuestion(Game game) {
		var gameId = game.getGameId();
		var question = questionService.generateQuestion(gameId);
		new Timer().schedule(
				new TimerTask() {
					@Override
					public void run() {
						game.startNewQuestion(question);
						outgoingController.sendQuestion(new QuestionMessage(question, game.getQuestionNumber()),
								game.getPlayerIds());
						game.startTimer(timerService.getTime());
					}
				}, QUESTION_DELAY
		);
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
