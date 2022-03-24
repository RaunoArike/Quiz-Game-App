package client.scenes;

import client.model.QuestionData;
import client.service.ServerService;
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

	@Inject
	public MultiChoiceScreenCtrl(ServerService server, MainCtrl mainCtrl) {
		super(server, mainCtrl);
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

		server.answerQuestion(0);
		//return to a mainctrl answer method with a specific parameter

	}

	public void optionBClicked() {
		timeStop();

		server.answerQuestion(1);
	}

	public void optionCClicked() {
		timeStop();

		server.answerQuestion(2);
	}

	public void showAnswer(int option) {
		switch (option) {
			case 0:
				optionA.setStyle("-fx-background-color: #00ff7f; ");
				break;
			case 1:
				optionB.setStyle("-fx-background-color: #00ff7f; ");
				break;
			case 2:
				optionC.setStyle("-fx-background-color: #00ff7f; ");
				break;
		}
		timeStop();
	}

	public void useEliminateOption() {
	}

	public void useDoublePoints() {
	}



}
