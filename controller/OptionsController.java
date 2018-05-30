package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import tools.Options;

public class OptionsController implements Initializable{
	@FXML
	private RadioButton radioButtonYesSound;

	@FXML
	private RadioButton radioButtonNoSound;

	@FXML
	private RadioButton radioButtonYesNotif;

	@FXML
	private RadioButton radioButtonNoNotif;

	@FXML
	private ChoiceBox<String> choiceBoxCountry;

	@FXML
	private ChoiceBox<String> choiceBoxMessagesBy;

	@FXML
	private Button saveButton;

	private static boolean paramettered = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (paramettered)
			return;

		choiceBoxMessagesBy.getItems().addAll(Options.getShowMessagesByChoices());
		choiceBoxMessagesBy.getSelectionModel().select(Options.getShowMessagesBy());

		if(Options.isSoundAllowed()) {
			radioButtonYesSound.setSelected(true);
			radioButtonNoSound.setSelected(false);
		}
		else {
			radioButtonYesSound.setSelected(false);
			radioButtonNoSound.setSelected(true);
		}

		if(Options.isWindowsNotificationAllowed()) {
			radioButtonYesNotif.setSelected(true);
			radioButtonNoNotif.setSelected(false);
		}
		else {
			radioButtonYesNotif.setSelected(false);
			radioButtonNoNotif.setSelected(true);
		}

		choiceBoxCountry.getItems().addAll(Options.getCountryChoices());
		choiceBoxCountry.getSelectionModel().select(Options.getCountry());

		paramettered = true;
	}

	@FXML
	public void save(ActionEvent event) throws InterruptedException{
		boolean mustRestart = !Options.getCountry().equals(choiceBoxCountry.getSelectionModel().getSelectedItem());
		if(mustRestart) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Country has changed");
			alert.setHeaderText("Country has changed");
			alert.setContentText("You must restart the software to apply changes");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() != ButtonType.OK)
				return;
		}

		if(radioButtonYesSound.isSelected()) 
			Options.setSoundAllowed(true);		
		else 
			Options.setSoundAllowed(false);
		
		if(radioButtonYesNotif.isSelected()) 
			Options.setWindowsNotificationAllowed(true);
		else 
			Options.setWindowsNotificationAllowed(false);
		
		Options.setCountry(choiceBoxCountry.getSelectionModel().getSelectedItem());
		Options.setShowMessagesBy(choiceBoxMessagesBy.getSelectionModel().getSelectedItem());

		System.out.println(Options.getShowMessagesBy());
		Stage stage = (Stage) saveButton.getScene().getWindow();
		
		if(mustRestart)
			System.exit(0);
		else
			stage.close();
	}

	public static void setParamettered(boolean b) {
		paramettered = b;

	}

}
