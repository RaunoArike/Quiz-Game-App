package server.service;

import commons.model.Activity;
import commons.model.Question;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import server.repository.ActivityRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceImplTest {
	private static final Activity FAKE_ACTIVITY = new Activity("A1", null);
	private static final List<Activity> FAKE_ACTIVITY_LIST = List.of(
			FAKE_ACTIVITY,
			new Activity("A2", null),
			new Activity("A3", null)
	);

	@Mock
	private ActivityRepository activityRepository;

	private QuestionServiceImpl createService() {
		return new QuestionServiceImpl(activityRepository);
	}

	@Test
	public void correct_answer_for_mc_question_should_give_max_score() {
		var service = createService();
		var question = new Question.MultiChoice(FAKE_ACTIVITY_LIST, 2);
		var score = service.calculateScore(question, 2);
		assertEquals(QuestionServiceImpl.MAX_SCORE, score);
	}

	@Test
	public void wrong_answer_for_mc_question_should_give_zero_score() {
		var service = createService();
		var question = new Question.MultiChoice(FAKE_ACTIVITY_LIST, 2);
		var score = service.calculateScore(question, 1);
		assertEquals(0, score);
	}

	@Test
	public void close_answer_for_est_question_should_give_max_score() {
		var service = createService();
		var question = new Question.EstimationQuestion(FAKE_ACTIVITY, 100f);
		var score = service.calculateScore(question, 95f);
		assertEquals(QuestionServiceImpl.MAX_SCORE, score);
	}

	@Test
	public void distant_answer_for_est_question_should_give_zero_score() {
		var service = createService();
		var question = new Question.EstimationQuestion(FAKE_ACTIVITY, 100f);
		var score = service.calculateScore(question, 40f);
		assertEquals(0, score);
	}

	@Test
	public void medium_answer_for_est_question_should_partial_score() {
		var service = createService();
		var question = new Question.EstimationQuestion(FAKE_ACTIVITY, 100f);
		var score = service.calculateScore(question, 70f);
		assertTrue(score > 0);
		assertTrue(score < QuestionServiceImpl.MAX_SCORE);
	}
}
