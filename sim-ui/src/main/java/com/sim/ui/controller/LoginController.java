package com.sim.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.sim.InvoiceStartup;
import com.sim.common.constant.MessageConstant;
import com.sim.spring.security.UserAuthentication;
import com.sim.ui.validator.UIValidator;
import com.sim.view.dialog.MessageDialog;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

@Component
public class LoginController extends Application implements Initializable {
	private static AnchorPane mainLayout;

	static ApplicationContext applicationContext;

	private final static Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	UserAuthentication userAuthentication;

	@FXML
	private TextField userField;

	@FXML
	private TextField passwordField;

	@FXML
	private Button loginButton;

	@FXML
	private Label errorMessage;

	@Autowired
	private Environment env;

	public void authenticationPage(Stage primaryStage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(this.getClass().getResource("/view/login.fxml"));
		fxmlLoader.setControllerFactory(applicationContext::getBean);

		mainLayout = fxmlLoader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	void handleLogin(Event event) {
		try {
			if (KeyEvents.isEnterKeyPressed(event) && validateInput()) {
				resetErrorMessage();
				String username = userField.getText().trim();
				String password = passwordField.getText().trim();

				try {
					userAuthentication.authenticate(username, password);
					InvoiceStartup.openRootHomePage();
				} catch (AuthenticationException e) {
					setErrorMessage(e.getMessage());
				} catch (Exception e) {
					setErrorMessage("Application startup failed. Already running.");
				}
				userField.clear();
				passwordField.clear();
			}
		} catch (Exception e) {
			LOGGER.error("Login Failed : " + e);
			MessageDialog.showAlert(AlertType.ERROR, "Login Failed", null,
					env.getProperty(MessageConstant.INTERNAL_ERROR));
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

	}

	private boolean validateInput() {
		if (UIValidator.validateEmptyTextField(userField)) {
			setErrorMessage("Username is required.");
			return false;
		}
		if (UIValidator.validateEmptyTextField(passwordField)) {
			setErrorMessage("Password is required.");
			return false;
		}
		return true;
	}

	private void setErrorMessage(String message) {
		errorMessage.setText(message);
	}

	private void resetErrorMessage() {
		errorMessage.setText("");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Platform.runLater(() -> {
			userField.requestFocus();
		});

	}
}
