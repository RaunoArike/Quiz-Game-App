package server.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.api.OutgoingControllerImpl;

@ExtendWith(MockitoExtension.class)
public class OutgoingControllerImplTest {

	@Mock
	private SimpMessagingTemplate template;

	private OutgoingControllerImpl createController() {
		return new OutgoingControllerImpl(template);
	}

	@Test
	public void test_template_creation () {

	}
}
