package views;

import controllers.CustomerController;
import controllers.DashboardController;
import controllers.ItemController;
import controllers.OrderController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Customer;
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
    public TableColumn colStatus;
    public TableView ordersTable;

    public ListView itemsListView;
    public static ArrayList<Item> itemListItems = new ArrayList<>();
    public static ArrayList<String> itemListItemsOnlyDescriptions = new ArrayList<>();

    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQtyOnHand;
    public TableView itemsTable;
    public ComboBox cmbCustomerList;

    public Label crntOrderTotal;

    public void initialize(){

        colOrderId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colItems.setCellValueFactory(new PropertyValueFactory<>("items"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));

        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));

        loadItems();
        loadOrders();

        //========render the table rows as the order status====>>>>
        ordersTable.setRowFactory(tv -> new TableRow<OrderDetails>() {
            @Override
            protected void updateItem(OrderDetails item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || item == null)
                    setStyle("");
                else if (item.getOrderStatus().equals("completed"))
                    setStyle("-fx-background-color: #a0d468;");
                else if (item.getOrderStatus().equals("pending"))
                    setStyle("-fx-background-color: #ed5565;");
                else if(item.getOrderStatus().equals("ready"))
                    setStyle("-fx-background-color: #48cffd;");
                else
                    setStyle("");
            }
        });

        //====================grab the customerIds and fill the combo box===================>
        fillCustomerCmbBox();

        //======================right click event listener on table=====================>>>>>>>
        MenuItem mi1 = new MenuItem("ready");
        MenuItem mi2 = new MenuItem("completed");
        mi1.setOnAction((ActionEvent event) -> {
            OrderDetails item = (OrderDetails) ordersTable.getSelectionModel().getSelectedItem();
            OrderController.setOrderStatusReady(item.getId());
            loadOrders();
        });
        mi2.setOnAction((ActionEvent event) -> {
            OrderDetails item = (OrderDetails) ordersTable.getSelectionModel().getSelectedItem();
            OrderController.setOrderStatusCompleted(item.getId());
            loadOrders();
        });

        ContextMenu menu = new ContextMenu();
        menu.getItems().add(mi1);
        menu.getItems().add(mi2);
        ordersTable.setContextMenu(menu);
    }

    public void fillCustomerCmbBox(){
        ArrayList<Customer> customerList = CustomerController.getAllCustomers();
        for(Customer customer: customerList){
            cmbCustomerList.getItems().add(customer.getId());
        }
    }

    public void selectCustomerIdCmb(ActionEvent actionEvent) {

    }

    private void loadOrders(){
        ArrayList<OrderDetails> ordersList = OrderController.getAllOrders();
        if(ordersList == null){
            new Alert(Alert.AlertType.ERROR, "no order records", ButtonType.CLOSE).show();
        }else{
            ordersTable.setItems(FXCollections.observableArrayList(ordersList));
        }
    }

    public void addOrder(ActionEvent actionEvent) {
        if(inputFieldValidator()){
            if(itemListItems.size() ==0){
                new Alert(Alert.AlertType.ERROR, "you must select at least one item to proceed", ButtonType.CLOSE).show();
            }else{
                String orderStatus = "pending";
                Order order = new Order(
                        txtOrderId.getText(),
                        LocalDate.now(),
                        (String) cmbCustomerList.getValue(),
                        orderStatus
                );

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
                    DashboardFormController.setTotalSalesDetail();
                    DashboardFormController.setTotalOrderedDetail();
                    DashboardFormController.setTotalLeftDetail();
                    clearFields();
                    clearLists();
                    loadOrders();
                    crntOrderTotal.setText("0");
                }else{
                    new Alert(Alert.AlertType.ERROR, "failed", ButtonType.CLOSE).show();
                }
            };
        }
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
            stage.setResizable(false);
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
                new Alert(Alert.AlertType.ERROR, "failed, cannot delete pending orders", ButtonType.CLOSE).show();
            }
        }
    }

    public void selectOrder(MouseEvent mouseEvent){
        if(mouseEvent.getButton() == MouseButton.PRIMARY){
            OrderDetails orderDetails =(OrderDetails) ordersTable.getSelectionModel().getSelectedItem();

            //add items to itemListItems and itemListItemsOnlyDescription
            ArrayList<Item> orderedItems = OrderController.getAllItemsOrdered(orderDetails.getId());
            for(Item item: orderedItems){
                item.setItemTotalPrice();
                itemListItems.add(item);
                itemListItemsOnlyDescriptions.add(item.getDescription());
            }

            txtOrderId.setText(orderDetails.getId());
            cmbCustomerList.setValue(orderDetails.getCustomerId());
            crntOrderTotal.setText(String.valueOf(orderDetails.getTotalPrice()));

            //adding item description to current selected item view
            itemsListView.setItems(FXCollections.observableArrayList(orderDetails.getItems().split(",")));
        }
    }



    public void selectItem(javafx.scene.input.MouseEvent mouseEvent) {
        if(txtOrderId.getText().equals("") || cmbCustomerList.getValue() == null){
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

                //update the order total label
                crntOrderTotal.setText(String.valueOf(Double.parseDouble(crntOrderTotal.getText()) + item.getItemTotalPrice()));

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
        if(txtOrderId.getText().equals("")|| cmbCustomerList.getValue() == null){
            new Alert(Alert.AlertType.ERROR, "cannot leave the fields empty", ButtonType.CLOSE).show();
            return false;
        }
        return true;
    }

    private void clearFields() {
        txtOrderId.setText("");
        cmbCustomerList.setValue(null);
        crntOrderTotal.setText("0");
    }

    private void clearLists(){
        itemListItems.clear();
        itemListItemsOnlyDescriptions.clear();
        itemsListView.setItems(FXCollections.observableArrayList(itemListItemsOnlyDescriptions));
    }

}
