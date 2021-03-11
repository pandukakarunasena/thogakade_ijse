package controllers;

import database.Database;
import models.Customer;
import models.Item;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemController {

    public static boolean addItem(Item item){
        try {
            String addItemQuery = "INSERT INTO item VALUES (?,?,?,?)";
            PreparedStatement ps = Database.getInstance().getConnection().prepareStatement(addItemQuery);


            if (searchItem(item.getCode()) == null) {
                ps.setString(1, item.getCode());
                ps.setString(2, item.getDescription());
                ps.setDouble(3, item.getUnitPrice());
                ps.setInt(4, item.getQtyOnHand());
                ps.execute();
            } else {
                throw new Exception();
            }
            ps.close();

        } catch (SQLException throwables) {
            System.out.println("SQLException");
            throwables.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.out.println("existing item description found");
            return false;
        }

        return true;
    }

    public static Item searchItem(String description) {
        Item item = null;

        try {
            String checkItemQuery = "SELECT * FROM item WHERE item.description=?";
            PreparedStatement ps = Database.getInstance().getConnection().prepareStatement(checkItemQuery);
            ps.setString(1,description);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                item = new Item(rs.getString("code"),
                        rs.getString("description"),
                        rs.getDouble("unit_price"),
                        rs.getInt("qty_on_hand")
                );
            }
            ps.close();

        } catch (SQLException | ClassNotFoundException throwables) {
            System.out.println("SQLException");
            throwables.printStackTrace();
        }
        return item;
    }

    public static Item deleteItem(String itemCode) {
        String deleteItem = "DELETE FROM item WHERE code=?";
        Item deletedItem = searchItem(itemCode);
        try {
            PreparedStatement ps = Database.getInstance().getConnection().prepareStatement(deleteItem);
            ps.setString(1,itemCode);
            ps.execute();

        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return deletedItem;
    }

    public static boolean editItem(Item item) {
        try{
            Item editItem = searchItem(item.getCode());
            String updateItem = "UPDATE item SET code=?, description=?, unit_price=?, qty_on_hand=? WHERE item.code=?" ;
            PreparedStatement ps = Database.getInstance().getConnection().prepareStatement(updateItem);
            ps.setString(1,item.getCode());
            ps.setString(2,item.getDescription());
            ps.setDouble(3,item.getUnitPrice());
            ps.setInt(4,item.getQtyOnHand());
            ps.setString(5,item.getCode());
            ps.execute();

        }catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static ArrayList<Item> getAllItems() {
        ArrayList<Item> itemList = new ArrayList<>();

        String getAllItems = "SELECT * FROM item";
        try {
            ResultSet rs = Database.getInstance().getConnection().createStatement().executeQuery(getAllItems);

            while(rs.next()){
                Item item = new Item(
                        rs.getString("code"),
                        rs.getString("description"),
                        rs.getDouble("unit_price"),
                        rs.getInt("qty_on_hand")
                );
                itemList.add(item);
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return itemList;
    }



}
