package views;

import database.Database;
import database.Reservation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StudentMenuView extends View{
    private JPanel studentMenuPanel;
    private JLabel welcomeLabel;
    private JButton createReservationButton;
    private JButton updateReservationButton;
    private JButton cancelReservationButton;
    private JButton showReservationsButton;
    private JButton logOutButton;
    private int userId;

    public StudentMenuView(ViewPicker viewPicker) {
        this(viewPicker, -1);
    }

    public StudentMenuView(ViewPicker viewPicker, int userId) {
        this.viewPicker = viewPicker;
        this.userId = userId;
    }

    @Override
    public void showTestView(int x, int y) {
        JFrame frame = new JFrame();
        frame.setContentPane(studentMenuPanel);
        frame.setLocationRelativeTo(null);
        frame.setSize(x, y);
        frame.setVisible(true);
    }

    public void showTestView() {
        JFrame frame = new JFrame();
        frame.setContentPane(studentMenuPanel);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void implementComponents() {
        ArrayList<Reservation> resList = Database.getReservations(userId, false);

        if (resList.size() > 0){
            updateReservationButton.setEnabled(true);
            cancelReservationButton.setEnabled(true);
        } else {
            updateReservationButton.setEnabled(false);
            cancelReservationButton.setEnabled(false);
        }


        createReservationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPicker.pickView(
                        new CreateReservation(viewPicker, userId), 700, 500
                );
            }
        });

        updateReservationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPicker.pickView(
                        new UpdateReservation(viewPicker, userId), 650, 500
                );
            }
        });

        cancelReservationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPicker.pickView(
                        new CancelReservation(viewPicker, userId), 600, 500
                );
            }
        });

        showReservationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                viewPicker.pickView(new ShowReservationsView(viewPicker, userId), 800, 450);
            }
        });

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPicker.pickView(
                        new LoginView(viewPicker), 500, 300);
            }
        });
    }

    @Override
    public JPanel getPanel() {
        return studentMenuPanel;
    }

    public static void main(String[] args) {
        new StudentMenuView(null).showTestView();
    }
}
