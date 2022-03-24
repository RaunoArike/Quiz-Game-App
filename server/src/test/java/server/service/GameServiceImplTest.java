package server.service;

import commons.clientmessage.QuestionAnswerMessage;
import commons.model.Activity;
import commons.model.LeaderboardEntry;
import commons.model.Question;
import commons.servermessage.IntermediateLeaderboardMessage;
import commons.servermessage.QuestionMessage;
import commons.servermessage.ScoreMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.api.OutgoingController;
import server.model.Game;
import server.model.Player;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceImplTest {
	private static final Question FAKE_QUESTION = new Question.EstimationQuestion(new Activity("a", "b", 42f), 4f);

	private static final List<Player> FAKE_PLAYER_LIST = List.of(
		new Player("name1", 1, 100),
		new Player("name2", 2, 200),
		new Player("name3", 3, 300)
	);

	private static final List<LeaderboardEntry> FAKE_LEADERBOARD = List.of(
		new LeaderboardEntry("name3", 300),
		new LeaderboardEntry("name2", 200),
		new LeaderboardEntry("name1", 100)
	);

	private static final List<Integer> FAKE_PLAYER_ID_LIST = List.of(1, 2, 3);

	@Mock
	private QuestionService questionService;
	@Mock
	private OutgoingController outgoingController;
	private PlayerService playerService = new PlayerServiceImpl();
	@Mock
	private Game game;
	@Captor
	private ArgumentCaptor<QuestionMessage> questionMessageCaptor;
	@Captor
	private ArgumentCaptor<ScoreMessage> correctAnswerMessageCaptor;
	@Captor
	private ArgumentCaptor<IntermediateLeaderboardMessage> leaderboardMessageCaptor;

	private GameServiceImpl createService() {
		return new GameServiceImpl(questionService, outgoingController, playerService);
	}

	@Test
	public void starting_single_player_game_should_send_question() {
		when(questionService.generateQuestion(anyInt())).thenReturn(FAKE_QUESTION);

		var service = createService();
		service.startSinglePlayerGame(30, "abc");

		verify(outgoingController).sendQuestion(
				new QuestionMessage(FAKE_QUESTION, 0),
				List.of(30)
		);
	}

	@Test
	public void answering_question_should_send_another_question() {
		when(questionService.generateQuestion(anyInt())).thenReturn(FAKE_QUESTION);

		var service = createService();
		service.startSinglePlayerGame(30, "abc");
		service.submitAnswer(30, new QuestionAnswerMessage(null, 5f, 420));

		verify(outgoingController, times(2)).sendQuestion(
				questionMessageCaptor.capture(),
				eq(List.of(30))
		);
		assertEquals(new QuestionMessage(FAKE_QUESTION, 0), questionMessageCaptor.getAllValues().get(0));
		assertEquals(new QuestionMessage(FAKE_QUESTION, 1), questionMessageCaptor.getAllValues().get(1));

		verify(questionService, times(2)).generateQuestion(anyInt());
	}

	@Test
	public void answering_question_should_send_score() {
		when(questionService.calculateScore(any(), eq(5f))).thenReturn(77);

		var service = createService();
		service.startSinglePlayerGame(30, "abc");
		service.submitAnswer(30, new QuestionAnswerMessage(null, 5f, 420));

		verify(outgoingController).sendScore(
				new ScoreMessage(77, 77),
				List.of(30)
		);
	}

	@Test
	public void answering_second_question_should_send_increased_total_score() {
		when(questionService.calculateScore(any(), eq(5f))).thenReturn(77);
		when(questionService.calculateScore(any(), eq(11f))).thenReturn(23);

		var service = createService();
		service.startSinglePlayerGame(30, "abc");
		service.submitAnswer(30, new QuestionAnswerMessage(null, 5f, 420));
		service.submitAnswer(30, new QuestionAnswerMessage(null, 11f, 840));

		verify(outgoingController, times(2)).sendScore(
				correctAnswerMessageCaptor.capture(),
				eq(List.of(30))
		);

		assertEquals(new ScoreMessage(23, 100), correctAnswerMessageCaptor.getAllValues().get(1));
	}

	@Test
	public void after_answering_last_question_game_should_not_exist() {
		var service = createService();
		service.startSinglePlayerGame(30, "abc");
		for (int i = 0; i < Game.QUESTIONS_PER_GAME; i++) {
			service.submitAnswer(30, new QuestionAnswerMessage(null, null, 0));
		}

		assertThrows(Exception.class, () -> {
			service.submitAnswer(30, new QuestionAnswerMessage(null, null, 0));
		});
	}

	@Test
	public void show_intermediate_leaderboard_should_send_leaderboard() {
		when(game.getPlayers()).thenReturn(FAKE_PLAYER_LIST);
		when(game.getPlayerIds()).thenReturn(List.of(1, 2, 3));

		var service = createService();
		service.showIntermediateLeaderboard(game);

		IntermediateLeaderboardMessage message = new IntermediateLeaderboardMessage(FAKE_LEADERBOARD);
		verify(outgoingController).sendIntermediateLeaderboard(message, FAKE_PLAYER_ID_LIST);

	}
}
