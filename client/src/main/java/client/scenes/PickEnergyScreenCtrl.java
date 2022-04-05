package client.scenes;

import client.model.QuestionData;
import client.service.MessageLogicService;
import com.google.inject.Inject;
import commons.model.Question;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;


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

	@FXML
	private ImageView imageView;

	@FXML
	private Label scoreMessage;

	private int selectedAnswer = -1;

	private final ToggleGroup toggleGroup = new ToggleGroup();

	private final int fitWidth = 200;
	private final int fitHeight = 150;

	@Inject
	public PickEnergyScreenCtrl(MessageLogicService messageService, MainCtrl mainCtrl) {
		super(messageService, mainCtrl);
	}

	@Override
	public void init() {
		super.init();
		scoreMessage.setText("");
		optionAtext.setStyle(null);
		optionBtext.setStyle(null);
		optionCtext.setStyle(null);

		optionA.setToggleGroup(toggleGroup);
		optionB.setToggleGroup(toggleGroup);
		optionC.setToggleGroup(toggleGroup);

		optionA.setSelected(false);
		optionB.setSelected(false);
		optionC.setSelected(false);
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

		String url = question.activity().imageUrl();
		setActivityImages(url);
	}

	private void setOptions(String a, String b, String c) {
		this.optionAtext.setText(a);
		this.optionBtext.setText(b);
		this.optionCtext.setText(c);

		optionA.setDisable(false);
		optionB.setDisable(false);
		optionC.setDisable(false);
	}

	public void setActivityImages(String url) {
		Image image =  new Image(url);
		imageView.setFitWidth(fitWidth);
		imageView.setFitHeight(fitHeight);
		imageView.setImage(image);
	}

	public void optionAClicked() {
		messageService.answerQuestion(0);
		timeStop();
		selectedAnswer = 0;
	}

	public void optionBClicked() {
		messageService.answerQuestion(1);
		timeStop();
		selectedAnswer = 1;
	}

	public void optionCClicked() {
		messageService.answerQuestion(2);
		timeStop();
		selectedAnswer = 2;
	}

	public void showAnswer(int option, int numberOfPlayersScored) {
		optionA.setDisable(true);
		optionB.setDisable(true);
		optionC.setDisable(true);
		switch (option) {
			case 0:
				optionAtext.setStyle("-fx-background-color: #8dcc4f; ");
				if (selectedAnswer == 1) {
					optionBtext.setStyle("-fx-background-color: #cc4f4f; ");
				} else if (selectedAnswer == 2) {
					optionCtext.setStyle("-fx-background-color: #cc4f4f; ");
				}
				break;

			case 1:
				optionBtext.setStyle("-fx-background-color: #8dcc4f; ");
				if (selectedAnswer == 0) {
					optionAtext.setStyle("-fx-background-color: #cc4f4f; ");
				}
				if (selectedAnswer == 2) {
					optionCtext.setStyle("-fx-background-color: #cc4f4f; ");
				}
				break;

				case 2:
				optionCtext.setStyle("-fx-background-color: #8dcc4f; ");
				if (selectedAnswer == 1) {
					optionBtext.setStyle("-fx-background-color: #cc4f4f; ");
				}
				if (selectedAnswer == 0) {
					optionAtext.setStyle("-fx-background-color: #cc4f4f; ");
				}
				break;
		}
		String message = "";
		if (numberOfPlayersScored != -1) {
			if (numberOfPlayersScored == 1) {
				message += "\n" + numberOfPlayersScored + " player scored on this question.";
			} else {
				message += "\n" + numberOfPlayersScored + " players scored on this question.";
			}
		}
		scoreMessage.setText(message);
		disableJokers();
	}

	@Override
	public void useEliminateOptionJoker() {
		super.useEliminateOptionJoker();

		var correctAnswer = getQuestionData().question().correctAnswer();
		switch (correctAnswer) {
			case 0 -> disableOneOf(optionB, optionC);
			case 1 -> disableOneOf(optionA, optionC);
			case 2 -> disableOneOf(optionA, optionB);
		}
	}

	private void disableOneOf(RadioButton option1, RadioButton option2) {
		var randomBool = new Random().nextBoolean();
		var randomOption = randomBool ? option1 : option2;
		randomOption.setDisable(true);
	}
}
