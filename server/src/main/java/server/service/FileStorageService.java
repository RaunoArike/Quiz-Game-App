package server.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.FileAlreadyExistsException;

public interface FileStorageService {

	void save(MultipartFile file, String imagePath) throws FileAlreadyExistsException;

	void deleteAll();

}
