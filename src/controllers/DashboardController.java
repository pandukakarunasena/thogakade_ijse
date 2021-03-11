package controllers;

import database.Database;
import models.ItemAvailable;
import models.OrderDetails;
import models.OrderRecent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DashboardController {
    public static ArrayList<OrderRecent> getRecentOrders() {
        ArrayList<OrderRecent> recentOrderList = new ArrayList<>();
        String getRecentOrders = "select orderdetail.orderId, orderinfo.name, orderinfo.address,orderinfo.date, group_concat(item.description) as items, (sum(orderdetail.qty*orderdetail.unitPrice)) as total_price\n" +
                "from orderdetail\n" +
                "inner join item\n" +
                "on orderdetail.itemCode= item.code\n" +
                "inner join (select orders.id as orderID, customer.name, customer.address, orders.date\n" +
                "\t\t\tfrom orders\n" +
                "\t\t\tinner join customer \n" +
                "\t\t\ton orders.customerId = customer.id) as orderinfo\n" +
                "on orderdetail.orderId = orderinfo.orderId\n" +
                "group by orderdetail.orderId\n" +
                "order by orderinfo.date DESC\n" +
                "limit 5;\n";
        try {
            ResultSet rs = Database.getInstance().getConnection().createStatement().executeQuery(getRecentOrders);
            while(rs.next()){
                OrderRecent orderRecent = new OrderRecent(
                        rs.getString("orderId"),
                        rs.getString("name"),
                        rs.getDouble("total_price"),
                        rs.getDate("date")
                );
                recentOrderList.add(orderRecent);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return recentOrderList;

    }

    public static ArrayList<ItemAvailable> getItemAvailability() {
        String getItemAvailability = "select * from item order by qty_on_hand asc";
        ArrayList<ItemAvailable> itemAvalList = new ArrayList<>();
        try {
            ResultSet rs = Database.getInstance().getConnection().createStatement().executeQuery(getItemAvailability);
            while(rs.next()){
                ItemAvailable item = new ItemAvailable(
                        rs.getString("code"),
                        rs.getString("description"),
                        rs.getInt("qty_on_hand")
                );

                itemAvalList.add(item);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return itemAvalList;

    }

    public static int getTotalItemsLeft() {
        String getItemsLeft = "SELECT sum(qty_on_hand) as sum from item";
        int itemLeft = 0;
        try {
            ResultSet rs = Database.getInstance().getConnection().createStatement().executeQuery(getItemsLeft);
            rs.next();
            itemLeft = rs.getInt("sum");
        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return itemLeft;
    }

    public static int getTotalSales() {
        String getTotalSales = "SELECT sum(qty*unitPrice) as total FROM orderdetail";
        int totalSales = 0;
        try {
            ResultSet rs = Database.getInstance().getConnection().createStatement().executeQuery(getTotalSales);
            rs.next();
            totalSales =(int) rs.getDouble("total");
        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return totalSales;
    }

    public static int getTotalItemsOrdered() {
        String getItemsOrdered = "SELECT sum(qty) as total from orderdetail";
        int itemLeft =0;
        try {
            ResultSet rs = Database.getInstance().getConnection().createStatement().executeQuery(getItemsOrdered);
            rs.next();
            itemLeft = rs.getInt("total");
        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return itemLeft;
    }
}
