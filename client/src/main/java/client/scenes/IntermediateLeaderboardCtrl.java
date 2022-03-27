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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class IntermediateLeaderboardCtrl implements Initializable {
	private final ServerService server;

	private final MainCtrl mainCtrl;

	@FXML
	private TableView<LeaderboardEntry> multiLeaderboard;

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

			var rank  = multiLeaderboard.getItems().indexOf(x.getValue()) + 1;

			intermediaryRank = new ReadOnlyObjectWrapper<>(rank);

			return intermediaryRank;
		});
	}

	public void continueGame() {

	}
}
