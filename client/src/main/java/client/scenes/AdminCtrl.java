package client.scenes;

import client.utils.ServerUtils;

import javax.inject.Inject;

public class AdminCtrl {
	private final ServerUtils serverUtils;
	private final MainCtrl mainCtrl;

	@Inject
	public AdminCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
		this.serverUtils = serverUtils;
		this.mainCtrl = mainCtrl;
	}
	///The option to return home
	public void returnHome() {
		mainCtrl.showHome();
	}
}
