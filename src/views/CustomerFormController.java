package views;

import controllers.CustomerController;
import database.Database;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Customer;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.ArrayList;


public class CustomerFormController {

    public TableColumn colCustomerId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colSalary;
    public TableView customerTable;

    public TextField txtCustomerId;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtSalary;

    public Button btnAdd;
    public Button btnEdit;
    public Button btnDelete;

    public Pane addCustomerPane;



    public void initialize(){
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));

        loadCustomers();
    }

    //add customer
    public void onActionPerformedAdd(ActionEvent actionEvent) {
        if(inputFieldValidator()){
            boolean isAdded = CustomerController.addCustomer(new Customer(
                    txtCustomerId.getText(),
                    txtName.getText(),
                    txtAddress.getText(),
                    Double.parseDouble(txtSalary.getText())
            ));

            if(isAdded){
                new Alert(Alert.AlertType.CONFIRMATION, "success", ButtonType.OK).show();
            }
            if(!isAdded){
                new Alert(Alert.AlertType.ERROR, "failed", ButtonType.CLOSE).show();
            }

            loadCustomers();
            clearFields();
        }

    }

    //edit customer
    public void onActionPerformedEdit(ActionEvent actionEvent) {
        if(inputFieldValidator()){
            boolean hasEdited = CustomerController.editCustomer(new Customer(
                    txtCustomerId.getText(),
                    txtName.getText(),
                    txtAddress.getText(),
                    Double.parseDouble(txtSalary.getText())
            ));

            if(hasEdited){
                new Alert(Alert.AlertType.CONFIRMATION, "success", ButtonType.OK).show();
                loadCustomers();
                clearFields();
            }else{
                new Alert(Alert.AlertType.ERROR, "failed", ButtonType.CLOSE).show();
            }
        }
    }

    //delete customer
    public void onActionPerformedDelete(ActionEvent actionEvent) {
        if(txtCustomerId.getText().equals("")){
            new Alert(Alert.AlertType.ERROR, "customer ID is empty", ButtonType.CLOSE).show();
        }else{
            Customer deletedCustomer = CustomerController.deleteCustomer(txtCustomerId.getText());
            loadCustomers();
            clearFields();
        }
    }

    //search customer
    public void onActionPerformedSearch(ActionEvent actionEvent) {
        if(txtCustomerId.getText().equals("")){
            new Alert(Alert.AlertType.ERROR, "specify a customer ID", ButtonType.CLOSE).show();
        }else{
            Customer customerFound = CustomerController.searchCustomer(txtCustomerId.getText());
            if(customerFound != null){
                txtName.setText(customerFound.getName());
                txtAddress.setText(customerFound.getAddress());
                txtSalary.setText(String.valueOf(customerFound.getSalary()));
            }else{
                new Alert(Alert.AlertType.ERROR, "no customer record", ButtonType.CLOSE).show();
            }
            clearFields();
        }

    }

    public void back(ActionEvent actionEvent) throws IOException {
        Stage window = (Stage) addCustomerPane.getScene().getWindow();
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("dashboard.fxml"))));
        window.centerOnScreen();
    }

    public void selectCustomer(javafx.scene.input.MouseEvent mouseEvent) {
        Customer customer = (Customer) customerTable.getSelectionModel().getSelectedItem();
        txtCustomerId.setText(customer.getId());
        txtName.setText(customer.getName());
        txtAddress.setText(customer.getAddress());
        txtSalary.setText(String.valueOf(customer.getSalary()));
    }


    public void reloadTable(ActionEvent actionEvent) {
       loadCustomers();
    }

    private void loadCustomers(){
        ArrayList<Customer> customerList = CustomerController.getAllCustomers();
        if(customerList == null){
            new Alert(Alert.AlertType.ERROR, "no customer records", ButtonType.CLOSE).show();
        }else{
            customerTable.setItems(FXCollections.observableArrayList(customerList));
        }
    }

    private void clearFields(){
        txtCustomerId.setText("");
        txtSalary.setText("");
        txtName.setText("");
        txtAddress.setText("");
    }

    private boolean inputFieldValidator(){
        if(txtCustomerId.getText().equals("")||
            txtSalary.getText().equals("")||
            txtAddress.getText().equals("")||
            txtName.getText().equals("")
        ){
            new Alert(Alert.AlertType.ERROR, "cannot leave the fields empty", ButtonType.CLOSE).show();
            return false;
        }
        return true;
    }


}
