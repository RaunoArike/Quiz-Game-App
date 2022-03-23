package server.service;

import commons.model.Activity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import server.entity.ActivityEntity;
import server.repository.ActivityRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ActivityServiceImplTest {
	private static final List<Activity> FAKE_ACTIVITY_LIST = List.of(
			new Activity("A1", null, 1f),
			new Activity("A2", null, 2f),
			new Activity("A3", null, 3f)
	);

	@Mock
	private ActivityRepository activityRepository;

	@Captor
	private ArgumentCaptor<List<ActivityEntity>> activityEntityListCaptor;

	private ActivityServiceImpl createService() {
		return new ActivityServiceImpl(activityRepository);
	}

	@Test
	public void addActivities_should_save_activities_to_repo() {
		var service = createService();
		service.addActivities(FAKE_ACTIVITY_LIST);

		verify(activityRepository).saveAll(activityEntityListCaptor.capture());

		var activityEntities = activityEntityListCaptor.getValue();
		var activityModels = activityEntities.stream().map(ActivityEntity::toModel).toList();

		assertEquals(FAKE_ACTIVITY_LIST, activityModels);
	}

	@Test
	public void provideActivities_should_return_a_list_of_all_activities_in_repo() {

	}

	@Test
	public void removeAllActivities_should_delete_activities_from_repo() {
		var service = createService();
		service.removeAllActivities();

		verify(activityRepository).deleteAll();
	}
}
