package client.scenes;

import com.google.inject.Inject;
import client.utils.ServerUtils;

public class OpeningCtrl  {

	private final ServerUtils server;
	private final MainCtrl mainCtrl;

	/**Shared controller class for OpeningScreen and EndingScreen */

	@Inject
	public OpeningCtrl(ServerUtils server, MainCtrl mainCtrl) {
		this.server = server;
		this.mainCtrl = mainCtrl;
	}

	public void goToLeaderboard() {
		mainCtrl.showLeaderboard();
	}

	public void startNewSingleGame() {
		mainCtrl.showUsername();
	}

	public void startNewMultiGame() {
		mainCtrl.showJoinWaitingroom();
	}

	public void returnHome() {
		mainCtrl.showHome();
	}

}
