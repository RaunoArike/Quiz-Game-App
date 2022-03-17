package server.service;

import commons.model.Activity;
import org.springframework.stereotype.Service;
import server.entity.ActivityEntity;
import server.repository.ActivityRepository;

import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {
	private final ActivityRepository activityRepository;

	public ActivityServiceImpl(ActivityRepository activityRepository) {
		this.activityRepository = activityRepository;
	}

	@Override
	public void addActivities(List<Activity> activities) {
		List<ActivityEntity> entities = activities.stream().map(ActivityEntity::fromModel).toList();
		activityRepository.saveAll(entities);
	}

	@Override
	public void removeAllActivities() {
		activityRepository.deleteAll();
	}
}
