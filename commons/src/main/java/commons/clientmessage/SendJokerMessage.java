package commons.clientmessage;

import commons.model.JokerType;

public record SendJokerMessage(JokerType jokerType) {

	public JokerType getJokerType() {
		return jokerType;
	}
}
