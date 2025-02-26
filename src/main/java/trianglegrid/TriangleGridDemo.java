package trianglegrid;


import javax.swing.*;
import java.awt.*;

public class TriangleGridDemo {
    public TriangleGridDemo()
    {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Composite Bezier Curve Demo");
        frame.setSize(800, 800);
        JPanel panel = createPreviewPane();
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private JPanel createPreviewPane() {
        return new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

            }
        };
    }

    public static void main(String[] args) {
        new TriangleGridDemo();
    }
}