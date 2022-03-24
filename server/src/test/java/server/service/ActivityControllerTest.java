package server.service;

import commons.model.Activity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.api.ActivityController;
import server.exception.IdNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ActivityControllerTest {

	private static final List<Activity> FAKE_ACTIVITY_LIST = List.of(
			new Activity("A1", null, 1f),
			new Activity("A2", null, 2f),
			new Activity("A3", null, 3f)
	);

	@Mock
	private ActivityService activityService;

	private ActivityController createController() {
		return new ActivityController(activityService);
	}
}
