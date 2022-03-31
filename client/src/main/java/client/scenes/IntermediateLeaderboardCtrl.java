package client.scenes;

import client.service.ServerService;
import com.google.inject.Inject;
import commons.model.LeaderboardEntry;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
public class IntermediateLeaderboardCtrl extends AbstractCtrl implements Initializable {
	private final ServerService server;
	private final MainCtrl mainCtrl;

	private static final int TIME_PASSED = 3000;
	@FXML
	private Label state;

	@FXML
	private TableView<LeaderboardEntry> intermediaryLeaderboard;

	@FXML
	private TableColumn<LeaderboardEntry, String> colUsername;

	@FXML
	private TableColumn<LeaderboardEntry, Number> colScore;

	@FXML
	private TableColumn<LeaderboardEntry, Number> colRanking;

	private ObservableList<LeaderboardEntry> data;

	@Inject
	public IntermediateLeaderboardCtrl(ServerService server, MainCtrl mainCtrl) {
		this.server = server;
		this.mainCtrl = mainCtrl;

		intermediaryLeaderboard = new TableView<>();
		colUsername = new TableColumn<>();
		colScore = new TableColumn<>();
		colRanking = new TableColumn<>();

	}

	/**
	 * Called to initialize a controller after its root element has been
	 * completely processed.
	 *
	 * @param location  The location used to resolve relative paths for the root object, or
	 *                  {@code null} if the location is not known.
	 * @param resources The resources used to localize the root object, or {@code null} if
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		colUsername.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().name()));

		colScore.setCellValueFactory(x -> new SimpleIntegerProperty(x.getValue().score()));

		colRanking.setCellValueFactory(x -> {
			ReadOnlyObjectWrapper<Number> intermediaryRank;

			var rank  = intermediaryLeaderboard.getItems().indexOf(x.getValue()) + 1;

			intermediaryRank = new ReadOnlyObjectWrapper<>(rank);

			return intermediaryRank;
		});
	}

	public void continueGame() {
		Timer timer = new Timer();

		timer.schedule(new TimerTask() {

			/**
			 * Wait for 3 seconds for the leaderboard to be displayed
			 */
			public void run() {

				Node current = new Node() {
					/**
					 * Convenience method for setting a single Object property that can be
					 * retrieved at a later date. This is functionally equivalent to calling
					 * the getProperties().put(Object key, Object value) method. This can later
					 * be retrieved by calling {@link Node#getUserData()}.
					 *
					 * @param value The value to be stored - this can later be retrieved by calling
					 *              {@link Node#getUserData()}.
					 */

					@Override
					public void setUserData(Object value) {
						super.setUserData(value);
					}
				};

				current.setUserData(new String("WAIT FOR 3 SECONDS"));

			}
		}, TIME_PASSED);
		mainCtrl.showIntermediateLeaderboard();
	}



	public void keyPressed(KeyEvent keyEvent) {
		switch (keyEvent.getCode()) {
			case ESCAPE:
				continueGame();
				break;
			case ENTER:
				continueGame();
			default:
				break;
		}
	}
}
