package server.service;

import commons.model.Question;
import server.entity.ActivityEntity;
import server.repository.ActivityRepository;
import server.util.MathUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class QuestionServiceImpl implements QuestionService {
	public static final int MAX_SCORE = 100;

	private static final float EST_SCORE_RATIO_GOOD = 0.1f;
	private static final float EST_SCORE_RATIO_BAD = 0.5f;

	private static final int NUM = 3;

	private final List<ActivityEntity> visited = new ArrayList<>();
	private final ActivityRepository activityRepository;

	public QuestionServiceImpl(ActivityRepository activityRepository) {
		this.activityRepository = activityRepository;
	}

	public Question generateQuestion(int gameId) {

		int no = Math.abs(new Random().nextInt());

		switch (no % NUM) {
			case 0:	return generateMC();
			case 1: return generateEst();
			case 2: return generateCal();
		}
		return null;
	}

	public List<ActivityEntity> generateActivities() {
		List<ActivityEntity> listActivities = activityRepository.findAll();
		List<ActivityEntity> selectedEntities = new ArrayList<>();

		int index = 0;
		///MAGIC NUMBERS HAVE TO BE REMOVED

		while (index < NUM) {
			int no = Math.abs(new Random().nextInt());
			ActivityEntity act = listActivities.get(no % listActivities.size());
			if (!visited.contains(act)) {
				listActivities.add(act);
				visited.add(act);
				selectedEntities.add(act);
				index++;
			}
		}
		return selectedEntities;
	}

	public Question.MultiChoice generateMC() {
		List<ActivityEntity> selectedEntities = generateActivities();
		return new Question.MultiChoice(
				selectedEntities.stream().map(ActivityEntity::toModel).collect(Collectors.toList()),
				generateAnswer(selectedEntities)
		);
	}

	public Question.EstimationQuestion generateEst() {
		List<ActivityEntity> selectedEntities = generateActivities();
		return new Question.EstimationQuestion(
				selectedEntities.get(1).toModel(),
				selectedEntities.get(1).getEnergyInWh()
		);
	}

	public Question.CalculationQuestion generateCal() {
		///TODO
		return null;
	}

	public int generateAnswer(List<ActivityEntity> listActivities) {
		int maxIndex = 0;
		float maxValue = 0f;
		for (int i = 0; i < listActivities.size(); i++) {
			float currentEnergy = listActivities.get(i).getEnergyInWh();
			if (currentEnergy > maxValue) {
				maxIndex = i;
				maxValue = currentEnergy;
			}
		}
		return maxIndex;
	}

	@Override
	public int calculateScore(Question question, Number answer) {
		if (question instanceof Question.MultiChoice mc) {
			return calculateScoreMC(mc, answer.intValue());
		} else if (question instanceof Question.EstimationQuestion est) {
			return calculateScoreEst(est, answer.floatValue());
		} else if (question instanceof Question.CalculationQuestion cal) {
			return calculateScoreCal(cal, answer.floatValue());
		}
		return 0;
	}

	private int calculateScoreMC(Question.MultiChoice question, int answer) {
		if (question.getCorrectAnswer() == answer) return MAX_SCORE;
		else return 0;
	}

	// TODO Consider improving the formula
	private int calculateScoreEst(Question.EstimationQuestion question, float answer) {
		float error = Math.abs(answer - question.getCorrectAnswer());
		float errorRatio = error / question.getCorrectAnswer();
		if (errorRatio < EST_SCORE_RATIO_GOOD) {
			return MAX_SCORE;
		} else if (errorRatio > EST_SCORE_RATIO_BAD) {
			return 0;
		} else {
			return Math.round(MathUtil.linearMap(errorRatio, EST_SCORE_RATIO_GOOD, EST_SCORE_RATIO_BAD, MAX_SCORE, 0));
		}
	}

	private int calculateScoreCal(Question.CalculationQuestion question, float answer) {
		return 0; // TODO
	}
}
