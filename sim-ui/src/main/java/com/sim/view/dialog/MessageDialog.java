package com.sim.view.dialog;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;

public class MessageDialog {

	public static void showAlert(AlertType alertType, String title, String headerMsg, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(headerMsg);
		alert.setContentText(content);
		alert.showAndWait();
	}

	public static ButtonData showConfirmationAlert(AlertType alertType, String title, String headerMsg,
			String content) {
		// TODO: Change the below code to callback handlers.
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(headerMsg);
		alert.setContentText(content);
		ButtonType buttonTypeYes = new ButtonType("Yes", ButtonData.YES);
		ButtonType buttonTypeNo = new ButtonType("No", ButtonData.NO);
		alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
		Optional<ButtonType> result = alert.showAndWait();
		return result.get().getButtonData();
	}

	public static boolean showConfirmation(String header, String message) {
		ButtonData buttonSelected = MessageDialog.showConfirmationAlert(AlertType.CONFIRMATION, header, null, message);
		if (buttonSelected == ButtonData.YES) {
			return true;
		} else {
			return false;
		}
	}
}
