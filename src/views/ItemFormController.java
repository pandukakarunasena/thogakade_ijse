package views;

import controllers.ItemController;
import controllers.ItemController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Item;
import models.Item;

import java.io.IOException;
import java.util.ArrayList;

public class ItemFormController {
    public TextField txtCode;
    public TextField txtDescription;
    public TextField txtUnitPrice;
    public TextField txtQtyOnHand;

    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQtyOnHand;
    public TableView itemsTable;

    public void initialize(){
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));

        loadItems();
    }

    public AnchorPane itemFormPane;

    public void addItem(ActionEvent actionEvent) {
        if(inputFieldValidator()){
            boolean isAdded = ItemController.addItem(
                    new Item(
                            txtCode.getText(),
                            txtDescription.getText(),
                            Double.parseDouble(txtUnitPrice.getText()),
                            Integer.parseInt(txtQtyOnHand.getText())
                    )
            );

            if(isAdded){
                new Alert(Alert.AlertType.CONFIRMATION, "success", ButtonType.OK).show();
                loadItems();
                clearFields();
            }
            if(!isAdded){
                new Alert(Alert.AlertType.ERROR, "failed", ButtonType.CLOSE).show();
            }
        };

    }

    public void editItem(ActionEvent actionEvent) {
        if(inputFieldValidator()){
            boolean hasEdited = ItemController.editItem(new Item(
                    txtCode.getText(),
                    txtDescription.getText(),
                    Double.parseDouble(txtUnitPrice.getText()),
                    Integer.parseInt(txtQtyOnHand.getText())
            ));
            if(hasEdited){
                new Alert(Alert.AlertType.CONFIRMATION, "success", ButtonType.OK).show();
                loadItems();
                clearFields();
            }else{
                new Alert(Alert.AlertType.ERROR, "failed", ButtonType.CLOSE).show();
            }
        };

    }

    public void deleteItem(ActionEvent actionEvent) {
        if(txtCode.getText().equals("")){
            new Alert(Alert.AlertType.ERROR, "specify an item code", ButtonType.CLOSE).show();
        }else{
            Item deletedItem = ItemController.deleteItem(txtCode.getText());
            loadItems();
            clearFields();
        }

    }



    public void back(ActionEvent actionEvent) throws IOException {
        Stage window = (Stage) itemFormPane.getScene().getWindow();
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("dashboard.fxml"))));
        window.centerOnScreen();
    }

    public void reloadItemTable(ActionEvent actionEvent) {
        loadItems();
    }

    public void selectItem(javafx.scene.input.MouseEvent mouseEvent) {
        Item item = (Item) itemsTable.getSelectionModel().getSelectedItem();
        txtCode.setText(item.getCode()+"");
        txtDescription.setText(item.getDescription());
        txtUnitPrice.setText(String.valueOf(item.getUnitPrice()));
        txtQtyOnHand.setText(String.valueOf(item.getQtyOnHand()));
    }

    private void loadItems() {
        ArrayList<Item> itemsList = ItemController.getAllItems();
        if(itemsList == null){
            new Alert(Alert.AlertType.ERROR, "no item records", ButtonType.CLOSE).show();
        }else{
            itemsTable.setItems(FXCollections.observableArrayList(itemsList));
        }
    }

    private void clearFields(){
        txtCode.setText("");
        txtDescription.setText("");
        txtUnitPrice.setText("");
        txtQtyOnHand.setText("");
    }

    private boolean inputFieldValidator(){
        if(txtCode.getText().equals("")||
                txtUnitPrice.getText().equals("")||
                txtDescription.getText().equals("")||
                txtQtyOnHand.getText().equals("")
        ){
            new Alert(Alert.AlertType.ERROR, "cannot leave the fields empty", ButtonType.CLOSE).show();
            return false;
        }
        return true;
    }
}
