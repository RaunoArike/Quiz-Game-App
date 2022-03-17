package importer;

import com.fasterxml.jackson.databind.ObjectMapper;
import importer.api.ActivityApiImpl;
import importer.service.ImportServiceImpl;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		if (args.length < 2) throw new IllegalArgumentException("Not enough arguments provided");
		var serverUrl = args[0];
		var filePath = args[1];

		var service = new ImportServiceImpl(new ActivityApiImpl(), new ObjectMapper());
		service.importServicesFromFile(serverUrl, filePath);
	}
}
