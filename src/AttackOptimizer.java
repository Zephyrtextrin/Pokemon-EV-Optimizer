import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class AttackOptimizer{
    //make the opponent a different panel and u a different panel cause im TIRED of resizing shit we are a layout manager household now
    public static void initUI() throws IOException{
        final String[] WEATHER = Pokedex.getWeather();
        final String[] natDex = Pokedex.getNatDex();
        final int SIZE = 500;

        final JFrame frame = new JFrame();
        frame.setSize(SIZE,SIZE);
        frame.setVisible(true);

        final JPanel panel = new JPanel();
        panel.setBounds(0,0,panel.getWidth(),panel.getHeight());
        panel.setLayout(null);
        frame.add(panel);



        //gallade is placeholder rn i dont have the files for everyone else


        //left side (the pokemon who is using the move
        JPanel attackerPanel = new JPanel();
        attackerPanel.setBounds(0,0,SIZE/3,SIZE);
        attackerPanel.setLayout(new BoxLayout(attackerPanel, BoxLayout.PAGE_AXIS));
        frame.add(attackerPanel);

        //text to denote who is attacking
        JLabel attackerTitleLabel = new JLabel("   Attacking");
        attackerPanel.add(attackerTitleLabel);

        //image display for your pokemon
        final File yourIMGFile = new File("src/assets/gallade.png"); //the opponentsPokemonIMGFile itself. (just the data the image isnt actually read)
        final ImageIcon yourIcon = new ImageIcon(ImageIO.read(yourIMGFile)); //ImageIO actually reads the data from the image and then that data is set to an icon
        //the label that actually displays it
        final JLabel youDisplayLabel = new JLabel(yourIcon);
        attackerPanel.add(youDisplayLabel);

        //select what pokemonis attacking
        JComboBox<String> pokemonSelect = new JComboBox<>(natDex);
        attackerPanel.add(pokemonSelect);

        //select what item PLACEHOLDER MAKE A LIST OF ALL THE ITEMS LATER
        JComboBox<String> itemSelect = new JComboBox<>(natDex);
        attackerPanel.add(itemSelect);

        //select what weather
        JComboBox<String> weather = new JComboBox<>(WEATHER);
        attackerPanel.add(weather);

        //select what move PLACEHOLDER MAKE A LIST OF ALL THE ITEMS LATER
        JComboBox<String> moveSelect = new JComboBox<>(natDex);
        attackerPanel.add(moveSelect);

        attackerPanel.revalidate();
    }
}
