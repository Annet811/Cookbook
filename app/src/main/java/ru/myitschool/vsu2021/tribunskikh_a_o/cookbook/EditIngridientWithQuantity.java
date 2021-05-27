package ru.myitschool.vsu2021.tribunskikh_a_o.cookbook;

public class EditIngridientWithQuantity {
    private Ingridient edit_food;
    private String edit_quantity;

    public EditIngridientWithQuantity (Ingridient edit_food, String edit_quantity) {
        this.edit_food = edit_food;
        this.edit_quantity = edit_quantity;
    }
    public Ingridient getEditFood () {
        return edit_food;
    }
    public String getEditQuantity () {
        return edit_quantity;
    }

    public void setQuantity(String edit_quantity) {
        this.edit_quantity = edit_quantity;
    }
}
