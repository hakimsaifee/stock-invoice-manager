package com.sim;

import java.io.IOException;

import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sim.ui.controller.SelectOfferProductPopupController;
import com.sim.view.dialog.MessageDialog;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

@Configuration
@ComponentScan(basePackages = "com.sim")
@PropertySource({ "classpath:messages.properties", "classpath:sim-configuration.properties" })
public class InvoiceStartup extends Application {

	private static final Integer MIN_WIDTH = Integer.getInteger("MIN_WIDTH", 800);
	private static final Integer MIN_HEIGHT = Integer.getInteger("MIN_HEIGHT", 600);

	private static BorderPane mainLayout;

	static ApplicationContext applicationContext;

	private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(InvoiceStartup.class);

	private static Stage primaryStage;

	@Override
	public void start(Stage stage) throws IOException {
		try {
			LOGGER.info("Loading up login page.");
			applicationContext = new ClassPathXmlApplicationContext("classpath*:spring-context-domain.xml");
			primaryStage = stage;

			openLoginRootPage();
			LOGGER.info("Login page is loaded.");

		} catch (Exception e) {
			LOGGER.error("Failed to load up the application : " + e);
			MessageDialog.showAlert(AlertType.ERROR, "Startup Error", null,
					"Failed to start the application.Please contact administrator.");
		}
	}

	public static void openLoginRootPage() {
		try {

			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(InvoiceStartup.class.getResource("/view/login.fxml"));
			fxmlLoader.setControllerFactory(applicationContext::getBean);
			mainLayout = fxmlLoader.load();
			Scene scene = new Scene(mainLayout);
			primaryStage.setScene(scene);
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

			// set Stage boundaries to visible bounds of the main screen
			primaryStage.setX(primaryScreenBounds.getMinX());
			primaryStage.setY(primaryScreenBounds.getMinY());
			primaryStage.setWidth(primaryScreenBounds.getWidth());
			primaryStage.setHeight(primaryScreenBounds.getHeight());
			primaryStage.setTitle("Stock Invoice Manager");
			primaryStage.show();
		} catch (Exception e) {
			LOGGER.info("Failed to load the home page view. {} ", e);
		}
	}

	public static void openRootHomePage() {
		try {

			FXMLLoader fxmlLoader = new FXMLLoader();
			fxmlLoader.setLocation(InvoiceStartup.class.getResource("/view/home.fxml"));
			fxmlLoader.setControllerFactory(applicationContext::getBean);
			mainLayout = fxmlLoader.load();
			primaryStage.close();
			Scene scene = new Scene(mainLayout);
			primaryStage.setScene(scene);
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

			// set Stage boundaries to visible bounds of the main screen
			primaryStage.setX(primaryScreenBounds.getMinX());
			primaryStage.setY(primaryScreenBounds.getMinY());
			primaryStage.setWidth(primaryScreenBounds.getWidth());
			primaryStage.setHeight(primaryScreenBounds.getHeight());

			primaryStage.setTitle("Stock Invoice Manager");
			primaryStage.show();
		} catch (Exception e) {
			LOGGER.info("Failed to load the home page view. {} ", e);
		}
	}

	public static void navigate(String pageLocation) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(InvoiceStartup.class.getResource(pageLocation));
		fxmlLoader.setControllerFactory(applicationContext::getBean);
		Pane mainItems = fxmlLoader.load();
		mainLayout.setCenter(mainItems);
	}

	public static void shutdown() {
		Platform.exit();
	}

	public static void main(String[] args) {
		LOGGER.info("Launching SIM-MANAGER application......");
		launch(args);
	}

	@Override
	public void stop() {
		if (applicationContext != null) {
		}
	}

	public static SelectOfferProductPopupController loadSelectOfferProduct() {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(InvoiceStartup.class.getResource("/view/selectProductPopup.fxml"));
		fxmlLoader.setControllerFactory(applicationContext::getBean);
		AnchorPane layout;
		try {
			layout = fxmlLoader.load();
			Scene scene = new Scene(layout);
			// this is the popup stage
			Stage popupStage = new Stage();
			
			popupStage.initOwner(primaryStage);
			popupStage.initModality(Modality.WINDOW_MODAL);
			popupStage.setScene(scene);
			// Giving the popup controller access to the popup stage (to allow
			// the controller to close the stage)
			SelectOfferProductPopupController controller = fxmlLoader.getController();
			controller.setStage(popupStage);
			return controller;
		} catch (Exception e) {
			LOGGER.error("Failed to load Select Offer Product Popup. {}" , e);
			e.printStackTrace();
		}
		return null;

	}

}
