package importer.api;

import commons.model.Activity;

import java.util.List;

/**
 * Server API for managing activities
 */
public interface ActivityApi {
	/**
	 * Adds activities on the server.
	 *
	 * @param serverUrl server url
	 * @param activities activities to add
	 */
	void addActivities(String serverUrl, List<Activity> activities);

	/**
	 * Deletes all activities on the server.
	 *
	 * @param serverUrl server url
	 */
	void deleteAllActivities(String serverUrl);
}
