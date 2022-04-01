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

		private static final float EST_SCORE_RATIO_GOOD = 0.075f;
		private static final float EST_SCORE_RATIO_BAD = 0.45f;
		private static final int NUMBER_OF_CASES = 3;
		private static final int NUMBER_OF_ANSWER_OPTIONS = 3;
		private static final int NUMBER_OF_QUESTION_TYPES = 4;

		private static final double TIME_RATIO_PERFECT = 1;
		private static final double TIME_RATIO_GOOD = 0.85;
		private static final double TIME_RATIO_AVERAGE = 0.55;
		private static final double TIME_RATIO_BAD = 0.33;
		private static final double TIME_RATIO_POOR = 0.45;

		private static final int TIME_PERIOD_1 = 5000;
		private static final int TIME_PERIOD_2 = 10000;
		private static final int TIME_PERIOD_3 = 15000;
		private static final int TOTAL_TIME = 20000;

		private static final int BASE = 20;

		private static final int DEFAULT = 0;
		private final List<ActivityEntity> visited = new ArrayList<>();
		private final ActivityRepository activityRepository;

		public QuestionServiceImpl(ActivityRepository activityRepository) {
			this.activityRepository = activityRepository;
		}

		public Question generateQuestion(int gameId) {

			int no = Math.abs(new Random().nextInt());

			switch (no % NUMBER_OF_QUESTION_TYPES) {
				case 0:
					return generateMC();
				case 1:
					return generateEst();
				case 2:
					return generateComp();
				case NUMBER_OF_CASES:
					return generatePick();
			}
			return null;
		}

		private List<ActivityEntity> generateActivities() {
			List<ActivityEntity> listActivities = activityRepository.findAll();
			List<ActivityEntity> selectedEntities = new ArrayList<>();

			int index = 0;
			///MAGIC NUMBERS HAVE TO BE REMOVED

			while (index < NUMBER_OF_ANSWER_OPTIONS) {
				int no = Math.abs(new Random().nextInt());
				ActivityEntity act = listActivities.get(no % listActivities.size());
				if (!visited.contains(act)) {
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
			int correctAnswer = Math.abs(new Random().nextInt()) % NUMBER_OF_ANSWER_OPTIONS;
			return new Question.PickEnergyQuestion(
					selectedEntities.get(1).toModel(),
					correctAnswer,
					generatePickOptions(selectedEntities.get(1).getEnergyInWh(), correctAnswer)
			);
		}


		private int generateMCAnswer(List<ActivityEntity> listActivities) {
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

		private float generateCompAnswer(List<ActivityEntity> listActivities) {
			ActivityEntity activity1 = listActivities.get(0);
			ActivityEntity activity2 = listActivities.get(1);
			return activity2.getEnergyInWh() / activity1.getEnergyInWh();
		}

		//TODO consider improving the formula
		public List<Float> generatePickOptions(float correctAnswerInWh, int answerNumber) {
			int correctAnswerInt = (int) correctAnswerInWh;
			List<Float> answerList = new ArrayList<>();
			for (int i = 0; i < NUMBER_OF_ANSWER_OPTIONS; i++) {
				float wrongAnswer = correctAnswerInWh + (new Random().nextInt() % correctAnswerInt);
				answerList.add(wrongAnswer);
			}
			answerList.set(answerNumber, correctAnswerInWh);
			return answerList;
		}


		@Override
		public int calculateScore(Question question, Number answer, long timeSpent, boolean doublePoints) {
			if (question instanceof Question.MultiChoiceQuestion mc) {
				return calculateScoreMC(mc, answer.intValue(), timeSpent, doublePoints);
			} else if (question instanceof Question.EstimationQuestion est) {
				return calculateScoreEst(est, answer.floatValue(), timeSpent, doublePoints);
			} else if (question instanceof Question.ComparisonQuestion comp) {
				return calculateScoreComp(comp, answer.floatValue(), timeSpent, doublePoints);
			} else if (question instanceof Question.PickEnergyQuestion pick) {
				return calculateScorePick(pick, answer.intValue(), timeSpent, doublePoints);
			}
			return 0;
		}

		private int estimationScoring(Question.EstimationQuestion question, float answer,
										long timeSpent, boolean doublePoints) {

			if (answer == question.correctAnswer()) {
				if (answer == question.correctAnswer()) {
					if (doublePoints) {
						return 2 * scoreToTime(timeSpent);
					}

					return scoreToTime(timeSpent);
				}
			}
			double limit1 = answer - Math.pow(MAX_SCORE, 1 / BASE);
			double limit2 = answer + Math.pow(MAX_SCORE, 1 / BASE);

			float base = answer - question.correctAnswer();
			float value = 1;
			for (int i = 0; i < BASE; i++) {
				value *= base;
			}
			value = MAX_SCORE - value;
			if (answer < limit1) {
				return 0;
			}

			if (answer > limit2) {
				return 0;
			}

			if (question.correctAnswer() < 1) {
				float base2 = answer - question.correctAnswer() + 1;
				float value2 = 1;
				for (int i = 0; i < BASE; i++) {
					value *= base2;
				}
				value = MAX_SCORE - value;
				return (int) value;
			}
			return (int) value;
		}

		private int comparisonScoring(Question.ComparisonQuestion question,
										float answer, long timeSpent, boolean doublePoints) {
			if (answer == question.correctAnswer()) {
				if (doublePoints) {
					return 2 * scoreToTime(timeSpent);
				}

				return scoreToTime(timeSpent);
			}
			double limit1 = answer - Math.pow(MAX_SCORE, 1 / BASE);
			double limit2 = answer + Math.pow(MAX_SCORE, 1 / BASE);

			float base = answer - question.correctAnswer();
			float value = 1;
			for (int i = 0; i < BASE; i++) {
				value *= base;
			}
			value = MAX_SCORE - value;
			if (answer < limit1) {
				return 0;
			}

			if (answer > limit2) {
				return 0;
			}

			if (question.correctAnswer() < 1) {
				float base2 = answer - question.correctAnswer() + 1;
				float value2 = 1;
				for (int i = 0; i < BASE; i++) {
					value *= base2;
				}
				value = MAX_SCORE - value;
				return (int) value;
			}
			return (int) value;
		}


		public int scoreToTime(long timeSpent) {
			if (timeSpent < TIME_PERIOD_1) {
				return (int) (MAX_SCORE * TIME_RATIO_PERFECT);
			}

			if (timeSpent > TIME_PERIOD_1
					&& timeSpent < TIME_PERIOD_2) {
				return (int) (MAX_SCORE * TIME_RATIO_GOOD);
			}

			if (timeSpent > TOTAL_TIME - TIME_PERIOD_2
					&& timeSpent < TIME_PERIOD_3) {
				return (int) (MAX_SCORE * TIME_RATIO_AVERAGE);
			} else {
				return (int) (MAX_SCORE * TIME_RATIO_BAD);
			}
		}
		private int calculateScorePick(Question.PickEnergyQuestion question,
										int answer, long timeSpent, boolean doublePoints) {
			/*
			 * If the time spent for the question is less than time period 1, we give the player the maximum score
			 * If it is greater but smaller than time period 2, we still give them a fairly high score
			 * to make the game competitive
			 */
			if (answer == question.correctAnswer()) {
				if (doublePoints) {
					return 2 * scoreToTime(timeSpent);
				}

				return scoreToTime(timeSpent);
			}
			return DEFAULT;
		}

		private int calculateScoreMC(Question.MultiChoiceQuestion question,
									int answer, long timeSpent, boolean doublePoints) {
			/*
			 * If the time spent for the question is less than time period 1, we give the player the maximum score
			 * If it is greater but smaller than time period 2, we still give them a fairly high score
			 * to make the game competitive
			 */
			if (answer == question.correctAnswer()) {
				if (timeSpent < TIME_PERIOD_1) {
					return (int) (MAX_SCORE * TIME_RATIO_PERFECT);
				}

				if (timeSpent > TIME_PERIOD_1
						&& timeSpent < TIME_PERIOD_2) {
					return (int) (MAX_SCORE * TIME_RATIO_GOOD);
				}

				if (timeSpent > TOTAL_TIME - TIME_PERIOD_2
						&& timeSpent < TIME_PERIOD_3) {
					return (int) (MAX_SCORE * TIME_RATIO_AVERAGE);
				} else {
					return (int) (MAX_SCORE * TIME_RATIO_BAD);
				}

			}
			return DEFAULT;
		}


		private int calculateScoreEst(Question.EstimationQuestion question,
										float answer, long timeSpent, boolean doublePoints) {
			var error = Math.abs(answer - question.correctAnswer());
			var errorRatio = error / question.correctAnswer();
			return calculateScoreShared(errorRatio, timeSpent);
		}

		private int calculateScoreComp(Question.ComparisonQuestion question,
										float answer, long timeSpent, boolean doublePoints) {
			float errorRatio;
			if (answer < question.correctAnswer()) {
				errorRatio = 1 - (answer / question.correctAnswer());
			} else {
				errorRatio = 1 - (question.correctAnswer() / answer);
			}
			return calculateScoreShared(errorRatio, timeSpent);
		}

		private int calculateScoreShared(float errorRatio, long timeSpent) {
			if (errorRatio < EST_SCORE_RATIO_GOOD) {
				return MAX_SCORE;
			} else if (errorRatio > EST_SCORE_RATIO_BAD) {
				return 0;
			} else {
				return Math.round(MathUtil.linearMap(errorRatio,
						EST_SCORE_RATIO_GOOD, EST_SCORE_RATIO_BAD, MAX_SCORE, 0));
			}
		}
}
