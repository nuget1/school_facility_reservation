package views;

import javax.swing.*;

public abstract class View {

    public ViewPicker viewPicker;

    public void showTestView(int x, int y) {};

    public abstract void implementComponents();

    public abstract JPanel getPanel();

}
