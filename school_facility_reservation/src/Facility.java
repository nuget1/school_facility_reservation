public class Facility {
    private int facilityID;
    private String name;
    private String type;
    private String description;
    private String capacity;

    public Facility (int facilityID, String name, String type, String description, String capacity) {
        this.facilityID = facilityID;
        this.name = name;
        this.type = type;
        this.description = description;
        this.capacity = capacity;
    }

    public int getFacilityID() {
        return this.facilityID;
    }
    public void setFacilityID(int facilityID) {
        this.facilityID = facilityID;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return  this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

} // additional constructors may be added if deemed necessary

