package client.scenes;
import client.service.ServerService;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;


public class ServerAddressScreenCtrl {

	private final ServerService server;
	private final MainCtrl mainCtrl;

	@Inject
	public ServerAddressScreenCtrl(ServerService server, MainCtrl mainCtrl) {
		this.server = server;
		this.mainCtrl = mainCtrl;
	}

	@FXML
	private Label errorMessage;

	@FXML
	private TextField serverAddress;

	@FXML
	private Button ok;

	public void ok() {
		String url = this.serverAddress.getText();
		if (url != null && !url.isEmpty()) {
			boolean result = server.connectToServer(url);
			if (result) {
				serverAddress.clear();
				mainCtrl.showHome();
			} else {
				errorMessage.setText("Please enter a valid address: ");
			}
		} else {
			errorMessage.setText("Please enter a valid address: ");
		}
	}

	public void clear() {
		serverAddress.clear();
		errorMessage.setText("");
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getCode()) {
			case ENTER:
				ok();
				break;
			case ESCAPE:
				clear();
				break;
			default:
				break;
		}
	}

}