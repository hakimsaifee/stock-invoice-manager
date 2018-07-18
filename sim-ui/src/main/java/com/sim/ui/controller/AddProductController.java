package com.sim.ui.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.sim.LocalStorage;
import com.sim.common.constant.MessageConstant;
import com.sim.common.invoice.generation.BarcodeBuilder;
import com.sim.common.invoice.generation.JasperBarcodeBuilder;
import com.sim.common.util.BarCodeReader;
import com.sim.dto.CategoryDTO;
import com.sim.dto.ItemDTO;
import com.sim.dto.StockDTO;
import com.sim.service.CategoryService;
import com.sim.service.ItemService;
import com.sim.ui.validator.MessageTypeEnum;
import com.sim.ui.validator.UIValidator;
import com.sim.view.dialog.MessageDialog;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

@Component
@Scope(value = "prototype")
public class AddProductController implements Initializable {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AddProductController.class);

	private static final String DUMMY_BARCODE_SAMPLE = "barcodeSample.JPG";

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
	private Label barcodeNumberLabel;

	@FXML
	private TextField searchBarcodeField;

	@FXML
	private TextField offerQuantity;

	@FXML
	private TextField offerPrice;

	@FXML
	private Button saveButton;

	@FXML
	private Button editButton;

	@FXML
	private ComboBox<CategoryDTO> categoryCombo;

	@FXML
	private CheckBox isOfferApplicableCheckbox;

	@FXML
	private Label availableStockLabelValue;

	@FXML
	private Label availableStockLabelText;

	@Autowired
	private ItemService itemService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private UIValidator uiValidator;

	@Autowired
	private Environment env;

	private ItemDTO createdItem;

	private double availableStock;

	public AddProductController() {
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Platform.runLater(() -> {
			barcodeField.requestFocus();
		});

		barcodeField.textProperty().addListener(event -> {
			handleBarcodeScan();

		});
		barcodeField.addEventFilter(KeyEvent.KEY_PRESSED, checkItemExistence());

		try {
			Object value = LocalStorage.getValue(ViewProductController.ITEM_KEY);
			if (value != null) {
				LOGGER.trace("Found Item Value in the local storage.");
				ItemDTO itemDTO = (ItemDTO) value;
				LOGGER.trace("Finding item for barcode : {} ", itemDTO.getBarcode());

				searchItem(itemDTO.getBarcode());

			} else {
				// initializeCategoryComboBox();
				loadCategories();
				if (categoryCombo.getSelectionModel() != null) {
					categoryCombo.getSelectionModel().selectFirst();
				}
				// by default edit button should be disabled.
				editButton.setDisable(true);
			}
		} catch (IOException e) {
			LOGGER.error("Failed to find the product." + e);
			uiValidator.showMessage(env.getProperty(MessageConstant.FIND_PRODUCT_ERROR), MessageTypeEnum.ERROR);
		} finally {
			// clear local storage.
			LocalStorage.clearValue(ViewProductController.ITEM_KEY);
		}

		// Set stock listener
		stockField.textProperty().addListener(event -> {
			final StockDTO stock = createdItem != null ? createdItem.getStock() : null;
			if (stock != null) {
				availableStock = stock != null ? stock.getQuantity() : 0.0;
			} else {
				availableStock = 0.0;
			}
			String stockText = stockField.getText();
			if (stockText == null || stockText.isEmpty()) {
				stockText = "0.0";
			}
			double stockValue = Double.parseDouble(stockText) + availableStock;
			availableStockLabelValue.setText(String.valueOf(stockValue));

		});

		// Check box event.
		isOfferApplicableCheckbox.selectedProperty()
				.addListener((observable, oldValue, newValue) -> offerApplicableCheckBoxChanged(oldValue, newValue));
	}

	private void offerApplicableCheckBoxChanged(Boolean oldValue, Boolean newValue) {
		this.offerQuantity.setDisable(!newValue);
		this.offerPrice.setDisable(!newValue);
	}

	private void handleBarcodeScan() {
		if (barcodeField != null & barcodeField.getText() != null) {
			if (BarCodeReader.isReadByBarcodeScanner(barcodeField.getText())) {
				barcodeField.setText(BarCodeReader.readBarcode(barcodeField.getText()));
			}
		}
	}

	@FXML
	public void saveProduct(Event event) {
		if (KeyEvents.isEnterKeyPressed(event)) {
			if (validateInput() && isConfirmed()) {

				ItemDTO itemDTO = null;
				if (createdItem != null && createdItem.getId() != null) {
					buildItem(this.createdItem);
					this.createdItem.setUpdatedTs(new Timestamp(System.currentTimeMillis()));
					this.createdItem = updateProduct(this.createdItem);
				} else {
					itemDTO = new ItemDTO();
					buildItem(itemDTO);
					itemDTO.setCreatedTs(new Timestamp(System.currentTimeMillis()));
					createdItem = itemService.create(itemDTO);
				}
				if (createdItem != null) {
					try {
						viewModeOn();
					} catch (IOException e) {
						LOGGER.error("Failed to load the barcode image. " + e);
						uiValidator.showMessage(env.getProperty(MessageConstant.FIND_PRODUCT_ERROR),
								MessageTypeEnum.ERROR);
						return;
					}
					uiValidator.showMessage(env.getProperty(MessageConstant.SAVE_ITEM_SUCCESS), MessageTypeEnum.INFO);
				} else {
					uiValidator.showMessage("Failed to add/update the product. Please contact administrator.",
							MessageTypeEnum.ERROR);
				}
			}
		}
	}

	private void viewModeOn() throws IOException {
		refreshFields(createdItem);
		disableAllFields(true);
		editButton.setDisable(false);
	}

	private void buildItem(ItemDTO itemDTO) {
		itemDTO.setBarcode(this.barcodeField.getText());
		itemDTO.setName(this.nameField.getText());
		itemDTO.setDescription(this.descriptionField.getText());
		itemDTO.setCostPrice(Double.valueOf(this.costPriceField.getText()));
		itemDTO.setMrp(Double.valueOf(this.mrpField.getText()));
		itemDTO.setRrp(Double.valueOf(this.revisedMrpField.getText()));
		/* Added to handle special offers */
		itemDTO.setOfferQuantity(Double.valueOf(this.offerQuantity.getText()));
		itemDTO.setOfferPrice(Double.valueOf(this.offerPrice.getText()));

		itemDTO.setCategory(categoryCombo.getSelectionModel().getSelectedItem());

		StockDTO stockDTO = new StockDTO();
		stockDTO.setQuantity(Double.valueOf(this.availableStockLabelValue.getText()));
		stockDTO.setItem(itemDTO);

		itemDTO.setStock(stockDTO);
	}

	private ItemDTO updateProduct(ItemDTO createdItem) {
		return this.itemService.update(createdItem);

	}

	private boolean isConfirmed() {

		return MessageDialog.showConfirmation("Product Confirmation", "Do you want to save changes ?");
	}

	private void disableAllFields(boolean isReadOnly) {
		// barcodeField.setDisable(isReadOnly);
		nameField.setDisable(isReadOnly);
		descriptionField.setDisable(isReadOnly);
		costPriceField.setDisable(isReadOnly);
		mrpField.setDisable(isReadOnly);
		revisedMrpField.setDisable(isReadOnly);
		stockField.setDisable(isReadOnly);

		// Offer case
		// offerPrice.setDisable(isReadOnly);
		// offerQuantity.setDisable(isReadOnly);

		categoryCombo.setDisable(isReadOnly);
		saveButton.setDisable(isReadOnly);

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
		try {

			if (BarCodeReader.isReadByBarcodeScanner(searchBarcodeField.getText())) {
				searchBarcodeField.setText(BarCodeReader.readBarcode(searchBarcodeField.getText()));
			}
			searchItem(this.searchBarcodeField.getText());
		} catch (Exception e) {
			LOGGER.error("Error while finding the product. " + e);
			uiValidator.showMessage(env.getProperty(MessageConstant.FIND_PRODUCT_ERROR), MessageTypeEnum.ERROR);

		}
	}

	private void searchItem(String barcode) throws IOException {

		if (barcode == null || barcode.isEmpty()) {
			uiValidator.showMessage("Please Enter valid barcode.", MessageTypeEnum.ERROR);
			return;
		}
		this.createdItem = itemService.findByBarcode(barcode);
		if (this.createdItem == null) {
			uiValidator.showMessage("No record found for the given barcode.", MessageTypeEnum.ERROR);
			return;
		}
		// Setting available stock.
		// StockDTO stock = this.createdItem.getStock();
		// setAvailableStock(stock);
		viewModeOn();
	}

	private void setAvailableStock(StockDTO stock) {
		if (stock != null) {
			availableStockLabelValue.setText(String.valueOf(stock.getQuantity()));
			availableStockLabelValue.setVisible(true);
			availableStockLabelText.setVisible(true);
		}
	}

	private void refreshFields(ItemDTO itemDTO) throws IOException {
		if (itemDTO == null) {
			// TODO: Show error message;
			resetFields();
			return;
		}
		this.barcodeField.setDisable(true);
		this.barcodeField.setText(itemDTO.getBarcode());
		this.nameField.setText(itemDTO.getName());
		this.descriptionField.setText(itemDTO.getDescription());
		this.costPriceField.setText(String.valueOf(itemDTO.getCostPrice()));
		this.mrpField.setText(String.valueOf(itemDTO.getMrp()));
		this.revisedMrpField.setText(String.valueOf(itemDTO.getRrp()));
		this.stockField.setText("0.0");
		this.availableStockLabelValue
				.setText(itemDTO.getStock() != null ? String.valueOf(itemDTO.getStock().getQuantity()) : "0");
		// Offer case
		this.offerPrice.setText(String.valueOf(itemDTO.getOfferPrice()));
		this.offerQuantity.setText(String.valueOf(itemDTO.getOfferQuantity()));

		loadCategories();
		// ObservableList<CategoryDTO> categoryList = null;
		// if (itemDTO.getCategory() != null) {
		// categoryList =
		// FXCollections.observableArrayList(itemDTO.getCategory());
		// categoryCombo.setItems(categoryList);
		// }
		// this.categoryCombo.setItems(categoryList);

		this.categoryCombo.getSelectionModel().select(findIndex(itemDTO.getCategory().getId()));
		setBarcodeImage(itemDTO.getBarcode());

	}

	private int findIndex(long id) {
		if (this.categoryCombo.getItems() != null) {
			for (int index = 0; index < this.categoryCombo.getItems().size(); index++) {
				if (this.categoryCombo.getItems().get(index).getId() == id) {
					return index;
				}
			}
		}
		return 0;
	}

	private void setBarcodeImage(String barcode) throws IOException {
		// if (barcodeImage != null && barcodeImage.length > 0) {
		try {
			InputStream inputStream = new ClassPathResource(DUMMY_BARCODE_SAMPLE).getInputStream();
			barcodeImageView.setImage(new Image(inputStream));
			barcodeDisplayLabel.setVisible(false);
			barcodeNumberLabel.setText(barcode);
			barcodeNumberLabel.setVisible(true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * } else { this.barcodeImageView.setImage(null);
		 * this.barcodeDisplayLabel.setVisible(true); }
		 */
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
		this.availableStockLabelValue.setText("0.0");
		this.barcodeImageView.setImage(null);
		this.barcodeDisplayLabel.setVisible(true);
		this.barcodeNumberLabel.setVisible(false);

	}

	private boolean validateInput() {
		if (UIValidator.validateEmptyTextField(nameField)) {
			uiValidator.showMessage("Product Name is required.", MessageTypeEnum.ERROR);
			return false;
		}
		if (UIValidator.validateEmptyTextField(costPriceField)) {
			uiValidator.showMessage("Cost Price is required.", MessageTypeEnum.ERROR);
			return false;
		}

		if (!UIValidator.isNumeric(costPriceField.getText())) {
			uiValidator.showMessage("Only number is allowed on Cost Price.", MessageTypeEnum.ERROR);
			return false;
		}
		if (UIValidator.validateEmptyTextField(mrpField)) {
			uiValidator.showMessage("MRP is required.", MessageTypeEnum.ERROR);
			return false;
		}

		if (!UIValidator.isNumeric(mrpField.getText())) {
			uiValidator.showMessage("Only number is allowed on MRP.", MessageTypeEnum.ERROR);
			return false;
		}
		if (UIValidator.validateEmptyTextField(revisedMrpField)) {
			uiValidator.showMessage("Revised MRP is required.", MessageTypeEnum.ERROR);
			return false;
		}

		if (!UIValidator.isNumeric(revisedMrpField.getText())) {
			uiValidator.showMessage("Only number is allowed on Revised MRP.", MessageTypeEnum.ERROR);
			return false;
		}

		if (UIValidator.validateEmptyTextField(stockField)) {
			uiValidator.showMessage("Stock is required.", MessageTypeEnum.ERROR);
			return false;
		}

		if (!UIValidator.isNumeric(stockField.getText())) {
			uiValidator.showMessage("Only number is allowed on Stock.", MessageTypeEnum.ERROR);
			return false;
		}
		if (UIValidator.isGreaterThan(costPriceField.getText(), mrpField.getText())) {
			uiValidator.showMessage("Invalid : 'Cost Price' can not be more than 'Mrp'.", MessageTypeEnum.ERROR);
			return false;
		}
		if (UIValidator.isGreaterThan(revisedMrpField.getText(), mrpField.getText())) {
			uiValidator.showMessage("Invalid : 'Our Price' can not be more than 'Mrp'.", MessageTypeEnum.ERROR);
			return false;
		}
		if (UIValidator.isGreaterThan(costPriceField.getText(), revisedMrpField.getText())) {
			uiValidator.showMessage("Invalid : 'Cost Price' can not be more than 'Our Price'.", MessageTypeEnum.ERROR);
			return false;
		}
		return true;
	}

	private void initializeCategoryComboBox() {
		Callback<ListView<CategoryDTO>, ListCell<CategoryDTO>> factory = new Callback<ListView<CategoryDTO>, ListCell<CategoryDTO>>() {

			@Override
			public ListCell<CategoryDTO> call(ListView<CategoryDTO> p) {

				final ListCell<CategoryDTO> cell = new ListCell<CategoryDTO>() {

					@Override
					protected void updateItem(CategoryDTO dto, boolean empty) {
						super.updateItem(dto, empty);

						if (dto != null) {
							setText(dto.getCategoryName());
						} else {
							setText(null);
						}
					}
				};

				return cell;
			}
		};
		categoryCombo.setCellFactory(factory);

		categoryCombo.setButtonCell(factory.call(null));

	}

	public void loadCategories() {
		initializeCategoryComboBox();
		List<CategoryDTO> categories = categoryService.findAll();
		if (categories != null && !categories.isEmpty()) {
			// Sorting category by name.
			Collections.sort(categories);

			ObservableList<CategoryDTO> categoryList = FXCollections.observableArrayList(categories);
			categoryCombo.setItems(categoryList);
		}
	}

	@FXML
	private void printBarcode() {
		int numberOfCopies = getNumberOfBarcodeCopiesCount();
		BarcodeBuilder bb = new JasperBarcodeBuilder();
		if (this.createdItem != null) {
			try {
				for (int x = 0; x < numberOfCopies; x++) {
					bb.generateBarcode(createdItem,
							Boolean.parseBoolean(env.getProperty(MessageConstant.IS_GENERATE_PDF)));
				}
			} catch (Exception e) {
				MessageDialog.showAlert(AlertType.ERROR, "Print Status", null,
						"Failed.Please check the printer connection.");
			}
		}
	}

	@FXML
	private void editButtonClicked() {
		disableAllFields(false);
		editButton.setDisable(true);
		this.barcodeField.setDisable(true);
	}

	@FXML
	private void showAddCategoryPopup(Event event) {
		if (KeyEvents.isEnterKeyPressed(event)) {
			TextInputDialog dialog = new TextInputDialog("");
			dialog.setTitle("Add Category");
			dialog.setHeaderText("Create New Category");
			dialog.setContentText("Category Name");
			dialog.setWidth(500);
			dialog.setHeight(100);
			// Traditional way to get the response value.
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent() && !result.get().trim().isEmpty()) {
				addAndReloadCategory(result.get());
				uiValidator.showMessage(env.getProperty(MessageConstant.SAVE_CATEGORY_SUCCESS), MessageTypeEnum.INFO);
			} else {
				uiValidator.showMessage(env.getProperty(MessageConstant.EMPTY_CATEGORY), MessageTypeEnum.ERROR);
			}
		}

	}

	private void addAndReloadCategory(String name) {
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setCategoryName(name);
		CategoryDTO createdCategory = categoryService.create(categoryDTO);
		if (createdCategory != null) {
			loadCategories();
		}
	}

	/**
	 * Event which gets trigger on perform tab on barcode field.
	 * 
	 * @param number
	 * @return
	 */
	public EventHandler<KeyEvent> checkItemExistence() {
		return new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.TAB) {
					LOGGER.info("Tab Event Handled : {}", barcodeField.getText());
					if (itemService.findByBarcode(barcodeField.getText()) != null) {
						LOGGER.info("Item already exists for barcode : {}", barcodeField.getText());
						MessageDialog.showAlert(AlertType.WARNING, "Product Already Exists", null,
								"Product already exists for a given barcode : [" + barcodeField.getText() + "]");
						barcodeField.clear();
						return;
					}
				}

			}
		};
	}

	private int getNumberOfBarcodeCopiesCount() {
		TextInputDialog dialog = new TextInputDialog("1");
		dialog.setTitle("Print Barcode Configuration");
		dialog.setContentText("No. of Copies");
		dialog.setHeaderText(null);
		dialog.setWidth(500);
		dialog.setHeight(100);
		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		try {
			if (result.isPresent()) {
				if (!result.get().trim().isEmpty() && Integer.parseInt(result.get()) > 0) {
					return Integer.parseInt(result.get());
				} else {
					MessageDialog.showAlert(AlertType.ERROR, "Print Barcode Error", null, "Invalid Input");
				}
			}
		} catch (NumberFormatException e) {
			LOGGER.error("Invalid Input for Barcode Copies {} ", e);
			MessageDialog.showAlert(AlertType.ERROR, "Print Barcode Error", null, "Invalid Input");
		}

		return 0;
	}
}
