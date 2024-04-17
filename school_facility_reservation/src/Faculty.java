public class Faculty {
    private int facultyID;
    private String password;
    private String firstName;
    private String lastName;
    private String dept;

    public Faculty (int facultyID, String password,String dept, String firstName, String lastName) {
        this.facultyID = facultyID;
        this.password = password;
        this.dept = dept;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getFacultyID() {
        return this.facultyID;
    }
    public void setFacultyID(int facultyID) {
        this.facultyID = facultyID;
    }

    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getDept() {
        return this.dept;
    }
    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getFirstName() {
        return this.firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return  this.lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

} // additional constructors may be added if deemed necessary

