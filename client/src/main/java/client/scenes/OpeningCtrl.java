package client.scenes;

import client.service.ServerService;
import com.google.inject.Inject;

public class OpeningCtrl {

	private final ServerService server;
	private final MainCtrl mainCtrl;

	/**Shared controller class for OpeningScreen and EndingScreen */

	@Inject
	public OpeningCtrl(ServerService server, MainCtrl mainCtrl) {
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

	public void connectToServer() {
		mainCtrl.showServerAddress();
	}
}
