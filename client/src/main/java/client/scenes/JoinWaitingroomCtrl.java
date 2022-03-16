package client.scenes;

import com.google.inject.Inject;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class JoinWaitingroomCtrl {
	private final ServerUtils server;
	private final MainCtrl mainCtrl;

	@FXML
	private TextField username;

	@FXML
	private TextField gamePin;

	@Inject
	public JoinWaitingroomCtrl(ServerUtils server, MainCtrl mainCtrl) {
		this.server = server;
		this.mainCtrl = mainCtrl;
	}

	//Links to the cancel button
	public void returnHome() {
		mainCtrl.showHome();
	}

	//Links to the join button
	public void join() {
		// TODO -  call on serverutils method
		clearField();
	}

	//Helper method for key functionality
	public void clearField() {
		username.clear();
		gamePin.clear();
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
				clearField();
				returnHome();
				break;
			default:
				break;
		}
	}

}
