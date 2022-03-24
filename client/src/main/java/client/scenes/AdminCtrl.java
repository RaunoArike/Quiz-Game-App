package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;

import javax.inject.Inject;
import java.awt.*;

import static javafx.scene.input.KeyCode.ENTER;

public class AdminCtrl {
	private final ServerUtils serverUtils;
	private final MainCtrl mainCtrl;









	@FXML
	private  List listOfQuestions;








	@Inject
	public AdminCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
		this.serverUtils = serverUtils;
		this.mainCtrl = mainCtrl;
		listOfQuestions = new List();
	}





	///The option to return home
	public void returnHome() {
		mainCtrl.showHome();
	}

	public void keyPressed(KeyEvent keyEvent) {
		switch (keyEvent.getCode()) {
			case ENTER: break;
			case ESCAPE: break;
			default: break;
		}
	}







}
