package models;

public class Item {
    private String code;
    private String description;
    private double unitPrice;
    private int qtyOnHand;
    private int orderedQty;
    private double itemTotalPrice;

    public Item() {
    }

    public Item(String code, String description, double unitPrice, int qtyOnHand) {
        this.code = code;
        this.description = description;
        this.unitPrice = unitPrice;
        this.qtyOnHand= qtyOnHand;

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        qtyOnHand = qtyOnHand;
    }

    public int getOrderedQty() {
        return orderedQty;
    }

    public void setOrderedQty(int orderedQty) {
        if(orderedQty > qtyOnHand){
            throw new Error("not enough items");
        }
        this.orderedQty = orderedQty;
    }

    public double getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setItemTotalPrice() {
        this.itemTotalPrice = orderedQty*unitPrice;
    }


    @Override
    public String toString() {
        return "Item{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", unitPrice=" + unitPrice +
                ", QtyOnHand=" + qtyOnHand +
                '}';
    }
}