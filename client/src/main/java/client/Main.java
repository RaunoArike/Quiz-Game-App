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
package client;

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;

import client.scenes.*;
import client.service.MessageLogicService;
import com.google.inject.Injector;

// import client.scenes.AddQuoteCtrl;
// import client.scenes.QuoteOverviewCtrl;
import client.scenes.JoinWaitingroomCtrl;
import client.scenes.LeaderboardCtrl;
import client.scenes.OpeningCtrl;
import client.scenes.ServerAddressScreenCtrl;
import client.scenes.UsernameCtrl;
import client.scenes.ComparisonScreenCtrl;
import client.scenes.EndingScreenCtrl;
import client.scenes.EstimationScreenCtrl;
import client.scenes.MultiChoiceScreenCtrl;
import client.scenes.PickEnergyScreenCtrl;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	private static final Injector INJECTOR = createInjector(new MyModule());
	private static final MyFXML FXML = new MyFXML(INJECTOR);

	public static void main(String[] args) throws URISyntaxException, IOException {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws IOException {

		var leaderboard = FXML.load(LeaderboardCtrl.class, "client", "scenes", "Leaderboard.fxml");


		var home = FXML.load(OpeningCtrl.class, "client", "scenes", "OpeningScreen.fxml");


		var username = FXML.load(UsernameCtrl.class, "client", "scenes", "UsernameScreen.fxml");


		var joinWaitingroom = FXML.load(JoinWaitingroomCtrl.class, "client", "scenes", "JoinWaitingroomScreen.fxml");
		var ending = FXML.load(EndingScreenCtrl.class, "client", "scenes", "EndingScreen.fxml");
		var serverAddress = FXML.load(ServerAddressScreenCtrl.class, "client", "scenes", "ServerAddressScreen.fxml");


		var comparisonScreen = FXML.load(ComparisonScreenCtrl.class, "client", "scenes", "ComparisonScreen.fxml");


		var estimationScreen = FXML.load(EstimationScreenCtrl.class, "client", "scenes", "EstimationScreen.fxml");


		var multiChoiceScreen = FXML.load(MultiChoiceScreenCtrl.class, "client", "scenes", "MultiChoiceScreen.fxml");


		var pickEnergyScreen = FXML.load(PickEnergyScreenCtrl.class, "client", "scenes", "PickEnergyScreen.fxml");


		var adminScreen = FXML.load(AdminCtrl.class, "client", "scenes", "AdminScreen.fxml");

		var mainCtrl = INJECTOR.getInstance(MainCtrl.class);

		mainCtrl.initialize(primaryStage, leaderboard, home, username, joinWaitingroom, serverAddress,
		comparisonScreen, estimationScreen, multiChoiceScreen, pickEnergyScreen, ending);

		INJECTOR.getInstance(MessageLogicService.class);
	}
}
