package client.service;

import org.springframework.lang.NonNull;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class ServerServiceImpl implements ServerService {
	private StompSession session;
	private String dest;

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

	private <T> void registerForMessages(String d, Class<T> type, Consumer<T> consumer) {
		session.subscribe(d, new StompSessionHandlerAdapter() {

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
		if (url.equals("pwd")) {
			return true;
		}
		return false;
	}

	@Override
	public void startSingleGame(String username) {

	}

	@Override
	public void answerQuestions(Number answer) {

	}

	@Override
	public void registerListener(ServerListener serverListener) {

	}
}

