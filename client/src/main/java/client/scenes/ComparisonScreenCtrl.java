package client.scenes;

import client.service.ServerService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import com.google.inject.Inject;

public class ComparisonScreenCtrl extends QuestionCtrl {

	@FXML
	private TextField answer;

	@Inject
	public ComparisonScreenCtrl(ServerService server, MainCtrl mainCtrl) {
		super(server, mainCtrl);
	}

	public void sendAnswer() {
		timeStop();
	}
}