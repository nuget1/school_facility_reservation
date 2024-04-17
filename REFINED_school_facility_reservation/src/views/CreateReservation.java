package views;

import database.Database;
import database.Reservation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.time.*;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public class CreateReservation extends View {
    private JTextField facilityIDTextField;
    private JTextField facultyIDTextField;
    private JTextField purposeTextField;
    private JButton createButton;
    private JPanel createResPanel;
    private JButton seeFACILITIESButton;
    private JList idList;
    private JButton seeFACULTIESButton;
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
    private int userId;

    private int facilityID;
    public CreateReservation(ViewPicker viewPicker) {
        this(viewPicker, -1);
    }

    public CreateReservation(ViewPicker viewPicker, int userId) {
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

    @Override
    public void implementComponents() {

        addParticipants.setVisible(false);
        removeButton.setVisible(false);

        hourStart.removeAllItems(); // Remove all previous items from the JComboBox
        for (int i = 0; i < 24; i++) {
            String hour = String.format("%02d", i); // Add leading zero if hour is a single digit
            hourStart.addItem(hour); // Add hour as a string to the JComboBox
        }

        hourEnd.removeAllItems(); // Remove all previous items from the JComboBox
        for (int i = 0; i < 24; i++) {
            String hour = String.format("%02d", i); // Add leading zero if hour is a single digit
            hourEnd.addItem(hour); // Add hour as a string to the JComboBox
        }

        minuteStart.removeAllItems(); // Remove all previous items from the JComboBox
        for (int i = 0; i < 60; i++) {
            minuteStart.addItem(String.format("%02d", i)); // Add the minute to the JComboBox as a string formatted to have 2 digits
        }

        minuteEnd.removeAllItems(); // Remove all previous items from the JComboBox
        for (int i = 0; i < 60; i++) {
            minuteEnd.addItem(String.format("%02d", i)); // Add the minute to the JComboBox as a string formatted to have 2 digits
        }

        yearStart.removeAllItems();
        int startYear = 2023;
        int endYear = 2050;
        for (int year = startYear; year <= endYear; year++) {
            yearStart.addItem(String.valueOf(year));
        }

        yearEnd.removeAllItems();
        for (int year = startYear; year <= endYear; year++) {
            yearEnd.addItem(String.valueOf(year));
        }

        monthStart.removeAllItems(); // Remove all previous items from the JComboBox
        for (int i = 1; i < 13; i++) {
            monthStart.addItem(String.format("%02d", i)); // Add the minute to the JComboBox as a string formatted to have 2 digits
        }

        monthEnd.removeAllItems(); // Remove all previous items from the JComboBox
        for (int i = 1; i < 13; i++) {
            monthEnd.addItem(String.format("%02d", i)); // Add the minute to the JComboBox as a string formatted to have 2 digits
        }

        dayStart.removeAllItems(); // Remove all previous items from the JComboBox
        for (int i = 1; i < 32; i++) {
            dayStart.addItem(String.format("%02d", i)); // Add the minute to the JComboBox as a string formatted to have 2 digits
        }

        dayEnd.removeAllItems(); // Remove all previous items from the JComboBox
        for (int i = 1; i < 32; i++) {
            dayEnd.addItem(String.format("%02d", i)); // Add the minute to the JComboBox as a string formatted to have 2 digits
        }

        seeFACULTIESButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> facultyList = Database.getAllFaculty();
                DefaultListModel<String> facultyModel = new DefaultListModel<>();
                for (String faculty : facultyList) {
                    facultyModel.addElement(faculty);
                }
                idList.setModel(facultyModel);
                seeFACULTIESButton.setEnabled(false);
                seeFACILITIESButton.setEnabled(true);
                showAddableParticipants.setEnabled(true);
                addParticipants.setVisible(false);
                showParticipants.setEnabled(true);
                removeButton.setVisible(false);
            }
        });

        seeFACILITIESButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> facilityList = Database.getAllFacility();
                DefaultListModel<String> participantsModel = new DefaultListModel<>();
                for (String facility : facilityList) {
                    participantsModel.addElement(facility);
                }
                idList.setModel(participantsModel);
                idList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                seeFACILITIESButton.setEnabled(false);
                seeFACULTIESButton.setEnabled(true);
                showAddableParticipants.setEnabled(true);
                addParticipants.setVisible(false);
                showParticipants.setEnabled(true);
                removeButton.setVisible(false);
            }
        });

        List<String> studentList = Database.getAllStudent();
        List<String> participants = new ArrayList<>();
        List<String> removedParticipants = new ArrayList<>();
        List<String> participantIds = new ArrayList<>();

        showAddableParticipants.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultListModel<String> studentModel = new DefaultListModel<>();

                // Add back the removed participants to the studentList
                studentList.addAll(removedParticipants);
                // Clear the removedParticipants list
                removedParticipants.clear();

                for (String student : studentList) {
                    studentModel.addElement(student);
                }
                idList.setModel(studentModel);


                seeFACILITIESButton.setEnabled(true);
                seeFACULTIESButton.setEnabled(true);
                showAddableParticipants.setEnabled(false);
                addParticipants.setVisible(true);
                showParticipants.setEnabled(true);
                removeButton.setVisible(false);
            }
        });

        addParticipants.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultListModel<String> addStudentsModel = new DefaultListModel<>();

                List<String> selectedValues = idList.getSelectedValuesList();
                // Add the selected students to the addedStudentsModel
                for (String studentId : selectedValues) {
                    addStudentsModel.addElement(studentId);
                    participants.add(studentId);
                    studentList.remove(studentId);
                }
                // Add back the removed participants to the participants list
                participants.addAll(removedParticipants);
                // Clear the removedParticipants list
                removedParticipants.clear();

                // Update the addable model
                DefaultListModel<String> studentModel = new DefaultListModel<>();

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
                // Remove the selected students from the participants list and add them to the removedParticipants list
                for (String studentId : selectedValues) {
                    participants.remove(studentId);
                    removedParticipants.add(studentId);
                }

                // Update the participants model
                DefaultListModel<String> addedStudentsModel = new DefaultListModel<>();
                if (!participants.isEmpty()) {
                    for (String participant : participants) {
                        addedStudentsModel.addElement(participant);
                    }
                }
                idList.setModel(addedStudentsModel);
            }
        });

        showParticipants.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultListModel<String> addedStudentsModel = new DefaultListModel<>();
                addedStudentsModel.addElement("Student ID" + "   |   " + "Name");
                // Set the addedStudentsModel to the idList to display added students
                if (!participants.isEmpty()) {
                    for (String participant : participants) {
                        addedStudentsModel.addElement(participant);
                    }
                }
                idList.setModel(addedStudentsModel);

                seeFACILITIESButton.setEnabled(true);
                seeFACULTIESButton.setEnabled(true);
                showAddableParticipants.setEnabled(true);
                addParticipants.setVisible(false);
                showParticipants.setEnabled(false);
                removeButton.setVisible(true);
            }
        });

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String startTime = hourStart.getSelectedItem().toString() + ":" + minuteStart.getSelectedItem().toString() + ":00";
                String endTime = hourEnd.getSelectedItem().toString() + ":" + minuteEnd.getSelectedItem().toString() + ":00";
                String startDate = yearStart.getSelectedItem().toString() + "-" + monthStart.getSelectedItem().toString() + "-"  + dayStart.getSelectedItem().toString();
                String endDate = yearEnd.getSelectedItem().toString() + "-" + monthEnd.getSelectedItem().toString() + "-"  + dayEnd.getSelectedItem().toString();
                boolean isStartDateBeforeEndDate = isStartDateBeforeEndDate(startDate, endDate);
                boolean isStartTimeBeforeEndTime = isStartTimeBeforeEndTime(startTime, endTime);
                boolean isEndDateAtLeastOneDayAfterStartDate = isEndDateAtLeastOneDayAfterStartDate(startDate,endDate);
                boolean moreThanAWeek = isOneWeek(startDate, endDate);
                boolean lessThanAnHour = isAnHour(startTime, endTime);
                boolean moreThanCapacity = isMoreThanCapacity(Integer.parseInt(facilityIDTextField.getText()),participants.size());


                if(isEndDateAtLeastOneDayAfterStartDate){
                    isStartTimeBeforeEndTime = true;
                    lessThanAnHour = true;
                }

                facilityID = Integer.parseInt(facilityIDTextField.getText());

                    if (facilityIDTextField.getText().isEmpty() ||
                            purposeTextField.getText().isEmpty() ||
                            hourStart.getSelectedItem() == null ||
                            hourEnd.getSelectedItem() == null ||
                            minuteStart.getSelectedItem() == null ||
                            minuteEnd.getSelectedItem() == null) {
                        JOptionPane.showMessageDialog(createResPanel, "Please fill in all fields.");
                    } else if (!Database.facilityExists(Integer.parseInt(facilityIDTextField.getText()))) {
                        JOptionPane.showMessageDialog(createResPanel, "Facility ID does not exist.");
                        } else if (!facultyIDTextField.getText().isEmpty() && !Database.facultyExists(Integer.parseInt(facultyIDTextField.getText()))) {
                            JOptionPane.showMessageDialog(createResPanel, "Faculty ID does not exist.");
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
                                                        } else if (moreThanCapacity) {
                                                                JOptionPane.showMessageDialog(createResPanel, "Participants should not be more than the facility capacity.");
                                                            } else if (!Database.isTimeRangeAvailable(facilityID, startTime, endTime, startDate, endDate)) {
                                                                    JOptionPane.showMessageDialog(createResPanel, "The selected time range overlaps with an existing reservation.");
                                                                } else if (!Database.isStudentInAnotherReservation(Database.getStudent(userId, true).getStudentId(), startTime, endTime, startDate, endDate)) {
                                                                        String studentName = Database.getStudent(userId, true).toString();
                                                                        JOptionPane.showMessageDialog(createResPanel, studentName + " is already in a reservation in that time range.");
                                                                    } else {
                                                                        for (String participant : participants) {
                                                                            String[] parts = participant.split(" ");
                                                                            String studentId = parts[0];
                                                                            participantIds.add(studentId);
                                                                        }
                                                                        int studentID = Database.getStudent(userId, true).getStudentId();
                                                                        String facultyIDText = facultyIDTextField.getText();
                                                                        int facultyID;
                                                                        if (facultyIDText.isEmpty()) {
                                                                            facultyID = 0;
                                                                        } else {
                                                                            facultyID = Integer.parseInt(facultyIDText);
                                                                        }
                                                                        int reservationStatus = 1; // assuming all reservations start as pending
                                                                        String purpose = purposeTextField.getText();
                                                                        int res_id = Database.createReservation(studentID, facilityID, facultyID, reservationStatus, purpose, startTime, endTime, startDate, endDate);
                                                                        Database.insertParticipants(participantIds, res_id);
                                                                        JOptionPane.showMessageDialog(createResPanel, "Successfully created reservation.");
                                                                        viewPicker.pickView( new StudentMenuView(viewPicker, userId), 450, 400);
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



    private boolean isStartDateBeforeEndDate(String startDateStr, String endDateStr) {
        LocalDate startDate, endDate;
        try {
            startDate = LocalDate.parse(startDateStr);
            endDate = LocalDate.parse(endDateStr);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(createResPanel, "Date does not exist.");
            return false;
        }
        return !startDate.isAfter(endDate);
    }

    public boolean isStartTimeBeforeEndTime(String startTime, String endTime) {
        try {
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);
            return start.isBefore(end);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isEndDateAtLeastOneDayAfterStartDate(String startDateStr, String endDateStr) {
        LocalDate startDate, endDate;
        try {
            startDate = LocalDate.parse(startDateStr);
            endDate = LocalDate.parse(endDateStr);
        } catch (DateTimeParseException e) {
            return false;
        }
        return startDate.plusDays(1).isBefore(endDate) || startDate.plusDays(1).isEqual(endDate);
    }

    public static boolean isAnHour(String startTime, String endTime) {
        try {
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);
            Duration duration = Duration.between(start, end);
            return duration.toMinutes() >= 60;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isOneWeek(String startDate, String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            long daysBetween = ChronoUnit.DAYS.between(start, end);
            return daysBetween < 7;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isMoreThanCapacity(int reservation, int participants) {
        return Database.getFacility(reservation).getCapacity() < participants;
    }




    @Override
    public JPanel getPanel() {
        return createResPanel;
    }

    public static void main(String[] args) {
        new CreateReservation(null).showTestView(900,500);

    }
}
