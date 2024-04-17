package views;

import database.Database;
import database.Faculty;
import database.Reservation;
import database.Student;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ShowReservationsView extends View {
    private JPanel showReservationsPanel;
    private JComboBox reservationComboBox;
    private JRadioButton yesRadioButton;
    private JRadioButton noRadioButton;
    private JList participantsList;
    private JLabel reserveeLabel;
    private JLabel faciliityLabel;
    private JLabel facilitatorLabel;
    private JLabel PurposeLabel;
    private JLabel dateRangeLabel;
    private JLabel dailyTimesLabel;
    private JLabel statusLabel;
    private JLabel actualReserveeLabel;
    private JLabel actualFacilityLabel;
    private JLabel actualFacilitatorLabel;
    private JLabel actualPurposeLabel;
    private JLabel actualDateRangeLabel;
    private JLabel actualDailyTimesLabel;
    private JLabel actualStatusLabel;
    private JLabel actualCapacityLabel;
    private JButton backButton;

    private int userId;
    private DefaultListModel<Student> dlm = new DefaultListModel<>();

    public ShowReservationsView(ViewPicker viewPicker) {
        this(viewPicker, -1);
    }

    public ShowReservationsView(ViewPicker viewPicker, int userId) {
        this.viewPicker = viewPicker;
        this.userId = userId;
        noRadioButton.setSelected(true);
    }

    @Override
    public void showTestView(int x, int y) {
        JFrame frame = new JFrame();
        frame.setContentPane(showReservationsPanel);

        frame.setSize(x, y);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        participantsList.setModel(dlm);
    }

    public void showTestView() {
        JFrame frame = new JFrame();
        frame.setContentPane(showReservationsPanel);

        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void implementComponents() {

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPicker.pickView(new StudentMenuView(viewPicker, userId), 450, 400);
            }
        });

        yesRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                updateReservationList(true);
                updateReservationDetails((Reservation) reservationComboBox.getSelectedItem());
                reservationComboBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Reservation selectedReservation = (Reservation) reservationComboBox.getSelectedItem();
                        if (selectedReservation != null) {
                            updateReservationDetails(selectedReservation);
                        }
                    }
                });

            }
        });

        noRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                updateReservationList(false);
                updateReservationDetails((Reservation) reservationComboBox.getSelectedItem());
                reservationComboBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Reservation selectedReservation = (Reservation) reservationComboBox.getSelectedItem();
                        if (selectedReservation != null) {
                            updateReservationDetails(selectedReservation);
                        }

                    }
                });

            }
        });

        reservationComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    @Override
    public JPanel getPanel() {
        return showReservationsPanel;
    }

    public static void main(String[] args) {
        new ShowReservationsView(null).showTestView(800, 450);

    }

    private void updateReservationDetails(Reservation reservation) {
        if (reservation == null) {
            actualReserveeLabel.setText("");
            actualFacilityLabel.setText("");
            actualFacilitatorLabel.setText("");
            actualPurposeLabel.setText("");
            actualDateRangeLabel.setText("");
            actualDailyTimesLabel.setText("");
            dlm.clear();
            // Return early if the reservation is null
            return;
        }

        actualCapacityLabel.setText(
                String.valueOf(reservation.getFacility().getCapacity())
        );

        actualReserveeLabel.setText(
                reservation.getStudent().getFirstName() + " " + reservation.getStudent().getLastName()
        );

        actualFacilityLabel.setText(
                reservation.getFacility().getName()
        );

        Faculty facilitator = reservation.getFaculty();
        actualFacilitatorLabel.setText(
                facilitator == null ? "None" : facilitator.getFirstName() + " " + facilitator.getLastName()
        );

        actualPurposeLabel.setText(
                reservation.getPurpose()
        );

        actualDateRangeLabel.setText(
                reservation.getDateStart().toString() + " to " + reservation.getDateEnd().toString()
        );

        actualDailyTimesLabel.setText(
                reservation.getTimeStart().toString() + " to " + reservation.getTimeEnd().toString()
        );

        actualStatusLabel.setText(
                reservation.getReservationStatus()
        );

        DefaultListModel<String> dlm = new DefaultListModel<>();
        ArrayList<Student> participants = Database.getParticipants(reservation.getResID());
        System.out.println(reservation.getResID());
        System.out.println(participants.size());
        for (Student participant : participants) {
            dlm.addElement(participant.toString());
        }
        participantsList.setModel(dlm);
    }

    private void updateReservationList(boolean showAll) {
        reservationComboBox.removeAllItems();
        ArrayList<Reservation> reservations = Database.getReservations(userId, showAll);

        for (Reservation reservation: reservations) {
            reservationComboBox.addItem(reservation);
        }
    }
}
