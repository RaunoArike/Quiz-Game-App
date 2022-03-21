package server.service;

import commons.clientmessage.QuestionAnswerMessage;
import commons.servermessage.QuestionMessage;
import commons.servermessage.ScoreMessage;
import org.springframework.stereotype.Service;
import server.api.OutgoingController;
import server.model.Game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameServiceImpl implements GameService {
	private final QuestionService questionService;
	private final OutgoingController outgoingController;
	private final PlayerService playerService;
	private final Map<Integer, Game> games = new HashMap<>(); // Maps gameId to Game
	private final Map<Integer, Integer> players = new HashMap<>(); // Maps playerId to gameId

	private int nextGameId = 0;
	public GameServiceImpl(QuestionService questionService, OutgoingController outgoingController,
		PlayerService playerService) {
		this.questionService = questionService;
		this.outgoingController = outgoingController;
		this.playerService = playerService;
	}

	@Override
	public int startSinglePlayerGame(String userName) {
		var gameId = nextGameId++;
		var game = new Game(gameId);
		var player = playerService.generatePlayer(userName);
		game.addPlayer(player.getPlayerId(), player);

		players.put(player.getPlayerId(), gameId);
		games.put(gameId, game);

		startNewQuestion(game);

		return player.getPlayerId();
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

		outgoingController.sendScore(new ScoreMessage(scoreDelta, player.getScore()), List.of(playerId));

		if (!game.isLastQuestion()) startNewQuestion(game);
		else cleanUpGame(game);
	}
	@Override
	public void startNewQuestion(Game game) {
		var question = questionService.generateQuestion(game.getGameId());
		game.startNewQuestion(question);
		outgoingController.sendQuestion(new QuestionMessage(question, game.getQuestionNumber()), game.getPlayerIds());
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
