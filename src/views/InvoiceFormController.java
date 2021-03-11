package views;

import controllers.InvoiceController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Item;
import models.Order;

import java.util.ArrayList;

public class InvoiceFormController {
    public Label lblOrderId;
    public Label lblCustomerId;
    public Label lblTotalPrice;

    public TableColumn colDescription;
    public TableColumn colAmount;
    public TableColumn colPrice;
    public TableColumn colUnitPrice;

    public TableView invoiceItemsTable;


    public void initialize(){
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("orderedQty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("itemTotalPrice"));
    }

    public void createInvoice(Order order , ArrayList<Item> items){
        lblOrderId.setText(order.getId());
        lblCustomerId.setText(order.getCustomerId());
        lblTotalPrice.setText("Rs. " + InvoiceController.getTotalPrice(items));
        loadItemsInvoice(items);
    }

    private void loadItemsInvoice(ArrayList<Item> items){
        invoiceItemsTable.setItems(FXCollections.observableArrayList(items));
    }

    public void printInvoice(ActionEvent actionEvent) {

    }
}
