package a03a.e2;

import javax.swing.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JFrame {
    
    private final Map<JButton, Coord> cells = new HashMap<>();
    private final Controller controller;
    
    public GUI(int size) {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(100*size, 100*size);
        controller = new ControllerImpl(size);

        JPanel panel = new JPanel(new GridLayout(size,size));
        this.getContentPane().add(panel);
        
        ActionListener al = new ActionListener(){
            public void actionPerformed(ActionEvent e){
        	    var button = (JButton)e.getSource();
        	    var position = cells.get(button);
                // button.setText(""+position);
                if(controller.movePlayer(position)) {
                    updateBoard();
                }
                final var status = controller.getGameStatus();
                if(controller.isOver()) {
                    System.out.println(status.getStatus());
                    resetGame();
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

        this.resetGame();

        this.setVisible(true);
    }

    private void updateBoard() {
        final Coord computerPos = controller.getComputerPosition(); 
        final Coord playerPos = controller.getPlayerPosition();
        /* update board */
        for (final var entry: cells.entrySet()) {
            final var pos = entry.getValue();
            final var btn = entry.getKey();
            if(pos.equals(playerPos)) {
                btn.setText("*");    
            } else if(pos.equals(computerPos)) {
                btn.setText("o");
            } else {
                entry.getKey().setText("");
            }
        }
    }

    private void resetGame() {
        controller.resetGame();
        this.updateBoard();
    }
}
