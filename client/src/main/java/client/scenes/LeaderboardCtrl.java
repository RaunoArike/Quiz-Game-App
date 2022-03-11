package client.scenes;

import com.google.inject.Inject;
import client.utils.ServerUtils;

public class LeaderboardCtrl {

	private final ServerUtils server;
	private final MainCtrl mainCtrl;


	@Inject
	public LeaderboardCtrl(ServerUtils server, MainCtrl mainCtrl) {
		this.server = server;
		this.mainCtrl = mainCtrl;
	}

	public void returnHome() {
		mainCtrl.showHome();
	}
}