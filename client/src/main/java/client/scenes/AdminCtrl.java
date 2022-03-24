package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;

import javax.inject.Inject;
import java.awt.*;

public class AdminCtrl {
	private final ServerUtils serverUtils;
	private final MainCtrl mainCtrl;

	@FXML
	private  List listOfActivities;

	@Inject
	public AdminCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
		this.serverUtils = serverUtils;
		this.mainCtrl = mainCtrl;
		listOfActivities = new List();
	}
	///SHOW, ADD, REMOVE, UPDATE

	///The option to return home
	public void returnHome() {
		mainCtrl.showHome();
	}

	public void keyPressed(KeyEvent keyEvent) {
		switch (keyEvent.getCode()) {
			case ESCAPE: returnHome(); break;
			default: break;
		}
	}
}
