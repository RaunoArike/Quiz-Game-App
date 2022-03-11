package client.scenes;

import com.google.inject.Inject;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class UsernameCtrl {
	private final ServerUtils server;
	private final MainCtrl mainCtrl;

	@FXML
	private TextField username;

	@Inject
	public UsernameCtrl(ServerUtils server, MainCtrl mainCtrl) {
		this.server = server;
		this.mainCtrl = mainCtrl;
	}

	public void start() {
	}

	public void clearField() {
		username.clear();
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
