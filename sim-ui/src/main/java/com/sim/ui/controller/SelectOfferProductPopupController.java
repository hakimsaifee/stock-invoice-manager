package com.sim.ui.controller;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sim.common.constant.OfferTypeEnum;
import com.sim.dto.ItemDTO;
import com.sim.dto.ItemInvoiceDTO;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

@Component
public class SelectOfferProductPopupController extends Application implements Initializable {

	private final static Logger LOGGER = LoggerFactory.getLogger(SelectOfferProductPopupController.class);

	private Stage stage;

	@FXML
	Button selectButton;

	@FXML
	private TableView<ItemInvoiceDTO> selectOfferProductTable;

	@FXML
	private TableColumn<ItemInvoiceDTO, String> offerTypeColumn;

	@FXML
	private TableColumn<ItemInvoiceDTO, String> productNameColumn;

	@FXML
	private TableColumn<ItemInvoiceDTO, String> mrpColumn;

	@FXML
	private TableColumn<?, ?> offerPriceColumn;

	@FXML
	private TableColumn<?, ?> offerQuantityColumn;

	private ObservableList<ItemInvoiceDTO> selectOfferItems = FXCollections.observableArrayList();

	private ItemInvoiceDTO selectedRow;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/*
		 * selectButton.setOnAction((event) -> {
		 * 
		 * closePopup(); });
		 */
		offerTypeColumn
				.setCellValueFactory(new Callback<CellDataFeatures<ItemInvoiceDTO, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<ItemInvoiceDTO, String> itemInvoiceDTO) {
						return new SimpleStringProperty(itemInvoiceDTO.getValue() != null
								? itemInvoiceDTO.getValue().getOfferType().getName() : "");
					}
				});

		offerQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("purchasedQuantity"));
		offerPriceColumn.setCellValueFactory(new PropertyValueFactory<>("productSellingPrice"));

		mrpColumn
				.setCellValueFactory(new Callback<CellDataFeatures<ItemInvoiceDTO, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<ItemInvoiceDTO, String> itemInvoiceDTO) {
						ItemDTO itemDTO = itemInvoiceDTO.getValue().getItem();
						return new SimpleStringProperty(String.valueOf(itemDTO != null ? itemDTO.getMrp() : 0.0));
					}
				});

		productNameColumn
				.setCellValueFactory(new Callback<CellDataFeatures<ItemInvoiceDTO, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<ItemInvoiceDTO, String> itemInvoiceDTO) {
						ItemDTO itemDTO = itemInvoiceDTO.getValue().getItem();
						return new SimpleStringProperty(itemDTO != null ? itemDTO.getName() : "");
					}
				});

		
		//Row double-click event handler
		selectOfferProductTable.setOnMouseClicked((MouseEvent event) -> {
			if (event.getClickCount() == 2) {
				saveAndClosePoup();
			}
		});
	}

	private void saveAndClosePoup() {
		if (selectOfferProductTable.getSelectionModel().getSelectedItem() != null) {
			ItemInvoiceDTO itemInvoiceDTO = selectOfferProductTable.getSelectionModel().getSelectedItem();
			this.selectedRow = itemInvoiceDTO;
			closePopup();
		}
	}

	public ItemInvoiceDTO openPopup(ItemDTO inputData) {
		loadProductOfferTable(inputData);
		this.getStage().showAndWait();
		return selectedRow;
	}

	private void loadProductOfferTable(ItemDTO inputData) {
		createOfferProductDetails(inputData);
	}

	private void createOfferProductDetails(ItemDTO inputData) {
		// With offer row.
		ItemInvoiceDTO withOffer = new ItemInvoiceDTO();
		withOffer.setItem(inputData);
		withOffer.setPurchasedQuantity(inputData.getOfferQuantity());
		withOffer.setProductSellingPrice(inputData.getOfferPrice());
		withOffer.setCreatedTs(new Timestamp(System.currentTimeMillis()));
		withOffer.setOfferType(OfferTypeEnum.WITH_OFFER);
		// Without offer row.
		ItemInvoiceDTO withoutOffer = new ItemInvoiceDTO();
		withoutOffer.setItem(inputData);
		withoutOffer.setPurchasedQuantity(1d);
		withoutOffer.setProductSellingPrice(inputData.getRrp());
		withoutOffer.setCreatedTs(new Timestamp(System.currentTimeMillis()));
		withoutOffer.setOfferType(OfferTypeEnum.WITHOUT_OFFER);
		
		selectOfferItems.clear();
		selectOfferItems.add(withOffer);
		selectOfferItems.add(withoutOffer);
		
		selectOfferProductTable.setItems(selectOfferItems);

	}

	public void closePopup() {
		if (stage != null) {
			stage.close();
		}
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
	}
}
