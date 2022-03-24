package client.scenes;

import client.service.ServerService;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

public class WaitingroomCtrl extends AbstractCtrl {
	private final ServerService server;
	private final MainCtrl mainCtrl;

	@FXML
	private Label noOfPeopleText;

	@Inject
	public WaitingroomCtrl(ServerService server, MainCtrl mainCtrl) {
		this.server = server;
		this.mainCtrl = mainCtrl;
	}

	// Links to the cancel button
	public void returnHome() {
		mainCtrl.showHome();
	}

	// Links to the start button
	public void startGame() {
		server.startMultiGame();
	}

	public void updateWaitingroomState(int noOfPeople) {
		noOfPeopleText.setText("Number of players in the waiting room: " + noOfPeople);
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getCode()) {
			case ESCAPE:
				returnHome();
				break;
			default:
				break;
		}
	}
}
