package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import com.google.inject.Inject;


public class EstimationScreenCtrl extends QuestionCtrl {

	@FXML
	private TextField answer;

	@FXML
	private Button ok;

	@Inject
	public EstimationScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
		super(server, mainCtrl);
	}

	public void answerEntered() {
	}

}
