package commons.servermessage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ScoreMessage {
	private final int questionScore;
	private final int totalScore;
}
