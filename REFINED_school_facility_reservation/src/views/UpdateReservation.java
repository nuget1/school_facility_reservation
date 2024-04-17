package views;

import database.Database;
import database.Reservation;
import database.Student;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static views.CreateReservation.isAnHour;
import static views.CreateReservation.isOneWeek;

public class UpdateReservation extends View {
    private JTextField facilityIDTextField;
    private JTextField facultyIDTextField;
    private JTextField purposeTextField;
    private JButton confirmButton;
    private JPanel createResPanel;
    private JList idList;
    private JButton cancelButton;
    private JComboBox hourStart;
    private JComboBox minuteStart;
    private JComboBox hourEnd;
    private JComboBox dayStart;
    private JComboBox minuteEnd;
    private JComboBox yearStart;
    private JComboBox monthStart;
    private JComboBox yearEnd;
    private JComboBox monthEnd;
    private JComboBox dayEnd;
    private JButton showAddableParticipants;
    private JButton addParticipants;
    private JButton showParticipants;
    private JButton removeButton;
    private JButton showPendingReservationsButton;
    private JTextField reservationIDTextField;
    private JButton selectButton;
    private int resID;
    private int userId;

    public UpdateReservation(ViewPicker viewPicker) {
        this(viewPicker, -1);
    }

    public UpdateReservation(ViewPicker viewPicker, int userId) {
        this.viewPicker = viewPicker;
        this.userId = userId;
    }

    @Override
    public void showTestView(int x, int y) {
        JFrame frame = new JFrame();
        frame.setContentPane(createResPanel);

        frame.setLocationRelativeTo(null);
        frame.setSize(x, y);
        frame.setVisible(true);
    }

