package views;

import database.Database;
import database.Reservation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ManageRequests extends View{

    private JPanel manageReqPanel;
    private JList list1;
    private JButton showPendingButton;
    private JButton showApprovedButton;
    private JButton backButton;
    private JButton showRejectedButton;
    private JButton rejectButton;
    private JButton acceptButton;
    private JButton showAllButton;
    private JButton checkDetailsButton;
    private int userId;

    public ManageRequests(ViewPicker viewPicker, int userId) {
        this.viewPicker = viewPicker;
        this.userId = userId;
    }

    @Override
    public void implementComponents() {
        showAllButton.addActionListener(e -> updateReservationList("0"));
        showPendingButton.addActionListener(e -> updateReservationList("1"));
        showApprovedButton.addActionListener(e -> updateReservationList("2"));
        showRejectedButton.addActionListener(e -> updateReservationList("3"));

        acceptButton.addActionListener(e -> updateReservationStatus("2"));
        rejectButton.addActionListener(e -> updateReservationStatus("3"));

        // By default, show all reservations
        updateReservationList("0");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPicker.pickView(
                        new FacultyMenuView(viewPicker, userId), 450, 400);
            }
        });

        checkDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Reservation selected = (Reservation) list1.getSelectedValue();
                if (selected == null) {
                    JOptionPane.showMessageDialog(null, "Please select a reservation.");
                    return;
                }

                JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 5, 5));
                detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                detailsPanel.add(new JLabel("Reserver ID: "));
                detailsPanel.add(new JLabel(String.valueOf(selected.getStudent().getStudentId())));

                detailsPanel.add(new JLabel("Name: "));
                detailsPanel.add(new JLabel(selected.getStudent().getLastName() + ", " + selected.getStudent().getFirstName()));

                detailsPanel.add(new JLabel("Facilitator: "));
                detailsPanel.add(new JLabel(selected.getFaculty().getLastName() + ", " + selected.getFaculty().getFirstName()));

                detailsPanel.add(new JLabel("Facility: "));
                detailsPanel.add(new JLabel(selected.getFacility().getType()));

                detailsPanel.add(new JLabel("Purpose: "));
                detailsPanel.add(new JLabel(selected.getPurpose()));

                detailsPanel.add(new JLabel("Start Date: "));
                detailsPanel.add(new JLabel(String.valueOf(selected.getDateStart())));

                detailsPanel.add(new JLabel("End Date: "));
                detailsPanel.add(new JLabel(String.valueOf(selected.getDateEnd())));

                detailsPanel.add(new JLabel("Time Start: "));
                detailsPanel.add(new JLabel(String.valueOf(selected.getTimeStart())));

                detailsPanel.add(new JLabel("Time End: "));
                detailsPanel.add(new JLabel(String.valueOf(selected.getTimeEnd())));

                detailsPanel.add(new JLabel("Status: "));
                detailsPanel.add(new JLabel(String.valueOf(selected.getReservationStatus())));

                JOptionPane.showMessageDialog(null, detailsPanel, "Reservation Details", JOptionPane.PLAIN_MESSAGE);
            }
        });
    }

    private void updateReservationList(String status) {
        ArrayList<Reservation> resList = Database.getReservations(0, true);
        DefaultListModel<Reservation> model = new DefaultListModel<>();

        for (Reservation curr : resList) {
            switch (status) {
                case "1":
                    if (curr.getReservationStatus().equalsIgnoreCase("PENDING")) {
                        model.addElement(curr);
                    }
                    break;
                case "2":
                    if (curr.getReservationStatus().equalsIgnoreCase("APPROVED")) {
                        model.addElement(curr);
                    }
                    break;
                case "3":
                    if (curr.getReservationStatus().equalsIgnoreCase("REJECTED")) {
                        model.addElement(curr);
                    }
                    break;
                default:
                    model.addElement(curr);
                    break;
            }
        }

        list1.setModel(model);

        showAllButton.setEnabled(!status.equals("0"));
        showPendingButton.setEnabled(!status.equals("1"));
        showApprovedButton.setEnabled(!status.equals("2"));
        showRejectedButton.setEnabled(!status.equals("3"));

        if (status.equals("2") || status.equals("3")) {
            acceptButton.setEnabled(false);
            rejectButton.setEnabled(false);
        }
    }

    private void updateReservationStatus(String status) {
        int selectedIndex = list1.getSelectedIndex();
        Reservation selected = (Reservation) list1.getSelectedValue();
        if (status.equals("2")){
            selected.setReservationStatus("APPROVED");
        }else if (status.equals("3")){
            selected.setReservationStatus("REJECTED");
        }
        Database.update("reservation", new String[]{"reservation_status"},
                new String[]{status}, "reservation_id = " + selected.getResID());

        // Update the reservation status in the JList
        DefaultListModel<Reservation> model = (DefaultListModel<Reservation>) list1.getModel();
        model.set(selectedIndex, selected);
        list1.setModel(model);
    }

    @Override
    public JPanel getPanel() {
        return manageReqPanel;
    }
}
