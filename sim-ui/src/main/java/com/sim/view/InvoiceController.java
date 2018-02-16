package com.sim.view;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sim.dto.InvoiceDTO;
import com.sim.dto.ItemDTO;
import com.sim.dto.ItemInvoiceDTO;
import com.sim.service.InvoiceService;
import com.sim.service.ItemService;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

@Component
@Scope(value = "prototype")
public class InvoiceController implements Initializable {

	private final Logger LOGGER = LoggerFactory.getLogger(InvoiceController.class);

	@Autowired
	private ItemService itemService;

	@Autowired
	private InvoiceService invoiceService;

	@FXML
	private TextField barcode;

	@FXML
	private TableView<ItemInvoiceDTO> itemInvoiceTable;

	@FXML
	private TableColumn<ItemInvoiceDTO, String> productNameColumn;

	@FXML
	private TableColumn<ItemInvoiceDTO, String> rateColumn;

	@FXML
	private TableColumn<?, ?> quantityColumn;

	@FXML
	private TableColumn<?, ?> amountColumn;

	@FXML
	private DatePicker invoiceDate;

	@FXML
	private TextField subTotalField;
	@FXML
	private TextField discountField;
	@FXML
	private TextField additionalChargesField;
	@FXML
	private Label totalAmountField;
	
	@FXML
	private TextField cashTakenField;
	@FXML
	private Label changeGivenField;

	private ObservableList<ItemInvoiceDTO> invoiceItems = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		invoiceDate.setValue(LocalDate.now());
		quantityColumn.setCellValueFactory(new PropertyValueFactory<>("purchasedQuantity"));
		amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

		productNameColumn
				.setCellValueFactory(new Callback<CellDataFeatures<ItemInvoiceDTO, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<ItemInvoiceDTO, String> itemInvoiceDTO) {
						ItemDTO itemDTO = itemInvoiceDTO.getValue().getItem();
						return new SimpleStringProperty(itemDTO != null ? itemDTO.getName() : "");
					}
				});

		rateColumn
				.setCellValueFactory(new Callback<CellDataFeatures<ItemInvoiceDTO, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<ItemInvoiceDTO, String> itemInvoiceDTO) {
						ItemDTO itemDTO = itemInvoiceDTO.getValue().getItem();
						return new SimpleStringProperty(String.valueOf(itemDTO != null ? itemDTO.getRrp() : 0.0));
					}
				});
		
		
		discountField.textProperty().addListener(event -> { recalcuateCart();});
		
		additionalChargesField.textProperty().addListener(event -> { recalcuateCart();});

		cashTakenField.textProperty().addListener(event -> { calculateChangeGiven();});
		
		totalAmountField.textProperty().addListener(event -> {calculateChangeGiven();} );
	}

	

	@FXML
	public void findProduct() {

		if (barcode != null && !barcode.getText().isEmpty()) {
			LOGGER.trace("Find Product : {} ", barcode.getText());
			if (!isProductAlreadyInInvoiceList(barcode.getText())) {
				ItemDTO itemDTO = itemService.findByBarcode(barcode.getText());
				ItemInvoiceDTO itemInvoiceDTO = new ItemInvoiceDTO();
				itemInvoiceDTO.setPurchasedQuantity(1);
				itemInvoiceDTO.setAmount(itemDTO.getRrp()*itemInvoiceDTO.getPurchasedQuantity());
				itemInvoiceDTO.setCreatedTs(new Timestamp(System.currentTimeMillis()));
				itemInvoiceDTO.setItem(itemDTO);
				
				invoiceItems.add(itemInvoiceDTO);
				itemInvoiceTable.setItems(invoiceItems);
			}
			recalcuateCart();
		}
	}

	private void recalcuateCart() {
		double totalAmount = 0.00;
		for (ItemInvoiceDTO itemInvoiceDTO : invoiceItems) {
			totalAmount = totalAmount + itemInvoiceDTO.getAmount();
		}
		subTotalField.setText(String.valueOf(totalAmount));
		//calculate discount
		String discount = this.discountField.getText();
		totalAmount = totalAmount - Double.valueOf(discount.isEmpty() ? "0.0" : discount);
		//additional Charges
		String additionalCharges = additionalChargesField.getText();
		totalAmount = Double.valueOf(totalAmount + Double.valueOf(additionalCharges.isEmpty() ? "0.0" : additionalCharges));
		
		totalAmountField.setText(String.valueOf(totalAmount));
	}
	
	private boolean isProductAlreadyInInvoiceList(String barcode) {
		for (ItemInvoiceDTO itemInvoiceDTO : invoiceItems) {
			if (itemInvoiceDTO.getItem().getBarcode().equals(barcode)) {
				LOGGER.debug("Product already found in the invoice list : {} ", barcode);
				itemInvoiceDTO.setPurchasedQuantity(itemInvoiceDTO.getPurchasedQuantity()+1);
				itemInvoiceDTO.setAmount(itemInvoiceDTO.getItem().getRrp()*itemInvoiceDTO.getPurchasedQuantity());
				itemInvoiceTable.refresh();
				return true;
			}
		}
		return false;
	}
	
	@FXML
	public void saveInvoice() {
		InvoiceDTO invoiceDTO = new InvoiceDTO();
		invoiceDTO.setCreatedTs(new Timestamp(System.currentTimeMillis()));
		invoiceDTO.setTotalAmount(Double.valueOf(totalAmountField.getText()));
		invoiceDTO.setDiscount(Double.valueOf(discountField.getText().isEmpty() ? "0.0" : discountField.getText()));

		//TODO:Add Staff details...
		invoiceDTO.setItemInvoices(invoiceItems);
		
		invoiceService.create(invoiceDTO);
		
		
		//TODO: Change the below code to callback handlers.
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Invoice Success");
		alert.setHeaderText("Invoice Created Successfully. Do you want to print ?");
//		alert.setContentText("Invoice Created Successfully. Do you want to print ?");
		ButtonType buttonTypeYes = new ButtonType("Yes");
		ButtonType buttonTypeNo = new ButtonType("No");

		alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
		    // ... user chose OK
		} else {
		    // ... user chose CANCEL or closed the dialog
		}
	}
	
	private void calculateChangeGiven() {
		if(totalAmountField != null && totalAmountField.getText() != null ) {
			double totoalAmount = Double.parseDouble(totalAmountField.getText());
			if(cashTakenField != null && cashTakenField.getText() !=null && 
					!cashTakenField.getText().isEmpty()) {
				double changeGiven = Double.parseDouble(cashTakenField.getText()) - totoalAmount;
				changeGivenField.setText(String.valueOf(changeGiven));
			}
		}
	}
}
