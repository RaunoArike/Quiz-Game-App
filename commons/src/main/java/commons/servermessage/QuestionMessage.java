package commons.servermessage;

import commons.model.Question;

public record QuestionMessage(
		Question question,
		int questionNumber,
		boolean reduceTimeAvailable,
		boolean doublePointsAvailable,
		boolean eliminateMCOptionAvailable
) {
	public QuestionMessage(Question question, int questionNumber) {
		this(question, questionNumber, true, true, true);
	}
}
