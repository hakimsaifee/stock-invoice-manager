package com.sim.ui.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.sim.InvoiceStartup;
import com.sim.common.constant.MessageConstant;
import com.sim.common.constant.PaymentModeEnum;
import com.sim.common.invoice.generation.InvoiceBuilder;
import com.sim.common.invoice.generation.JasperInvoiceBuilder;
import com.sim.common.util.BarCodeReader;
import com.sim.common.util.MapperUtil;
import com.sim.common.util.MathUtil;
import com.sim.dto.BillModel;
import com.sim.dto.InvoiceDTO;
import com.sim.dto.ItemDTO;
import com.sim.dto.ItemInvoiceDTO;
import com.sim.dto.StaffDTO;
import com.sim.dto.StockDTO;
import com.sim.service.InvoiceService;
import com.sim.service.ItemService;
import com.sim.service.StaffService;
import com.sim.ui.custom.component.EditingCell;
import com.sim.ui.validator.MessageTypeEnum;
import com.sim.ui.validator.UIValidator;
import com.sim.view.dialog.MessageDialog;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

@Component
@Scope(value = "prototype")
public class InvoiceController implements Initializable {

	private StringBuilder bb;

	private static final String OUT_OF_STOCK_MSG = "Product out of stock. Available quantity : ";

	private final Logger LOGGER = LoggerFactory.getLogger(InvoiceController.class);

	@Autowired
	private ItemService itemService;

	@Autowired
	private InvoiceService invoiceService;

	@FXML
	private Label invoiceId;

	@FXML
	private TextField barcode;

	@FXML
	private TableView<ItemInvoiceDTO> itemInvoiceTable;

	@FXML
	private TableColumn<ItemInvoiceDTO, String> productNameColumn;

	@FXML
	private TableColumn<ItemInvoiceDTO, String> rateColumn;

	@SuppressWarnings("rawtypes")
	@FXML
	private TableColumn quantityColumn;

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
	private Label roundOffLabel;

	@FXML
	private TextField cashTakenField;

	@FXML
	private Label changeGivenField;

	@FXML
	private Button saveButton;

	@FXML
	private Button cancelButton;

	@FXML
	private Button printButton;

	@FXML
	private Button removeButton;

	@FXML
	private ComboBox<String> paymentModeCombo;

	@FXML
	private ComboBox<StaffDTO> staffCombo;

	private ObservableList<ItemInvoiceDTO> invoiceItems = FXCollections.observableArrayList();

	@Autowired
	private UIValidator uiValidator;

	@Autowired
	private StaffService staffService;

	private ItemInvoiceDTO selectedInvoiceRow;

	private InvoiceDTO savedInvoice;

	@Autowired
	private Environment env;

	@Autowired
	private MapperUtil mapperUtil;

	private SelectOfferProductPopupController selectOfferPopup;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		bb = new StringBuilder();
		itemInvoiceTable.setEditable(true);
		invoiceDate.setValue(LocalDate.now());

		itemInvoiceTable.setOnMouseClicked((MouseEvent event) -> {
			if (event.getClickCount() == 1) {
				rowSelected();
			}
		});

