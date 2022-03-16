package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class MultiChoiceScreenCtrl {

	private final ServerUtils server;
	private final MainCtrl mainCtrl;

	@FXML
	private Button optionA;

	@FXML
	private Button optionB;

	@FXML
	private Button optionC;

	@FXML
	private TextField questionText;

	@FXML
	private TextField score;

	@FXML
	private Button eliminateOption;

	@FXML
	private Button doublePoints;

	@FXML
	private TextField timer;

	@Inject
	public MultiChoiceScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
		this.server = server;
		this.mainCtrl = mainCtrl;
	}

	public void setQuestionText(String questiontext) {
		this.questionText.clear();
		this.questionText.appendText(questiontext);
	}

	public void setAnswerOptions(String a, String b, String c) {
		this.optionA.setText(a);
		this.optionB.setText(b);
		this.optionC.setText(c);
	}

	public void setScore(int score) {
		String scoretext = "Score: " + score;
		this.score.setText(scoretext);
	}

	public void optionAclicked() {

	}

	public void optionBclicked() {

	}

	public void optionCclicked() {

	}

	public void timer() {
	}

	public void useEliminateOption() {
	}

	public void useDoublePoints() {
	}



}