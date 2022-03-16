package client.service;

import commons.servermessage.QuestionMessage;
import commons.servermessage.ScoreMessage;

public interface ServerService {
	interface ServerListener {
		void onQuestion(QuestionMessage questionMessage);
		void onScore(ScoreMessage scoreMessage);
	}

	//connect client to server
	boolean connectToServer(String url);

	//start a single player game
	void startSingleGame(String username);

	//answer submit
	void answerQuestions(Number answer);

	//receive messages from server
	void registerListener(ServerListener serverListener);
}
