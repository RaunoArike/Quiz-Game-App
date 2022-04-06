package client.scenes;

import client.service.MessageLogicService;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UsernameCtrl extends AbstractCtrl {

	/**
	 * This controller class refers to the username input screen for starting a singleplayer game.
	 * For multiplayer game refer to JoinWaitingRoomCtrl.
	 */
	private final MessageLogicService messageService;
	private final MainCtrl mainCtrl;

	@FXML
	private TextField username;

	@FXML
	private Label errorMessage;

	@FXML
	private ComboBox comboBox;


	@Inject
	public UsernameCtrl(MessageLogicService messageService, MainCtrl mainCtrl) {
		this.messageService = messageService;
		this.mainCtrl = mainCtrl;
	}

	@Override
	public void init() {
		super.init();
		clearField();
		comboBox.getSelectionModel().clearSelection();

		initializeDropdownMenu();
		comboBox.setOnAction(e -> setUsername((String) comboBox.getValue()));
	}


	public void start() {
		this.errorMessage.setText("");
		String username = this.username.getText();

		String path = Path.of("src", "main", "resources", "usernames", "usernames.txt").toAbsolutePath().toString();

		if (username != null && !username.isEmpty()) {
			this.messageService.startSingleGame(username);
			errorMessage.setText("");
			try {
				Writer writer = new FileWriter(path, true);
				writer.write(username + "\n");
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			this.errorMessage.setText("Please enter a valid username: ");
		}
		this.username.clear();
	}

	public void clearField() {
		username.clear();
		errorMessage.setText("");
	}

	public void cancel() {
		mainCtrl.showHome();
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getCode()) {
			case ENTER:
				start();
				break;
			case ESCAPE:
				clearField();
				break;
			default:
				break;
		}
	}


	/**
	 * Reads previously used usernames from the usernames.txt file
	 * and adds them to the dropdown menu.
	 */
	private void initializeDropdownMenu() {
		try {
			comboBox.setPromptText("Choose username");

			List<String> usernames = new ArrayList<>();
			String path = Path.of("src", "main", "resources", "usernames", "usernames.txt").toAbsolutePath().toString();
			Scanner scanner = new Scanner(new File(path));
			while (scanner.hasNextLine()) {
				String next = scanner.nextLine();
				if (usernames.contains(next) || next.equals("")) {
					continue;
				}
				usernames.add(next);
			}

			for (String name : usernames) {
				if (comboBox.getItems().contains(name)) {
					continue;
				}
				comboBox.getItems().add(name);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void setUsername(String name) {
		username.setText(name);
	}
}
