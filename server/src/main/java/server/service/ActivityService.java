package server.service;

import commons.model.Activity;

import java.util.List;

/**
 * Activity management service
 */
public interface ActivityService {
	/**
	 * Adds activities.
	 *
	 * @param activities activities to add
	 */
	void addActivities(List<Activity> activities);
}
