package views;

import database.Database;
import database.Reservation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FacultyMenuView extends View {

    private JPanel facultyMenuPanel;
    private JButton logOutButton;
    private JButton showFacilitatedReservationsButton;
    private JButton manageReservationRequestsButton;
    private static int userId;

    public FacultyMenuView(ViewPicker viewPicker) {

        this(viewPicker, -1);
    }

    public FacultyMenuView(ViewPicker viewPicker, int userId) {
        this.viewPicker = viewPicker;
        FacultyMenuView.userId = userId;
    }

    public void showTestView(int x, int y) {

        JFrame frame = new JFrame();
        frame.setContentPane(facultyMenuPanel);

        frame.setSize(x, y);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void showTestView() {
        JFrame frame = new JFrame();
        frame.setContentPane(facultyMenuPanel);

        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void implementComponents() {
        ArrayList<Reservation> resList = Database.getReservations(0, true);
        manageReservationRequestsButton.setEnabled(false);
        showFacilitatedReservationsButton.setEnabled(false);
        int facultyId;

        if (resList.size() != 0) {
            manageReservationRequestsButton.setEnabled(true);

            for (Reservation curr : resList) {
                try{
                    facultyId = curr.getFaculty().getFacultyId();
                }catch (NullPointerException e){
                    continue;
                }
                if (curr.getFaculty().getFacultyId() == userId) {
                    showFacilitatedReservationsButton.setEnabled(true);
                    break;
                }
            }
        }

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPicker.pickView(
                        new LoginView(viewPicker), 500, 300
                );
            }
        });

        manageReservationRequestsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPicker.pickView(
                        new ManageRequests(viewPicker, userId), 600, 500
                );
            }
        });

        showFacilitatedReservationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPicker.pickView(
                        new ShowFacilitated(viewPicker, userId), 600, 500
                );
            }
        });
    }

    @Override
    public JPanel getPanel() {
        return facultyMenuPanel;
    }


    public static void main(String[] args) {
        new FacultyMenuView(null).showTestView();

    }
}
