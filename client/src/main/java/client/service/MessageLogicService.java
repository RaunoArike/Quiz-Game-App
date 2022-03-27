package client.service;

import client.service.ServerService.ServerListener;
import commons.model.Question;
import commons.model.Question.*;
import commons.servermessage.*;
import client.scenes.MainCtrl;
import client.model.QuestionTypes;

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
	@Override
	public void onQuestion(QuestionMessage message) {
		int questionNumber = message.questionNumber();
		Question question = message.question();
		if (question instanceof ComparisonQuestion) {
			ComparisonQuestion q = (ComparisonQuestion) question;
			currentType = QuestionTypes.COMPARISON;
			correctAnswer = q.correctAnswer();
			mainCtrl.showComparisonQuestion(q, questionNumber, score);
		}
		if (question instanceof EstimationQuestion) {
			EstimationQuestion q = (EstimationQuestion) question;
			currentType = QuestionTypes.ESTIMATION;
			correctAnswer = q.correctAnswer();
			mainCtrl.showEstimationQuestion(q, questionNumber, score);
		}
		if (question instanceof MultiChoiceQuestion) {
			MultiChoiceQuestion q = (MultiChoiceQuestion) question;
			currentType = QuestionTypes.MULTI_CHOICE;
			correctAnswer = q.correctAnswer();
			mainCtrl.showMultiChoiceQuestion(q, questionNumber, score);
		}
		if (question instanceof PickEnergyQuestion) {
			PickEnergyQuestion q = (PickEnergyQuestion) question;
			currentType = QuestionTypes.PICK_ENERGY;
			correctAnswer = q.correctAnswer();
			mainCtrl.showPickEnergyQuestion(q, questionNumber, score);
		}
	}

	/**
	 * Called when question ends
	 * @param message message with player's score
	 */
	@Override
	public void onScore(ScoreMessage message) {
		this.score = message.totalScore();
		mainCtrl.showAnswer(currentType, correctAnswer, message.questionScore());
	}

	/**
	 * Called when waiting room state is updated
	 * @param message message with waiting room details
	 */
	@Override
	public void onWaitingRoomState(WaitingRoomStateMessage message) {

	}

	/**
	 * Called when an endOfGame message is received from the server
	 */
	@Override
	public void onEndOfGame() {
		mainCtrl.showEndingScreen(score);
	}

	/**
	 * Called when error occurs at the server side
	 * @param message message with error details
	 */
	@Override
	public void onError(ErrorMessage message) {

	}

}
