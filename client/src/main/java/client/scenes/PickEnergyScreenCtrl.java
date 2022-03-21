package client.scenes;

import client.service.ServerService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import com.google.inject.Inject;

public class PickEnergyScreenCtrl extends QuestionCtrl {

	@FXML
	private RadioButton optionA;

	@FXML
	private RadioButton optionB;

	@FXML
	private RadioButton optionC;

	@FXML
	private Label optionAtext;

	@FXML
	private Label optionBtext;

	@FXML
	private Label optionCtext;

	@Inject
	public PickEnergyScreenCtrl(ServerService server, MainCtrl mainCtrl) {
		super(server, mainCtrl);
	}

	public void setOptions(String a, String b, String c) {
		this.optionAtext.setText(a);
		this.optionBtext.setText(b);
		this.optionCtext.setText(c);
	}

	public void optionAclicked() {
		server.answerQuestion(0);
	}

	public void optionBclicked() {
		server.answerQuestion(1);
	}

	public void optionCclicked() {
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
	}
}