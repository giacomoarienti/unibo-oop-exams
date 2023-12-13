package a02a.e2;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GUI extends JFrame {
    
    private static final long serialVersionUID = -6218820567019985015L;
    private final HashMap<Coord, JButton> cells = new HashMap<>();
    private final Controller controller;
    
    public GUI(int size) {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(50*size, 50*size);
        controller = new ControllerImpl(size);
        
        JPanel panel = new JPanel(new GridLayout(size,size));
        this.getContentPane().add(panel);
        
        ActionListener al = e -> {
            final Optional<Pair<Coord, Integer>> res = controller.newNumber();
            if(res.isEmpty()) {
                this.exit();
            }
            final Coord coord = res.get().get1();
            final int number = res.get().get2();
            final var button = this.cells.get(coord);
            button.setText(String.valueOf(number));
        };
                
        for (int i=0; i<size; i++){
            for (int j=0; j<size; j++){
                final JButton jb = new JButton(" ");
                this.cells.put(new Coord(i, j), jb);
                jb.addActionListener(al);
                panel.add(jb);
            }
        }
        this.setVisible(true);
    }
    
    public void exit() {
        System.exit(0);
    }
}
