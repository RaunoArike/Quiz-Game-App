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
		this.usernameCtrl.clearField();
		primaryStage.setScene(username);
	}

	public void showJoinWaitingroom() {
		primaryStage.setTitle("Join a waiting room");
		primaryStage.setScene(joinWaitingroom);
	}

	public void showServerAddress() {
		primaryStage.setTitle("Join a server");
		serverAddressScreenCtrl.clear();
		primaryStage.setScene(serverAddress);
	}

	public void showQuestion() {
		//called from server-comm with parameters indicating type of question and the question itself
		//based on question type, show one of the 4 screens
		//set appropriate score, question text, option texts
		primaryStage.setTitle("Quiz in progress");
		primaryStage.setScene(comparisonScreen);
	}

	public void sendAnswer() {
		//called with parameters that indicate type of question, answer (option or number)
		//additionally time taken
		//should in turn pass this on to server-comm
	}

}
