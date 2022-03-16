package importer.service;

import java.io.IOException;

public interface ImportService {
	void importServicesFromFile(String serverUrl, String filePath) throws IOException;
}
