public class ReservationParticipants {

    private int participantID;
    private int reservationID;
    private String firstName;
    private String lastName;

    public ReservationParticipants(int participantID, int reservationID,
                                   String firstName, String lastName) {
        this.participantID = participantID;
        this.reservationID = reservationID;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public int getParticipantID() {
        return this.participantID;
    }

    public void setParticipantID(int participantID) {
        this.participantID = participantID;
    }
    public int getReservationID() {
        return this.reservationID;
    }
    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }
    public String getFirstName() {
        return this.firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

