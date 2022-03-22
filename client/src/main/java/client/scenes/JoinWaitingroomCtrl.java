package client.scenes;

import client.service.ServerService;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

public class JoinWaitingroomCtrl {
	private final ServerService server;
	private final MainCtrl mainCtrl;

	@FXML
	private TextField username;

	@FXML
	private Label errorMessage;

	@Inject
	public JoinWaitingroomCtrl(ServerService server, MainCtrl mainCtrl) {
		this.server = server;
		this.mainCtrl = mainCtrl;
	}

	//Links to the cancel button
	public void returnHome() {
		username.clear();
		this.errorMessage.setText("");
		mainCtrl.showHome();
	}

	//Links to the join button
	public void join() {
		username.clear();
		this.errorMessage.setText("");
		String username = this.username.getText();
		if (username != null && !username.isEmpty()) {
			server.joinWaitingRoom(username);
		} else {
			this.errorMessage.setText("Please enter a valid username: ");
		}
		this.username.clear();
	}

	/**
	 * Adds key functionality to the textfields - enter to trigger join,
	 * escape to clear fields
	 */
	public void keyPressed(KeyEvent e) {
		switch (e.getCode()) {
			case ENTER:
				join();
				break;
			case ESCAPE:
				returnHome();
				break;
			default:
				break;
		}
	}

}
