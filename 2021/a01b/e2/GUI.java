package a01b.e2;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GUI extends JFrame {
    
    private static final long serialVersionUID = -6218820567019985015L;
    private final Map<JButton, Coord> cells = new HashMap<>();
    private final Controller controller;
    
    public GUI(int size) {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(50*size, 50*size);

        controller = new ControllerImpl(size);
        
        JPanel panel = new JPanel(new GridLayout(size,size));
        this.getContentPane().add(panel);
        
        ActionListener al = e -> {
        	final var button = (JButton)e.getSource();
        	final var coord = cells.get(button);
            final var res = controller.addPoint(coord);
            /* register click */
            if(res.isPresent()) {
                button.setText(String.valueOf(res.get()));
                button.setEnabled(false);
            }
            /* check if game is over */
            if(controller.isOver()) {
                gameOver(controller.getAngle());
            }
        };
                
        for (int i=0; i<size; i++){
            for (int j=0; j<size; j++){
                final JButton jb = new JButton(" ");
                this.cells.put(jb, new Coord(i, j));
                jb.addActionListener(al);
                panel.add(jb);
            }
        }
        this.setVisible(true);
    }

    private void gameOver(final Set<Coord> angle) {
        for (final var entry: cells.entrySet()) {
            final var btn = entry.getKey();
            if(angle.contains(entry.getValue())) {
                btn.setText("*");
            }
            btn.setEnabled(false);
        }
    }
    
}
