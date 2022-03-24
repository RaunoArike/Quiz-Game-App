package client.scenes;

import client.model.QuestionData;
import client.service.ServerService;
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

	@Inject
	public PickEnergyScreenCtrl(ServerService server, MainCtrl mainCtrl) {
		super(server, mainCtrl);
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
		server.answerQuestion(0);
		timeStop();
	}

	public void optionBclicked() {
		server.answerQuestion(1);
		timeStop();
	}

	public void optionCclicked() {
		server.answerQuestion(2);
		timeStop();
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
	}
}
