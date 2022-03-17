package server.service;

import commons.model.Question;
import org.springframework.stereotype.Service;
import server.entity.ActivityEntity;
import server.repository.ActivityRepository;
import server.util.MathUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {
	public static final int MAX_SCORE = 100;

	private static final float EST_SCORE_RATIO_GOOD = 0.1f;
	private static final float EST_SCORE_RATIO_BAD = 0.5f;

	private static final int NUM1 = 3;
	private static final int NUM2 = 4;

	private final List<ActivityEntity> visited = new ArrayList<>();
	private final ActivityRepository activityRepository;

	public QuestionServiceImpl(ActivityRepository activityRepository) {
		this.activityRepository = activityRepository;
	}

	public Question generateQuestion(int gameId) {

		int no = Math.abs(new Random().nextInt());

		switch (no % NUM2) {
			case 0:	return generateMC();
			case 1: return generateEst();
			case 2: return generateComp();
			case NUM1: return generatePick();
		}
		return null;
	}

	public List<ActivityEntity> generateActivities() {
		List<ActivityEntity> listActivities = activityRepository.findAll();
		List<ActivityEntity> selectedEntities = new ArrayList<>();

		int index = 0;
		///MAGIC NUMBERS HAVE TO BE REMOVED

		while (index < NUM1) {
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


	public Question.MultiChoiceQuestion generateMC() {
		List<ActivityEntity> selectedEntities = generateActivities();
		return new Question.MultiChoiceQuestion(
				selectedEntities.stream().map(ActivityEntity::toModel).collect(Collectors.toList()),
				generateMCAnswer(selectedEntities)
		);
	}

	public Question.EstimationQuestion generateEst() {
		List<ActivityEntity> selectedEntities = generateActivities();
		return new Question.EstimationQuestion(
				selectedEntities.get(1).toModel(),
				selectedEntities.get(1).getEnergyInWh()
		);
	}

	public Question.ComparisonQuestion generateComp() {
		List<ActivityEntity> selectedEntities = generateActivities().subList(0, 2);
		return new Question.ComparisonQuestion(
				selectedEntities.stream().map(ActivityEntity::toModel).collect(Collectors.toList()),
				generateCompAnswer(selectedEntities)
		);
	}

	public Question.PickEnergyQuestion generatePick() {
		List<ActivityEntity> selectedEntities = generateActivities();
		int correctAnswer = Math.abs(new Random().nextInt()) % NUM1;
		return new Question.PickEnergyQuestion(
				selectedEntities.get(1).toModel(),
				correctAnswer,
				generatePickOptions(selectedEntities.get(1).getEnergyInWh(), correctAnswer)
		);
	}


	public int generateMCAnswer(List<ActivityEntity> listActivities) {
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

	public float generateCompAnswer(List<ActivityEntity> listActivities) {
		ActivityEntity activity1 = listActivities.get(0);
		ActivityEntity activity2 = listActivities.get(1);
		return activity2.getEnergyInWh() / activity1.getEnergyInWh();
	}

	// consider improving the formula
	public List<Float> generatePickOptions(float correctAnswerInWh, int answerNumber) {
		int correctAnswerInt = (int) correctAnswerInWh;
		List<Float> answerList = new ArrayList<>();
		for (int i = 0; i < NUM1; i++) {
			float wrongAnswer = correctAnswerInWh + (new Random().nextInt() % correctAnswerInt);
			while (wrongAnswer == 0) {
				wrongAnswer = correctAnswerInWh + (new Random().nextInt() % correctAnswerInt);
			}
			answerList.add(wrongAnswer);
		}
		if (answerNumber == 0) {
			answerList.add(0, correctAnswerInWh);
		} else if (answerNumber == 1) {
			answerList.add(1, correctAnswerInWh);
		} else {
			answerList.add(2, correctAnswerInWh);
		}
		return answerList;
	}


	@Override
	public int calculateScore(Question question, Number answer) {
		if (question instanceof Question.MultiChoiceQuestion mc) {
			return calculateScoreMC(mc, answer.intValue());
		} else if (question instanceof Question.EstimationQuestion est) {
			return calculateScoreEst(est, answer.floatValue());
		} else if (question instanceof Question.ComparisonQuestion comp) {
			return calculateScoreComp(comp, answer.floatValue());
		} else if (question instanceof Question.PickEnergyQuestion pick) {
			return calculateScorePick(pick, answer.intValue());
		}
		return 0;
	}

	private int calculateScoreMC(Question.MultiChoiceQuestion question, int answer) {
		if (question.getCorrectAnswer() == answer) return MAX_SCORE;
		else return 0;
	}

	// TODO Consider improving the formula
	private int calculateScoreEst(Question.EstimationQuestion question, float answer) {
		var error = Math.abs(answer - question.getCorrectAnswer());
		var errorRatio = error / question.getCorrectAnswer();
		return calculateScoreShared(errorRatio);
	}

	private int calculateScoreComp(Question.ComparisonQuestion question, float answer) {
		float errorRatio = 0;
		if (answer < question.getCorrectAnswer()) {
			errorRatio = 1 - (answer / question.getCorrectAnswer());
		} else {
			errorRatio = 1 - (question.getCorrectAnswer() / answer);
		}
		return calculateScoreShared(errorRatio);
	}

	private int calculateScoreShared(float errorRatio) {
		if (errorRatio < EST_SCORE_RATIO_GOOD) {
			return MAX_SCORE;
		} else if (errorRatio > EST_SCORE_RATIO_BAD) {
			return 0;
		} else {
			return Math.round(MathUtil.linearMap(errorRatio, EST_SCORE_RATIO_GOOD, EST_SCORE_RATIO_BAD, MAX_SCORE, 0));
		}
	}

	private int calculateScorePick(Question.PickEnergyQuestion question, int answer) {
		if (question.getCorrectAnswer() == answer) return MAX_SCORE;
		else return 0;
	}
}
