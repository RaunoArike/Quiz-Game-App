package server.service;

import commons.model.Activity;

import java.util.List;

/**
 * Activity management service
 */
public interface ActivityService {
	/**
	 * Adds specified activities.
	 *
	 * @param activities activities to add
	 */
	void addActivities(List<Activity> activities);

	/**
	 * Provides a list with all activities currently in the repository.
	 *
	 * @return returns a list with all activities
	 */
	List<Activity> provideActivities();

	/**
	 * Removes all activities.
	 */
	void removeAllActivities();

	/**
	 * Updates an activity in the repository.
	 * If an activity with the given id doesn't exist, adds the provided activity to the repository instead of updating.
	 *
	 * @param activityId the id of the activity to be updated
	 */
	void updateActivity(long activityId, Activity activity);
}
