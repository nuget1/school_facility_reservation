package views;

import database.Database;
import database.Department;
import database.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class RegistrationView extends View{
    private JPanel registrationPanel;
    private JLabel firstNameLabel;
    private JTextField firstNameField;
    private JLabel lastNameLabel;
    private JTextField lastNameField;
    private JLabel enterPasswordLabel;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JLabel confirmPasswordLabel;
    private JRadioButton studentRadioButton;
    private JRadioButton facultyRadioButton;
    private JButton registerButton;
    private JLabel registerAsLabel;
    private JComboBox departmentsComboBox;
    private JLabel facultyDepartmentLabel;
    private JButton cancelButton;
    private JButton loginButton;
    boolean student = true;
    String dept;

    public RegistrationView(ViewPicker viewPicker) {
        this.viewPicker = viewPicker;
        updateDepartments();
    }

    @Override
    public void showTestView(int x, int y) {
        JFrame frame = new JFrame();
        frame.setContentPane(registrationPanel);

        frame.setLocationRelativeTo(null);
        frame.setSize(x, y);
        frame.setVisible(true);
    }

    public void showTestView() {
        JFrame frame = new JFrame();
        frame.setContentPane(registrationPanel);

        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void implementComponents() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPicker.pickView(
                        new LoginView(viewPicker), 500, 300
                );
            }
        });

        studentRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                facultyDepartmentLabel.setEnabled(false);
                departmentsComboBox.setEnabled(false);

                student = true;
            }
        });

        facultyRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                facultyDepartmentLabel.setEnabled(true);
                departmentsComboBox.setEnabled(true);

                student = false;
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String password = passwordField.getText();
                String confirmedPassword = confirmPasswordField.getText();

                if (firstName.equals("") || lastName.equals("") || password.equals("") || confirmedPassword.equals("")
                || (!facultyRadioButton.isSelected() && !studentRadioButton.isSelected()) ||
                        (facultyRadioButton.isSelected() && departmentsComboBox.getSelectedItem() == null)) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Please fill in the remaining fields",
                            "Missing fields",
                            JOptionPane.ERROR_MESSAGE
                    );
                } else if (!password.equals(confirmedPassword)) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Please check your passwords again",
                            "Passwords mismatch",
                            JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    Database.insert("user",new String[] {"fname", "lname", "password"},
                            new String[] {firstName, lastName, password});
                    if (student){
                        Database.insert("student",new String[] {"user_id"},
                                new String[] {String.valueOf(Database.getLastUserID())});
                    }else{
                        ArrayList<Department> departments = Database.getDepartments();
                        dept = departmentsComboBox.getSelectedItem().toString();
                        for (Department curr : departments){
                            if(curr.getDepartmentName().equalsIgnoreCase(dept)){
                                System.out.println(curr.getDepartmentID());
                                System.out.println(curr.getDepartmentName());
                                dept = String.valueOf(curr.getDepartmentID());
                            }
                        }
                        Database.insert("faculty",new String[] {"user_id", "department"},
                                new String[] {String.valueOf(Database.getLastUserID()),dept});
                    }
                    JOptionPane.showMessageDialog(
                            null,
                            "Successfully added user!",
                            "User added",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    viewPicker.pickView(
                            new LoginView(viewPicker), 500, 300
                    );
                }
            }
        });
    }

    private void updateDepartments() {
        ArrayList<Department> departments = Database.getDepartments();
        for (Department department: departments) {
            departmentsComboBox.addItem(department);
        }
    }

    @Override
    public JPanel getPanel() {
        return registrationPanel;
    }

    public static void main(String[] args) {
        new RegistrationView(null).showTestView();
    }
}
