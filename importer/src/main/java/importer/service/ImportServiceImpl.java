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

	public ImportServiceImpl(ActivityApi activityApi, ObjectMapper mapper) {
		this.activityApi = activityApi;
		this.mapper = mapper;
	}

	@Override
	public void importServicesFromFile(String serverUrl, String filePath) throws IOException {
		var file = new File(filePath);
		var rawActivities = mapper.readValue(file, ImportedActivity[].class);
		var activities = Arrays.stream(rawActivities).map(ImportedActivity::toModel).toList();
		activityApi.addActivities(serverUrl, activities);
	}
}
