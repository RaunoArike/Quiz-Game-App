package client.scenes;

import client.model.QuestionData;
import client.service.MessageLogicService;
import commons.model.Question;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import com.google.inject.Inject;

public class MultiChoiceScreenCtrl extends QuestionCtrl<Question.MultiChoiceQuestion> {

	@FXML
	private Button optionA;

	@FXML
	private Button optionB;

	@FXML
	private Button optionC;

	private int selectedAnswer = -1;

	@Inject
	public MultiChoiceScreenCtrl(MessageLogicService messageService, MainCtrl mainCtrl) {
		super(messageService, mainCtrl);
	}

	@Override
	public void init() {
		super.init();
		optionA.setStyle(null);
		optionB.setStyle(null);
		optionC.setStyle(null);
	}

	@Override
	public void setQuestion(QuestionData<Question.MultiChoiceQuestion> questionData) {
		super.setQuestion(questionData);

		var question = questionData.question();
		var textQuestion = "Which of the following takes the most energy?";
		setQuestionText(textQuestion);

		var a = question.activities().get(0).name();
		var b = question.activities().get(1).name();
		var c = question.activities().get(2).name();
		setAnswerOptions(a, b, c);
	}

	private void setAnswerOptions(String a, String b, String c) {
		this.optionA.setText(a);
		this.optionB.setText(b);
		this.optionC.setText(c);

		optionA.setDisable(false);
		optionB.setDisable(false);
		optionC.setDisable(false);
	}

	public void optionAClicked() {
		timeStop();
		selectedAnswer = 0;
		messageService.answerQuestion(0);


	}

	public void optionBClicked() {
		timeStop();
		selectedAnswer = 1;
		messageService.answerQuestion(1);


	}

	public void optionCClicked() {
		timeStop();
		selectedAnswer = 2;
		messageService.answerQuestion(2);


	}

	public void showAnswer(int option) {
		switch (option) {
			case 0:
				optionA.setStyle("-fx-background-color: #00ff7f; ");

				optionA.setDisable(true);
				optionB.setDisable(true);
				optionC.setDisable(true);

				if (selectedAnswer == 1) {
					optionB.setStyle("-fx-background-color: #fd4119; ");
				}
				if (selectedAnswer == 2) {
					optionC.setStyle("-fx-background-color: #fd4119; ");
				}

				break;
			case 1:
				optionB.setStyle("-fx-background-color: #00ff7f; ");

				optionA.setDisable(true);
				optionB.setDisable(true);
				optionC.setDisable(true);

				if (selectedAnswer == 0) {
					optionA.setStyle("-fx-background-color: #fd4119; ");
				} else if (selectedAnswer == 2) {
					optionC.setStyle("-fx-background-color: #fd4119; ");
				}

				break;
			case 2:
				optionC.setStyle("-fx-background-color: #00ff7f; ");

				optionA.setDisable(true);
				optionB.setDisable(true);
				optionC.setDisable(true);

				if (selectedAnswer == 0) {
					optionA.setStyle("-fx-background-color: #fd4119; ");
				} else if (selectedAnswer == 1) {
					optionB.setStyle("-fx-background-color: #fd4119; ");
				}

				break;
		}
		timeStop();
	}

	public void useEliminateOption() {
	}

	public void useDoublePoints() {
	}


}