    public void showTestView() {
        JFrame frame = new JFrame();
        frame.setContentPane(createResPanel);

        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    private void addItemsToComboBox(JComboBox<String> comboBox, int startValue, int endValue) {
        comboBox.removeAllItems(); // Remove all previous items from the JComboBox
        for (int i = startValue; i <= endValue; i++) {
            String value = String.format("%02d", i); // Add leading zero if value is a single digit
            comboBox.addItem(value); // Add value as a string to the JComboBox
        }
    }

    private int populateList(ArrayList<Reservation> resList){
        DefaultListModel<Reservation> listModel = new DefaultListModel<>();
        int count = 0;

        for (Reservation res : resList) {
            if (res.getReservationStatus().equalsIgnoreCase("PENDING")){
                listModel.addElement(res);
                count++;
            }
        }
        idList.setModel(listModel);
        return count;
    }

    @Override
    public void implementComponents() {
        ArrayList<Reservation> resList = Database.getReservations(userId, false);
        DefaultListModel<String> addedStudentsModel = new DefaultListModel<>();
        List<String> studentList = Database.getAllStudent();
        List<String> participants = new ArrayList<>();
        List<String> removedParticipants = new ArrayList<>();
        List<String> participantIds = new ArrayList<>();
        List<String> temp = new ArrayList<>();

        addParticipants.setVisible(false);
        removeButton.setVisible(false);
        showParticipants.setEnabled(false);
        showAddableParticipants.setEnabled(false);

        // Add items to the combo boxes
        addItemsToComboBox(hourStart, 0, 23);
        addItemsToComboBox(hourEnd, 0, 23);
        addItemsToComboBox(minuteStart, 0, 59);
        addItemsToComboBox(minuteEnd, 0, 59);

        addItemsToComboBox(yearStart, 2023, 2050);
        addItemsToComboBox(yearEnd, 2023, 2050);
        addItemsToComboBox(monthStart, 1, 12);
        addItemsToComboBox(monthEnd, 1, 12);
        addItemsToComboBox(dayStart, 1, 31);
        addItemsToComboBox(dayEnd, 1, 31);

        int count = populateList(resList);
        if (count > 0){
            idList.setSelectedIndex(0);
            updateSelectedReservationDetails();
        } else {
            JOptionPane.showMessageDialog(createResPanel,
                    "No Pending Reservations.",
                    "Message",
                    JOptionPane.OK_OPTION);
            viewPicker.pickView(new StudentMenuView(viewPicker, userId), 450, 400);
        }

        showPendingReservationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                populateList(resList);
            }
        });

        idList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Object selectedValue = idList.getSelectedValue();
                if (selectedValue instanceof Reservation) {
                    updateSelectedReservationDetails();
                }
            }
        });

        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Student> studList = Database.getParticipants(Integer.parseInt(reservationIDTextField.getText()));
                addedStudentsModel.clear();
                addedStudentsModel.addElement("Student ID   |   Name");
                participants.clear();

                for (Student curr : studList){
                    addedStudentsModel.addElement(String.format("%-15s | %-30s", curr.getStudentId(),
                            curr.getFirstName() + " " + curr.getLastName()));
                    participants.add(String.format("%-15s | %-30s", curr.getStudentId(),
                            curr.getFirstName() + " " + curr.getLastName()));
                }
                idList.setModel(addedStudentsModel);

                showPendingReservationsButton.setVisible(false);
                addParticipants.setVisible(false);
                removeButton.setVisible(true);
                selectButton.setEnabled(false);

                showParticipants.setEnabled(false);
                showAddableParticipants.setEnabled(true);
            }
        });

        showAddableParticipants.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addedStudentsModel.clear();
                addedStudentsModel.addElement("Student ID   |   Name");

                studentList.removeAll(participants);

                for (String student : studentList) {
                    addedStudentsModel.addElement(student);
                }
                idList.setModel(addedStudentsModel);

                showAddableParticipants.setEnabled(false);
                addParticipants.setVisible(true);
                showParticipants.setEnabled(true);
                removeButton.setVisible(false);
            }
        });

        addParticipants.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> selectedValues = idList.getSelectedValuesList();
                // Add the selected students to the participants list
                participants.addAll(selectedValues);
                // Remove the selected students from the studentList
                studentList.removeAll(selectedValues);

                // Update the addable model
                DefaultListModel<String> studentModel = new DefaultListModel<>();
                studentModel.addElement("Student ID   |   Name");
                for (String student : studentList) {
                    studentModel.addElement(student);
                }
                idList.setModel(studentModel);
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> selectedValues = idList.getSelectedValuesList();
                // Remove the selected students from the participants list
                participants.removeAll(selectedValues);
                // Add the selected students back to the studentList
                studentList.addAll(selectedValues);

                // Update the addable model
                DefaultListModel<String> studentModel = new DefaultListModel<>();
                studentModel.addElement("Student ID   |   Name");
                for (String student : participants) {
                    studentModel.addElement(student);
                }
                idList.setModel(studentModel);
            }
        });

        showParticipants.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addedStudentsModel.clear();
                addedStudentsModel.addElement("Student ID   |   Name");

                for (String curr : participants){
                    addedStudentsModel.addElement(curr);
                }
                idList.setModel(addedStudentsModel);

                showAddableParticipants.setEnabled(true);
                addParticipants.setVisible(false);
                showParticipants.setEnabled(false);
                removeButton.setVisible(true);
            }
        });

        idList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value != null && value.toString().equals("Student ID   |   Name")) {
                    setEnabled(false);
                } else {
                    setEnabled(true);
                }

                return component;
            }
        });
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String startTime = hourStart.getSelectedItem().toString() + ":" + minuteStart.getSelectedItem().toString() + ":00";
                String endTime = hourEnd.getSelectedItem().toString() + ":" + minuteEnd.getSelectedItem().toString() + ":00";
                String startDate = yearStart.getSelectedItem().toString() + "-" + monthStart.getSelectedItem().toString() + "-"  + dayStart.getSelectedItem().toString();
                String endDate = yearEnd.getSelectedItem().toString() + "-" + monthEnd.getSelectedItem().toString() + "-"  + dayEnd.getSelectedItem().toString();
                boolean isStartDateBeforeEndDate = isStartDateBeforeEndDate(startDate, endDate);
                boolean isStartTimeBeforeEndTime = isStartTimeBeforeEndTime(startTime, endTime);
                boolean isEndDateAtLeastOneDayAfterStartDate = CreateReservation.isEndDateAtLeastOneDayAfterStartDate(startDate,endDate);
                boolean moreThanAWeek = isOneWeek(startDate, endDate);
                boolean lessThanAnHour = isAnHour(startTime, endTime);

                if(isEndDateAtLeastOneDayAfterStartDate){
                    isStartTimeBeforeEndTime = true;
                    lessThanAnHour = true;
                }

                int facilityID = Integer.parseInt(facilityIDTextField.getText());

                if (facilityIDTextField.getText().isEmpty() ||
                        purposeTextField.getText().isEmpty() ||
                        hourStart.getSelectedItem() == null ||
                        hourEnd.getSelectedItem() == null ||
                        minuteStart.getSelectedItem() == null ||
                        minuteEnd.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(createResPanel, "Please fill in all fields.");
                } else if (!Database.facilityExists(Integer.parseInt(facilityIDTextField.getText()))) {
                    JOptionPane.showMessageDialog(createResPanel, "Facility ID does not exist.");
                } else if (startTime.equals("00:00:00") || endTime.equals("00:00:00")) {
                    JOptionPane.showMessageDialog(createResPanel, "Input Start Time or End Time.");
                } else if (!isStartDateBeforeEndDate) {
                    JOptionPane.showMessageDialog(createResPanel, "Date End must not come before Date Start.");
                } else if (!isStartTimeBeforeEndTime) {
                    JOptionPane.showMessageDialog(createResPanel, "Time End must not come before Time Start.");
                } else if (!moreThanAWeek) {
                    JOptionPane.showMessageDialog(createResPanel, "Reservations must not exceed 1 week.");
                } else if (!lessThanAnHour) {
                    JOptionPane.showMessageDialog(createResPanel, "Reservations must be at least an hour long.");
                }  else if (participants.isEmpty()) {
                    JOptionPane.showMessageDialog(createResPanel, "There must be participants.");
                } else if (!Database.isTimeRangeAvailable(facilityID, startTime, endTime, startDate, endDate)) {
                    JOptionPane.showMessageDialog(createResPanel, "The selected time range overlaps with an existing reservation.");
                } else {
                    for (String participant : participants) {
                        String[] parts = participant.split(" ");
                        String studentId = parts[0];
                        participantIds.add(studentId);
                    }
                    String purpose = purposeTextField.getText();
                    Database.update("reservation",
                            new String[]{"purpose", "time_start", "time_end", "date_start", "date_end"},
                            new String[]{ purpose, startTime, endTime, startDate, endDate },
                            "reservation_id = " + reservationIDTextField.getText());
                    Database.insertParticipants(participantIds, Integer.parseInt(reservationIDTextField.getText()));
                    JOptionPane.showMessageDialog(createResPanel, "Successfully updated reservation.");

                    participants.clear();
                    removedParticipants.clear();
                    participantIds.clear();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPicker.pickView(
                        new StudentMenuView(viewPicker, userId), 450, 400
                );
            }
        });
    }

    public boolean isStartDateBeforeEndDate(String startDate, String endDate) {
        // Convert start and end dates to Java's LocalDate objects
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        // Compare start and end dates using LocalDate's isBefore method
        return start.isBefore(end) || start.isEqual(end);
    }

    public boolean isStartTimeBeforeEndTime(String startTime, String endTime) {
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);
        return start.isBefore(end);
    }

    private void updateSelectedReservationDetails() {
        Reservation selectedReservation = (Reservation) idList.getSelectedValue();
        resID = selectedReservation.getResID();

        //Basic Details
        reservationIDTextField.setText(String.valueOf(selectedReservation.getResID()));
        reservationIDTextField.setEnabled(false);
        facilityIDTextField.setText(String.valueOf(selectedReservation.getFacility().getFacilityId()));
        facilityIDTextField.setEnabled(false);

        try{
            facultyIDTextField.setText(String.valueOf(selectedReservation.getFaculty().getFacultyId()));
        } catch (Exception e){
            facultyIDTextField.setText("");
        }

        facultyIDTextField.setEnabled(false);
        purposeTextField.setText(String.valueOf(selectedReservation.getPurpose()));

        // Time
        LocalTime startTime = selectedReservation.getTimeStart().toLocalTime();
        hourStart.setSelectedItem(String.valueOf(startTime.getHour()));
        minuteStart.setSelectedItem(String.valueOf(startTime.getMinute()));

        LocalTime endTime = selectedReservation.getTimeEnd().toLocalTime();
        hourEnd.setSelectedItem(String.valueOf(endTime.getHour()));
        minuteEnd.setSelectedItem(String.valueOf(endTime.getMinute()));

        // Date
        String startDate = String.valueOf(selectedReservation.getDateStart());
        LocalDate startLocalDate = LocalDate.parse(startDate);
        yearStart.setSelectedItem(String.valueOf(startLocalDate.getYear()));
        monthStart.setSelectedItem(String.format("%02d", startLocalDate.getMonthValue()));
        dayStart.setSelectedItem(String.format("%02d", startLocalDate.getDayOfMonth()));

        String endDate = String.valueOf(selectedReservation.getDateEnd());
        LocalDate endLocalDate = LocalDate.parse(endDate);
        yearEnd.setSelectedItem(String.valueOf(endLocalDate.getYear()));
        monthEnd.setSelectedItem(String.format("%02d", endLocalDate.getMonthValue()));
        dayEnd.setSelectedItem(String.format("%02d", endLocalDate.getDayOfMonth()));
    }

    @Override
    public JPanel getPanel() {
        return createResPanel;
    }

    public static void main(String[] args) {
        new UpdateReservation(null).showTestView(600,500);

    }
}
