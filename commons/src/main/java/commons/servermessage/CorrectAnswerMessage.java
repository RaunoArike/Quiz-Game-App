package commons.servermessage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CorrectAnswerMessage {
	private final int questionScore;
	private final int totalScore;
}
