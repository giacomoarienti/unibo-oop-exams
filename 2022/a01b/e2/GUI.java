package a01b.e2;

import javax.swing.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JFrame {
    
    private final Map<JButton, Coord> cells = new HashMap<>();
    private final Controller controller = new ControllerImpl();
    
    public GUI(int size) {
        controller.setGridSize(size);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(100*size, 100*size);
        
        JPanel panel = new JPanel(new GridLayout(size,size));
        this.getContentPane().add(panel);
        
        ActionListener al = new ActionListener(){
            public void actionPerformed(ActionEvent e){
        	    final var button = (JButton)e.getSource();
        	    final var position = cells.get(button);
                /* perform action */
                controller.select(position);
                final var enabled = controller.lastEnabledCoords();
                final var disabled = controller.lastDisabledCoords();
                for (final var entry: cells.entrySet()) {
                    if(enabled.contains(entry.getValue())) {
                        entry.getKey().setText("*");
                    } else if(disabled.contains(entry.getValue())) {
                        entry.getKey().setText(" ");
                    }
                }
                if(controller.isOver()) {
                    GUI.exit();
                }
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

    private static void exit() {
        System.exit(0);
    }
}
