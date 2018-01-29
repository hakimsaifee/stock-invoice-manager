package com.sim;

import java.io.IOException;

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
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		
		applicationContext = new ClassPathXmlApplicationContext("classpath:spring-context-domain.xml");
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(InvoiceStartup.class.getResource("view/home.fxml"));
		fxmlLoader.setControllerFactory(applicationContext::getBean);
		
		mainLayout = fxmlLoader.load();
		Scene scene = new Scene(mainLayout);
		primaryStage.setScene(scene);
		primaryStage.show();
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
