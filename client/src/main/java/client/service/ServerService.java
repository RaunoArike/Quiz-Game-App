package client.service;

public interface ServerService {
    //connect client to server
    boolean connectToServer(String url);

    //start a single player game;
    void startSingleGame(String username);

    //answer submit
    void answerQuestions(Number answer);
}
