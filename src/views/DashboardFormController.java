package views;

import controllers.CustomerController;
import controllers.DashboardController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Customer;
import models.ItemAvailable;
import models.OrderRecent;

import java.io.IOException;
import java.util.ArrayList;

public class DashboardFormController {
    public AnchorPane dashboardPane;

    public TableView dbRecentOrdersTable;
    public TableColumn colOrderIdRecOrd;
    public TableColumn colCustomerNameRecOrd;
    public TableColumn colTotalPriceRecOrd;
    public TableColumn colDateRecOrd;

    public TableView dbItemAvalTable;
    public TableColumn colCodeItemAval;
    public TableColumn colDescriptionItemAval;
    public TableColumn colUnitItemAval;

    public TableView dbCompletedOrdersTable;
    public ListView newlyAddedCustomerListView;

    public Label lblStorage;
    public Label lblTotalSales;
    public Label lblItemOrdered;

    private static int totalSales, totalOrdered, totalLeft ;
    private int countUpSales, countUpOrdered, countUpLeft = 0;


    public void initialize(){
        //item availability table
        colCodeItemAval.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescriptionItemAval.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitItemAval.setCellValueFactory(new PropertyValueFactory<>("units"));
        loadItemAvailability();

        //recent orders table
        colOrderIdRecOrd.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustomerNameRecOrd.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colTotalPriceRecOrd.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        colDateRecOrd.setCellValueFactory(new PropertyValueFactory<>("date"));
        loadRecentOrders();

        if(totalOrdered == 0){
           totalOrdered = DashboardController.getTotalItemsOrdered();
        }
        if(totalLeft == 0){
            totalLeft = DashboardController.getTotalItemsLeft();
        }
        if(totalSales == 0){
            totalSales = DashboardController.getTotalSales();
        }

        animationTotalSales.setCycleCount(Timeline.INDEFINITE);
        animationTotalSales.play();

        animationItemsOrdered.setCycleCount(Timeline.INDEFINITE);
        animationItemsOrdered.play();

        animationItemsLeft.setCycleCount(Timeline.INDEFINITE);
        animationItemsLeft.play();

    }

    public static void setTotalSalesDetail(){
        totalSales = DashboardController.getTotalSales();
    }
    public static void setTotalLeftDetail(){ totalLeft = DashboardController.getTotalItemsLeft(); }
    public static void setTotalOrderedDetail(){
        totalOrdered = DashboardController.getTotalItemsOrdered();
    }

    public void addCustomer(ActionEvent actionEvent) throws IOException {
        Stage window = (Stage) dashboardPane.getScene().getWindow();
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("customerForm.fxml"))));
        window.centerOnScreen();
    }

    public void addItem(ActionEvent actionEvent) throws IOException {
        Stage window = (Stage) dashboardPane.getScene().getWindow();
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("itemForm.fxml"))));
        window.centerOnScreen();
    }

    public void addNewOrder(ActionEvent actionEvent) throws IOException {
        Stage window = (Stage) dashboardPane.getScene().getWindow();
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource("orderForm.fxml"))));
        window.centerOnScreen();
    }

    private void loadRecentOrders(){
        ArrayList<OrderRecent> recentOrderList = DashboardController.getRecentOrders();
        if(recentOrderList == null){
            new Alert(Alert.AlertType.ERROR, "no records to show", ButtonType.CLOSE).show();
        }else{
            dbRecentOrdersTable.setItems(FXCollections.observableArrayList(recentOrderList));
        }
    }

    private void loadItemAvailability(){
        ArrayList<ItemAvailable> availableItemList = DashboardController.getItemAvailability();
        if(availableItemList == null){
            new Alert(Alert.AlertType.ERROR, "no records to show", ButtonType.CLOSE).show();
        }else{
            dbItemAvalTable.setItems(FXCollections.observableArrayList(availableItemList));
        }

    }


    //==========================================ANIMATIONS=====================================================>
    Timeline animationItemsLeft = new Timeline(new KeyFrame(Duration.millis(0.5), e -> countUpItemsLeft()));
    Timeline animationItemsOrdered = new Timeline(new KeyFrame(Duration.millis(1), e -> countUpItemsOrdered()));
    Timeline animationTotalSales = new Timeline(new KeyFrame(Duration.millis(1), e -> countUpTotalSales()));

    private void countUpItemsLeft(){
        if(countUpLeft == totalLeft){
            animationItemsLeft.stop();
        }
        lblStorage.setText(Integer.toString(countUpLeft++));
    }
    private void countUpItemsOrdered(){
        if(countUpOrdered == totalOrdered){
            animationItemsOrdered.stop();
        }
        lblItemOrdered.setText(Integer.toString(countUpOrdered++));
    }
    private void countUpTotalSales(){
        if(countUpSales >= totalSales){
            animationTotalSales.stop();
        }
        countUpSales += 100;
        lblTotalSales.setText(Integer.toString(countUpSales));
    }
}
