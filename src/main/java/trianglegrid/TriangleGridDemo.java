package trianglegrid;


import trianglegrid.grid.*;

import javax.swing.*;
import java.awt.*;

public class TriangleGridDemo {
    private TriangleGrid grid = new TriangleGrid(8, 8);
    private TriangleGridRenderer renderer = new DemoGridRender(grid, 60);

    public TriangleGridDemo()
    {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Triangle Grid Demo");
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
                g2d.translate(250, 150);
                renderer.drawGrid(g2d);
            }
        };
    }

    public static void main(String[] args) {
        new TriangleGridDemo();
    }
}