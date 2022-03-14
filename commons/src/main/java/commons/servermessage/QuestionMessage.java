package commons.servermessage;

import commons.model.Question;
import lombok.Data;

@Data
public class QuestionMessage {
	private final Question question;
	private final int questionNumber;
}
