package client.usecase;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RememberUsernameUseCaseImpl implements RememberUsernamesUseCase {

	private final Path root = Paths.get("usernames/");

	@Override
	public List<String> readUsernames() {
		try {
			List<String> usernames = new ArrayList<>();
			String path = root.resolve("usernames.txt").toString();

			Scanner scanner = new Scanner(new File(path));
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (usernames.contains(next) || next.equals("")) {
					continue;
				}
				usernames.add(next);
			}
			return usernames;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void writeUsername(String username) {
		try {
			String path = root.resolve("usernames.txt").toString();
			Writer writer = new FileWriter(path, true);
			writer.write(username + "\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
