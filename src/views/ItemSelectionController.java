package views;

import controllers.ItemController;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.Item;

import java.util.ArrayList;

public class ItemSelectionController {


    public TableView itemsTable;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colQtyOnHand;

    public void initialize(){
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        loadItems();
    }

    public void selectItem(MouseEvent mouseEvent) {
        Item selectedItem =(Item) itemsTable.getSelectionModel().getSelectedItem();

    }

    private void loadItems(){
        ArrayList<Item> itemList = ItemController.getAllItems();
        if(itemList == null){
            new Alert(Alert.AlertType.ERROR, "no item records", ButtonType.CLOSE).show();
        }else{
            itemsTable.setItems(FXCollections.observableArrayList(itemList));
        }
    }
}
