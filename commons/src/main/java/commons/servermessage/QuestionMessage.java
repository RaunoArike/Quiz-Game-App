package commons.servermessage;

import commons.model.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class QuestionMessage {
	private final Question question;
	private final int questionNumber;
}
