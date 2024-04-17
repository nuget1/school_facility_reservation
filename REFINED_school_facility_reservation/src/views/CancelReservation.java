package views;

import database.Database;
import database.Reservation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CancelReservation extends View {
    private JPanel cancelResPanel;
    private JButton cancelReservationButton;
    private JList reservationList;
    private JButton mainMenuButton;

    private int userId;

    public CancelReservation(ViewPicker viewPicker) {
        this(viewPicker, -1);
    }

    public CancelReservation(ViewPicker viewPicker, int userId) {
        this.viewPicker = viewPicker;
        this.userId = userId;
    }

    @Override
    public void showTestView(int x, int y) {
        JFrame frame = new JFrame();
        frame.setContentPane(cancelResPanel);

        frame.setLocationRelativeTo(null);
        frame.setSize(x, y);
        frame.setVisible(true);
    }

    public void showTestView() {
        JFrame frame = new JFrame();
        frame.setContentPane(cancelResPanel);

        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void implementComponents() {
        Database.getStudent(userId, true).getStudentId();
        ArrayList<Reservation> reservationListData = Database.getReservations(userId, false);
        DefaultListModel<Reservation> model = new DefaultListModel<>();
        for (Reservation reservation : reservationListData) {
            model.addElement(reservation);
        }
        reservationList.setModel(model);

        cancelReservationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (reservationList.getSelectedValue() == null) {
                    JOptionPane.showMessageDialog(cancelResPanel, "Please select a reservation to cancel.");
                } else {
                    int reservationId = Integer.parseInt(reservationList.getSelectedValue().toString().split(" ")[0]);
         
                    Database.deleteReservation(reservationId);
                    JOptionPane.showMessageDialog(cancelResPanel, "Reservation canceled.");

                    // update the model of the JList
                    DefaultListModel<Reservation> model = (DefaultListModel<Reservation>) reservationList.getModel();
                    model.removeElementAt(reservationList.getSelectedIndex());
                }
            }
        });


        mainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPicker.pickView(
                        new StudentMenuView(viewPicker, userId), 450, 400
                );
            }
        });
    }

    @Override
    public JPanel getPanel() {
        return cancelResPanel;
    }

    public static void main(String[] args) {
        new CancelReservation(null).showTestView(600,500);

    }
}
