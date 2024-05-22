package entity;


public class Items extends Product {

    private final int calories;
    private String type, origin;


    public Items(String name, String type, int calories) {
        super(name);
        this.type = type;
        this.calories = calories;
    }

    public Items(int calories, String origin, String name) {
        super(name);
        this.calories = calories;
        this.origin = origin;
    }


    public int getCalories() {
        return calories;
    }

    public String getType() {
        return type;
    }

    public String getOrigin() {
        return origin;
    }


}

