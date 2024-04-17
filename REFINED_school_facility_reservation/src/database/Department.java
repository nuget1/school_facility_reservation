package database;

public class Department {
    private int departmentID;
    private String departmentName;

    public Department(int departmentID, String departmentName) {
        this.departmentID = departmentID;
        this.departmentName = departmentName;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public String toString() {
        return departmentName;
    }
}
