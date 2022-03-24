package client.scenes;

import client.model.QuestionData;
import client.service.ServerService;
import client.utils.NumberUtils;
import commons.model.Question;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import com.google.inject.Inject;

public class ComparisonScreenCtrl extends QuestionCtrl<Question.ComparisonQuestion> {

	@FXML
	private TextField answer;

	@FXML
	private Label answerMessage;

	@FXML
	private Button ok;

	@FXML
	private Label errorMessage;

	@Inject
	public ComparisonScreenCtrl(ServerService server, MainCtrl mainCtrl) {
		super(server, mainCtrl);
	}

	@Override
	public void init() {
		super.init();
		resetError();
	}

	@Override
	public void setQuestion(QuestionData<Question.ComparisonQuestion> questionData) {
		super.setQuestion(questionData);

		var question = questionData.question();
		var textActivity1 = question.activities().get(0).name();
		var textActivity2 = question.activities().get(1).name();
		var textQuestion = "Instead of " + textActivity1 + " , you can " + textActivity2 + " how many times?";
		setQuestionText(textQuestion);
	}

	public void sendAnswer() {
		var parsedValue = NumberUtils.parseFloatOrNull(answer.getText());
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
