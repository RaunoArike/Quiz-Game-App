package client.scenes;

import client.service.ServerService;
import com.google.inject.Inject;

public class LeaderboardCtrl {

	private final ServerService server;
	private final MainCtrl mainCtrl;

	@Inject
	public LeaderboardCtrl(ServerService server, MainCtrl mainCtrl) {
		this.server = server;
		this.mainCtrl = mainCtrl;
	}

	// Links to the return button
	public void returnHome() {
		mainCtrl.showHome();
	}
}
