package database;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    //TODO

    private static Connection connection;

    static {
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reservations",
                    "root", "");
        } catch (SQLException e) {
            System.out.println("Database Connection Failed.");
            System.out.println();
        }
    }

    public static boolean checkUser(int userId, String password) throws Exception{
        String query = "SELECT * FROM user WHERE user_id = ? LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query,
                ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.beforeFirst();

            if (!resultSet.next()) return false;
            String queriedPassword = resultSet.getString("password");
            if (queriedPassword.equals(password)) return true;
            else throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static boolean checkStudent(int userId) {
        String query = "SELECT * FROM student WHERE user_id = " + userId + " LIMIT 1";
        try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE)) {
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.beforeFirst();
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<Department> getDepartments() {
        String query = "SELECT * FROM department";
        ArrayList<Department> departments = new ArrayList<>();

        try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE)) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                departments.add(
                        new Department(resultSet.getInt("department_id"),
                                resultSet.getString("department_name"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SQL ERROR IN getDepartments");
        }
        return departments;
    }

    public static ArrayList<Reservation> getReservations(int userId, boolean getAll) {
        ArrayList<Reservation> reservations = new ArrayList<>();
        Student student = getStudent(userId, true);


        String query = "SELECT * FROM reservation";
        if (!getAll) query += " WHERE student_id = " + student.getStudentId();
        try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE)) {
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.beforeFirst();

            while (resultSet.next()) {
                Reservation reservation;
                int resID = resultSet.getInt("reservation_id");
                student = getStudent(resultSet.getInt("student_id"), false);
                Faculty facilitator = getFaculty(resultSet.getInt("faculty_id"), false);
                Facility facility = getFacility(resultSet.getInt("facility_id"));
                String reservationStatus = getReservationStatus(resultSet.getInt("reservation_status"));
                String purpose = resultSet.getString("purpose");
                Time timeStart = resultSet.getTime("time_start");
                Time timeEnd = resultSet.getTime("time_end");
                Date dateStart = resultSet.getDate("date_start");
                Date dateEnd = resultSet.getDate("date_end");
                reservation = new Reservation(
                       resID, student, facilitator, facility, reservationStatus, purpose, dateStart, dateEnd, timeStart, timeEnd
                );
                ArrayList<Student> participants = getParticipants(resultSet.getInt("reservation_id"));
                for (Student participant: participants) reservation.addParticipant(participant);
                reservations.add(reservation);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("QUERY FAILED IN getReservations");
        }

        return reservations;
    }

    public static ArrayList<Student> getParticipants(int reservationId) {
        ArrayList<Student> participants = new ArrayList<>();
        String query = "SELECT * FROM reservation_participants WHERE reservation_id = " + reservationId;
        try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE)) {
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.beforeFirst();
            while (resultSet.next()) {
                int participantId = resultSet.getInt("student_id");
                participants.add(getStudent(participantId, false));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("QUERY FAILED IN getParticipants");
        }

        return participants;
    }

    public static Facility getFacility(int facilityId) {
        Facility facility = null;
        String query = "SELECT * FROM facility WHERE facility_id = " + facilityId;
        try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE)) {
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.beforeFirst();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String type = resultSet.getString("type");
                String description = resultSet.getString("description");
                int capacity = resultSet.getInt("capacity");
                facility = new Facility(facilityId, name, type, description, capacity);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("QUERY FAILED IN getFacility");
        }

        return facility;
    }

    public static List<String> getAllFacility() {
        List<String> facultyList = new ArrayList<>();

        String query = "SELECT facility_id, name, type, capacity " +
                "FROM FACILITY";

        try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE)) {
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.beforeFirst();
            facultyList.add(String.format("%-15s| %-25s| %-15s| %-10s", "Facility ID", "Name", "Type", "Capacity"));
            facultyList.add("----------------------------------------------------------------------------------------");
            while (resultSet.next()) {
                String facilityid = resultSet.getString("facility_id");
                String name = resultSet.getString("name");
                String type = resultSet.getString("type");
                String capacity = resultSet.getString("capacity");
                String facultyString = String.format("%-15s| %-25s| %-15s| %-10s", facilityid, name, type, capacity);

                facultyList.add(facultyString);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return facultyList;
    }

    public static String getReservationStatus(int reservationId) {
        String reservationStatus = "";
        String query = "SELECT * FROM reservation_status WHERE reservation_status_id = " + reservationId;
        try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE)) {
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.beforeFirst();
            if (resultSet.next()) {
                reservationStatus = resultSet.getString("reservation_status");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("QUERY FAILED IN getReservationStatus");
        }

        return reservationStatus;
    }

    public static Faculty getFaculty(int ID, boolean usingUserId) {
        Faculty faculty = null;
        String query = usingUserId ? "SELECT * FROM faculty WHERE user_id = " + ID :
                "SELECT * FROM faculty WHERE faculty_id = " + ID;

        try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE)) {
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.beforeFirst();
            if (resultSet.next()) {
                int user_id;
                int faculty_id;
                String firstName;
                String lastName;
                Department department;
                if (usingUserId) {
                    User user = getUser(ID);
                    user_id = ID;
                    firstName = user.getFirstName();
                    lastName = user.getLastName();
                    faculty_id = resultSet.getInt("faculty_id");
                    department = getDepartment(resultSet.getInt("department"));
                } else {
                    user_id = resultSet.getInt("user_id");
                    faculty_id = ID;
                    User user = getUser(user_id);
                    firstName = user.getFirstName();
                    lastName = user.getLastName();
                    department = getDepartment(resultSet.getInt("department"));
                }
                faculty = new Faculty(user_id, faculty_id, firstName, lastName, department);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("QUERY FAILED IN getFaculty");
        }

        return faculty;
    }

    public static List<String> getAllFaculty() {
        List<String> facultyList = new ArrayList<>();

        String query = "SELECT faculty.faculty_id, user.fname, user.lname " +
                "FROM FACULTY " +
                "LEFT JOIN USER ON faculty.user_id = user.user_id";

        try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE)) {
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.beforeFirst();
            facultyList.add(String.format("%-15s | %-30s", "Faculty ID", "Name"));
            facultyList.add("");
            while (resultSet.next()) {
                String facultyId = resultSet.getString("faculty_id");
                String firstName = resultSet.getString("fname");
                String lastName = resultSet.getString("lname");
                String facultyString = String.format("%-15s | %-30s", facultyId, firstName + " " + lastName);

                facultyList.add(facultyString);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return facultyList;
    }

    public static Department getDepartment(int departmentId) {
        Department department = null;
        String query = "SELECT * FROM department WHERE department_id = " + departmentId;
        try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE)) {
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.beforeFirst();
            if (resultSet.next()) {
                String departmentName = resultSet.getString("department_name");
                department = new Department(departmentId, departmentName);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return department;
    }

    public static Student getStudent(int ID, boolean usingUserID) {
        Student student = null;
        String query = usingUserID ? "SELECT * FROM student WHERE user_id = " + ID :
                "SELECT * FROM student WHERE student_id = " + ID;

        try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE)) {
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.beforeFirst();
            if (resultSet.next()) {
                int user_id;
                int student_id;
                String firstName;
                String lastName;
                if (usingUserID) {
                    User user = getUser(ID);
                    user_id = ID;
                    firstName = user.getFirstName();
                    lastName = user.getLastName();
                    student_id = resultSet.getInt("student_id");
                } else {
                    user_id = resultSet.getInt("user_id");
                    student_id = ID;
                    User user = getUser(user_id);
                    firstName = user.getFirstName();
                    lastName = user.getLastName();
                } student = new Student(user_id, student_id, firstName, lastName);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("QUERY FAILED IN getStudent");
        }
        return student;
    }

    public static List<String> getAllStudent() {
        List<String> studentList = new ArrayList<>();

        String query = "SELECT s.student_id, u.fname, u.lname " +
                "FROM STUDENT s " +
                "JOIN `USER` u ON s.user_id = u.user_id";

        try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE)) {
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.beforeFirst();
            while (resultSet.next()) {
                String studentId = resultSet.getString("student_id");
                String firstName = resultSet.getString("fname");
                String lastName = resultSet.getString("lname");
                String studentString = String.format("%-15s | %-30s", studentId, firstName + " " + lastName);

                studentList.add(studentString);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return studentList;
    }



    public static User getUser(int userId) {
        User user = null;
        String query = "SELECT * FROM user WHERE user_id = " + userId;
        try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_UPDATABLE)) {

            ResultSet resultSet = statement.executeQuery(query);
            resultSet.beforeFirst();
            if (resultSet.next()) {
                String firstName = resultSet.getString("fname");
                String lastName = resultSet.getString("lname");
                user = new User(userId, firstName, lastName);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("QUERY FAILED IN getUser");
        }

        return user;
    }

    public static int createReservation(int studentID, int facilityID, int facultyID, int reservationStatus, String purpose, String timeStart, String timeEnd, String dateStart, String dateEnd) {
        String query = "INSERT INTO reservation (student_id,facility_id,faculty_id,reservation_status,purpose,time_start,time_end,date_start,date_end) VALUES (?,?,?,?,?,?,?,?,?)";
        int reservationId;// default value in case no rows are affected

        try (PreparedStatement prep = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            prep.setInt(1, studentID);
            prep.setInt(2, facilityID);
            if (facultyID == 0) {
                prep.setNull(3, Types.INTEGER);
            } else {
                prep.setInt(3, facultyID);
            }
            prep.setInt(4, reservationStatus);
            prep.setString(5, purpose);
            prep.setString(6, timeStart);
            prep.setString(7, timeEnd);
            prep.setString(8, dateStart);
            prep.setString(9, dateEnd);

            int rowsAffected = prep.executeUpdate();
            if (rowsAffected != 1) {
                throw new SQLException("Failed to insert reservation");
            }

            try (ResultSet generatedKeys = prep.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reservationId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to get reservation ID");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return reservationId;
    }

    public static void insertParticipants(List<String> studentIds, int reservationId) {
        System.out.println(studentIds);
        System.out.println(reservationId);
        String query = "INSERT INTO reservation_participants (reservation_id, student_id) SELECT ?, ? WHERE NOT EXISTS (SELECT * FROM reservation_participants WHERE reservation_id = ? AND student_id = ?)";
        try (PreparedStatement prep = connection.prepareStatement(query)) {
            int rowsAffected = 0;
            for (String studentId : studentIds) {
                prep.setInt(1, reservationId);
                prep.setString(2, studentId);
                prep.setInt(3, reservationId);
                prep.setString(4, studentId);
                rowsAffected = prep.executeUpdate();
            }
            if (rowsAffected != 1) {
                throw new SQLException("Failed to insert reservation");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void insert(String table, String[] tuples, String[] values) {
        String tuple = "";
        String value = "";

        for (String col : tuples) {
            tuple += col + ", ";
        }
        tuple = tuple.substring(0, tuple.length() - 2);
        for (int x = 0; x < tuples.length; x++) {
            value += "?, ";
        }
        value = value.substring(0, value.length() - 2);

        String query = "INSERT INTO " + table + " (" + tuple + ") VALUES (" + value + ");";

        try {
            PreparedStatement prep = connection.prepareStatement(query);

            for (int i = 0; i < values.length; i++) {
                if (values[i] == null) {
                    prep.setNull(i + 1, java.sql.Types.NULL);
                } else {
                    prep.setObject(i + 1, values[i]);
                }
            }

            prep.executeUpdate();
            prep.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteReservation(int reservation_id) {
        try (Statement statement = connection.createStatement()) {
            // Delete rows from reservation_participants table
            String participantsQuery = "DELETE FROM reservation_participants WHERE reservation_id = " + reservation_id;
            int participantsRowsDeleted = statement.executeUpdate(participantsQuery);

            // Delete row from reservation table
            String reservationQuery = "DELETE FROM reservation WHERE reservation_id = " + reservation_id;
            int reservationRowsDeleted = statement.executeUpdate(reservationQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static int getLastUserID() {
        String query = "SELECT MAX(user_id) as last_user_id FROM user;";
        int lastUserID = -1;

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                lastUserID = resultSet.getInt("last_user_id");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving last user ID", e);
        }

        return lastUserID;
    }

    public static void update(String table, String[] tuples, String[] values, String whereArg) {
        String args = "";

        for (int x = 0; x < tuples.length; x++) {
            args += tuples[x] + " = ?, ";
        }
        args = args.substring(0, args.length() - 2);

        String query = "UPDATE " + table + " SET " + args + " WHERE " + whereArg + ";";

        PreparedStatement prep = null;
        try {
            prep = connection.prepareStatement(query);
            for (int i = 0; i < values.length; i++) {
                if (values[i] == null) {
                    prep.setNull(i + 1, java.sql.Types.NULL);
                } else {
                    prep.setObject(i + 1, values[i]);
                }
            }

            prep.executeUpdate();
            prep.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean facilityExists(int facilityID) {
        boolean facilityExists = false;
        String query = "SELECT COUNT(*) FROM facility WHERE facility_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, facilityID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                facilityExists = (count > 0);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return facilityExists;
    }

    public static boolean facultyExists(int facultyID) {
        boolean facultyExists = false;
        String query = "SELECT COUNT(*) FROM faculty WHERE faculty_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, facultyID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                facultyExists = (count > 0);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return facultyExists;
    }

    public static boolean isStudentInAnotherReservation(int studentId, String startTime, String endTime, String startDate, String endDate) {
        String query = "SELECT COUNT(*) FROM reservation " +
                "WHERE student_id = ? AND date_start <= ? AND date_end >= ? " +
                "AND time_start <= ? AND time_end >= ?" +
                "AND reservation_status = '2'";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);
            statement.setString(2, endDate);
            statement.setString(3, startDate);
            statement.setString(4, endTime);
            statement.setString(5, startTime);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count == 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }


    public static boolean isTimeRangeAvailable(int facilityId, String startTime, String endTime, String startDate, String endDate) {
        String query = "SELECT COUNT(*) FROM reservation " +
                "WHERE facility_id = ? " +
                "AND ((time_start >= ? " +
                "AND time_end <= ?)) " +
                "AND ((date_start >= ? " +
                "AND date_end <= ?))" +
                "AND reservation_status = '2'";


        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, facilityId);
            statement.setString(2, startTime);
            statement.setString(3, endTime);
            statement.setString(4, startDate);
            statement.setString(5, endDate);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count == 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
