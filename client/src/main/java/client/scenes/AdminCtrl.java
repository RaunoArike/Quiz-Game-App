package client.scenes;

import client.service.ServerService;


import com.google.inject.Inject;
import commons.model.Activity;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;



public class AdminCtrl extends AbstractCtrl implements Initializable {
		private final ServerService serverServicer;

		private final MainCtrl mainCtrl;

		public TextField nameTextField;

		public TextField energyTextField;

		public TextField urlTextField;

		public List<Activity> listActivities;

		@FXML
		private TableView<Activity> table;

		@FXML
		private TableColumn<Activity, String> columnOne;
		@FXML
		private TableColumn<Activity, Number> columnTwo;

		@Inject
		public AdminCtrl(ServerService serverService, MainCtrl mainCtrl) {
			this.serverServicer = serverService;
			this.mainCtrl = mainCtrl;
			listActivities = new ArrayList<>();
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




		@Override
		public void initialize(URL location, ResourceBundle resources) {

			columnOne.setCellValueFactory(x -> new SimpleStringProperty(x
					.getValue()
					.name()));

			columnTwo.setCellValueFactory(x -> new SimpleFloatProperty(x
					.getValue()
					.energyInWh()));


		}

		public void addButton() {
			Activity activity = new Activity(nameTextField.getText(),
					urlTextField.getText(),
					(float) Double.parseDouble(energyTextField.getText()));

			table.getItems().add(activity);

			nameTextField.clear();

			energyTextField.clear();

			urlTextField.clear();
		}

		public void updateButton() {
			Activity activity = new Activity(nameTextField.getText(),
					urlTextField.getText(),
					(float) Double.parseDouble(energyTextField.getText()));

			for (Activity activity1 : table.getItems()) {
				if (activity1.name().equals(activity.name())) {
					table.getItems().add(activity);

					listActivities.add(activity);

					table.getItems().remove(activity1);

					listActivities.remove(activity1);
				}
			}
			init();
			nameTextField.clear();

			energyTextField.clear();

			urlTextField.clear();
		}

		public void removeButton() {
			Activity activity = new Activity(nameTextField.getText(),
					urlTextField.getText(),
					(float) Double.parseDouble(energyTextField.getText()));

			for (Activity activity1 : table.getItems()) {
				if (activity1.name().equals(activity.name())) {
					table.getItems().remove(activity1);
				}
			}
			init();
			nameTextField.clear();

			energyTextField.clear();

			urlTextField.clear();
		}

	@Override
	public void init() {
		super.init();
		ObservableList<Activity> observableList = FXCollections
				.observableArrayList(listActivities);

		for (Activity activity : serverServicer.getActivities()) {
			observableList.add(activity);
			listActivities.add(activity);
		}
		table.setItems(observableList);
	}
}
