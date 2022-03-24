package server.service;

import commons.model.Activity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.api.ActivityController;
import server.exception.IdNotFoundException;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class ActivityControllerTest {

	private static final Activity FAKE_ACTIVITY = new Activity("A4", null, 4f);

	@Mock
	private ActivityService activityService;

	private ActivityController createController() {
		return new ActivityController(activityService);
	}

	
	@Test
	public void updateActivity_should_throw_exception_for_wrong_id() {
		var controller = createController();
		doThrow(new EntityNotFoundException()).when(activityService).updateActivity(150000, FAKE_ACTIVITY);

		assertThrows(IdNotFoundException.class, () -> controller.updateActivity(150000, FAKE_ACTIVITY));
	}
}
