package client.scenes;

import client.model.QuestionData;
import client.service.MessageLogicService;
import client.utils.NumberUtils;
import commons.model.Question;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import com.google.inject.Inject;
import javafx.scene.input.KeyEvent;

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
	public ComparisonScreenCtrl(MessageLogicService messageService, MainCtrl mainCtrl) {
		super(messageService, mainCtrl);
	}

	@Override
	public void init() {
		super.init();
		answer.setText("");
		resetError();
		resetCorrectAnswer();
	}

	@Override
	public void setQuestion(QuestionData<Question.ComparisonQuestion> questionData) {
		super.setQuestion(questionData);

		var question = questionData.question();
		var textActivity1 = question.activities().get(0).name();
		var textActivity2 = question.activities().get(1).name();
		var textQuestion = "Instead of " + textActivity1 + " , you can " + textActivity2 + " how many times?";
		setQuestionText(textQuestion);

		ok.setDisable(false);
	}

	public void sendAnswer() {

		var parsedValue = NumberUtils.parseFloatOrNull(answer.getText());
		if (parsedValue != null) {
			messageService.answerQuestion(parsedValue);
			resetError();
			timeStop();
		} else {
			errorMessage.setText("Invalid value");
		}
	}

	public void showAnswer(Number correctAnswer, int scoreIncrement) {
		String message = "The correct answer was: " + correctAnswer + " times. "
						+ "\nYou score " + scoreIncrement + " points.";

		answerMessage.setText(message);

		ok.setDisable(true);
	}

	private void resetError() {
		errorMessage.setText("");
	}

	private void resetCorrectAnswer() {
		answerMessage.setText("");
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getCode()) {
			case ENTER:
				sendAnswer();
				break;
			default:
				break;
		}
	}

}
