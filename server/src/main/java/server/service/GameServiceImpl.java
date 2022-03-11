package server.service;

import commons.clientmessage.QuestionAnswerMessage;
import commons.servermessage.CorrectAnswerMessage;
import commons.servermessage.QuestionMessage;
import org.springframework.stereotype.Service;
import server.api.OutgoingController;
import server.model.Game;
import server.model.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameServiceImpl implements GameService {
	private final QuestionService questionService;
	private final OutgoingController outgoingController;

	private final Map<Integer, Game> games = new HashMap<>(); // Maps gameId to Game
	private final Map<Integer, Integer> players = new HashMap<>(); // Maps playerId to gameId

	private int nextGameId = 0;
	private int nextPlayerId = 0;

	public GameServiceImpl(QuestionService questionService, OutgoingController outgoingController) {
		this.questionService = questionService;
		this.outgoingController = outgoingController;
	}

	@Override
	public int startSinglePlayerGame(String userName) {
		var playerId = nextPlayerId++;
		var player = new Player(userName);

		var gameId = nextGameId++;
		var game = new Game(gameId);
		game.addPlayer(playerId, player);

		players.put(playerId, gameId);
		games.put(gameId, game);

		startNewQuestion(game);

		return playerId;
	}

	@Override
	public void submitAnswer(int playerId, QuestionAnswerMessage answer) {
		var game = getPlayerGame(playerId);
		if (game == null) throw new RuntimeException("Game not found");

		var player = game.getPlayer(playerId);
		if (player == null) throw new RuntimeException("Player not found");

		var currentQuestion = game.getCurrentQuestion();

		var scoreDelta = questionService.calculateScore(currentQuestion, answer.getAnswer());
		player.incrementScore(scoreDelta);

		outgoingController.sendScore(new CorrectAnswerMessage(scoreDelta, player.getScore()), List.of(playerId));

		startNewQuestion(game);
	}

	private void startNewQuestion(Game game) {
		var question = questionService.generateQuestion(game.getGameId());
		game.startNewQuestion(question);
		outgoingController.sendQuestion(new QuestionMessage(question, game.getQuestionNumber()), game.getPlayerIds());
	}

	private Game getPlayerGame(int playerId) {
		var gameId = players.get(playerId);
		if (gameId == null) return null;
		return games.get(gameId);
	}
}
