package client.scenes;
import client.service.ServerServiceImpl;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class ServerAddressScreenCtrl {

	private final ServerServiceImpl server;
	private final MainCtrl mainCtrl;

	@Inject
	public ServerAddressScreenCtrl(ServerServiceImpl server, MainCtrl mainCtrl) {
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
			boolean result = this.server.connectToServer(url);
			if (result) {
				this.serverAddress.clear();
				mainCtrl.showHome();
			} else {
				errorMessage.setText("Please enter a valid address: ");
			}
		} else {
			errorMessage.setText("Please enter a valid address: ");
		}
	}

	public void clear() {
		this.serverAddress.clear();
		this.errorMessage.setText("");
	}
}
