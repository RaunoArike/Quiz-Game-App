package client.scenes;

import client.utils.ServerUtils;
import commons.model.Activity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import javax.inject.Inject;

public class AdminCtrl {
	private final ServerUtils serverUtils;
	private final MainCtrl mainCtrl;


	@FXML
	TableView<Activity> tableOfActivities;
	@FXML
	TableColumn<Activity, String> nameColumn;
	@FXML
	TableColumn<Activity, Float> energyColumn;


	@Inject
	public AdminCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
		this.serverUtils = serverUtils;
		this.mainCtrl = mainCtrl;

		///set up the columns of the table

		if (nameColumn != null) {
			nameColumn.setCellValueFactory(new PropertyValueFactory<Activity, String>("NAME"));
		}
		assert energyColumn != null;
		energyColumn.setCellValueFactory(new PropertyValueFactory<Activity, Float>("ENERGY IN Wh"));

		tableOfActivities.setItems(getActivities());
	}

	public ObservableList<Activity> getActivities() {
		ObservableList<Activity> activities = FXCollections.observableArrayList();
		//populate the list with activities
		activities.add(1, new Activity("ds", "das", 1));
		return activities;
	}

	///The option to return home
	public void returnHome() {
		mainCtrl.showHome();
	}

	public void keyPressed(KeyEvent keyEvent) {
		switch (keyEvent.getCode()) {
			case ESCAPE:
				returnHome();
				break;
			default:
				break;
		}
	}
}
