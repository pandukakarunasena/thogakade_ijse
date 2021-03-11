package views;

import controllers.ItemController;
import controllers.OrderController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Item;
import models.Order;
import models.OrderDetails;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class OrderFormController {
    public AnchorPane orderFormPane;

    public TextField txtOrderId;
    public TextField txtCustomer;

    public TableColumn colOrderId;
    public TableColumn colCustomer;
    public TableColumn colItems;
    public TableColumn colDate;
    public TableColumn colPrice;
    public TableColumn colAddress;
    public TableView ordersTable;

    public ListView itemsListView;
    public static ArrayList<Item> itemListItems = new ArrayList<>();
    public static ArrayList<String> itemListItemsOnlyDescriptions = new ArrayList<>();

    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQtyOnHand;
    public TableView itemsTable;

    public void initialize(){
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colItems.setCellValueFactory(new PropertyValueFactory<>("items"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));

        loadItems();
        loadOrders();
    }

    public void addOrder(ActionEvent actionEvent) {
        if(inputFieldValidator()){
            if(itemListItems.size() ==0){
                new Alert(Alert.AlertType.ERROR, "you must select at least one item to proceed", ButtonType.CLOSE).show();
            }else{
                Order order = new Order(
                        txtOrderId.getText(),
                        LocalDate.now(),
                        txtCustomer.getText());

                boolean isAdded = OrderController.addOrder(order, itemListItems);

                if(isAdded){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "successful", ButtonType.CLOSE);
                    ButtonType buttonTypeOne = new ButtonType("Show Invoice");
                    ButtonType buttonTypeTwo = new ButtonType("OK");
                    alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == buttonTypeOne){
                        showOrderInvoice(order, itemListItems);
                    }
                    if(result.get() ==buttonTypeTwo){
                        alert.close();
                    }
                    clearFields();
                    clearLists();
                    loadOrders();
                }else{
                    new Alert(Alert.AlertType.ERROR, "failed", ButtonType.CLOSE).show();
                }
            };
        }
    }
    private void clearFields() {
        txtOrderId.setText("");
        txtCustomer.setText("");

    }
    private void clearLists(){
        itemListItems.clear();
        itemListItemsOnlyDescriptions.clear();
        itemsListView.setItems(FXCollections.observableArrayList(itemListItemsOnlyDescriptions));
    }

    private void showOrderInvoice(Order order, ArrayList<Item> itemListItems){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("invoice.fxml"));
            Parent selectionWindow = loader.load();
            InvoiceFormController controller = loader.getController();
            controller.createInvoice(order, itemListItems);

            Stage stage = new Stage();
            stage.setTitle("invoice");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(selectionWindow, 350, 400));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void editOrder(ActionEvent actionEvent) {
    }

    public void deleteOrder(ActionEvent actionEvent) {
        if(txtOrderId.getText().equals("")){
            new Alert(Alert.AlertType.ERROR, "specify an order ID", ButtonType.CLOSE).show();

        }else{
            boolean isDeleted = OrderController.deleteOrder(txtOrderId.getText());
            if(isDeleted){
                new Alert(Alert.AlertType.CONFIRMATION, "order deleted", ButtonType.CLOSE).show();
                clearLists();
                clearFields();
                loadOrders();
            }else{
                new Alert(Alert.AlertType.ERROR, "failed", ButtonType.CLOSE).show();
            }
        }
    }

    public void selectOrder(MouseEvent mouseEvent){
        OrderDetails orderDetails =(OrderDetails) ordersTable.getSelectionModel().getSelectedItem();

        //add items to itemListItems and itemListItemsOnlyDescription
        ArrayList<Item> orderedItems = OrderController.getAllItemsOrdered(orderDetails.getId());
        for(Item item: orderedItems){
            item.setItemTotalPrice();
            itemListItems.add(item);
            itemListItemsOnlyDescriptions.add(item.getDescription());
        }

        txtOrderId.setText(orderDetails.getId());
        txtCustomer.setText(orderDetails.getName());

        //adding item description to current selected item view
        itemsListView.setItems(FXCollections.observableArrayList(orderDetails.getItems().split(",")));
    }

    private void loadOrders(){
        ArrayList<OrderDetails> ordersList = OrderController.getAllOrders();
        if(ordersList == null){
            new Alert(Alert.AlertType.ERROR, "no order records", ButtonType.CLOSE).show();
        }else{
            ordersTable.setItems(FXCollections.observableArrayList(ordersList));
        }
    }

    public void selectItem(javafx.scene.input.MouseEvent mouseEvent) {
        if(txtOrderId.getText().equals("") || txtCustomer.getText().equals("")){
            new Alert(Alert.AlertType.ERROR, "cannot leave the fields empty", ButtonType.CLOSE).show();
        }else{
            Item item = (Item) itemsTable.getSelectionModel().getSelectedItem();

            //dialog box to enter the amount needed
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Item Amount");
            dialog.setContentText("Enter the amount needed:");
            dialog.setHeaderText(null);
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){
                item.setOrderedQty(Integer.parseInt(result.get()));
                item.setItemTotalPrice();
                //subtract from the qty_on_hand in the item table
                OrderController.subFromItemTable(item.getCode(), Integer.parseInt(result.get()));
                loadItems();
                //add items to send with the order
                itemListItems.add(item);

                //render selected items in the list view
                itemListItemsOnlyDescriptions.add(item.getDescription());
                itemsListView.setItems(FXCollections.observableArrayList(itemListItemsOnlyDescriptions));
                System.out.println("Added: " + result.get());
            }
        }

    }

    private void loadItems() {
        ArrayList<Item> itemsList = ItemController.getAllItems();
        if(itemsList == null){
            new Alert(Alert.AlertType.ERROR, "no item records", ButtonType.CLOSE).show();
        }else{
            itemsTable.setItems(FXCollections.observableArrayList(itemsList));
        }
    }

    public void back(ActionEvent actionEvent) throws IOException {
        Stage window = (Stage) orderFormPane.getScene().getWindow();
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("dashboard.fxml"))));
        window.centerOnScreen();
    }

    public void clearFieldsAndLists(ActionEvent actionEvent) {
        clearFields();
        clearLists();
    }

    private boolean inputFieldValidator(){
        if(txtOrderId.getText().equals("")||
                txtCustomer.getText().equals("")
        ){
            new Alert(Alert.AlertType.ERROR, "cannot leave the fields empty", ButtonType.CLOSE).show();
            return false;
        }
        return true;
    }
}
