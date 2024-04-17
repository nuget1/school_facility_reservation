public class Student {
        private int studentID;
        private String password;
        private String firstName;
        private String lastName;


        public Student (int studID, String pass, String fName, String lName) {
            this.studentID = studID;
            this.password = pass;
            this.firstName = fName;
            this.lastName = lName;
        }

    public void setStudentID(int studID) {
        this.studentID = studID;
    }

    public void setPassword(String pass) {
        this.password = pass;
    }

    public void setFirstName(String fName) {
        this.firstName = fName;
    }

    public void setLastName(String lName) {
        this.lastName = lName;
    }

    public int getStudentID() {
            return this.studentID;
        }
        public String getPassword() {
            return password;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return  lastName;
        }

    } // additional constructors may be added if deemed necessary
