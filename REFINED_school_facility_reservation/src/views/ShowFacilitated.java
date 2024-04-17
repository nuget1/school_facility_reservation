package views;

import database.Database;
import database.Reservation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ShowFacilitated extends View{
    private JPanel panel1;
    private JButton backButton;
    private JButton showDetailsButton;
    private JList list1;
    private int userId;

    public ShowFacilitated(ViewPicker viewPicker, int userId) {
        this.viewPicker = viewPicker;
        this.userId = userId;
    }
    @Override
    public void implementComponents() {
        ArrayList<Reservation> resList = Database.getReservations(userId, true);
        DefaultListModel<Reservation> model = new DefaultListModel<>();
        int facultyId;

        for (Reservation curr : resList) {
            try{
                facultyId = curr.getFaculty().getFacultyId();
            }catch (NullPointerException e){
                continue;
            }
            if (facultyId == userId) {
                model.addElement(curr);
            }
        }

        list1.setModel(model);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPicker.pickView(
                        new FacultyMenuView(viewPicker, userId), 450, 400);
            }
        });

        showDetailsButton.addActionListener(new ActionListener() {
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

                JOptionPane.showMessageDialog(null, detailsPanel, "Reservation Details", JOptionPane.PLAIN_MESSAGE);
            }
        });
    }

    @Override
    public JPanel getPanel() {
        return panel1;
    }
}
