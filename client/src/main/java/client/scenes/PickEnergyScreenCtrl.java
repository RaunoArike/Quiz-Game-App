package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;


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


	public PickEnergyScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
		super(server, mainCtrl);
	}

	public void setOptions(String a, String b, String c) {
		this.optionAtext.setText(a);
		this.optionBtext.setText(b);
		this.optionCtext.setText(c);
	}

	public void optionAclicked() {
	}

	public void optionBclicked() {
	}

	public void optionCclicked() {
	}
}