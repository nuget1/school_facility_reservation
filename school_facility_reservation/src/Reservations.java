public class Reservations {
    private int reservationID;
    private int studentID;
    private int facilityID;
    private int facultyID;
    private String status;

    public Reservations() {
    }

    public Reservations(int reservationID, int studentID, int facilityID, int facultyID, String status) {
        this.reservationID = reservationID;
        this.studentID = studentID;
        this.facilityID = facilityID;
        this.facultyID = facultyID;
        this.status = status;
    }

    public int getReservationID() {
        return this.reservationID;
    }
    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }
    public int getStudentID() {
        return this.studentID;
    }
    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }
    public int getFacilityID() {
        return this.facilityID;
    }
    public void setFacilityID(int facilityID) {
        this.facilityID = facilityID;
    }
    public int getFacultyID() {
        return this.facultyID;
    }
    public void setFacultyID(int facultyID) {
        this.facultyID = facultyID;
    }
    public String getStatus() {
        return  this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
} // additional constructors may be added if deemed necessary

