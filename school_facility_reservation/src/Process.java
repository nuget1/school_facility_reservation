import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Process extends Database{
    // TODO: - Admin processes
    //       - Register method


    static String resID;
    static Scanner sc = new Scanner(System.in);
    static ArrayList<Student> studList = new ArrayList<>();
    static ArrayList<Faculty> facuList = new ArrayList<>();
    static ArrayList<Facility> faciList = new ArrayList<>();
    static ArrayList<Reservations> resList = new ArrayList<>();
    static ArrayList<ReservationDetails> resDetList = new ArrayList<>();
    static ArrayList<ReservationParticipants> resParList = new ArrayList<>();

    public static void readTables(){
        studList.clear();
        studList = DataInOut.studentList();

        facuList.clear();
        facuList = DataInOut.facultyList();

        faciList.clear();
        faciList = DataInOut.facilityList();

        resList.clear();
        resList = DataInOut.reservationsList();

        resDetList.clear();
        resDetList = DataInOut.resDetList();

        resParList.clear();
        resParList = DataInOut.resParList();
    }

    public static void readList(String user, boolean toggle) {
        System.out.println("""
        ============================================ List of Reservations =============================================
        |   Reservation ID    |       Reservee      |     Facilitator     |       Facility      |       Status        |
        ===============================================================================================================""");

        for (Reservations res : resList) {
            int studID = Integer.parseInt(user);
            int reserveID = res.getReservationID();
            String status = res.getStatus();

            // Check if reservation belongs to the current user
            if (toggle) {
                if (!(studID == res.getStudentID())) {
                    // Skip reservation if it doesn't belong to the current user
                    continue;
                }
            }

            // join with student table
            Optional<Student> student = studList.stream().filter(s -> s.getStudentID() == res.getStudentID()).findFirst();
            String reservee = student.isPresent() ? student.get().getLastName() + ", " + student.get().getFirstName() : "";

            // join with faculty table
            int facuID = res.getFacultyID();
            Optional<Faculty> faculty = facuList.stream().filter(f -> f.getFacultyID() == facuID).findFirst();
            String facilitator = faculty.isPresent() ? faculty.get().getLastName() + ", " + faculty.get().getFirstName() : "-";

            // join with facility table
            int faciID = res.getFacilityID();
            Optional<Facility> facility = faciList.stream().filter(f -> f.getFacilityID() == faciID).findFirst();
            String facilityName = facility.isPresent() ? facility.get().getName() : "";

            System.out.printf("| %19s | %19s | %19s | %19s | %19s | \n" +
                              "=============================================================================================================== \n",
                    reserveID, reservee, facilitator, facilityName, status);
        }
    }

    public static void createReservation(String user){
        System.out.println("============================================= CREATE RESERVATION ==============================================");
        System.out.println("""
                    ====================== Available Faculty =================
                    |   Faculty ID    |   Department   |      Full Name      |
                    ==========================================================""");
        for (Faculty facu : facuList){
            int facID = facu.getFacultyID();
            String dept = facu.getDept();
            String name = facu.getLastName() + ", " + facu.getFirstName();
            System.out.printf("| %17s | %16s | %21s | \n" +
                              "==========================================================\n",
                    facID, dept, name);
        }
        System.out.print("Enter Facilitator's ID (Press Enter if none): ");
        String facu = sc.nextLine();
        if (facu.equals("")){
            facu = null;
        }

        System.out.println("""
                    ====================== Available Faculty ===========================================
                    |    Facility ID    |        Name        |               Description               |
                    ====================================================================================""");
        for(Facility faci : faciList){
            int faciID = faci.getFacilityID();
            String faciName = faci.getName();
            String desc = faci.getDescription();
            System.out.printf(" | %19s | %20s | %41s | \n" +
                              "====================================================================================\n",
                    faciID, faciName, desc);
        }
        System.out.print("Enter Facility's ID: ");
        String faci = sc.nextLine();
        String lastResID = String.valueOf(resList.get(resList.size() - 1).getReservationID());

        String[] tuple = {"`student-id`", "`facility-id`", "`faculty-id`", "status"};
        String[] value = {user, faci, facu, "PENDING"};

        try {
            DataInOut.insert("RESERVATIONS", tuple, value);
            provideResDetails(lastResID, user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: Add exception for exceeding facility capacity
    public static void provideResDetails(String resID, String user) {
        System.out.println("Enter purpose: ");
        String purpose = sc.nextLine();

        ArrayList<Student> participants = new ArrayList();

        do {
            System.out.println("\nEnter a student participant's ID (enter blank to end): ");
            String studentId = sc.nextLine();

            if (studentId == "") {
                break;
            } else if (Integer.parseInt(studentId) == Integer.parseInt(user)) {
                System.out.println("You cannot add yourself as a participant.");
                continue;
            } else ;

            Student studentInput = null;
            for (Student stud : studList) {
                if ((stud.getStudentID() == Integer.parseInt(studentId))) {
                    studentInput = stud;
                }
            }

            if (studentInput == null) {
                System.out.println("No student found with given ID.");
            } else if (participants.contains(studentInput)) {
                System.out.println("This student has already been added as a participant.");
            } else {
                System.out.println("Student added as participant.");
                participants.add(studentInput);
            }

        } while (true);

        for (Student stud : participants) {
            String fname = stud.getFirstName();
            String lname = stud.getLastName();

            String[] tuple = {"`reservation-id`", "`fname`", "`lname`"};
            String[] value = {resID, fname, lname};

            try {
                DataInOut.insert("RESERVATION_PARTICIPANTS", tuple, value);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("\n");
        String startDate = getInputDate("Enter the starting date (YYYY-MM-DD): ");
        String endDate = getInputDate("Enter the end date (YYYY-MM-DD): ");
        String startTime = getInputTime("Enter the starting time (HH:MM) : ");
        String endTime = getInputTime("Enter the ending time (HH:MM) : ");
        String participantSize = String.valueOf(participants.size());

        String[] tuple = {"`reservation-id`", "`purpose`", "`participants`", "`time-start`",
                "`time-end`","`date-start`","`date-end`"};
        String[] value = {resID, purpose, participantSize, startTime, endTime, startDate, endDate};

        try {
            DataInOut.insert("RESERVATION_DETAILS", tuple, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteReservation(String user) {
        System.out.println("============================================= DELETE RESERVATION ==============================================");
        readList(user, true);
        System.out.print("Enter Reservation ID: ");
        resID = sc.nextLine();

        DataInOut.deleteTable("reservation_details", resID);
        DataInOut.deleteTable("reservation_participants", resID);
        DataInOut.deleteTable("reservations", resID);
    }

    //TODO: Add addition and removal of participants and Add exception for exceeding facility capacity
    public static void updateReservation(String user) {

        System.out.println("============================================= UPDATE RESERVATION ==============================================");
        readList(user, true);
        System.out.print("Enter Reservation ID: ");
        resID = sc.nextLine();

        DataInOut.updateTable(resID, "reservation_details");
    }

    public static String getInputDate(String prompt) {
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.print(prompt);
            String userInput = sc.nextLine();
            if (isValidDate(userInput)) {
                return userInput;
            } else {
                System.out.println("Please enter a valid date.");
                continue;
            }
        }
    }

    public static boolean isValidDate(String date) {
        // Date format to validate against
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); // Disable lenient mode

        try {
            // Attempt to parse the date input
            Date parsedDate = dateFormat.parse(date);
            // If parsing is successful, and the parsed date matches the original input,
            // then the date input is valid
            return date.equals(dateFormat.format(parsedDate));
        } catch (ParseException e) {
            // If an exception is thrown during parsing, the date input is invalid
            return false;
        }
    }

    public static String getInputTime(String prompt) {
        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.print(prompt);
            String userInput = sc.nextLine();
            if (isValidTime(userInput)) {
                return userInput;
            } else {
                System.out.println("Please enter a valid time.");
                continue;
            }
        }
    }

    public static boolean isValidTime(String timeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            LocalTime time = LocalTime.parse(timeStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            // Invalid time format
            return false;
        }
    }
}