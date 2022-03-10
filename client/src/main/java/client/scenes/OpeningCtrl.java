package client.scenes;

import com.google.inject.Inject;
import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class OpeningCtrl  {

	private final ServerUtils server;
	private final MainCtrl mainCtrl;


	@Inject
	public OpeningCtrl(ServerUtils server, MainCtrl mainCtrl) {
		this.server = server;
		this.mainCtrl = mainCtrl;
	}

	public void goToLeaderboard () {
		mainCtrl.showLeaderboard();
	}

	public void startNewSingleGame () {

	}

	public void startNewMultiGame () {

	}

}
