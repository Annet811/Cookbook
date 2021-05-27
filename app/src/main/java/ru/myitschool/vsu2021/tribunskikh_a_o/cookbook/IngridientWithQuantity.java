package ru.myitschool.vsu2021.tribunskikh_a_o.cookbook;

public class IngridientWithQuantity {
    private Ingridient food;
    private String quantity;
    private long id;

    public IngridientWithQuantity (Ingridient food, String quantity, long id) {
        this.food = food;
        this.quantity = quantity;
        this.id=id;
    }
    public Ingridient getFood () {
        return food;
    }
    public String getQuantity () {
        return quantity;
    }
    public long getId() {
        return id;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
