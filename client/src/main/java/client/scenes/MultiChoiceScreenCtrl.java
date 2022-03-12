package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class MultiChoiceScreenCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public MultiChoiceScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }
}