		@SuppressWarnings("rawtypes")
		Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
			public TableCell call(TableColumn p) {
				itemInvoiceTable.getSelectionModel().getSelectedItem();
				return new EditingCell();
			}
		};

		quantityColumn.setCellValueFactory(new PropertyValueFactory<ItemInvoiceDTO, Double>("purchasedQuantity"));
		quantityColumn.setCellFactory(cellFactory);
		quantityColumn.setOnEditCommit(new EventHandler<CellEditEvent<ItemInvoiceDTO, Double>>() {
			@Override
			public void handle(CellEditEvent<ItemInvoiceDTO, Double> t) {

				ItemInvoiceDTO itemInvoiceDTO = ((ItemInvoiceDTO) t.getTableView().getItems()
						.get(t.getTablePosition().getRow()));
				if (productOutOfStock(null, itemInvoiceDTO.getItem().getBarcode(), t.getNewValue())) {
					itemInvoiceDTO.setPurchasedQuantity(0);
				} else {
					// Handle offer.
					if (isProductEligibleForOffer(itemInvoiceDTO.getItem())) {
						// Notify user if purchased quantity is eligible for
						// offer.
						if (t.getNewValue() > t.getOldValue()
								&& t.getNewValue() >= itemInvoiceDTO.getItem().getOfferQuantity()) {
							// Notify user that product is eligible for offer
							// and apply offer.
							showProductOnOfferAlert(itemInvoiceDTO.getItem(), true);
							applyOffer(itemInvoiceDTO, itemInvoiceDTO.getItem());
						}
						// Notify user if purchased quantity is below the offer
						// value.
						if (t.getNewValue() < t.getOldValue()
								&& t.getOldValue() >= itemInvoiceDTO.getItem().getOfferQuantity()
								&& itemInvoiceDTO.getItem().getOfferQuantity() > t.getNewValue()) {
							// Notify User and remove offer.
							showProductOnOfferAlert(itemInvoiceDTO.getItem(), false);
							removeOffer(itemInvoiceDTO, itemInvoiceDTO.getItem());
						}
					}
					itemInvoiceDTO.setPurchasedQuantity(t.getNewValue());
				}
				recalcuateCart();
			}

		});

		amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

		productNameColumn
				.setCellValueFactory(new Callback<CellDataFeatures<ItemInvoiceDTO, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<ItemInvoiceDTO, String> itemInvoiceDTO) {
						ItemDTO itemDTO = itemInvoiceDTO.getValue().getItem();
						return new SimpleStringProperty(itemDTO != null ? itemDTO.getName() : "");
					}
				});

		rateColumn.setCellValueFactory(new PropertyValueFactory<>("productSellingPrice"));

		discountField.textProperty().addListener(event -> {
			recalcuateCart();
		});

		additionalChargesField.textProperty().addListener(event -> {
			recalcuateCart();
		});

		cashTakenField.textProperty().addListener(event -> {
			calculateChangeGiven();
		});

		totalAmountField.textProperty().addListener(event -> {
			calculateChangeGiven();
		});

		quantityColumn.textProperty().addListener(event -> {
			recalcuateCart();
		});

		initializeStaffComboBox();

		loadPaymentModes();

		loadSaffDetails();

		if (staffCombo.getSelectionModel() != null) {
			staffCombo.getSelectionModel().selectFirst();
		}

		printButton.setDisable(true);

	}

	@FXML
	public void findProduct(Event event) {

		if (((KeyEvent) event).getCode() == KeyCode.TAB) {
			event.consume();
		}

		if (!KeyEvents.isEnterKeyEvent(event)) {
			return;
		}
		String barcodeValue = barcode.getText();
		if (barcode != null && !barcodeValue.isEmpty()) {
			LOGGER.trace("Find Product : {} ", barcodeValue);

			if (BarCodeReader.isReadByBarcodeScanner(barcodeValue)) {
				barcode.setText(BarCodeReader.readBarcode(barcodeValue));
			}

			ItemInvoiceDTO itemAlreadyPresent = productAlreadyPresentInInvoiceList(barcode.getText());
			ItemDTO itemDTO = itemService.findByBarcode(barcode.getText());
			if (itemAlreadyPresent == null) {
				// If product is not present in the cart.
				if (itemDTO == null) {
					MessageDialog.showAlert(AlertType.WARNING, "Product Not Found", null,
							"Prodcut not found : Barcod - [" + barcode.getText() + "]");
					return;
				}
				if (productOutOfStock(itemDTO, null, 1)) {
					return;
				}

				// Case 1 : If product is eligible for special offer.
				if (isProductEligibleForOffer(itemDTO)) {
					ItemInvoiceDTO offerItemInvoice = showSelectPricePopup(itemDTO);
					invoiceItems.add(offerItemInvoice);
				} else {
					ItemInvoiceDTO itemInvoiceDTO = new ItemInvoiceDTO();
					itemInvoiceDTO.setPurchasedQuantity(1);
					// itemInvoiceDTO.setAmount(itemDTO.getRrp()*itemInvoiceDTO.getPurchasedQuantity());
					itemInvoiceDTO.setCreatedTs(new Timestamp(System.currentTimeMillis()));
					itemInvoiceDTO.setItem(itemDTO);
					itemInvoiceDTO.setProductSellingPrice(itemDTO.getRrp());
					invoiceItems.add(itemInvoiceDTO);
				}
				itemInvoiceTable.setItems(invoiceItems);
			} else {
				double purchasedQuantity = itemAlreadyPresent.getPurchasedQuantity() + 1;
				if (productOutOfStock(null, itemAlreadyPresent.getItem().getBarcode(), purchasedQuantity)) {
					return;
				}

				// Case : Check if purchased quantity is eligible for the offer.
				if (isPurchasedQuantityEligibleForOffer(itemDTO, purchasedQuantity)) {
					// Notify user and apply offer.
					showProductOnOfferAlert(itemDTO, true);
					applyOffer(itemAlreadyPresent, itemDTO);
				}
				itemAlreadyPresent.setPurchasedQuantity(purchasedQuantity);
			}
			recalcuateCart();
			clearField(barcode);
			setFocus(barcode);
		}
	}

	private void showProductOnOfferAlert(ItemDTO itemDTO, boolean isOffer) {

		String message;
		if (isOffer) {
			message = "Offer Applied!! \n Product is Eligible for Offer.";
		} else {
			message = "Offer Removed!! \n Product is not Eligible for the Offer.";
		}
		MessageDialog.showAlert(AlertType.WARNING, "Product On Offer", null,
				message + "\n" + "Minimum Qty. to Avail Offer : [" + itemDTO.getOfferQuantity() + "]" + "\n"
						+ "Offer Price : [" + itemDTO.getOfferPrice() + " ]");
	}

	private void applyOffer(ItemInvoiceDTO itemInvoice, ItemDTO itemDTO) {
		itemInvoice.setProductSellingPrice(itemDTO.getOfferPrice());
	}

	private void removeOffer(ItemInvoiceDTO itemInvoice, ItemDTO itemDTO) {
		itemInvoice.setProductSellingPrice(itemDTO.getRrp());
	}

	private boolean isPurchasedQuantityEligibleForOffer(ItemDTO itemDto, double purchasedQuantity) {
		if (isProductEligibleForOffer(itemDto) && purchasedQuantity >= itemDto.getOfferQuantity()) {
			return true;
		}
		return false;
	}

	private ItemInvoiceDTO showSelectPricePopup(ItemDTO itemDTO) {
		this.selectOfferPopup = InvoiceStartup.loadSelectOfferProduct();
		if (this.selectOfferPopup != null) {
			return this.selectOfferPopup.openPopup(itemDTO);
		}
		return null;
	}

	private boolean isProductEligibleForOffer(ItemDTO itemDTO) {
		if (itemDTO.getOfferQuantity() > 0 && itemDTO.getOfferPrice() > 0) {
			LOGGER.info("Product [{}] is eligible for Offer.", itemDTO.getBarcode());
			return true;
		}
		return false;
	}

	private boolean productOutOfStock(ItemDTO itemDTO, String barcode, double requiredQuantity) {
		if (itemDTO == null) {
			itemDTO = itemService.findByBarcode(barcode);
		}

		StockDTO stock = itemDTO.getStock();
		if (stock != null) {
			if ((stock.getQuantity() - requiredQuantity) < 0) {
				MessageDialog.showAlert(AlertType.WARNING, "Out Of Stock", null,
						OUT_OF_STOCK_MSG + stock.getQuantity());
				// uiValidator.showMessage(OUT_OF_STOCK_MSG +
				// stock.getQuantity(), MessageTypeEnum.ERROR);
				return true;
			} else {
				return false;
			}
		} else {
			MessageDialog.showAlert(AlertType.WARNING, "Out Of Stock", null, OUT_OF_STOCK_MSG + 0);
			// uiValidator.showMessage(OUT_OF_STOCK_MSG + 0,
			// MessageTypeEnum.ERROR);
			return true;
		}
	}

	private void recalcuateCart() {
		double totalAmount = 0.00;
		for (ItemInvoiceDTO itemInvoiceDTO : invoiceItems) {
			itemInvoiceDTO.setAmount(itemInvoiceDTO.getProductSellingPrice() * itemInvoiceDTO.getPurchasedQuantity());
			totalAmount = totalAmount + itemInvoiceDTO.getAmount();
		}
		subTotalField.setText(String.valueOf(totalAmount));
		// calculate discount
		String discount = this.discountField.getText();
		totalAmount = totalAmount - Double.valueOf(discount.isEmpty() ? "0.0" : discount);
		// additional Charges
		String additionalCharges = additionalChargesField.getText();
		totalAmount = Double
				.valueOf(totalAmount + Double.valueOf(additionalCharges.isEmpty() ? "0.0" : additionalCharges));
		//Rounding off to closest value.
		double grandTotal = Math.round(totalAmount);
		double roundOff = grandTotal-totalAmount;
		roundOff = MathUtil.preciseDouble(roundOff, 2); 
		roundOffLabel.setText(String.valueOf(roundOff));
		totalAmountField.setText(String.valueOf(grandTotal));
		
		refreshTable();
	}

	private ItemInvoiceDTO productAlreadyPresentInInvoiceList(String barcode) {
		for (ItemInvoiceDTO itemInvoiceDTO : invoiceItems) {
			if (itemInvoiceDTO.getItem().getBarcode().equals(barcode)) {
				LOGGER.debug("Product already found in the invoice list : {} ", barcode);
				return itemInvoiceDTO;
			}
		}
		return null;
	}

	private void refreshTable() {
		itemInvoiceTable.refresh();
	}

	private void clearField(TextField field) {
		field.clear();
	}

	private void setFocus(TextField field) {
		Platform.runLater(() -> {
			field.requestFocus();
		});
		barcode.requestFocus();
	}

	@FXML
	public void saveInvoice() {
		if (MessageDialog.showConfirmation("Invoice Confirmation", "Do you want to save changes ?")) {

			InvoiceDTO invoiceDTO = new InvoiceDTO();
			invoiceDTO.setCreatedTs(new Timestamp(System.currentTimeMillis()));
			invoiceDTO.setTotalAmount(Double.valueOf(totalAmountField.getText()));
			invoiceDTO.setDiscount(Double.valueOf(discountField.getText().isEmpty() ? "0.0" : discountField.getText()));
			invoiceDTO.setAdditionalCharges(Double
					.valueOf(additionalChargesField.getText().isEmpty() ? "0.0" : additionalChargesField.getText()));
			invoiceDTO.setRoundOff(Double.valueOf(roundOffLabel.getText()));
			invoiceDTO.setPaymentMode(paymentModeCombo.getSelectionModel().getSelectedItem());
			LOGGER.trace("Selected Payment Mode : {} ", paymentModeCombo.getSelectionModel().getSelectedItem());
			// TODO:Add Staff details...
			StaffDTO selectedStaff = staffCombo.getSelectionModel().getSelectedItem();
			invoiceDTO.setStaff(selectedStaff);

			invoiceDTO.setItemInvoices(invoiceItems);

			InvoiceDTO generatedInvoice = invoiceService.create(invoiceDTO);
			if (generatedInvoice != null) {
				this.savedInvoice = generatedInvoice;
				showSuccessPopup(generatedInvoice.getId());
				invoiceId.setText(String.valueOf(generatedInvoice.getId()));
				disableComponents(true);
			} else {
				uiValidator.showMessage(env.getProperty(MessageConstant.SAVE_INVOICE_FAILURE), MessageTypeEnum.ERROR);
			}
		}

	}

	private void calculateChangeGiven() {
		if (totalAmountField != null && totalAmountField.getText() != null) {
			double totoalAmount = Double.parseDouble(totalAmountField.getText());
			if (cashTakenField != null && cashTakenField.getText() != null && !cashTakenField.getText().isEmpty()) {
				double changeGiven = Double.parseDouble(cashTakenField.getText()) - totoalAmount;
				changeGivenField.setText(String.valueOf(changeGiven));
			}
		}
	}

	private void disableComponents(boolean isDisable) {
		itemInvoiceTable.setDisable(isDisable);
		barcode.setDisable(isDisable);
		discountField.setDisable(isDisable);
		additionalChargesField.setDisable(isDisable);
		cashTakenField.setDisable(isDisable);
		changeGivenField.setDisable(isDisable);
		saveButton.setDisable(isDisable);
		cancelButton.setDisable(isDisable);
		paymentModeCombo.setDisable(isDisable);
		staffCombo.setDisable(isDisable);
		roundOffLabel.setDisable(isDisable);
		printButton.setDisable(false);
	}

	private void showSuccessPopup(int invoiceId) {
		// TODO: Change the below code to callback handlers.
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Invoice Success");
		alert.setHeaderText(
				"Invoice Generated Successfully with Inovoice ID : " + invoiceId + ". Do you want to print ?");
		// alert.setContentText("Invoice Created Successfully. Do you want to
		// print ?");
		ButtonType buttonTypeYes = new ButtonType("Yes");
		ButtonType buttonTypeNo = new ButtonType("No");

		alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get().getText() == buttonTypeYes.getText()) {
			printInvoice();
		} else {
		}
	}

	public void rowSelected() {
		// check the table's selected item and get selected item
		if (itemInvoiceTable.getSelectionModel().getSelectedItem() != null) {
			ItemInvoiceDTO itemInvoiceDTO = itemInvoiceTable.getSelectionModel().getSelectedItem();
			selectedInvoiceRow = itemInvoiceDTO;
			removeButton.setDisable(false);
		}
	}

	@FXML
	public void removeSelectedItem() {
		ButtonData buttonSelected = MessageDialog.showConfirmationAlert(AlertType.CONFIRMATION,
				"Remove Product Confirmation", null, "Are you sure, you want to remove a product ["
						+ selectedInvoiceRow.getItem().getName() + "] from the cart ? ");
		if (buttonSelected == ButtonData.YES) {
			invoiceItems.remove(selectedInvoiceRow);
			if (invoiceItems.size() == 0) {
				removeButton.setDisable(true);
			}
			recalcuateCart();
			rowSelected();

		} else {
		}

	}

	public void loadPaymentModes() {
		ObservableList<String> paymentModes = FXCollections.observableArrayList(PaymentModeEnum.getValues());
		paymentModeCombo.setItems(paymentModes);
		paymentModeCombo.getSelectionModel().select(0);
	}

	public void loadSaffDetails() {
		List<StaffDTO> stafDetails = staffService.findAll();
		if (stafDetails != null && !stafDetails.isEmpty()) {
			ObservableList<StaffDTO> staffList = FXCollections.observableArrayList(stafDetails);
			staffCombo.setItems(staffList);
		}
	}

	private void initializeStaffComboBox() {
		Callback<ListView<StaffDTO>, ListCell<StaffDTO>> factory = new Callback<ListView<StaffDTO>, ListCell<StaffDTO>>() {

			@Override
			public ListCell<StaffDTO> call(ListView<StaffDTO> p) {

				final ListCell<StaffDTO> cell = new ListCell<StaffDTO>() {

					@Override
					protected void updateItem(StaffDTO dto, boolean empty) {
						super.updateItem(dto, empty);

						if (dto != null) {
							setText(dto.getName());
						} else {
							setText(null);
						}
					}
				};

				return cell;
			}
		};
		staffCombo.setCellFactory(factory);

		staffCombo.setButtonCell(factory.call(null));

	}

	public void barcodeHandle(KeyEvent e) {
		if (e.getCode() == KeyCode.TAB) {
			System.out.println("tAB pressed");
			e.consume();
		}
		System.out.println(e.getCode());
		StringBuilder sb = new StringBuilder();
		switch (e.getCode()) {

		case DIGIT0:
		case DIGIT1:
		case DIGIT2:
		case DIGIT3:
		case DIGIT4:
		case DIGIT5:
		case DIGIT6:
		case DIGIT7:
		case DIGIT8:
		case DIGIT9:
			sb.append(e.getText());
		default:
			break;
		}

		System.out.println(sb.toString());
		bb.append(e.getText());
		barcode.setText(sb.toString());

	}

	@FXML
	public void printInvoice() {
		if (savedInvoice != null) {
			InvoiceBuilder ib = new JasperInvoiceBuilder();
			BillModel billModel = mapperUtil.prepareInvoiceModel(savedInvoice);
			try {
				ib.generateInvoice(billModel, Boolean.parseBoolean(env.getProperty(MessageConstant.IS_GENERATE_PDF)));
			} catch (Exception e) {
				MessageDialog.showAlert(AlertType.ERROR, "Print Status", null,
						"Failed.Please check the printer connection.");
			}
		}
	}

}
