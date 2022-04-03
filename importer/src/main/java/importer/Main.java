package importer;

import com.fasterxml.jackson.databind.ObjectMapper;
import importer.api.ActivityApiImpl;
import importer.service.FilePathProvider;
import importer.service.ImportServiceImpl;

import java.util.Arrays;

public class Main {
	public static void main(String[] args) {
		if (args.length < 2) throw new IllegalArgumentException("Not enough arguments provided");
		var serverUrl = args[0];
		var filePath = args[1];
		var flags = Arrays.stream(args).skip(2).toList();

		var service = new ImportServiceImpl(new ActivityApiImpl(), new ObjectMapper(), new FilePathProvider());

		if (flags.contains("-D")) service.deleteAllActivities(serverUrl);

		service.importActivitiesFromFile(serverUrl, filePath);
	}
}
