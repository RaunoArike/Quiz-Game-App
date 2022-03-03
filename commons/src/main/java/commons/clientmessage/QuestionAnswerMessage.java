package commons.clientmessage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class QuestionAnswerMessage {
	private final Integer answerInt;
	private final Float answerFloat;
	private final int passedTime;
}
