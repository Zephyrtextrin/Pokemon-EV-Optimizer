import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main{
    public static void main(String[] args){
        Database.initialize(); //loads the pokedex
        final int SIZE = 200;

        final JFrame frame = new JFrame();
        frame.setSize(SIZE*3, SIZE);
        frame.setLayout(new GridLayout());
        frame.setVisible(true);

        final JButton ev = new JButton("EV Optimizer");
        frame.add(ev);

        final JButton db = new JButton("Pokemon Database");
        frame.add(db);

        db.addActionListener(_-> PokemonDatabaseUI.initUI());

        ev.addActionListener(_-> {
            try{EVCalculatorUI.initUI();
            }catch(IOException e) {throw new RuntimeException(e);}
        });

        frame.repaint();
        frame.revalidate();

        //FOR DEBUG ONLY. REMOVE ASAP
        int speed = Calculators.statCalculation(135,31,140,1.0,100,0);
        Calculators.findSpeedPercentage(speed);
    }
}
