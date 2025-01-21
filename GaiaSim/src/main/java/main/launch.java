package main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import map.TileMap;
import ui.MainUI;

public class launch {
    
    public static void main(String[] args) {
        
        TileMap tm = new TileMap(257,257);
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.setSize(980,980);
                frame.setResizable(false);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new MainUI(1024,1024,tm));
                frame.setVisible(true);
            }
        });
    }
    
}
