package controllers;

import database.Database;
import javafx.scene.control.TextField;
import middlewares.CustomException;
import models.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;



public class CustomerController {


    public static boolean addCustomer(Customer customer){
        try {
            String addCustomerQuery = "INSERT INTO customer VALUES (?,?,?,?)";
            PreparedStatement ps = Database.getInstance().getConnection().prepareStatement(addCustomerQuery);


            if (searchCustomer(customer.getName()) == null) {
                ps.setString(1, customer.getId());
                ps.setString(2, customer.getName());
                ps.setString(3, customer.getAddress());
                ps.setDouble(4, customer.getSalary());
                ps.execute();
            } else {
                throw new Exception();
            }
            ps.close();

        } catch (CustomException throwables) {
            System.out.println("SQLException");
            throwables.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.out.println("existing customer name found");
            return false;
        }

        return true;
    }

    public static Customer searchCustomer(String customerId) {
        Customer customer = null;
        try {
            String checkCustomerQuery = "SELECT * FROM customer WHERE customer.id=?";
            PreparedStatement ps = Database.getInstance().getConnection().prepareStatement(checkCustomerQuery);
            ps.setString(1,customerId);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                customer = new Customer(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getDouble("salary"));
            }

            ps.close();

        } catch (SQLException | ClassNotFoundException throwables) {
            System.out.println("SQLException");
            throwables.printStackTrace();
        }
        return customer;
    }



    public static boolean editCustomer(Customer customer) {
        try{
            Customer editCustomer = searchCustomer(customer.getId());
            String updateCustomer = "UPDATE customer SET id=?, name=?, address=?, salary=? WHERE customer.id=?" ;
            PreparedStatement ps = Database.getInstance().getConnection().prepareStatement(updateCustomer);
            ps.setString(1,customer.getId());
            ps.setString(2,customer.getName());
            ps.setString(3,customer.getAddress());
            ps.setDouble(4,customer.getSalary());
            ps.setString(5,customer.getId());
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

    public static Customer deleteCustomer(String customerId) {
        String deleteCustomer = "DELETE FROM customer WHERE customer.id=?";
        Customer deletedCustomer = searchCustomer(customerId);
        PreparedStatement ps = null;
        try {
            ps = Database.getInstance().getConnection().prepareStatement(deleteCustomer);
            ps.setString(1,customerId);
            ps.execute();

        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return deletedCustomer;
    }

    public static ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> customerList = new ArrayList<>();

        String getAllCustomers = "SELECT * FROM customer";
        try {
            ResultSet rs = Database.getInstance().getConnection().createStatement().executeQuery(getAllCustomers);

            while(rs.next()){
                Customer customer = new Customer(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getDouble("salary")
                );
                customerList.add(customer);
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return customerList;
    }


}
