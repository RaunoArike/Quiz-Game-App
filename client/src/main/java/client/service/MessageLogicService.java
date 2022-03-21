package client.service;

import client.service.ServerService.ServerListener;
import commons.model.Question;
import commons.model.Question.*;
import commons.servermessage.*;
import client.scenes.MainCtrl;
import client.scenes.QuestionTypes;

import com.google.inject.Inject;


public class MessageLogicService implements ServerListener {

	private MainCtrl mainCtrl;
	private ServerService server;
	private int score;
	private QuestionTypes currentType;
	private Number correctAnswer;

	@Inject
	public MessageLogicService(MainCtrl mainCtrl, ServerService server) {
		this.mainCtrl = mainCtrl;
		this.score = 0;
		this.server = server;
		server.registerListener(this);
	}

	/**
	 * Called when new question starts
	 * @param message message with new question details
	 */
	public void onQuestion(QuestionMessage message) {
		int questionNumber = message.getQuestionNumber();
		Question question = message.getQuestion();
		if (question instanceof ComparisonQuestion) {
			ComparisonQuestion q = (ComparisonQuestion) question;
			currentType = QuestionTypes.COMPARISON;
			correctAnswer = q.getCorrectAnswer();
			mainCtrl.showComparisonQuestion(q, questionNumber, score);
		}
		if (question instanceof EstimationQuestion) {
			EstimationQuestion q = (EstimationQuestion) question;
			currentType = QuestionTypes.ESTIMATION;
			correctAnswer = q.getCorrectAnswer();
			mainCtrl.showEstimationQuestion(q, questionNumber, score);
		}
		if (question instanceof MultiChoiceQuestion) {
			MultiChoiceQuestion q = (MultiChoiceQuestion) question;
			currentType = QuestionTypes.MULTI_CHOICE;
			correctAnswer = q.getCorrectAnswer();
			mainCtrl.showMultiChoiceQuestion(q, questionNumber, score);
		}
		if (question instanceof PickEnergyQuestion) {
			PickEnergyQuestion q = (PickEnergyQuestion) question;
			currentType = QuestionTypes.PICK_ENERGY;
			correctAnswer = q.getCorrectAnswer();
			mainCtrl.showPickEnergyQuestion(q, questionNumber, score);
		}
	}

	/**
	 * Called when question ends
	 * @param message message with player's score
	 */
	public void onScore(ScoreMessage message) {
		this.score = message.getTotalScore();
		mainCtrl.showAnswer(currentType, correctAnswer, message.getQuestionScore());
	}

	/**
	 * Called when waiting room state is updated
	 * @param message message with waiting room details
	 */
	public void onWaitingRoomState(WaitingRoomStateMessage message) {

	}

	/**
	 * Called when error occurs at the server side
	 * @param message message with error details
	 */
	public void onError(ErrorMessage message) {

	}
}
