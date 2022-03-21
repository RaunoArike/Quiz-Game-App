package server.service;

import commons.clientmessage.QuestionAnswerMessage;
import commons.model.Activity;
import commons.model.Question;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceImplTest {
	private static final Question FAKE_QUESTION = new Question.EstimationQuestion(new Activity("a", "b", 42f), 4f);

	@Mock
	private QuestionService questionService;
	@Mock
	private OutgoingController outgoingController;

	@Captor
	private ArgumentCaptor<QuestionMessage> questionMessageCaptor;
	@Captor
	private ArgumentCaptor<ScoreMessage> correctAnswerMessageCaptor;

	private GameServiceImpl createService() {
		return new GameServiceImpl(questionService, outgoingController);
	}

	@Test
	public void starting_single_player_game_should_send_question() {
		when(questionService.generateQuestion(anyInt())).thenReturn(FAKE_QUESTION);

		var service = createService();
		var playerId = service.startSinglePlayerGame("abc");

		verify(outgoingController).sendQuestion(
				new QuestionMessage(FAKE_QUESTION, 0),
				List.of(playerId)
		);
	}

	@Test
	public void starting_two_single_player_games_should_yield_different_player_ids() {
		var service = createService();
		var playerId1 = service.startSinglePlayerGame("abc");
		var playerId2 = service.startSinglePlayerGame("def");

		assertNotEquals(playerId1, playerId2);
	}

	@Test
	public void answering_question_should_send_another_question() {
		when(questionService.generateQuestion(anyInt())).thenReturn(FAKE_QUESTION);

		var service = createService();
		var playerId = service.startSinglePlayerGame("abc");
		service.submitAnswer(playerId, new QuestionAnswerMessage(null, 5f, 420));

		verify(outgoingController, times(2)).sendQuestion(
				questionMessageCaptor.capture(),
				eq(List.of(playerId))
		);
		assertEquals(new QuestionMessage(FAKE_QUESTION, 0), questionMessageCaptor.getAllValues().get(0));
		assertEquals(new QuestionMessage(FAKE_QUESTION, 1), questionMessageCaptor.getAllValues().get(1));

		verify(questionService, times(2)).generateQuestion(anyInt());
	}

	@Test
	public void answering_question_should_send_score() {
		when(questionService.calculateScore(any(), eq(5f))).thenReturn(77);

		var service = createService();
		var playerId = service.startSinglePlayerGame("abc");
		service.submitAnswer(playerId, new QuestionAnswerMessage(null, 5f, 420));

		verify(outgoingController).sendScore(
				new ScoreMessage(77, 77),
				List.of(playerId)
		);
	}

	@Test
	public void answering_second_question_should_send_increased_total_score() {
		when(questionService.calculateScore(any(), eq(5f))).thenReturn(77);
		when(questionService.calculateScore(any(), eq(11f))).thenReturn(23);

		var service = createService();
		var playerId = service.startSinglePlayerGame("abc");
		service.submitAnswer(playerId, new QuestionAnswerMessage(null, 5f, 420));
		service.submitAnswer(playerId, new QuestionAnswerMessage(null, 11f, 840));

		verify(outgoingController, times(2)).sendScore(
				correctAnswerMessageCaptor.capture(),
				eq(List.of(playerId))
		);

		assertEquals(new ScoreMessage(23, 100), correctAnswerMessageCaptor.getAllValues().get(1));
	}

	@Test
	public void after_answering_last_question_game_should_not_exist() {
		var service = createService();
		var playerId = service.startSinglePlayerGame("abc");
		for (int i = 0; i < Game.QUESTIONS_PER_GAME; i++) {
			service.submitAnswer(playerId, new QuestionAnswerMessage(null, null, 0));
		}

		assertThrows(Exception.class, () -> {
			service.submitAnswer(playerId, new QuestionAnswerMessage(null, null, 0));
		});
	}
}
