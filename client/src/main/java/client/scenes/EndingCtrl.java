package client.scenes;

import com.google.inject.Inject;
import client.utils.ServerUtils;

public class EndingCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;


    @Inject
    public EndingCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void goToLeaderboard() {
        mainCtrl.showLeaderboard();
    }

    public void returnHome() {
        mainCtrl.showHome();
    }
    
}
