package database;

public class Student extends User{

    private int studentId;

    public Student(int userId, String firstName, String lastName) {
        super(userId, firstName, lastName);
        studentId = -1;
    }

    public Student(int userId, int studentId, String firstName, String lastName) {
        super(userId, firstName, lastName);
        this.studentId = studentId;
    }

    public int getStudentId() { return studentId; }

    public String toString() { return studentId + "     | " + getFirstName() + " " + getLastName(); }

}
