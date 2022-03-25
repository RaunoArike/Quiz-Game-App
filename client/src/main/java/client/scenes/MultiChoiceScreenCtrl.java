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

	private boolean clickedA = false;
	private boolean clickedB = false;
	private boolean clickedC = false;

	@Inject
	public MultiChoiceScreenCtrl(MessageLogicService messageService, MainCtrl mainCtrl) {
		super(messageService, mainCtrl);
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
	}

	public void optionAClicked() {
		timeStop();
		clickedA = true;
		messageService.answerQuestion(0);
		//return to a mainctrl answer method with a specific parameter

	}

	public void optionBClicked() {
		timeStop();
		clickedB = true;
		messageService.answerQuestion(1);
	}

	public void optionCClicked() {
		timeStop();
		clickedC = true;
		messageService.answerQuestion(2);
	}

	public void showAnswer(int option) {
		switch (option) {
			case 0:
				optionA.setStyle("-fx-background-color: #00ff7f; ");


				if (clickedB) {
					optionB.setStyle("-fx-background-color: #fd4119; ");
					clickedB = false;
				}
				if (clickedC) {
					optionC.setStyle("-fx-background-color: #fd4119; ");
					clickedC = false;
				}

				break;
			case 1:
				optionB.setStyle("-fx-background-color: #00ff7f; ");


				if (clickedA) {
					optionA.setStyle("-fx-background-color: #fd4119; ");
					clickedA = false;
				} else if (clickedC) {
					optionC.setStyle("-fx-background-color: #fd4119; ");
					clickedC = false;
					}

				break;
			case 2:
				optionC.setStyle("-fx-background-color: #00ff7f; ");


				if (clickedA) {
					optionA.setStyle("-fx-background-color: #fd4119; ");
					clickedA = false;
				} else if (clickedB) {
					optionB.setStyle("-fx-background-color: #fd4119; ");
					clickedB = false;
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
