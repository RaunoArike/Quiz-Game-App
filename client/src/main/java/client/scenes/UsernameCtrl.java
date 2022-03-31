package client.scenes;

import client.service.MessageLogicService;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class UsernameCtrl extends AbstractCtrl {

	/**
	 * This controller class refers to the username input screen for starting a single-player game.
	 * For multiplayer game refer to JoinWaitingRoomCtrl.
	 */
	private final MessageLogicService messageService;
	private final MainCtrl mainCtrl;

	@FXML
	private TextField username;

	@FXML
	private Label errorMessage;

	@Inject
	public UsernameCtrl(MessageLogicService messageService, MainCtrl mainCtrl) {
		this.messageService = messageService;
		this.mainCtrl = mainCtrl;
	}

	@Override
	public void init() {
		super.init();
		clearField();
	}

	public void start() {
		this.errorMessage.setText("");
		String username = this.username.getText();
		if (username != null && !username.isEmpty()) {
			this.messageService.startSingleGame(username);
			try {
				Writer writer = new FileWriter("@/client/resources/usernames.txt", true);
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

}
