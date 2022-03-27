package client.scenes;

import client.model.QuestionData;
import client.service.MessageLogicService;
import commons.model.Question;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import com.google.inject.Inject;

public class PickEnergyScreenCtrl extends QuestionCtrl<Question.PickEnergyQuestion> {

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

	private boolean clickedA = false;
	private boolean clickedB = false;
	private boolean clickedC = false;

	@Inject
	public PickEnergyScreenCtrl(MessageLogicService messageService, MainCtrl mainCtrl) {
		super(messageService, mainCtrl);
	}

	@Override
	public void setQuestion(QuestionData<Question.PickEnergyQuestion> questionData) {
		super.setQuestion(questionData);

		var question = questionData.question();
		var textActivity = question.activity().name();
		var textQuestion = "How much energy does " + textActivity + " take?";
		setQuestionText(textQuestion);

		var a = question.answerOptions().get(0).toString();
		var b = question.answerOptions().get(1).toString();
		var c = question.answerOptions().get(2).toString();
		setOptions(a, b, c);
	}

	private void setOptions(String a, String b, String c) {
		this.optionAtext.setText(a);
		this.optionBtext.setText(b);
		this.optionCtext.setText(c);
	}

	public void optionAclicked() {
		messageService.answerQuestion(0);
		timeStop();

		clickedA = true;
	}

	public void optionBclicked() {
		messageService.answerQuestion(1);
		timeStop();

		clickedB = true;
	}

	public void optionCclicked() {
		messageService.answerQuestion(2);
		timeStop();

		clickedC = true;
	}

	public void showAnswer(int option) {
		switch (option) {
			case 0:
				optionA.setStyle("-fx-background-color: #00c203; ");


				if (clickedB) {
					optionB.setStyle("-fx-background-color: #fd4119; ");
					clickedB = false;
				} else if (clickedC) {
					optionC.setStyle("-fx-background-color: #fd4119; ");
					clickedC = false;
				}

				break;
			case 1:
				optionB.setStyle("-fx-background-color: #00c203; ");


				if (clickedA) {
					optionA.setStyle("-fx-background-color: #fd4119; ");
					clickedA = false;
				}
				if (clickedC) {
					optionC.setStyle("-fx-background-color: #fd4119; ");
					clickedC = false;
				}


				break;
			case 2:
				optionC.setStyle("-fx-background-color: #00c203; ");


				if (clickedB) {
					optionB.setStyle("-fx-background-color: #fd4119; ");
					clickedB = false;
				}
				if (clickedA) {
					optionA.setStyle("-fx-background-color: #fd4119; ");
					clickedA = false;
				}

				break;
		}
	}
}
