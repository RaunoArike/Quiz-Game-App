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
		//return to a mainctrl answer method with a specific parameter
		timeStop();
	}

	public void optionBClicked() {
		timeStop();
	}

	public void optionCClicked() {
		timeStop();
	}

	public void useEliminateOption() {
	}

	public void useDoublePoints() {
	}



}