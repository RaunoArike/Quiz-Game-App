package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
public class EstimationScreenCtrl {

	private final ServerUtils server;
	private final MainCtrl mainCtrl;

	@FXML
	private Label questionText;

	@FXML
	private Label score;

	@FXML
	private TextField answer;

	@FXML
	private Button ok;

	@FXML
	private Label timer;

	@FXML
	private Button doublePoints;

	@Inject
	public EstimationScreenCtrl(ServerUtils server, MainCtrl mainCtrl) {
		this.server = server;
		this.mainCtrl = mainCtrl;
	}

	public void setQuestion(String questionText) {
		this.questionText.setText(questionText);
	}

	public void setScore(int score) {
		String scoretext = "Score: " + score;
		this.score.setText(scoretext);
	}

	public void answerEntered() {
	}

	public void timer() {
	}




}
