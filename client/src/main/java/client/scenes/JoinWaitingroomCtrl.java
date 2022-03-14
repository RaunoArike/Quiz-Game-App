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

    @Inject
	public JoinWaitingroomCtrl(ServerUtils server, MainCtrl mainCtrl) {
		this.server = server;
		this.mainCtrl = mainCtrl;
	}

	public void returnHome() {
		mainCtrl.showHome();
	}

	public void join() {
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
