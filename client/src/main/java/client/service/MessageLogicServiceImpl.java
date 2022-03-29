package client.service;

import client.model.GameType;
import client.model.QuestionData;
import client.service.ServerService.ServerListener;
import commons.model.Question;
import commons.model.Question.*;
import commons.servermessage.*;
import client.scenes.MainCtrl;
import client.model.QuestionTypes;

import com.google.inject.Inject;


public class MessageLogicServiceImpl implements MessageLogicService, ServerListener {

	private final MainCtrl mainCtrl;
	private final ServerService server;

	private GameType gameType;
	private int score;
	private QuestionTypes currentType;
	private Number correctAnswer;


	@Inject
	public MessageLogicServiceImpl(MainCtrl mainCtrl, ServerService server) {
		this.mainCtrl = mainCtrl;
		this.server = server;

		this.score = 0;

		server.registerListener(this);
	}

	@Override
	public void startSingleGame(String username) {
		server.startSingleGame(username);
		gameType = GameType.SINGLE;
	}

	@Override
	public void joinWaitingRoom(String username) {
		server.joinWaitingRoom(username);
		gameType = GameType.MULTI;
	}

	@Override
	public void startMultiGame() {
		server.startMultiGame();
	}

	@Override
	public void answerQuestion(Number answer) {
		server.answerQuestion(answer);
	}

	/**
	 * Called when new question starts
	 * @param message message with new question details
	 */
	@Override
	public void onQuestion(QuestionMessage message) {
		int questionNumber = message.questionNumber();
		Question question = message.question();
		if (question instanceof ComparisonQuestion q) {
			currentType = QuestionTypes.COMPARISON;
			correctAnswer = q.correctAnswer();

			var questionData = new QuestionData<>(q, questionNumber, score, gameType);
			mainCtrl.showComparisonQuestion(questionData);
		}
		if (question instanceof EstimationQuestion q) {
			currentType = QuestionTypes.ESTIMATION;
			correctAnswer = q.correctAnswer();

			var questionData = new QuestionData<>(q, questionNumber, score, gameType);
			mainCtrl.showEstimationQuestion(questionData);
		}
		if (question instanceof MultiChoiceQuestion q) {
			currentType = QuestionTypes.MULTI_CHOICE;
			correctAnswer = q.correctAnswer();

			var questionData = new QuestionData<>(q, questionNumber, score, gameType);
			mainCtrl.showMultiChoiceQuestion(questionData);
		}
		if (question instanceof PickEnergyQuestion q) {
			currentType = QuestionTypes.PICK_ENERGY;
			correctAnswer = q.correctAnswer();

			var questionData = new QuestionData<>(q, questionNumber, score, gameType);
			mainCtrl.showPickEnergyQuestion(questionData);
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
		mainCtrl.showWaitingroom(message.noOfPeopleInRoom());
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
		if (message.errorType() == ErrorMessage.Type.USERNAME_BUSY) {
			mainCtrl.showUsernameBusyError();
		}
	}

	/**
	 * Called to get the intermediate leaderboard
	 *
	 * @param intermediateLeaderboardMessage the message of the intermediate leaderboard
	 */
	@Override
	public void onIntermediateLeaderboard(IntermediateLeaderboardMessage intermediateLeaderboardMessage) {
		mainCtrl.showIntermediateleaderboard();
	}

}
