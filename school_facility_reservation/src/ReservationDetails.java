public class ReservationDetails {
    private int reservationNum;
    private int reservationID;
    private String purpose;
    private String timeStart; // change data type to time
    private String timeEnd; // change data type to time
    private String dateStart; // change data type to date
    private String dateEnd; // change data type to date

    public ReservationDetails(){

    }

    public ReservationDetails(int reservationNum, int reservationID, String purpose,
                              String timeStart, String timeEnd, String dateStart, String dateEnd) {
        this.reservationNum = reservationNum;
        this.reservationID = reservationID;
        this.purpose = purpose;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public int getReservationNum() {
        return this.reservationNum;
    }

    public int getReservationID() {
        return this.reservationID;
    }


    public String getPurpose() {
        return this.purpose;
    }

    public String getTimeStart() { // subject to change
        return this.timeStart;
    }

    public String getTimeEnd() { // subject to change
        return this.timeEnd;
    }

    public String getDateStart() { // subject to change
        return this.dateStart;
    }

    public String getDateEnd() { // subject to change
        return this.dateEnd;
    }

    public void setReservationNum(int reservationNum) {
        this.reservationNum = reservationNum;
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }
} // additional constructors may be added if deemed necessary

