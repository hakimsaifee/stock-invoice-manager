package com.sim.ui.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.sim.InvoiceStartup;
import com.sim.LocalStorage;
import com.sim.common.constant.MessageConstant;
import com.sim.dto.CategoryDTO;
import com.sim.dto.ItemDTO;
import com.sim.dto.StockDTO;
import com.sim.service.ItemService;
import com.sim.spring.security.SecurityContext;
import com.sim.ui.custom.component.AutoCompleteTextField;
import com.sim.ui.validator.MessageTypeEnum;
import com.sim.ui.validator.UIValidator;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

@Component
public class ViewProductController implements Initializable {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ViewProductController.class);

	private static final String BARCODE_START_BRACKET = "[";
	private static final String BARCODE_END_BRACKET = "]";

	public static final String ITEM_KEY = "ITEM";

	private ObservableList<ItemDTO> itemList = FXCollections.observableArrayList();

	// product table.
	@FXML
	private TableView<ItemDTO> itemTable;

	@FXML
	private TableColumn<?, ?> barCodeColumn;

	@FXML
	private TableColumn<?, ?> nameColumn;
	@FXML
	private TableColumn<ItemDTO, String> categoryColumn;
	@FXML
	private TableColumn<?, ?> descriptionColumn;
	@FXML
	private TableColumn<?, ?> mrpColumn;
	@FXML
	private TableColumn<?, ?> revisedMrpColumn;
	@FXML
	private TableColumn<ItemDTO, String> stockColumn;

	@FXML
	private AutoCompleteTextField autoCompleteTextField;

	@FXML
	private Button editItemButton;

	@FXML
	private Label totalProductsLabel;

	@Autowired
	private ItemService itemService;
	
	@Autowired
	private UIValidator uiValidator;

	@Autowired
	private Environment env;


	private ItemDTO selectedItemRow;

	public ViewProductController() {
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		barCodeColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
		mrpColumn.setCellValueFactory(new PropertyValueFactory<>("mrp"));
		revisedMrpColumn.setCellValueFactory(new PropertyValueFactory<>("rrp"));

		stockColumn.setCellValueFactory(new Callback<CellDataFeatures<ItemDTO, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<ItemDTO, String> itemDto) {
				StockDTO stock = itemDto.getValue().getStock();
				return new SimpleStringProperty(String.valueOf(stock != null ? stock.getQuantity() : 0.0));
			}
		});

		categoryColumn.setCellValueFactory(new Callback<CellDataFeatures<ItemDTO, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<ItemDTO, String> itemDto) {
				CategoryDTO category = itemDto.getValue().getCategory();
				return new SimpleStringProperty(String.valueOf(category != null ? category.getCategoryName() : ""));
			}
		});

		loadItems();

		// autoCompleteTextField = new AutoCompleteTextField();
		if (autoCompleteTextField != null && autoCompleteTextField.getEntries() != null) {
			Collection<? extends String> populateNameAndBarcode = populateNameAndBarcode();
			if (populateNameAndBarcode != null && !populateNameAndBarcode.isEmpty()) {
				autoCompleteTextField.getEntries().addAll(populateNameAndBarcode);
			}
		}
		
		
		//Initializing row select event.
		itemTable.setOnMouseClicked((MouseEvent event) -> {
			if (event.getClickCount() == 1 && SecurityContext.isAdmin()) {
				rowSelected();
			}
		});
	}

	private Collection<? extends String> populateNameAndBarcode() {
		if (itemList != null && !itemList.isEmpty()) {
			List<String> barcodeNameList = new ArrayList<>();
			for (ItemDTO itemDTO : itemList) {
				String displayName = constructNameBarcodeDisplayFormat(itemDTO);
				if (displayName != null) {
					barcodeNameList.add(displayName);
				}
			}
			return barcodeNameList;
		}
		return null;
	}

	private String constructNameBarcodeDisplayFormat(ItemDTO itemDTO) {
		StringBuilder barcodNameFormat = new StringBuilder();
		/*if (itemDTO.getName() != null) {
			barcodNameFormat.append(itemDTO.getName());
		}
		if (itemDTO.getBarcode() != null && !itemDTO.getBarcode().isEmpty()) {
			barcodNameFormat.append(" ");
			barcodNameFormat.append(BARCODE_START_BRACKET);
			barcodNameFormat.append(itemDTO.getBarcode());
			barcodNameFormat.append(BARCODE_END_BRACKET);
		}*/
		//Changing it to description
		if(itemDTO.getDescription() != null) {
			barcodNameFormat.append(itemDTO.getDescription());
		}
		if (barcodNameFormat.toString().isEmpty()) {
			return null;
		}
		return barcodNameFormat.toString();
	}

	@FXML
	public void loadItemsOnClick() {
		loadItems();
	}

	public void loadItems() {
		itemList.clear();
		List<ItemDTO> items = itemService.getAll();
		itemList.addAll(items);
		itemTable.setItems(itemList);
		//Set Number of Products.
		totalProductsLabel.setText(items != null ? String.valueOf(items.size()) : "0");
	}

	@FXML
	public void searchName() {
		if (autoCompleteTextField.getText() != null && !autoCompleteTextField.getText().isEmpty()) {
			String name = retrieveName(autoCompleteTextField.getText());
			if (name != null && !name.isEmpty()) {
				List<ItemDTO> itemsFound = findDataInItemList(name);
				itemList.clear();
				itemList.addAll(itemsFound);
				itemTable.setItems(itemList);
				//Set Number of Products.
				totalProductsLabel.setText(itemList != null ? String.valueOf(itemList.size()) : "0");
			}
		} else {
			// loading all values.
			loadItems();
		}
	}

	private List<ItemDTO> findDataInItemList(String name) {

		if (itemList != null && !itemList.isEmpty()) {
			return itemList.stream().filter(e -> e.getDescription().equals(name)).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	private String retrieveName(String nameWithBarcode) {/*
		int startPosition = nameWithBarcode.indexOf(BARCODE_START_BRACKET);
		if (startPosition > 0) {
			return nameWithBarcode.substring(0, startPosition - 1);
		}
		return null;
	*/
		//Flat description.
		return nameWithBarcode;
	}

	@FXML
	public void clearNameSearch(Event event) {
		if (KeyEvents.isEnterKeyPressed(event)) {
			autoCompleteTextField.setText("");
			searchName();
		}
	}

	public void rowSelected() {
		// check the table's selected item and get selected item
		if (itemTable.getSelectionModel().getSelectedItem() != null) {
			ItemDTO itemDTO = itemTable.getSelectionModel().getSelectedItem();
			selectedItemRow = itemDTO;
			editItemButton.setDisable(false);
		}
	}

	@FXML
	public void editButtonClicked() {
		try {
			LOGGER.info("Edit Item Clicked with selected row : {}", selectedItemRow);
			//Setting the selected item into local storage.
			LocalStorage.setValue(ITEM_KEY, selectedItemRow);
			InvoiceStartup.navigate(HomeController.ADD_PRODUCT_PAGE);
		} catch (Exception e) {
			LOGGER.error("Failed to navigate to edit product. : {}" + e);
			uiValidator.showMessage(env.getProperty(MessageConstant.EDIT_ITEM_ERROR), MessageTypeEnum.ERROR);
		}

	}
}
