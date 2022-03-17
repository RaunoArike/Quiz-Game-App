package server.service;

import commons.model.Activity;
import commons.model.Question;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
	private static final List<Float> FAKE_ENERGIES = List.of(0.5f, 15f, 25f);


	@Mock
	private ActivityRepository activityRepository;

	private QuestionServiceImpl createService() {
		return new QuestionServiceImpl(activityRepository);
	}

	@Test
	public void correct_answer_for_mc_question_should_give_max_score() {
		var service = createService();
		var question = new Question.MultiChoiceQuestion(FAKE_ACTIVITY_LIST, 2);
		var score = service.calculateScore(question, 2);
		assertEquals(QuestionServiceImpl.MAX_SCORE, score);
	}

	@Test
	public void wrong_answer_for_mc_question_should_give_zero_score() {
		var service = createService();
		var question = new Question.MultiChoiceQuestion(FAKE_ACTIVITY_LIST, 2);
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

	@Test
	public void close_answer_for_comp_question_should_give_max_score() {
		var service = createService();
		var question = new Question.ComparisonQuestion(FAKE_ACTIVITY_LIST.subList(0, 2), 1f);
		var score = service.calculateScore(question, 1.1f);
		assertEquals(QuestionServiceImpl.MAX_SCORE, score);
	}

	@Test
	public void distant_answer_for_comp_question_should_give_zero_score() {
		var service = createService();
		var question = new Question.ComparisonQuestion(FAKE_ACTIVITY_LIST.subList(0, 2), 1f);
		var score = service.calculateScore(question, 2.1f);
		assertEquals(0, score);
	}

	@Test
	public void medium_answer_for_comp_question_should_partial_score() {
		var service = createService();
		var question = new Question.ComparisonQuestion(FAKE_ACTIVITY_LIST.subList(0, 2), 1f);
		var score = service.calculateScore(question, 1.5f);
		assertTrue(score > 0);
		assertTrue(score < QuestionServiceImpl.MAX_SCORE);
	}

	@Test
	public void correct_answer_for_pick_question_should_give_max_score() {
		var service = createService();
		var question = new Question.PickEnergyQuestion(FAKE_ACTIVITY, 2, FAKE_ENERGIES);
		var score = service.calculateScore(question, 2);
		assertEquals(QuestionServiceImpl.MAX_SCORE, score);
	}

	@Test
	public void wrong_answer_for_pick_question_should_give_zero_score() {
		var service = createService();
		var question = new Question.PickEnergyQuestion(FAKE_ACTIVITY, 2, FAKE_ENERGIES);
		var score = service.calculateScore(question, 1);
		assertEquals(0, score);
	}

	@Test
	public void pick_energy_answer_generator_should_generate_positive_answers() {
		var service = createService();
		var answerOptions = service.generatePickOptions(55, 2);
		assertTrue(answerOptions.get(0) > 0 && answerOptions.get(1) > 0 && answerOptions.get(2) > 0);
	}
}
