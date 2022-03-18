package server.service;

import commons.model.Activity;
import commons.model.Question;
import commons.servermessage.QuestionMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.api.OutgoingController;
import server.api.OutgoingControllerImpl;

import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OutgoingControllerImplTest {
	private static final Question FAKE_QUESTION = new Question.EstimationQuestion(new Activity("a", "b"), 4f);

	@Mock
	private SimpMessagingTemplate template;

	private OutgoingControllerImpl createController() {
		return new OutgoingControllerImpl(template);
	}

	@Test
	public void test_sending_message() {
		OutgoingController outgoingController = createController();
		int playerId = 15;
		String user = "15";
		String dest = "/topic/question";

		outgoingController.sendQuestion(
				new QuestionMessage(FAKE_QUESTION, 0),
				List.of(playerId));

		verify(template).convertAndSendToUser(user, dest, new QuestionMessage(FAKE_QUESTION, 0));
	}
}
