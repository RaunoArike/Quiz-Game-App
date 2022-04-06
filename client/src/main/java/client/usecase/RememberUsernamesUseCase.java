package client.usecase;

import java.util.List;

public interface RememberUsernamesUseCase {

	List<String> readUsernames();

	void writeUsername(String username);
}
