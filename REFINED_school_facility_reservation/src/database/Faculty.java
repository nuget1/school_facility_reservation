package database;

public class Faculty extends User{

    private int facultyId;
    private Department department;

    public Faculty(int userId, String firstName, String lastName) {
        this(userId, -1, firstName, lastName, null);
    }

    public Faculty(int userId, int facultyId, String firstName, String lastName, Department department) {
        super(userId, firstName, lastName);
        this.facultyId = facultyId;
        this.department = department;
    }

    public int getFacultyId() { return facultyId; }
}
