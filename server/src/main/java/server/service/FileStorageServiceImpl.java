package server.service;
import org.springframework.stereotype.Service;
import java.nio.file.Files;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.util.FileSystemUtils;

@Service
public class FileStorageServiceImpl implements FileStorageService {

	private final Path root = Paths.get("images/");

	@Override
	public void save(MultipartFile file) {
		try {
			Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
		} catch (Exception e) {
			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(root.toFile());
	}

}
