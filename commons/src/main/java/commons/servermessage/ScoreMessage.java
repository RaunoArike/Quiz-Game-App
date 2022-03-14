package commons.servermessage;

import lombok.Data;

@Data
public class ScoreMessage {
	private final int questionScore;
	private final int totalScore;
}
