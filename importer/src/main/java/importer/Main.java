package importer;

import com.fasterxml.jackson.databind.ObjectMapper;
import importer.api.ActivityApiImpl;
import importer.service.ImportServiceImpl;

import java.io.IOException;
import java.util.Arrays;

/**
 * In order to import activities, run with arguments:
 * <pre>
 * SERVER_URL ACTIVITIES_FILE_PATH [-D]
 * </pre>
 * where:
 * <ul>
 *     <li>SERVER_URL is address of the server to connect to</li>
 *     <li>ACTIVITIES_FILE_PATH is path to activities.json file</li>
 *     <li>-D is an optional flag for removing all prior activities</li>
 * </ul>
 *
 * For example:
 * <pre>
 * gradle :importer:run --args "http://localhost:8080 ../activities/activities.json" -D
 * </pre>
 */
public class Main {
	public static void main(String[] args) throws IOException {
		if (args.length < 2) throw new IllegalArgumentException("Not enough arguments provided");
		var serverUrl = args[0];
		var filePath = args[1];
		var flags = Arrays.stream(args).skip(2).toList();

		var service = new ImportServiceImpl(new ActivityApiImpl(), new ObjectMapper());

		if (flags.contains("-D")) service.deleteAllActivities(serverUrl);

		service.importServicesFromFile(serverUrl, filePath);
	}
}
