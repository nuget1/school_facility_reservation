package views;

import javax.swing.*;

public class ViewPicker {

    private JFrame frame;

    public ViewPicker(JFrame frame) {
        this.frame = frame;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void pickView(View view) {
        view.implementComponents();
        frame.setContentPane(view.getPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    public void pickView(View view, int x, int y) {
        view.implementComponents();
        frame.setContentPane(view.getPanel());
        frame.setSize(x, y);
        frame.setLocationRelativeTo(null);
    }
}
