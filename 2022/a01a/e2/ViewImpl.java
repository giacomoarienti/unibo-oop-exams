package a01a.e2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ViewImpl implements View {
    private Controller controller;
    private final JFrame frame;

    public ViewImpl(int size, Controller controller) {
        this.controller = controller;
        /* create rame */
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(100 * size, 100 * size);

        final JPanel panel = new JPanel(new GridLayout(size, size));
        frame.getContentPane().add(panel);

        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                var button = (JButton) e.getSource();
                if (button.getText().equals(ACTIVE_BTN_LABEL)) {
                    button.setText(EMPTY_BTN_LABEL);
                } else {
                    button.setText(ACTIVE_BTN_LABEL);
                }
                ViewImpl.this.controller.registerButtonClick(button);
            }
        };

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                final JButton jb = new JButton(EMPTY_BTN_LABEL);
                this.controller.addCell(jb);
                jb.addActionListener(al);
                panel.add(jb);
            }
        }
    }

    @Override
    public void show() {
        this.frame.setVisible(true);
    }

    @Override
    public void setController(Controller controller) {
        this.setController(controller);
    }

}
