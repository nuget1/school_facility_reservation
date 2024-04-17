package database;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class Reservation {
    int resID;
    Student student;
    Faculty faculty;
    Facility facility;
    ArrayList<Student> participants;
    String reservationStatus;
    String purpose;
    Date dateStart;
    Date dateEnd;
    Time timeStart;
    Time timeEnd;

    public Reservation(int resID, Student student, Faculty faculty, Facility facility, String status,
                       String purpose, Date dateStart, Date dateEnd, Time timeStart, Time timeEnd) {
        this.resID = resID;
        this.student = student;
        this.facility = facility;
        this.faculty = faculty;
        this.purpose = purpose;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        reservationStatus = status;
        participants = new ArrayList<>();
    }

    public void addParticipant(Student student) {
        participants.add(student);
    }
    public Student getStudent() {
        return student;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public Facility getFacility() {
        return facility;
    }
    public Faculty getFaculty() {
        return faculty;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public Time getTimeEnd() {
        return timeEnd;
    }

    public Time getTimeStart() {
        return timeStart;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public int getResID() {
        return resID;
    }

    public String toString() {
        return getResID() + " || " + facility.getFacilityId() + " - " + facility.getName()
                + " - " + facility.getType() + " || " + reservationStatus;
    }
}
