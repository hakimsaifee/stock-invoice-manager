package com.sim;

import java.io.IOException;
import java.net.URL;

import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

@ComponentScan(basePackages = "com.sim")
public class InvoiceStartup extends Application {

	private static BorderPane mainLayout;

	static ApplicationContext applicationContext;
	
	private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(InvoiceStartup.class);
	
	@Override
	public void start(Stage primaryStage) throws IOException {
try{
		applicationContext = new ClassPathXmlApplicationContext("classpath*:spring-context-domain.xml");
		FXMLLoader fxmlLoader = new FXMLLoader();
		LOGGER.info("***************** sTARING *********8");
//		fxmlLoader.setLocation(InvoiceStartup.class.getResource("view/home.fxml"));
		LOGGER.info("s3 {}", this.getClass().getResourceAsStream("/home.fxml"));
		LOGGER.info("s4 {}", this.getClass().getResource("/home.fxml"));
//		fxmlLoader.setLocation(new URL(InvoiceStartup.class.getResource("view/home.fxml").getPath()));
		fxmlLoader.setLocation(this.getClass().getResource("/home.fxml"));
		fxmlLoader.setControllerFactory(applicationContext::getBean);

		mainLayout = fxmlLoader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
}catch(Exception e) {
	LOGGER.debug("Exception : " + e);
}
	}

	public static void navigate(String pageLocation) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(InvoiceStartup.class.getResource(pageLocation));
		fxmlLoader.setControllerFactory(applicationContext::getBean);
		Pane mainItems = fxmlLoader.load();
		mainLayout.setCenter(mainItems);

	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void stop() {
		if (applicationContext != null) {
		}
	}

}
