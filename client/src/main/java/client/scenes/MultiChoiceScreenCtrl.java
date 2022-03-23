package client.scenes;

import client.service.ServerService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import com.google.inject.Inject;

public class MultiChoiceScreenCtrl extends QuestionCtrl {

	@FXML
	private Button optionA;

	@FXML
	private Button optionB;

	@FXML
	private Button optionC;

	@Inject
	public MultiChoiceScreenCtrl(ServerService server, MainCtrl mainCtrl) {
		super(server, mainCtrl);
	}

	public void setAnswerOptions(String a, String b, String c) {
		this.optionA.setText(a);
		this.optionB.setText(b);
		this.optionC.setText(c);
	}

	public void optionAClicked() {
		server.answerQuestion(0);
		//return to a mainctrl answer method with a specific parameter
		timeStop();
	}

	public void optionBClicked() {
		server.answerQuestion(1);
		timeStop();
	}

	public void optionCClicked() {
		server.answerQuestion(2);
	}

	public void showAnswer(int option) {
		switch (option) {
			case 0:
				optionA.setStyle("-fx-background-color: #00ff7f; ");
				break;
			case 1:
				optionA.setStyle("-fx-background-color: #00ff7f; ");
				break;
			case 2:
				optionA.setStyle("-fx-background-color: #00ff7f; ");
				break;
		}
		timeStop();
	}

	public void useEliminateOption() {
	}

	public void useDoublePoints() {
	}



}