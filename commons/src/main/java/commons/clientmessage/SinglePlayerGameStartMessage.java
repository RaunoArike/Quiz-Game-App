package commons.clientmessage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SinglePlayerGameStartMessage {
	private final String username;
}
