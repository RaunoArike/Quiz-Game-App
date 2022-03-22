/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import commons.model.Question.ComparisonQuestion;
import commons.model.Question.EstimationQuestion;
import commons.model.Question.MultiChoiceQuestion;
import commons.model.Question.PickEnergyQuestion;


//import client.service.MessageLogicService;

public class MainCtrl {

	private Stage primaryStage;

	private LeaderboardCtrl leaderboardCtrl;
	private Scene leaderboard;

	private OpeningCtrl openingCtrl;
	private Scene home;

	private UsernameCtrl usernameCtrl;
	private Scene username;

	private JoinWaitingroomCtrl joinWaitingroomCtrl;
	private Scene joinWaitingroom;

	private ServerAddressScreenCtrl serverAddressScreenCtrl;
	private Scene serverAddress;

	private ComparisonScreenCtrl comparisonScreenCtrl;
	private Scene comparisonScreen;

	private EstimationScreenCtrl estimationScreenCtrl;
	private Scene estimationScreen;

	private MultiChoiceScreenCtrl multiChoiceScreenCtrl;
	private Scene multiChoiceScreen;

	private PickEnergyScreenCtrl pickEnergyScreenCtrl;
	private Scene pickEnergyScreen;

	public void initialize(Stage primaryStage, Pair<LeaderboardCtrl, Parent> leaderboardCtrl,
	Pair<OpeningCtrl, Parent> openingCtrl,
	Pair<UsernameCtrl, Parent> usernameCtrl,
	Pair<JoinWaitingroomCtrl, Parent> joinWaitingroomCtrl,
	Pair<ServerAddressScreenCtrl, Parent> serverAddressCtrl,
	Pair<ComparisonScreenCtrl, Parent> comparsionScreenCtrl,
	Pair<EstimationScreenCtrl, Parent> estimationScreenCtrl,
	Pair<MultiChoiceScreenCtrl, Parent> multiChoiceScreenCtrl,
	Pair<PickEnergyScreenCtrl, Parent> pickEnergyScreenCtrl) {

		this.primaryStage = primaryStage;

		this.leaderboardCtrl = leaderboardCtrl.getKey();
		this.leaderboard = new Scene(leaderboardCtrl.getValue());

		this.openingCtrl = openingCtrl.getKey();
		this.home = new Scene(openingCtrl.getValue());

		this.usernameCtrl = usernameCtrl.getKey();
		this.username = new Scene(usernameCtrl.getValue());

		this.joinWaitingroomCtrl = joinWaitingroomCtrl.getKey();
		this.joinWaitingroom = new Scene(joinWaitingroomCtrl.getValue());

		this.serverAddressScreenCtrl = serverAddressCtrl.getKey();
		this.serverAddress = new Scene(serverAddressCtrl.getValue());

		this.comparisonScreenCtrl = comparsionScreenCtrl.getKey();
		this.comparisonScreen = new Scene(comparsionScreenCtrl.getValue());

		this.estimationScreenCtrl = estimationScreenCtrl.getKey();
		this.estimationScreen = new Scene(estimationScreenCtrl.getValue());

		this.multiChoiceScreenCtrl = multiChoiceScreenCtrl.getKey();
		this.multiChoiceScreen = new Scene(multiChoiceScreenCtrl.getValue());

		this.pickEnergyScreenCtrl = pickEnergyScreenCtrl.getKey();
		this.pickEnergyScreen = new Scene(pickEnergyScreenCtrl.getValue());

		showServerAddress();
		primaryStage.show();
	}


	public void showLeaderboard() {
		primaryStage.setTitle("All-time Leaderboard");
		primaryStage.setScene(leaderboard);
	}

	public void showHome() {
		primaryStage.setTitle("Quizz: home");
		primaryStage.setScene(home);
	}

	public void showUsername() {
		primaryStage.setTitle("Enter username");
		username.setOnKeyPressed(e -> usernameCtrl.keyPressed(e));
		this.usernameCtrl.clearField();
		primaryStage.setScene(username);
	}

	public void showJoinWaitingroom() {
		primaryStage.setTitle("Join a waiting room");
		primaryStage.setScene(joinWaitingroom);
	}

	public void showServerAddress() {
		primaryStage.setTitle("Join a server");
		serverAddress.setOnKeyPressed(e -> serverAddressScreenCtrl.keyPressed(e));
		serverAddressScreenCtrl.clear();
		primaryStage.setScene(serverAddress);
	}

	public void showComparisonQuestion(ComparisonQuestion q, int questionNumber, int score) {
		String textActivity1 = q.activities().get(0).name();
		String textActivity2 = q.activities().get(1).name();
		String textQuestion = "Instead of " + textActivity1 + " , you can " + textActivity2 + " how many times?";
		this.comparisonScreenCtrl.setQuestion(textQuestion);
		this.comparisonScreenCtrl.setScore(score);
		questionNumber++;
		primaryStage.setTitle("Question " + questionNumber + " of 20");
		primaryStage.setScene(comparisonScreen);
	}

	public void showEstimationQuestion(EstimationQuestion q, int questionNumber, int score) {
		String textQuestion = "Estimate the amount of energy it takes to " + q.activity().name();
		this.estimationScreenCtrl.setQuestion(textQuestion);
		this.comparisonScreenCtrl.setScore(score);
		questionNumber++;
		primaryStage.setTitle("Question " + questionNumber + " of 20");
		primaryStage.setScene(estimationScreen);
	}

	public void showMultiChoiceQuestion(MultiChoiceQuestion q, int questionNumber, int score) {
		String textQuestion = "Which of the following takes the most energy?";
		this.multiChoiceScreenCtrl.setQuestion(textQuestion);
		String a = q.activities().get(0).name();
		String b = q.activities().get(1).name();
		String c = q.activities().get(2).name();
		this.multiChoiceScreenCtrl.setAnswerOptions(a, b, c);
		this.multiChoiceScreenCtrl.setScore(score);
		questionNumber++;
		primaryStage.setTitle("Question " + questionNumber + " of 20");
		primaryStage.setScene(multiChoiceScreen);
	}

	public void showPickEnergyQuestion(PickEnergyQuestion q, int questionNumber, int score) {
		String textActivity = q.activity().name();
		String textQuestion = "How much energy does " + textActivity + " take?";
		this.pickEnergyScreenCtrl.setQuestion(textQuestion);
		String a = q.answerOptions().get(0).toString();
		String b = q.answerOptions().get(1).toString();
		String c = q.answerOptions().get(2).toString();
		this.pickEnergyScreenCtrl.setOptions(a, b, c);
		this.pickEnergyScreenCtrl.setScore(score);
		questionNumber++;
		primaryStage.setTitle("Question " + questionNumber + " of 20");
		primaryStage.setScene(pickEnergyScreen);
	}

	public void showAnswer(QuestionTypes type, Number correctAnswer, int scoreIncrement) {
		if (type == QuestionTypes.COMPARISON) {
			this.comparisonScreenCtrl.showAnswer(correctAnswer, scoreIncrement);
		}
		if (type == QuestionTypes.ESTIMATION) {
			this.estimationScreenCtrl.showAnswer(correctAnswer, scoreIncrement);
		}
		if (type == QuestionTypes.MULTI_CHOICE) {
			this.multiChoiceScreenCtrl.showAnswer((int) correctAnswer);
		}
		if (type == QuestionTypes.PICK_ENERGY) {
			this.pickEnergyScreenCtrl.showAnswer((int) correctAnswer);
		}
	}

}
