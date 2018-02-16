package com.sim.view;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sim.dto.ItemDTO;
import com.sim.dto.StockDTO;
import com.sim.service.ItemService;
import com.sim.view.dialog.MessageDialog;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

@Component
public class AddProductController implements Initializable{

	@FXML
	private TextField barcodeField;

	@FXML
	private TextField nameField;
	
	@FXML
	private TextField descriptionField;
	
	@FXML
	private TextField costPriceField;
	
	@FXML
	private TextField mrpField;
	
	@FXML
	private TextField revisedMrpField;

	@FXML
	private TextField stockField;
	
	@FXML
	private ImageView barcodeImageView;

	@FXML
	private Label barcodeDisplayLabel;
	
	@FXML
	private TextField searchBarcodeField;
	
	  @Autowired
	  private ItemService itemService;
	  
	public AddProductController() {
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {}

	@FXML
	public void saveProduct() {
		ItemDTO itemDTO = new ItemDTO();
		itemDTO.setBarcode(this.barcodeField.getText());
		itemDTO.setName(this.nameField.getText());
		itemDTO.setDescription(this.descriptionField.getText());
		itemDTO.setCostPrice(Double.valueOf(this.costPriceField.getText()));
		itemDTO.setMrp(Double.valueOf(this.mrpField.getText()));
		itemDTO.setRrp(Double.valueOf(this.revisedMrpField.getText()));
		itemDTO.setCreatedTs(new Timestamp(System.currentTimeMillis()));
		
		StockDTO stockDTO = new StockDTO();
		stockDTO.setQuantity(Double.valueOf(this.stockField.getText()));
		stockDTO.setItem(itemDTO);
		
		itemDTO.setStock(stockDTO);
		ItemDTO createdItem = itemService.create(itemDTO);
		
		setBarcodeImage(createdItem.getBarcodeImage());
		MessageDialog.showAlert("Product Success", null , "Product has been saved.");
	}
	
	 @FXML
	public void handleBarcodeScan(KeyEvent event) {
		System.out.println(event.getCharacter().charAt(0) == (char) 0x000d);
		System.out.println("Event " + event);
		if (event.getCharacter().charAt(0) == (char) 0x000d) {
			String barCode = nameField.getText().substring(1, nameField.getText().length());
			System.out.println(barCode);
			nameField.setText(barCode);
		}
	}
	
	 @FXML
	 public void searchProductByBarcode() {
		 searchItem(this.searchBarcodeField.getText());
	 }

	private void searchItem(String barcode) {
		
		if(barcode == null || barcode.isEmpty()) {
			// TODO : show error message
			
			return;
		}
		ItemDTO itemDTO = itemService.findByBarcode(barcode);
		refreshFields(itemDTO);
	}

	private void refreshFields(ItemDTO itemDTO) {
		if(itemDTO == null) {
			//TODO: Show error message;
			resetFields();
			return;
		}
		this.barcodeField.setText(itemDTO.getBarcode());
		this.barcodeField.setDisable(true);
		
		this.nameField.setText(itemDTO.getName());
		this.descriptionField.setText(itemDTO.getDescription());
		this.costPriceField.setText(String.valueOf(itemDTO.getCostPrice()));
		this.mrpField.setText(String.valueOf(itemDTO.getMrp()));
		this.revisedMrpField.setText(String.valueOf(itemDTO.getRrp()));
		this.stockField.setText(itemDTO.getStock() != null ? String.valueOf(itemDTO.getStock().getQuantity()) : "0");
		
		setBarcodeImage(itemDTO.getBarcodeImage());
		
	}

	private void setBarcodeImage(byte[] barcodeImage) {
		if(barcodeImage != null && barcodeImage.length > 0) {
			InputStream inputStream = new ByteArrayInputStream(barcodeImage);
			barcodeImageView.setImage(new Image(inputStream));
			barcodeDisplayLabel.setVisible(false);
		} else {
			this.barcodeImageView.setImage(null);
			this.barcodeDisplayLabel.setVisible(true);
		}
	}

	private void resetFields() {
		// TODO Auto-generated method stub
		this.barcodeField.clear();
		this.barcodeField.setDisable(false);
		this.nameField.clear();
		this.descriptionField.clear();
		this.mrpField.clear();
		this.revisedMrpField.clear();
		this.stockField.clear();
		this.barcodeImageView.setImage(null);
		this.barcodeDisplayLabel.setVisible(true);
	
	}
}
