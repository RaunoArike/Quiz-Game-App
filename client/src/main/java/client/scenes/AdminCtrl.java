package client.scenes;

import client.service.ServerService;
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
import com.google.inject.Inject;

import java.net.URL;
import java.util.ResourceBundle;


	public class AdminCtrl implements Initializable {
		private final ServerService serverServicer;
		private final MainCtrl mainCtrl;

		public TextField nameTextField;
		public TextField energyTextField;
		public TextField urlTextField;

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
		}

		ObservableList<Activity> observableList = FXCollections.observableArrayList(new Activity("", "", 0));

		///The option to return home
		public void returnHome() {
			mainCtrl.showHome();
		}

		public void keyPressed(KeyEvent keyEvent) {
			switch (keyEvent.getCode()) {
				case ESCAPE:
					returnHome();
					break;
				case ENTER:
					addButton();
				default:
					break;
			}
		}


		@Override
		public void initialize(URL location, ResourceBundle resources) {
			columnOne.setCellValueFactory(x -> new SimpleStringProperty(x.getValue().name()));
			columnTwo.setCellValueFactory(x -> new SimpleFloatProperty(x.getValue().energyInWh()));

			table.setItems(observableList);
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
}
