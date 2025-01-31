import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AttackOptimizer{
    //make the opponent a different panel and u a different panel cause im TIRED of resizing shit we are a layout manager household now
    public static void initUI() throws IOException{
        final String[] WEATHER = Constants.WEATHER;
        final String[] natDex = getNatDexAsStringArray();
        final int SIZE = 500;
        final int BOUNDS = 35;

        final JFrame frame = new JFrame();
        frame.setSize(SIZE,SIZE);

        frame.setLayout(new GridLayout(1,3));
        frame.setVisible(true);

        //gallade is placeholder rn i dont have the files for everyone else


        //left side (the pokemon who is using the move
        final JPanel attackerPanel = new JPanel();
        attackerPanel.setBounds(0,0,frame.getWidth()/3,frame.getHeight()-BOUNDS);
        attackerPanel.setLayout(new BoxLayout(attackerPanel, BoxLayout.PAGE_AXIS));
        frame.add(attackerPanel);

        //text to denote who is attacking
        final JLabel attackerTitleLabel = new JLabel("   Attacking");
        attackerPanel.add(attackerTitleLabel);

        //image display for your pokemon
        final File yourIMGFile = new File("src/assets/gallade.png"); //the opponentsPokemonIMGFile itself. (just the data the image isnt actually read)
        final ImageIcon yourIcon = new ImageIcon(ImageIO.read(yourIMGFile)); //ImageIO actually reads the data from the image and then that data is set to an icon
        //the label that actually displays it
        final JLabel youDisplayLabel = new JLabel(yourIcon);
        attackerPanel.add(youDisplayLabel);

        //select what pokemonis attacking
        final JComboBox<String> pokemonSelect = new JComboBox<>(natDex);
        attackerPanel.add(pokemonSelect);

        //select what item PLACEHOLDER MAKE A LIST OF ALL THE ITEMS LATER
        final JComboBox<String> itemSelect = new JComboBox<>(natDex);
        attackerPanel.add(itemSelect);
        
        //select what nature PLACEHOLDER MAKE A LIST OF ALL THE ITEMS LATER
        final JComboBox<String> natureSelect = new JComboBox<>(natDex);
        attackerPanel.add(natureSelect);

        //select what move PLACEHOLDER MAKE A LIST OF ALL THE ITEMS LATER
        final JComboBox<String> moveSelect = new JComboBox<>(natDex);
        attackerPanel.add(moveSelect);

        //select what attackBoost
        final JTextField attackBoost = new JTextField("Attack Boosts");
        attackerPanel.add(attackBoost);

        //select what spattackBoost
        final JTextField specialAttackBoost = new JTextField("Special Attack Boosts");
        attackerPanel.add(specialAttackBoost);

        attackerPanel.revalidate();

        //other features like weather
        final JPanel other = new JPanel();
        other.setBounds((int)((double)frame.getWidth()*((double)2/3)),0,frame.getWidth()/3,frame.getHeight()-BOUNDS);
        other.setLayout(new BoxLayout(other, BoxLayout.PAGE_AXIS));
        frame.add(other);

        //text to denote who is attacking
        final JLabel otherTitleLabel = new JLabel("Other");
        other.add(otherTitleLabel);

        //select what weather
        final JComboBox<String> weather = new JComboBox<>(WEATHER);
        other.add(weather);

        final JButton run = new JButton("Run");
        other.add(run);


        //right side (the pokemon who is defending

        final JPanel defenderPanel = new JPanel();
        attackerPanel.setBounds(0,0,frame.getWidth()/3,frame.getHeight()-BOUNDS);
        defenderPanel.setLayout(new BoxLayout(defenderPanel, BoxLayout.PAGE_AXIS));
        frame.add(defenderPanel);

        //text to denote who is attacking
        final JLabel defenderTitleLabel = new JLabel("   Defending");
        defenderPanel.add(defenderTitleLabel);

        //image display for your pokemon
        final File opponentIMGFile = new File("src/assets/gallade.png"); //the opponentsPokemonIMGFile itself. (just the data the image isnt actually read)
        final ImageIcon opponentIcon = new ImageIcon(ImageIO.read(opponentIMGFile)); //ImageIO actually reads the data from the image and then that data is set to an icon
        //the label that actually displays it
        final JLabel opponentDisplayLabel = new JLabel(opponentIcon);
        defenderPanel.add(opponentDisplayLabel);

        //select what pokemonis attacking
        final JComboBox<String> opponentPokemonSelect = new JComboBox<>(natDex);
        defenderPanel.add(opponentPokemonSelect);

        //select what nature PLACEHOLDER MAKE A LIST OF ALL THE ITEMS LATER
        final JComboBox<String> opponentNatureSelect = new JComboBox<>(natDex);
        defenderPanel.add(opponentNatureSelect);

        //select what hp ev
        final JTextField HP_EV = new JTextField("HP EV");
        defenderPanel.add(HP_EV);

        //select what defense ev
        final JTextField defenseEV = new JTextField("Defense EV");
        defenderPanel.add(defenseEV);

        //select what spdef ev
        final JTextField  spdefEV = new JTextField("Special Defense EV");
        defenderPanel.add(spdefEV);

        //select what def boost
        final JTextField defBoosts = new JTextField("Defense Boosts");
        defenderPanel.add(defBoosts);

        //select what spdef boost
        final JTextField spdefBoosts = new JTextField("Special Defense Boosts");
        defenderPanel.add(spdefBoosts);

        defenderPanel.revalidate();
    }

    private static String[] getNatDexAsStringArray(){
        final ArrayList<String> natDexAsArrayList = Pokedex.getNatDexAsArrayList();
        final int size = natDexAsArrayList.size();
        final String[] natDex = new String[size];

        for(int i =0;i<size;i++){natDex[i] = natDexAsArrayList.get(i);}
        return natDex;
    }
}
