import java.io.Console;
import java.sql.*;
import java.util.Scanner;
import java.io.Console;

public class Main extends Database{
    static Scanner sc = new Scanner(System.in);
    static String user = "";
    static boolean in = false;

    public static void login() throws SQLException {
        String query = "SELECT * FROM student WHERE `student-id` = " + user;
        Statement state = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet result = state.executeQuery(query);

        result.beforeFirst();

        if (!result.next()){
            System.out.println("Would you like to register? Y/N");
            String reg = sc.nextLine();

            if (reg.equalsIgnoreCase("y")) {
                String pass, lname, fname;

                System.out.println("Student ID:" + user);
                Console console = System.console();
                char[] passwordArray = console.readPassword("Password: ");
                for (int i = 0; i < passwordArray.length; i++) {
                    System.out.print("*");
                }

                System.out.print("First Name: ");
                fname = sc.nextLine();

                System.out.print("Last Name: ");
                lname = sc.nextLine();

                query = "INSERT INTO student (`student-id`,password,fname,lname) VALUES (?,?,?,?)";
                PreparedStatement prep = connect.prepareStatement(query);
                prep.setInt(1, Integer.parseInt(user));
                prep.setString(2, new String(passwordArray));
                prep.setString(3, fname);
                prep.setString(4, lname);
                prep.execute();
            }
        }
        else {
          //  Student.setStudentID(result.getInt(1));
          //  Student.setPassword(result.getString(2));
          //  Student.setFirstName(result.getString(3));
         //   Student.setLastName(result.getString(4));
            in = true;
        }
    }

    public static void mainMenu() {
        System.out.println("""
                MAIN MENU
                1. View List of Reservations
                2. Add a Reservation
                3. Edit a Reservation
                4. Cancel a Reservation
                5. View Reservations
                6. Exit
                """);
    }

    public static void enterChoice(){
        while(true) {
            Process.readTables();
            mainMenu();
            System.out.print("\nEnter Choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    Process.readList(user, false);
                    sc.nextLine();
                    break;
                case 2:
                    Process.createReservation(user);
                    sc.nextLine();
                    break;
                case 3:
                    Process.updateReservation(user);
                    sc.nextLine();
                    break;
                case 4:
                    Process.deleteReservation(user);
                    sc.nextLine();
                    break;
                case 5:
                    Process.readList(user, true);
                    sc.nextLine();
                    break;
                case 6:
                    System.out.println("Have a Nice Day");
                    System.exit(0);
                default:
                    System.out.println("Please Enter a Valid Choice.");
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        Database.getConnection();
        while (!in) {
            System.out.print("Student ID: ");
            user = sc.nextLine();
            login();
        }
        enterChoice();


    }
}
