package server.service;

import commons.clientmessage.QuestionAnswerMessage;
import commons.clientmessage.SendJokerMessage;
import commons.model.Activity;
import commons.model.JokerType;
import commons.model.LeaderboardEntry;
import commons.model.Question;
import commons.servermessage.IntermediateLeaderboardMessage;
import commons.servermessage.QuestionMessage;
import commons.servermessage.ReduceTimePlayedMessage;
import commons.servermessage.ScoreMessage;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.api.OutgoingController;
import server.api.mock.OutgoingControllerMock;
import server.model.Game;
import server.model.Player;
import server.service.mock.TimerServiceControllableMock;
import server.service.mock.TimerServiceImmediateMock;


import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceImplTest {
	private static final Question FAKE_QUESTION_EST = new Question.EstimationQuestion(
			new Activity("a", "b", 42f), 4f);

	private static final List<Activity> FAKE_ACTIVITIES_LIST = List.of(
			new Activity("a", "b", 42f),
			new Activity("d", "e", 42f),
			new Activity("g", "h", 42f)
	);

	private static final Question FAKE_QUESTION_MC = new Question.MultiChoiceQuestion(FAKE_ACTIVITIES_LIST, 0);

	private static final List<Player> FAKE_PLAYER_LIST = List.of(
			new Player("name1", 1, 0),
			new Player("name2", 2, 0),
			new Player("name3", 3, 0)
	);
	private static final List<Player> FAKE_PLAYER_LIST_2 = List.of(
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

	private final TimerServiceImmediateMock immediateTimer = new TimerServiceImmediateMock();
	private final TimerServiceControllableMock controllableTimer = new TimerServiceControllableMock();
	private final OutgoingControllerMock capturingOutgoingController = new OutgoingControllerMock();

	@Mock
	private QuestionService questionService;
	@Mock
	private OutgoingController mockitoOutgoingController;
	@Mock
	private Game game;
	@Mock
	private LeaderboardService leaderboardService;

	@Captor
	private ArgumentCaptor<QuestionMessage> questionMessageCaptor;
	@Captor
	private ArgumentCaptor<ScoreMessage> correctAnswerMessageCaptor;

	private GameServiceImpl createService(TimerService timerService, OutgoingController outgoingController) {
		return new GameServiceImpl(questionService, outgoingController,
				leaderboardService, timerService);
	}

	@Test
	public void starting_single_player_game_should_send_question_est() {
		when(questionService.generateQuestion(anyInt())).thenReturn(FAKE_QUESTION_EST);

		var service = createService(immediateTimer, mockitoOutgoingController);
		service.startSinglePlayerGame(30, "abc");

		verify(mockitoOutgoingController).sendQuestion(
				new QuestionMessage(FAKE_QUESTION_EST, 0, false, true, false),
				List.of(30)
		);
	}

	@Test
	public void starting_single_player_game_should_send_question_mc() {
		when(questionService.generateQuestion(anyInt())).thenReturn(FAKE_QUESTION_MC);

		var service = createService(immediateTimer, mockitoOutgoingController);
		service.startSinglePlayerGame(30, "abc");

		verify(mockitoOutgoingController).sendQuestion(
				new QuestionMessage(FAKE_QUESTION_MC, 0, false, true, true),
				List.of(30)
		);
	}

	@Test
	public void answering_question_should_send_another_question() {
		when(questionService.generateQuestion(anyInt())).thenReturn(FAKE_QUESTION_EST);

		var service = createService(controllableTimer, mockitoOutgoingController);
		service.startSinglePlayerGame(30, "abc");
		service.submitAnswer(30, new QuestionAnswerMessage(null, 5f));
		controllableTimer.advanceBy(3000);

		verify(mockitoOutgoingController, times(2)).sendQuestion(
				questionMessageCaptor.capture(),
				eq(List.of(30))
		);
		assertEquals(new QuestionMessage(FAKE_QUESTION_EST, 0, false, true, false),
				questionMessageCaptor.getAllValues().get(0));
		assertEquals(new QuestionMessage(FAKE_QUESTION_EST, 1, false, true, false),
				questionMessageCaptor.getAllValues().get(1));

		verify(questionService, times(2)).generateQuestion(anyInt());
	}

	@Test
	public void answering_question_should_send_score() {
		when(questionService.calculateScore(any(), eq(5f), anyLong(), eq(false))).thenReturn(77);

		var service = createService(controllableTimer, mockitoOutgoingController);
		service.startSinglePlayerGame(30, "abc");
		service.submitAnswer(30, new QuestionAnswerMessage(null, 5f));

		verify(mockitoOutgoingController).sendScore(
				new ScoreMessage(77, 77, -1),
				List.of(30)
		);
	}

	/**
	 * <strong>TO BE CONTINUED WHEN JOKER FUNCTIONALITY IS IMPLEMENTED </strong>
	 */

	/*@Test
	public void answering_second_question_should_send_increased_total_score() {
		when(questionService.calculateScore(any(), eq(5f), anyLong(), eq(false))).thenReturn(77);
		when(questionService.calculateScore(any(), eq(11f), anyLong(), eq(false))).thenReturn(23);

		var service = createService(controllableTimer, mockitoOutgoingController);
		service.startSinglePlayerGame(30, "abc");
		service.submitAnswer(30, new QuestionAnswerMessage(null, 5f));
		controllableTimer.advanceBy(3000);
		service.submitAnswer(30, new QuestionAnswerMessage(null, 11f));
		controllableTimer.advanceBy(3000);

		verify(mockitoOutgoingController, times(2)).sendScore(
				correctAnswerMessageCaptor.capture(),
				eq(List.of(30))
		);

		assertEquals(new ScoreMessage(23, 100, -1), correctAnswerMessageCaptor.getAllValues().get(1));
	}


	 */
	@Test
	public void after_answering_last_question_game_should_not_exist() {
		var service = createService(controllableTimer, mockitoOutgoingController);
		service.startSinglePlayerGame(30, "abc");
		for (int i = 0; i < Game.QUESTIONS_PER_GAME; i++) {
			service.submitAnswer(30, new QuestionAnswerMessage(null, null));
			controllableTimer.advanceBy(3000);
		}
		verify(leaderboardService).addToLeaderboard(new LeaderboardEntry("abc", 0));
		assertThrows(Exception.class, () -> {
			service.submitAnswer(30, new QuestionAnswerMessage(null, null));
		});
	}

	@Test
	public void show_intermediate_leaderboard_should_send_leaderboard() {
		when(game.getPlayers()).thenReturn(FAKE_PLAYER_LIST_2);
		when(game.getPlayerIds()).thenReturn(List.of(1, 2, 3));

		var service = createService(immediateTimer, mockitoOutgoingController);
		service.showIntermediateLeaderboard(game);

		IntermediateLeaderboardMessage message = new IntermediateLeaderboardMessage(FAKE_LEADERBOARD);
		verify(mockitoOutgoingController).sendIntermediateLeaderboard(message, FAKE_PLAYER_ID_LIST);
	}

	@Test
	public void starting_multi_player_game_should_send_question_est() {
		when(questionService.generateQuestion(anyInt())).thenReturn(FAKE_QUESTION_EST);

		var service = createService(controllableTimer, capturingOutgoingController);
		service.startMultiPlayerGame(FAKE_PLAYER_LIST);

		for (Player p : FAKE_PLAYER_LIST) {
			assertThat(capturingOutgoingController.getSentMessagesForPlayer(p.getPlayerId()), Matchers.contains(
					new QuestionMessage(FAKE_QUESTION_EST, 0, true, true, false))
			);

		}

	}

	@Test
	public void starting_multi_player_game_should_send_question_mc() {
		when(questionService.generateQuestion(anyInt())).thenReturn(FAKE_QUESTION_MC);

		var service = createService(controllableTimer, capturingOutgoingController);
		service.startMultiPlayerGame(FAKE_PLAYER_LIST);

		for (Player p : FAKE_PLAYER_LIST) {
			assertThat(capturingOutgoingController.getSentMessagesForPlayer(p.getPlayerId()), Matchers.contains(
					new QuestionMessage(FAKE_QUESTION_MC, 0, true, true, true))
			);
		}

	}

	@Test
	public void answering_multiplayer_question_should_save_answer() {
		var service = createService(controllableTimer, mockitoOutgoingController);
		service.startMultiPlayerGame(FAKE_PLAYER_LIST);

		Player p = FAKE_PLAYER_LIST.get(0);
		service.submitAnswer(p.getPlayerId(), new QuestionAnswerMessage(10, null));

		assertEquals(10, p.getLatestAnswer());
	}

	@Test
	public void starting_single_player_game_should_send_question_immediately_est() {
		when(questionService.generateQuestion(anyInt())).thenReturn(FAKE_QUESTION_EST);

		var service = createService(controllableTimer, mockitoOutgoingController);
		service.startSinglePlayerGame(30, "abc");

		// Timer not advanced

		verify(mockitoOutgoingController).sendQuestion(
				new QuestionMessage(FAKE_QUESTION_EST, 0, false, true, false),
				List.of(30)
		);
	}

	@Test
	public void starting_single_player_game_should_send_question_immediately_mc() {
		when(questionService.generateQuestion(anyInt())).thenReturn(FAKE_QUESTION_MC);

		var service = createService(controllableTimer, mockitoOutgoingController);
		service.startSinglePlayerGame(30, "abc");

		// Timer not advanced

		verify(mockitoOutgoingController).sendQuestion(
				new QuestionMessage(FAKE_QUESTION_MC, 0, false, true, true),
				List.of(30)
		);
	}

	@Test
	public void multiplayer_question_should_be_ended_after_20s() {
		var service = createService(controllableTimer, capturingOutgoingController);
		service.startMultiPlayerGame(FAKE_PLAYER_LIST);
		capturingOutgoingController.reset();

		controllableTimer.advanceBy(19999);
		assertTrue(capturingOutgoingController.getSentMessages().isEmpty());

		controllableTimer.advanceBy(1);
		FAKE_PLAYER_ID_LIST.forEach(player -> {
			assertThat(capturingOutgoingController.getSentMessagesForPlayer(player), Matchers.contains(
					new ScoreMessage(0, 0, 0)
			));
		});
	}

	@Test
	public void reduce_time_played_message_should_be_sent() {
		var service = createService(controllableTimer, mockitoOutgoingController);
		service.startMultiPlayerGame(FAKE_PLAYER_LIST);

		SendJokerMessage messages = new SendJokerMessage(JokerType.REDUCE_TIME);

		service.jokerPlayed(1, messages);

		ReduceTimePlayedMessage message = new ReduceTimePlayedMessage(15000);
		verify(mockitoOutgoingController).sendTimeReduced(message, FAKE_PLAYER_ID_LIST);
	}

}
