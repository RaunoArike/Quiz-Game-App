package client.scenes;

import client.utils.ServerUtils;
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
	public MultiChoiceScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
		super(server, mainCtrl);
	}


	public void setAnswerOptions(String a, String b, String c) {
		this.optionA.setText(a);
		this.optionB.setText(b);
		this.optionC.setText(c);
	}

	public void optionAclicked() {
		//return to a mainctrl answer method with a specific parameter
	}

	public void optionBclicked() {

	}

	public void optionCclicked() {

	}

	public void useEliminateOption() {
	}

	public void useDoublePoints() {
	}



}