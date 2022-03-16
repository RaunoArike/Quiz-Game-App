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
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({@Type(value = Question.MultiChoiceQuestion.class, name = "mc"),
		@Type(value = Question.ComparisonQuestion.class, name = "calculation"),
		@Type(value = Question.EstimationQuestion.class, name = "estimation"),
		@Type(value = Question.PickEnergyQuestion.class, name = "picking")

})
public abstract class Question {

	/**
	 * Question with x possible activities to choose, one of them is the correct answer
	 */
	@AllArgsConstructor
	@Getter
	public static class MultiChoiceQuestion extends Question {
		private final List<Activity> activities;
		private final int correctAnswer;

	}

	@AllArgsConstructor
	@Getter
	public static class ComparisonQuestion extends Question {
		private final List<Activity> activities;
		private final float correctAnswer;

	}

	@AllArgsConstructor
	@Getter
	public static class EstimationQuestion extends Question {
		private final Activity activity;
		private final float correctAnswer;
	}

	@AllArgsConstructor
	@Getter
	public static class PickEnergyQuestion extends Question {
		private final Activity activity;
		private final int correctAnswer;
	}
}
