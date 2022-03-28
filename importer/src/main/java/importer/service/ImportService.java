package importer.service;

import java.io.IOException;

/**
 * Activity importer service
 */
public interface ImportService {
	/**
	 * Imports activities in ActivityBank format from file [filePath + "\activities.json"] to server specified by [serverUrl].
	 *
	 * @param serverUrl server activities should be uploaded to
	 * @param filePath path to file to read activities from
	 * @throws IOException in case of unsuccessful file access
	 */
	void importServicesFromFile(String serverUrl, String filePath) throws IOException;

	/**
	 * Deletes all activities on the server.
	 *
	 * @param serverUrl server url
	 */
	void deleteAllActivities(String serverUrl);
}
