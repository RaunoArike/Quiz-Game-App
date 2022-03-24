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
	@Mock
	private LeaderboardService leaderboardService;
	@Mock
	private TimerService timerService;

	@Captor
	private ArgumentCaptor<QuestionMessage> questionMessageCaptor;
	@Captor
	private ArgumentCaptor<ScoreMessage> correctAnswerMessageCaptor;
	@Captor
	private ArgumentCaptor<Runnable> runnableCaptor;

	private GameServiceImpl createService(TimerService timerService) {
		return new GameServiceImpl(questionService, outgoingController, playerService,
			leaderboardService, timerService);
	}

	@Test
	public void starting_single_player_game_should_send_question() {
		when(questionService.generateQuestion(anyInt())).thenReturn(FAKE_QUESTION);

		var service = createService(new MockTimerService());
		service.startSinglePlayerGame(30, "abc");

		verify(outgoingController).sendQuestion(
				new QuestionMessage(FAKE_QUESTION, 0),
				List.of(30)
		);
	}

	@Test
	public void answering_question_should_send_another_question() {
		when(questionService.generateQuestion(anyInt())).thenReturn(FAKE_QUESTION);

		var service = createService(new MockTimerService());
		service.startSinglePlayerGame(30, "abc");
		service.submitAnswer(30, new QuestionAnswerMessage(null, 5f));

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
		when(questionService.calculateScore(any(), eq(5f), anyLong())).thenReturn(77);

		var service = createService(timerService);
		service.startSinglePlayerGame(30, "abc");
		service.submitAnswer(30, new QuestionAnswerMessage(null, 5f));

		verify(outgoingController).sendScore(
				new ScoreMessage(77, 77),
				List.of(30)
		);
	}

	@Test
	public void answering_second_question_should_send_increased_total_score() {
		when(questionService.calculateScore(any(), eq(5f), anyLong())).thenReturn(77);
		when(questionService.calculateScore(any(), eq(11f), anyLong())).thenReturn(23);

		var service = createService(timerService);
		service.startSinglePlayerGame(30, "abc");
		service.submitAnswer(30, new QuestionAnswerMessage(null, 5f));
		service.submitAnswer(30, new QuestionAnswerMessage(null, 11f));

		verify(outgoingController, times(2)).sendScore(
				correctAnswerMessageCaptor.capture(),
				eq(List.of(30))
		);

		assertEquals(new ScoreMessage(23, 100), correctAnswerMessageCaptor.getAllValues().get(1));
	}

	@Test
	public void after_answering_last_question_game_should_not_exist() {
		var service = createService(new MockTimerService());
		service.startSinglePlayerGame(30, "abc");
		for (int i = 0; i < Game.QUESTIONS_PER_GAME; i++) {
			service.submitAnswer(30, new QuestionAnswerMessage(null, null));
		}
		verify(leaderboardService).addToLeaderboard(new LeaderboardEntry("abc", 0));
		assertThrows(Exception.class, () -> {
			service.submitAnswer(30, new QuestionAnswerMessage(null, null));
		});
	}

	@Test
	public void show_intermediate_leaderboard_should_send_leaderboard() {
		when(game.getPlayers()).thenReturn(FAKE_PLAYER_LIST);
		when(game.getPlayerIds()).thenReturn(List.of(1, 2, 3));

		var service = createService(new MockTimerService());
		service.showIntermediateLeaderboard(game);

		IntermediateLeaderboardMessage message = new IntermediateLeaderboardMessage(FAKE_LEADERBOARD);
		verify(outgoingController).sendIntermediateLeaderboard(message, FAKE_PLAYER_ID_LIST);
	}

	public void starting_game_should_start_the_timer() {
		var service = createService(timerService);
		service.startSinglePlayerGame(30, "abc");

		verify(timerService).scheduleTimer(anyInt(), eq(3000L), runnableCaptor.capture());

		runnableCaptor.getValue().run();

		verify(timerService).getTime();
	}
}
