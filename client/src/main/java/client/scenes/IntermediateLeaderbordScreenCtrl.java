package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class IntermediateLeaderbordScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public IntermediateLeaderbordScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
}

