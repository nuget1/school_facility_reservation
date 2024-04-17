package database;

public class Facility {
    private int facilityId;
    private String name;
    private String type;
    private String description;
    private int capacity;

    public Facility(int facilityId, String name, String type, String description, int capacity) {
        this.facilityId = facilityId;
        this.name = name;
        this.type = type;
        this.description = description;
        this.capacity = capacity;
    }

    public int getFacilityId() {
        return facilityId;
    }
    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public int getCapacity() {
        return capacity;
    }
}