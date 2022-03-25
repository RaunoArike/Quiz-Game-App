package client.scenes;

import client.service.MessageLogicService;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

public class JoinWaitingroomCtrl extends AbstractCtrl {
	private final MessageLogicService messageService;
	private final MainCtrl mainCtrl;

	@FXML
	private TextField username;

	@FXML
	private Label errorMessage;

	@Inject
	public JoinWaitingroomCtrl(MessageLogicService messageService, MainCtrl mainCtrl) {
		this.messageService = messageService;
		this.mainCtrl = mainCtrl;
	}

	@Override
	public void init() {
		super.init();
		username.clear();
		errorMessage.setText("");
	}

	//Links to the cancel button
	public void returnHome() {
		mainCtrl.showHome();
	}

	//Links to the join button
	public void join() {
		String username = this.username.getText();
		if (username != null && !username.isEmpty()) {
			messageService.joinWaitingRoom(username);
			errorMessage.setText("");
		} else {
			errorMessage.setText("Please enter a valid username: ");
		}
		this.username.clear();
	}

	/**
	 * Adds key functionality to the textfields - enter to trigger join,
	 * escape to return to home screen
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
