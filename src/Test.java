import database.Database;
import models.Customer;

import java.util.Scanner;

/**
public class Test {
    public static void main(String[] args) {
        System.out.println("Enter details of the customer");

        Scanner input = new Scanner(System.in);
        System.out.println("name: ");
        String name = input.nextLine();
        System.out.println("major: ");
        String major = input.nextLine();
        System.out.println("paid: ");
        boolean paid = Boolean.getBoolean(input.nextLine());
        System.out.println("fee: ");
        double fee = Double.parseDouble(input.nextLine());

        Customer nc = new Customer(name, major, paid, fee);

        //put the database in a singleton .. otherwise multiple instances can be created...
        Database db = new Database();
        boolean added = db.add(nc);
        //System.out.println(added);
        System.out.println(added ? "added successfully": "something went wrong");

    }
}
*/
