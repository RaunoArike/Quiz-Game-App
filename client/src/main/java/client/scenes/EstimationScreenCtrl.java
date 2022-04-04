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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;


public class EstimationScreenCtrl extends QuestionCtrl<Question.EstimationQuestion> {

	@FXML
	private TextField answer;

	@FXML
	private Label answerMessage;

	@FXML
	private Button ok;

	@FXML
	private ImageView imageView;

	@FXML
	private Label errorMessage;

	private final int fitWidth = 208;
	private final int fitHeight = 145;

	@Inject
	public EstimationScreenCtrl(MessageLogicService messageService, MainCtrl mainCtrl) {
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
	public void setQuestion(QuestionData<Question.EstimationQuestion> questionData) {
		super.setQuestion(questionData);

		var question = questionData.question();
		var textQuestion = "Estimate the amount of energy it takes to " + question.activity().name();
		setQuestionText(textQuestion);

		String url = question.activity().imageUrl();
		setActivityImages(url);

		ok.setDisable(false);
	}


	public void setActivityImages(String url) {
		Image image =  new Image(url);

		imageView.setFitWidth(fitWidth);
		imageView.setFitHeight(fitHeight);
		imageView.setImage(image);
	}

	public void sendAnswer() {

		Float parsedValue = NumberUtils.parseFloatOrNull(answer.getText());
		if (parsedValue != null) {
			messageService.answerQuestion(parsedValue);
			resetError();
		} else {
			errorMessage.setText("Invalid value");
		}
	}

	public void showAnswer(Number correctAnswer, int scoreIncrement, int numberOfPlayersScored) {
		String message = "The correct answer was: " + correctAnswer + " kwH. "
						+ "\nYou score " + scoreIncrement + " points.";
		if (numberOfPlayersScored != -1) {
			if (numberOfPlayersScored == 1) {
				message += "\n" + numberOfPlayersScored + " player scored on this question.";
			} else {
				message += "\n" + numberOfPlayersScored + " players scored on this question.";
			}
		}
		answerMessage.setText(message);
		ok.setDisable(true);
		disableJokers();
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
