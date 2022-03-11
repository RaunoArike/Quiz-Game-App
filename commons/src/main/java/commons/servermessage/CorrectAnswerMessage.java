package commons.servermessage;

import lombok.Data;

@Data
public class CorrectAnswerMessage {
	private final int questionScore;
	private final int totalScore;
}
