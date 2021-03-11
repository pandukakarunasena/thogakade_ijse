package controllers;

import database.Database;
import models.Item;
import models.ItemOrdered;
import models.Order;
import models.OrderDetails;

import java.sql.*;
import java.util.ArrayList;

public class OrderController {
    public static ArrayList<OrderDetails> getAllOrders() {
        ArrayList<OrderDetails> ordersList = new ArrayList<>();
        try{
            String getAllOrders = "select orderdetail.orderId, orderinfo.name, orderinfo.address,orderinfo.date, group_concat(item.description) as items, (sum(orderdetail.qty*orderdetail.unitPrice)) as total_price\n" +
                    "from orderdetail\n" +
                    "inner join item\n" +
                    "on orderdetail.itemCode= item.code\n" +
                    "inner join (select orders.id as orderID, customer.name, customer.address, orders.date\n" +
                    "\t\t\tfrom orders\n" +
                    "\t\t\tinner join customer \n" +
                    "\t\t\ton orders.customerId = customer.id) as orderinfo\n" +
                    "on orderdetail.orderId = orderinfo.orderId\n" +
                    "group by orderdetail.orderId;\n" +
                    "\n";

            ResultSet rs = Database.getInstance().getConnection().createStatement().executeQuery(getAllOrders);
            while (rs.next()){
                OrderDetails order = new OrderDetails(
                        rs.getString("orderId"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getDate("date"),
                        rs.getString("items"),
                        rs.getDouble("total_price")
                );
                ordersList.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ordersList;
    }

    public static ArrayList<Item> getAllItemsOrdered(String orderId){
        ArrayList<Item> allItemsOrdered = new ArrayList<>();
        String getAllItemsPerOrder = "select item.code, item.description, item.unit_price, item.qty_on_hand, orderdetail.qty\n" +
                "from orderdetail\n" +
                "inner join item\n" +
                "on orderdetail.itemCode = item.code\n" +
                "where orderdetail.orderId =?";

        try {
            PreparedStatement ps = Database.getInstance().getConnection().prepareStatement(getAllItemsPerOrder);
            ps.setString(1,orderId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Item item = new ItemOrdered(
                        rs.getString("code"),
                        rs.getString("description"),
                        rs.getDouble("unit_price"),
                        rs.getInt("qty_on_hand"),
                        rs.getInt("qty")
                );

                allItemsOrdered.add(item);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return allItemsOrdered;
    }

    public static boolean addOrder(Order order, ArrayList<Item> itemListItems) {
        String addOrderToOrderTable = "INSERT INTO orders VALUES(?,?,?)";
        try {
            PreparedStatement psOrder = Database.getInstance().getConnection().prepareStatement(addOrderToOrderTable);
            psOrder.setString(1,order.getId());
            psOrder.setDate(2, Date.valueOf(order.getDate()));
            psOrder.setString(3,order.getCustomerId());
            psOrder.execute();

            //for loop
            for(Item item: itemListItems){
                String addOrderItemsToOrderDetailTable = "INSERT INTO orderdetail VALUES(?,?,?,?)";
                PreparedStatement psOrderDetail = Database.getInstance().getConnection().prepareStatement(addOrderItemsToOrderDetailTable);
                psOrderDetail.setString(1, order.getId());
                psOrderDetail.setString(2,item.getCode());
                psOrderDetail.setInt(3,item.getOrderedQty());
                psOrderDetail.setDouble(4,item.getUnitPrice());
                psOrderDetail.execute();
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    public static boolean deleteOrder(String orderId) {
        String deleteOrder = "DELETE FROM orders WHERE id=?";
        PreparedStatement ps = null;
        try {
            ps = Database.getInstance().getConnection().prepareStatement(deleteOrder);
            ps.setString(1,orderId);
            ps.execute();

        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void subFromItemTable(String code, int amount) {

        String getItemQty = "SELECT item.qty_on_hand FROM item WHERE item.code=?";
        String subItemQty = "UPDATE item SET item.qty_on_hand=qty_on_hand-? WHERE item.code=?";
        try {
            PreparedStatement psGet =Database.getInstance().getConnection().prepareStatement(getItemQty);
            psGet.setString(1,code);
            ResultSet rsGet = psGet.executeQuery();
            rsGet.next();
            if( rsGet.getInt("qty_on_hand") < amount){
                throw new Error("not enough items");
            }else{
                PreparedStatement psSet = Database.getInstance().getConnection().prepareStatement(subItemQty);
                psSet.setInt(1,amount);
                psSet.setString(2,code);
                psSet.execute();
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
