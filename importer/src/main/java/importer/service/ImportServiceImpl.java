package importer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import importer.api.ActivityApi;
import importer.model.ImportedActivity;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;


public class ImportServiceImpl implements ImportService {
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
		String absolutePath = fileProvider.provideAbsolutePath(filePath);
		File file = fileProvider.checkIfJsonFileExists(absolutePath);
		var rawActivities = mapper.readValue(file, ImportedActivity[].class);
		var activities = Arrays.stream(rawActivities).map(activity -> activity.toModel(absolutePath)).toList();
		activityApi.addActivities(serverUrl, activities);
	}

	@Override
	public void deleteAllActivities(String serverUrl) {
		activityApi.deleteAllActivities(serverUrl);
	}
}
