package com.sim.view.dialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MessageDialog {

	
	public static void showAlert(String title, String headerMsg, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Product Success");
		alert.setHeaderText(headerMsg);
		alert.setContentText("Product has been saved.");
		alert.showAndWait();
	}
}
