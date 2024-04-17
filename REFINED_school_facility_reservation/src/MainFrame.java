import views.LoginView;
import views.ViewPicker;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame {

    JFrame frame;
    ViewPicker viewPicker;


    public MainFrame() {
        frame = new JFrame();
        viewPicker = new ViewPicker(frame);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });
    }

    public void beginFrame() {
        frame.setTitle("Student Reservation System");
        viewPicker.pickView(
                new LoginView(viewPicker), 500, 300
        );
        frame.setVisible(true);
    }
}
