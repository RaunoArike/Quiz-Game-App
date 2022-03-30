package importer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import importer.api.ActivityApi;
import importer.model.ImportedActivity;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;


public class ImportServiceImpl implements ImportService {
	private static final String IMAGE_URL_PREFIX = "http://${SERVER_ADDRESS}/images/";

	private final ActivityApi activityApi;
	private final ObjectMapper mapper;
	private final FilePathProvider fileProvider;

	public ImportServiceImpl(ActivityApi activityApi, ObjectMapper mapper, FilePathProvider fileProvider) {
		this.activityApi = activityApi;
		this.mapper = mapper;
		this.fileProvider = fileProvider;
	}

	@Override
	public void importServicesFromFile(String serverUrl, String filePath) throws IOException {
		File file = fileProvider.checkIfJsonFileExists(filePath);
		var rawActivities = mapper.readValue(file, ImportedActivity[].class);
		var activities = Arrays.stream(rawActivities).map(activity -> activity.toModel(IMAGE_URL_PREFIX)).toList();
		activityApi.addActivities(serverUrl, activities);
	}

	@Override
	public void deleteAllActivities(String serverUrl) {
		activityApi.deleteAllActivities(serverUrl);
	}
}
