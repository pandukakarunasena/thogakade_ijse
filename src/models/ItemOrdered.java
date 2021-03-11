package models;

public class ItemOrdered extends Item{
    private int orderedQty;
    private double itemTotalPrice;

    public ItemOrdered(String code, String description, double unitPrice, int qtyOnHand, int orderedQty) {
        super(code, description, unitPrice, qtyOnHand);
        this.orderedQty = orderedQty;
    }

    public int getOrderedQty() {
        return orderedQty;
    }

    public void setOrderedQty(int orderedQty) {
        if(orderedQty > super.getQtyOnHand()){
            throw new Error("not enough amount to order");
        }
        this.orderedQty = orderedQty;
        setItemTotalPrice();
    }

    public double getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setItemTotalPrice() {
        this.itemTotalPrice = orderedQty*super.getUnitPrice();
    }
}
