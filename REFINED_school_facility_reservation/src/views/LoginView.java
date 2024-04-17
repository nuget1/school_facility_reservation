package views;

import database.Database;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends View{
    private JPanel loginPanel;
    private JLabel idLabel;
    private JTextField idField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JButton exitButton;


    public LoginView(ViewPicker viewPicker) {
        this.viewPicker = viewPicker;
    }

    @Override
    public void showTestView(int x, int y) {
        JFrame frame = new JFrame();
        frame.setContentPane(loginPanel);

        frame.setSize(x, y);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void showTestView() {
        JFrame frame = new JFrame();
        frame.setContentPane(loginPanel);

        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }


    @Override
    public void implementComponents() {

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPicker.pickView(
                        new RegistrationView(viewPicker), 750, 350
                );
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (idField.getText().equals("") || passwordField.getText().equals("")) {
                    JOptionPane.showMessageDialog(
                            null,
                            "You must input the missing credentials",
                            "Missing credentials",
                            JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    try {
                        int userId = Integer.parseInt(idField.getText());
                        String password = passwordField.getText();
                        boolean userExists = Database.checkUser(userId, password);
                        boolean isStudent = Database.checkStudent(userId);
                        if (!userExists) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "There is no user with the given ID",
                                    "No User Exists",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                        else if (userExists && isStudent) {
                            viewPicker.pickView(
                                    new StudentMenuView(viewPicker, userId), 450, 400
                            );
                        } else {
                            viewPicker.pickView(
                                    new FacultyMenuView(viewPicker, userId), 450, 400
                            );
                        }
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Password given was incorrect",
                                "Incorrect Password",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    @Override
    public JPanel getPanel() {
        return loginPanel;
    }

    public static void main(String[] args) {
        new LoginView(null).showTestView(300, 350);
    }
}
