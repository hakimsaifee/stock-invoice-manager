package com.sim.view;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sim.dto.InvoiceDTO;
import com.sim.dto.ItemInvoiceDTO;
import com.sim.service.InvoiceService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

@Component
@Scope(value = "prototype")
public class DisplayInvoiceSummaryController implements Initializable {

	@Autowired
	private InvoiceService invoiceService;

	@FXML
	private DatePicker startDate;

	@FXML
	private DatePicker endDate;

	@FXML
	private TableView<InvoiceDTO> invoiceTable;

	@FXML
	private TableColumn<?, ?> invoiceNumberColumn;

	@FXML
	private TableColumn<?, ?> invoiceDateColumn;

	@FXML
	private TableColumn<?, ?> totalAmountColumn;

	@FXML
	private Label totalSell;

	@FXML
	private Label netProfit;

	private ObservableList<InvoiceDTO> invoiceDTOList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		startDate.setValue(LocalDate.now());
		endDate.setValue(LocalDate.now());

		invoiceNumberColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		invoiceDateColumn.setCellValueFactory(new PropertyValueFactory<>("createdTs"));
		totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
	}

	@FXML
	public void searchInvoices() {
		try {
			Timestamp sDate = Timestamp.valueOf(startDate.getValue().atStartOfDay());
			Timestamp eDate = Timestamp.valueOf(endDate.getValue().atStartOfDay());
			List<InvoiceDTO> invoiceDTOs = invoiceService.getInvoicesByDate(sDate, eDate);

			invoiceDTOList.clear();
			if (invoiceDTOs != null) {
				invoiceDTOList.addAll(invoiceDTOs);
				invoiceTable.setItems(invoiceDTOList);
				// calculate amount and profit.
				calculateAndSetAmount(invoiceDTOs);
				// invoiceTable.refresh();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void calculateAndSetAmount(List<InvoiceDTO> invoiceDTOs) {
		double totalSell = 0.00;
		double netProfit = 0.00;
		for (InvoiceDTO invoiceDTO : invoiceDTOs) {
			totalSell = totalSell + invoiceDTO.getTotalAmount();
			netProfit = netProfit + calculateNetProfit(invoiceDTO);
		}
		this.totalSell.setText(String.valueOf(totalSell));
		this.netProfit.setText(String.valueOf(netProfit));
	}

	private double calculateNetProfit(InvoiceDTO invoiceDTO) {
		double calculatedProfit = 0.00;
		if (invoiceDTO.getItemInvoices() != null && !invoiceDTO.getItemInvoices().isEmpty()) {
			for (ItemInvoiceDTO itemInvoiceDTO : invoiceDTO.getItemInvoices()) {
				double amountDifference = itemInvoiceDTO.getItem().getRrp() - itemInvoiceDTO.getItem().getCostPrice();
				calculatedProfit = calculatedProfit + (amountDifference * itemInvoiceDTO.getPurchasedQuantity());
			}

			// take discount into consideration.
			// not considering additional charges into profit.
			calculatedProfit = calculatedProfit - invoiceDTO.getDiscount();
		}
		return calculatedProfit;
	}

}
