package client.scenes;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;


public abstract class QuestionCtrl {

	private final ServerUtils server;
	private final MainCtrl mainCtrl;

	@FXML
	private Label questionText;

	@FXML
	private Label score;

	@FXML
	private Button doublePoints;

	@FXML
	private Button eliminateOption;


	@Inject
	public QuestionCtrl(ServerUtils server, MainCtrl mainCtrl) {
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
}
