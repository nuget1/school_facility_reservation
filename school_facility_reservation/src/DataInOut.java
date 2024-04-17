import java.lang.reflect.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class DataInOut {
    static String query = "";
    static Scanner sc = new Scanner(System.in);
    static Statement state;
    static PreparedStatement prep;
    static ResultSet result;

    public static ArrayList<Student> studentList() {
        ArrayList<Student> students = new ArrayList<>();
        try {
            query = "SELECT * FROM STUDENT";
            state = Database.connect.createStatement();
            result = state.executeQuery(query);
            while (result.next()) {
                int studentID = result.getInt("student_id");
                String password = result.getString("password");
                String fname = result.getString("fname");
                String lname = result.getString("lname");
                students.add(new Student(studentID, password, fname, lname));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return students;
    }

    public static ArrayList<Facility> facilityList() {
        ArrayList<Facility> facilities = new ArrayList<>();
        try {
            String query = "SELECT * FROM FACILITY";
            PreparedStatement statement = Database.connect.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Facility facility = new Facility(
                        resultSet.getInt("facility_id"),
                        resultSet.getString("name"),
                        resultSet.getString("type"),
                        resultSet.getString("description"),
                        resultSet.getString("capacity")
                );
                facilities.add(facility);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return facilities;
    }

    public static ArrayList<Faculty> facultyList() {
        ArrayList<Faculty> faculties = new ArrayList<>();
        try {
            String query = "SELECT * FROM FACULTY";
            PreparedStatement statement = Database.connect.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Faculty faculty = new Faculty(
                        resultSet.getInt("faculty_id"),
                        resultSet.getString("password"),
                        resultSet.getString("department"),
                        resultSet.getString("fname"),
                        resultSet.getString("lname")
                );
                faculties.add(faculty);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return faculties;
    }

    public static ArrayList<Reservations> reservationsList() {
        ArrayList<Reservations> reservations = new ArrayList<>();
        try {
            String query = "SELECT * FROM RESERVATIONS";
            PreparedStatement statement = Database.connect.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Reservations reservation = new Reservations(
                        resultSet.getInt("reservation_id"),
                        resultSet.getInt("student_id"),
                        resultSet.getInt("facility_id"),
                        resultSet.getInt("faculty_id"),
                        resultSet.getString("status")
                );
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reservations;
    }

    public static ArrayList<ReservationDetails> resDetList() {
        ArrayList<ReservationDetails> resDetails = new ArrayList<>();
        try {
            query = "SELECT * FROM RESERVATION_DETAILS";
            state = Database.connect.createStatement();
            result = state.executeQuery(query);
            while (result.next()) {
                resDetails.add(new ReservationDetails(
                        result.getInt("reservation_no"),
                        result.getInt("reservation_id"),
                        result.getString("purpose"),
                        result.getString("time_start"),
                        result.getString("time_end"),
                        result.getString("date_start"),
                        result.getString("date_end")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resDetails;
    }

    public static ArrayList<ReservationParticipants> resParList() {
        ArrayList<ReservationParticipants> resParticipants = new ArrayList<>();
        try {
            query = "SELECT * FROM RESERVATION_PARTICIPANTS";
            state = Database.connect.createStatement();
            result = state.executeQuery(query);
            while (result.next()) {
                resParticipants.add(new ReservationParticipants(
                        result.getInt("participant_id"),
                        result.getInt("reservation_id"),
                        result.getString("fname"),
                        result.getString("lname")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resParticipants;
    }

    //TODO: Modify
    public static void updateTable(String resID, String tableName) {
        try {
            // Get the current reservation details
            query = "SELECT * FROM " + tableName + " INNER JOIN `reservations` " +
                    "ON `reservation_details`.`reservation_id` = `reservations`.`reservation_id` " +
                    "WHERE `reservation_details`.`reservation_id` = " + resID;
            state = Database.connect.createStatement();
            result = state.executeQuery(query);

            if (!result.next()) {
                System.out.println("Reservation not found.\n");
                return;
            }

            ReservationDetails reservationDetails = new ReservationDetails(
                    result.getInt("reservation_no"),
                    result.getInt("reservation_id"),
                    result.getString("purpose"),
                    result.getString("time_start"),
                    result.getString("time_end"),
                    result.getString("date_start"),
                    result.getString("date_end")
            );

            Reservations reservations = new Reservations(
                    result.getInt("reservation_id"),
                    result.getInt("student_id"),
                    result.getInt("facility_id"),
                    result.getInt("faculty_id"),
                    result.getString("status")
            );

            // Prompt the user for which fields to update
            System.out.println("Which fields do you want to update? (comma_separated list, no spaces)");
            System.out.println("1. Purpose");
            System.out.println("2. Time Start");
            System.out.println("3. Time End");
            System.out.println("4. Date Start");
            System.out.println("5. Date End");
            System.out.println("6. Change status");
            System.out.print("> ");

            String[] fieldsToUpdate = sc.nextLine().split(",");
            List<String> validFields = Arrays.asList("1", "2", "3", "4", "5", "6");

            // Update the selected fields
            for (String field : fieldsToUpdate) {
                if (validFields.contains(field)) {
                    switch (field) {
                        case "1":
                            System.out.print("Enter new purpose: ");
                            reservationDetails.setPurpose(sc.nextLine());
                            break;
                        case "2":
                            while (true) {
                                System.out.print("Enter new start time (HH:mm): ");
                                String newTime;
                                newTime = sc.nextLine();
                                if (Process.isValidTime(newTime)) {
                                    reservationDetails.setTimeStart(newTime);
                                    break;
                                } else {
                                    System.out.println("Invalid date format. Please enter a time in the format (HH:mm): ");
                                }
                            }
                            break;
                        case "3":
                            while (true) {
                                System.out.print("Enter new end time (HH:mm): ");
                                String newTime;
                                newTime = sc.nextLine();
                                if (Process.isValidTime(newTime)) {
                                    reservationDetails.setTimeEnd(newTime);
                                    break;
                                } else {
                                    System.out.println("Invalid date format. Please enter a time in the format (HH:mm): ");
                                }
                            }
                        case "4":
                            while (true) {
                                System.out.print("Enter new start date (YYYY_MM_DD): ");
                                String newDate = sc.nextLine();
                                if (Process.isValidDate(newDate)) {
                                    reservationDetails.setDateStart(newDate);
                                    break;
                                } else {
                                    System.out.println("Invalid date format. Please enter a date in the format YYYY_MM_DD.");
                                }
                            }
                            break;
                        case "5":
                            while (true) {
                                System.out.print("Enter new start date (YYYY_MM_DD): ");
                                String newDate = sc.nextLine();
                                if (Process.isValidDate(newDate)) {
                                    reservationDetails.setDateEnd(newDate);
                                    break;
                                } else {
                                    System.out.println("Invalid date format. Please enter a date in the format YYYY_MM_DD.");
                                }
                            }
                            break;
                        case "6":
                            String status;
                            while (true) {
                                System.out.println("Enter status: (REJECTED or APPROVED)");
                                status = sc.nextLine().toUpperCase();
                                if (status.equals("REJECTED") || status.equals("APPROVED")) {
                                    break;
                                }
                            }
                            reservations.setStatus(status);
                    }
                }
            }

            // Update the reservation in the database
            query = "UPDATE reservation_details " +
                    "INNER JOIN reservations ON reservation_details.`reservation_id` = reservations.`reservation_id` " +
                    "SET reservation_details.purpose=?, reservation_details.participants=?, " +
                    "reservation_details.`time_start`=?, reservation_details.`time_end`=?, " +
                    "reservation_details.`date_start`=?, reservation_details.`date_end`=?, " +
                    "reservations.status=? " +
                    "WHERE reservation_details.`reservation_id`=?";
            prep = Database.connect.prepareStatement(query);

            prep.setString(1, reservationDetails.getPurpose());
            prep.setString(3, reservationDetails.getTimeStart());
            prep.setString(4, reservationDetails.getTimeEnd());
            prep.setString(5, reservationDetails.getDateStart());
            prep.setString(6, reservationDetails.getDateEnd());
            prep.setString(7, reservations.getStatus());
            prep.setInt(8, reservationDetails.getReservationID());


            prep.executeUpdate();

            System.out.println("Reservation Updated!\n");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //TODO: modify
    public static void deleteTable(String table, String data) {
        try {
            query = "DELETE FROM " + table + " WHERE `reservation_id` =" + data;
            state = Database.connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            int rowsDeleted = state.executeUpdate(query);

            if (rowsDeleted > 0) {
                System.out.println("Data deleted successfully.");
            } else {
                System.out.println("No data found for deletion.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void insert(String table, String[] tuples, String[] values) throws SQLException {
        String tuple = "";
        String value = "";

        for (String col : tuples) {
            tuple += col + ", ";
        }
        System.out.println(tuple);
        for (int x = 0; x < tuples.length; x++) {
            value += "?, ";
        }
        tuple = tuple.substring(0, tuple.length() - 2);
        value = value.substring(0, value.length() - 2);

        query = "INSERT INTO " + table + " (" + tuple + ") VALUES (" + value + ");";

        PreparedStatement prep = Database.connect.prepareStatement(query);

        for (int i = 0; i < values.length; i++) {
            if (values[i] == null) {
                prep.setNull(i + 1, java.sql.Types.NULL);
            } else {
                prep.setObject(i + 1, values[i]);
            }
        }

        prep.executeUpdate();
        prep.close();
    }
}
