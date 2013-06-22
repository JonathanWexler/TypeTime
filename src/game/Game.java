package game;

import javax.swing.JFrame;

import layers.NameLayer;

import java.io.FileNotFoundException;

@SuppressWarnings("serial")
public class Game extends JFrame {
    static NameLayer nl;
    
    public Game() throws FileNotFoundException {

        nl = new NameLayer();
        add(nl);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setTitle("Little Stars School: Learning Game");
        setResizable(false);
        setVisible(true);
//        this.setExtendedState(this.MAXIMIZED_BOTH);
//        this.setUndecorated(true);



    }

    public static void main(String[] args) throws FileNotFoundException {
        new Game();
    }

}