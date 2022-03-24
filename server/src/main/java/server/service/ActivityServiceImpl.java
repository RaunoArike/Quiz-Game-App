package server.service;

import commons.model.Activity;
import org.springframework.stereotype.Service;
import server.entity.ActivityEntity;
import server.repository.ActivityRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
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
	public List<Activity> provideActivities() {
		List<ActivityEntity> entities = activityRepository.findAll();
		List<Activity> activities = new ArrayList<>();

		for (ActivityEntity entity : entities) {
			activities.add(entity.toModel());
		}
		return activities;
	}

	@Override
	public void removeAllActivities() {
		activityRepository.deleteAll();
	}

	@Override
	public void updateActivity(long activityId, Activity activity) throws EntityNotFoundException {
		ActivityEntity entity = activityRepository.getById(activityId);
		entity.setName(activity.name());
		entity.setImageUrl(activity.imageUrl());
		entity.setEnergyInWh(activity.energyInWh());
	}
}
