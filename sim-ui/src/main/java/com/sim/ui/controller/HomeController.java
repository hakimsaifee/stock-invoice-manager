package com.sim.ui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.sim.InvoiceStartup;
import com.sim.spring.security.UserPrincipal;
import com.sim.ui.validator.UIValidator;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

@Component
public class HomeController implements Initializable {

	private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

	public static final String ADD_PRODUCT_PAGE = "/view/addProduct.fxml";
	private static final String VIEW_PRODUCTS_PAGE = "/view/viewProducts.fxml";
	private static final String INVOICE_SUMMARY_PAGE = "/view/invoiceSummary.fxml";
	private static final String INVOICE_GENERATION_PAGE = "/view/invoiceGeneration.fxml";

	@Autowired
	private UIValidator uiValidator;

	@FXML
	public ImageView addProductImg;

	@FXML
	public HBox homeToolbarHbox;

	@FXML
	public Label username;

	@FXML
	public HBox messageBox;

	@FXML
	public void gotoAddProducts() throws IOException {
		InvoiceStartup.navigate(ADD_PRODUCT_PAGE);

	}

	@FXML
	public void gotoViewProducts() throws IOException {
		InvoiceStartup.navigate(VIEW_PRODUCTS_PAGE);
	}

	@FXML
	public void gotoInvoice() throws IOException {
		InvoiceStartup.navigate(INVOICE_GENERATION_PAGE);
	}

	@FXML
	public void gotoInvoiceSummary() throws IOException {
		InvoiceStartup.navigate(INVOICE_SUMMARY_PAGE);
	}

	@FXML
	public void logout() {
		SecurityContextHolder.clearContext();
		InvoiceStartup.openLoginRootPage();
	}

	
	@FXML
	public void closeMessageBox() {
		uiValidator.hidePanel();
	}

	@FXML
	public void exit() {
		InvoiceStartup.shutdown();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			uiValidator.setMessagePanel(messageBox);
			setPermissions();
			setUserName();
		} catch (Exception e) {
			LOGGER.info("Exception : {} ", e);
		}

	}

	private void setUserName() {
		UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		username.setText(userPrincipal.getUsername());

	}

	private void setPermissions() {
		UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Collection<? extends GrantedAuthority> authorities = userPrincipal.getAuthorities();
		boolean isAdmin = false;
		if (authorities != null) {
			for (GrantedAuthority grantedAuthority : authorities) {
				if ("ADMIN".equals(grantedAuthority.getAuthority())) {
					isAdmin = true;
					break;
				}
			}
		}
		if (!isAdmin) {
			ObservableList<Node> nodes = homeToolbarHbox.getChildren();
			for (Node node : nodes) {
				if (node.getTypeSelector().equals("Label"))
					node.setDisable(isPermissonGranted(node.getId()));
			}
		}

	}

	private boolean isPermissonGranted(String id) {
		switch (id) {
		case "addProduct":
			return true;
		case "getProduts":
			return false;
		case "createInvoice":
			return false;
		case "showSummary":
			return true;
		case "showStaff":
			return true;
		default:
			return false;
		}
	}

	// public void setInfoMessage() {
	// messageBox.setVisible(true);
	// }
}
