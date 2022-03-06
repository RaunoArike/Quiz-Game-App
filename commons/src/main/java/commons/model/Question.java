package commons.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Model of a question
 */
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		property = "type"
)
@JsonSubTypes({
		@Type(value = Question.MultiChoice.class, name = "mc"),
		@Type(value = Question.CalculationQuestion.class, name = "calculation"),
		@Type(value = Question.EstimationQuestion.class, name = "estimation"),

})
public abstract class Question implements QuestionService{

	public abstract Question generateQuestion(int gameId);

	/**
	 * Question with x possible activities to choose, one of them is the correct answer
	 */
	@AllArgsConstructor
	@Getter
	public static class MultiChoice extends Question {
		private final List<Activity> activities;
		private final int correctAnswer;

		@Override
		public Question generateQuestion(int gameId) {
			return null;
		}
	}

	@AllArgsConstructor
	@Getter
	public static class CalculationQuestion extends Question{
		private final List<Activity> activities;
		private final int correctAnswer;

		@Override
		public Question generateQuestion(int gameId) {
			return null;
		}
	}

	@AllArgsConstructor
	@Getter
	public static class EstimationQuestion extends Question{
		private final List<Activity> activities;
		private final int correctAnswer;

		@Override
		public Question generateQuestion(int gameId) {
			return null;
		}
	}
}
