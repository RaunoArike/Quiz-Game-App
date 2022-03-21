package client.scenes;

import client.service.ServerService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import com.google.inject.Inject;


public class EstimationScreenCtrl extends QuestionCtrl {

	@FXML
	private TextField answer;

	@FXML
	private Button ok;

	@FXML
	private Label answerMessage;

	@Inject
	public EstimationScreenCtrl(ServerService server, MainCtrl mainCtrl) {
		super(server, mainCtrl);
	}

	public void answerEntered() {
		mainCtrl.sendAnswer(QuestionTypes.ESTIMATION, Float.parseFloat(answer.getText()));
		//TO DO - make sure the input is an integer that can be parsed, show error otherwise
		//ie. catch numberformatexception
	}

	public void showAnswer(float correctAnswer, int scoreIncrement) {
		String message = "The correct answer was: " + correctAnswer + "kwH . You score " + scoreIncrement + " points.";
		answerMessage.setText(message);
	}

}
