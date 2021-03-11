package controllers;

import models.Item;

import java.util.ArrayList;

public class InvoiceController {
    public static String getTotalPrice(ArrayList<Item> items){
        int totalBill = 0;
        for(Item item : items){
            totalBill += item.getItemTotalPrice();
        }

        return String.valueOf(totalBill);
    }
}
