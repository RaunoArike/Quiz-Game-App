package client.scenes;

import com.google.inject.Inject;
import client.service.ServerServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

public class JoinWaitingroomCtrl {
	private final ServerServiceImpl server;
	private final MainCtrl mainCtrl;

	@FXML
	private TextField username;

	@FXML
	private Label errorMessage;

	@Inject
	public JoinWaitingroomCtrl(ServerServiceImpl server, MainCtrl mainCtrl) {
		this.server = server;
		this.mainCtrl = mainCtrl;
	}

	public void returnHome() {
		mainCtrl.showHome();
	}

	public void join() {
		this.errorMessage.setText("");
		String username = this.username.getText();
		if (username != null && !username.isEmpty()) {
			this.server.joinWaitingRoom(username);
		} else {
			this.errorMessage.setText("Please enter a valid username: ");
		}
		this.username.clear();
	}

	public void clearField() {
		username.clear();
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getCode()) {
			case ENTER:
				join();
				break;
			case ESCAPE:
				clearField();
				break;
			default:
				break;
		}
	}

}
