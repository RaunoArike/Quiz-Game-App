package server.service;

import commons.clientmessage.QuestionAnswerMessage;
import commons.model.Question;
import commons.servermessage.QuestionMessage;
import commons.servermessage.CorrectAnswerMessage;
import org.springframework.stereotype.Service;
import server.model.Game;
import server.model.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameServiceImpl implements GameService {
	private final QuestionService questionService;

	private final Map<Integer, Game> games = new HashMap<>(); // Maps gameId to Game
	private final Map<Integer, Integer> players = new HashMap<>(); // Maps playerId to gameId

	private int nextGameId = 0;
	private int nextPlayerId = 0;

	public GameServiceImpl(QuestionService questionService) {
		this.questionService = questionService;
	}

	@Override
	public int startSinglePlayerGame(String userName) {
		int playerId = nextPlayerId++;
		Player player = new Player(userName);

		int gameId = nextGameId++;
		Game game = new Game(gameId);
		game.addPlayer(playerId, player);

		players.put(playerId, gameId);
		games.put(gameId, game);

		startNewQuestion(game);

		return playerId;
	}

	@Override
	public void submitAnswer(int playerId, QuestionAnswerMessage answer) {
		Game game = getPlayerGame(playerId);
		if (game == null) throw new RuntimeException("Game not found");
		Player player = game.getPlayer(playerId);
		if (player == null) throw new RuntimeException("Player not found");

		Question currentQuestion = game.getCurrentQuestion();

		int scoreDelta = questionService.calculateScore(currentQuestion, answer.getAnswer());
		player.incrementScore(scoreDelta);

		sendMessage(new CorrectAnswerMessage(scoreDelta, player.getScore()), List.of(playerId));

		startNewQuestion(game);
	}

	private void startNewQuestion(Game game) {
		Question question = questionService.generateQuestion(game.getGameId());
		game.startNewQuestion(question);
		sendMessage(new QuestionMessage(question, game.getQuestionNumber()), game.getPlayerIds());
	}

	private Game getPlayerGame(int playerId) {
		Integer gameId = players.get(playerId);
		if (gameId == null) return null;
		return games.get(gameId);
	}

	private void sendMessage(Object message, List<Integer> playerIds) {
		// Stub; TODO Replace with actual controller implementation
	}
}
