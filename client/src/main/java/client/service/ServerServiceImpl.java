package client.service;

import commons.clientmessage.QuestionAnswerMessage;
import commons.clientmessage.SinglePlayerGameStartMessage;
import commons.clientmessage.WaitingRoomJoinMessage;
import commons.servermessage.QuestionMessage;
import commons.servermessage.ScoreMessage;
import org.springframework.lang.NonNull;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class ServerServiceImpl implements ServerService {
	private StompSession session;
	private List<ServerListener> serverListeners = new ArrayList<>();

	private StompSession connect(String url) {
		var client = new StandardWebSocketClient();
		var stomp = new WebSocketStompClient(client);
		stomp.setMessageConverter(new MappingJackson2MessageConverter());
		try {
			return stomp.connect(url, new StompSessionHandlerAdapter() {
			}).get();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
		throw new IllegalStateException();
	}

	private <T> void registerForMessages(String dest, Class<T> type, Consumer<T> consumer) {
		session.subscribe(dest, new StompSessionHandlerAdapter() {

			@Override
			@NonNull
			public Type getPayloadType(StompHeaders headers) {
				return type;
			}

			@SuppressWarnings("unchecked")
			@Override
			public void handleFrame(StompHeaders headers, Object payload) {
				consumer.accept((T) payload);
			}
		});
	}

	@Override
	public boolean connectToServer(String url) {
		try {
			session = connect(url);
			registerForMessages("/topic/question", QuestionMessage.class, message -> {
				serverListeners.forEach(serverListener -> serverListener.onQuestion(message));
			});
			registerForMessages("/topic/score", ScoreMessage.class, message -> {
				serverListeners.forEach(serverListener -> serverListener.onScore(message));
			});
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public void startSingleGame(String username) {
		session.send("/app/start-single-player", new SinglePlayerGameStartMessage(username));
	}

	@Override
	public void joinWaitingRoom(String username) {
		session.send("/app/join-waiting-room", new WaitingRoomJoinMessage(username));
	}

	@Override
	public void startMultiGame() {
		// TODO
	}

	@Override
	public void answerQuestion(Number answer) {
		Integer answerInt = answer instanceof Integer ? (Integer) answer : null;
		Float answerFloat = answer instanceof Float ? (Float) answer : null;
		session.send("/app/submit-answer", new QuestionAnswerMessage(answerInt, answerFloat, 0));
	}

	@Override
	public void registerListener(ServerListener serverListener) {
		serverListeners.add(serverListener);
	}
}
