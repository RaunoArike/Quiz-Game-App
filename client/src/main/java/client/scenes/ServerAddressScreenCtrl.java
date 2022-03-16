package client.scenes;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;



public class ServerAddressScreenCtrl {

	private final ServerUtils server;
	private final MainCtrl mainCtrl;

	@Inject
	public ServerAddressScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
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
		//call serverconnect method
		//if false, show error message
		errorMessage.setText("Please enter a valid address: ");
		//if true, proceed to show home
		mainCtrl.showHome();
	}
}
