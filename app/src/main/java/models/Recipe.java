package models;

public class Recipe {

    int recipe_id;
    String recipe_name;

    public Recipe(int recipe_id, String recipe_name) {
        this.recipe_id = recipe_id;
        this.recipe_name = recipe_name;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }
}
