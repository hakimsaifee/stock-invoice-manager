package com.sim.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sim.dto.ItemDTO;
import com.sim.dto.StockDTO;
import com.sim.service.ItemService;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

@Component
public class ViewProductController implements Initializable {

	private ObservableList<ItemDTO> itemList = FXCollections.observableArrayList();

	// product table.
	@FXML
	private TableView<ItemDTO> itemTable;

	@FXML
	private TableColumn<?, ?> barCodeColumn;

	@FXML
	private TableColumn<?, ?> nameColumn;
	@FXML
	private TableColumn<?, ?> categoryColumn;
	@FXML
	private TableColumn<?, ?> descriptionColumn;
	@FXML
	private TableColumn<?, ?> mrpColumn;
	@FXML
	private TableColumn<?, ?> revisedMrpColumn;
	@FXML
	private TableColumn<ItemDTO, String> stockColumn;

	@Autowired
	private ItemService itemService;

	public ViewProductController() {
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		barCodeColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
		mrpColumn.setCellValueFactory(new PropertyValueFactory<>("mrp"));
		revisedMrpColumn.setCellValueFactory(new PropertyValueFactory<>("rrp"));
		stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock.name"));

		stockColumn.setCellValueFactory(new Callback<CellDataFeatures<ItemDTO, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<ItemDTO, String> itemDto) {
				StockDTO stock = itemDto.getValue().getStock();
				return new SimpleStringProperty(String.valueOf(stock != null ? stock.getQuantity() : 0.0));
			}
		});

		loadItems();
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
	}
}
