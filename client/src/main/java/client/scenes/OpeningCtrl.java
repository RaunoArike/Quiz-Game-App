package client.scenes;

import com.google.inject.Inject;

public class OpeningCtrl  {

	private final MainCtrl mainCtrl;

	@Inject
	public OpeningCtrl(MainCtrl mainCtrl) {
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

	public void connectToServer() {
		mainCtrl.showServerAddress();
	}
}
