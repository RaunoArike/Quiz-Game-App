package client.scenes;

import client.service.MessageLogicService;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

public class WaitingroomCtrl extends AbstractCtrl {
	private final MessageLogicService messageService;
	private final MainCtrl mainCtrl;

	@FXML
	private Label noOfPeopleText;

	@Inject
	public WaitingroomCtrl(MessageLogicService messageService, MainCtrl mainCtrl) {
		this.messageService = messageService;
		this.mainCtrl = mainCtrl;
	}

	// Links to the cancel button
	public void returnHome() {
		mainCtrl.showHome();
	}

	// Links to the start button
	public void startGame() {
		messageService.startMultiGame();
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
