package ru.myitschool.vsu2021.tribunskikh_a_o.cookbook;

public class IngridientInRecipe {
    private long id;
    private long ingridientId;
    private long recipeId;
    private String quantity;

    public IngridientInRecipe(long id, long ingridientId, long recipeId, String quantity) {
        this.id = id;
        this.ingridientId = ingridientId;
        this.recipeId =recipeId;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIngridientId() {
        return ingridientId;
    }

    public void setIngridientId(long Ingridientid) {
        this.ingridientId = ingridientId;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long Recipeid) {
        this.recipeId = recipeId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
