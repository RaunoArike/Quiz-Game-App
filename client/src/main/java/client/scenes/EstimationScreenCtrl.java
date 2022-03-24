package client.scenes;

import client.service.ServerService;
import client.utils.NumberUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import com.google.inject.Inject;

public class EstimationScreenCtrl extends QuestionCtrl {

	@FXML
	private TextField answer;

	@FXML
	private Label answerMessage;

	@FXML
	private Button ok;

	@FXML
	private Label errorMessage;

	@Inject
	public EstimationScreenCtrl(ServerService server, MainCtrl mainCtrl) {
		super(server, mainCtrl);
	}

	@Override
	public void init() {
		super.init();
		resetError();
	}

	public void sendAnswer() {
		Float parsedValue = NumberUtils.parseFloatOrNull(answer.getText());
		if (parsedValue != null) {
			server.answerQuestion(parsedValue);
			resetError();
			timeStop();
		} else {
			errorMessage.setText("Invalid value");
		}
	}

	public void showAnswer(Number correctAnswer, int scoreIncrement) {
		String message = "The correct answer was: " + correctAnswer + " kwH. You score " + scoreIncrement + " points.";
		answerMessage.setText(message);
	}
	public void resetError() {
		errorMessage.setText("");
	}
}
