package models;

public class ItemAvailable {
    private String code;
    private String description;
    private int units;

    public ItemAvailable() { }

    public ItemAvailable(String code, String description, int units) {
        this.code = code;
        this.description = description;
        this.units = units;
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

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }
}
