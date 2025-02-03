import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class AttackOptimizer extends Pokedex{
    //make the opponent a different panel and u a different panel cause im TIRED of resizing shit we are a layout manager household now
    public static void initUI() throws IOException{
        final String[] WEATHER = Constants.WEATHER_LIST;
        final String[] natDex = arrayListToArray(getNatDexAsArrayList());
        final String[] moveList = arrayListToArray(getMoveListAsArrayList());
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

        //select what level PLACEHOLDER MAKE A LIST OF ALL THE ITEMS LATER
        final JTextField yourLevelSelect = new JTextField();
        attackerPanel.add(yourLevelSelect);

        //select what item PLACEHOLDER MAKE A LIST OF ALL THE ITEMS LATER
        final JComboBox<String> itemSelect = new JComboBox<>(natDex);
        attackerPanel.add(itemSelect);

        //select what nature PLACEHOLDER MAKE A LIST OF ALL THE ITEMS LATER
        final JComboBox<String> natureSelect = new JComboBox<>(Constants.NATURE);
        attackerPanel.add(natureSelect);

        //select what move PLACEHOLDER MAKE A LIST OF ALL THE ITEMS LATER
        final JComboBox<String> moveSelect = new JComboBox<>(moveList);
        attackerPanel.add(moveSelect);

        //select what attackBoost
        final JComboBox<String> attackBoost = new JComboBox<>(Constants.BOOSTS);
        attackerPanel.add(attackBoost);

        //select what spattackBoost
        final JComboBox<String> specialAttackBoost = new JComboBox<>(Constants.BOOSTS);
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

        //select what item PLACEHOLDER MAKE A LIST OF ALL THE ITEMS LATER
        final JTextField oppLevelSelect = new JTextField();
        defenderPanel.add(oppLevelSelect);

        //select what nature PLACEHOLDER MAKE A LIST OF ALL THE ITEMS LATER
        final JComboBox<String> opponentNatureSelect = new JComboBox<>(Constants.NATURE);
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
        final JComboBox<String> defBoosts = new JComboBox<>(Constants.BOOSTS);
        defenderPanel.add(defBoosts);

        //select what spdef boost
        final JComboBox<String> spdefBoosts = new JComboBox<>(Constants.BOOSTS);
        defenderPanel.add(spdefBoosts);

        defenderPanel.revalidate();

        //gathers all info on both sides and performs preliminary calcs needed for damage calcing
        run.addActionListener(_ ->{
            final Move move = getMove((String)moveSelect.getSelectedItem());

            final Pokemon you = getPokemon((String)pokemonSelect.getSelectedItem());
            final Pokemon opp = getPokemon((String)opponentPokemonSelect.getSelectedItem());

            assert you != null;
            int yourBase = you.baseAttack;
            assert opp != null;
            int oppBase = opp.baseDefense;

            JTextField defenderEVSource = defenseEV;

            final String yourNature = (String)natureSelect.getSelectedItem();
            final String oppNature = (String)opponentNatureSelect.getSelectedItem();
            double yourNatureMultiplier;
            double oppNatureMultiplier;

            final int yourLevel = Integer.parseInt(Objects.requireNonNull(yourLevelSelect.getText()));
            final int oppLevel = Integer.parseInt(Objects.requireNonNull(oppLevelSelect.getText()));

            int atkBoostCount = (int)Double.parseDouble((String)Objects.requireNonNull(attackBoost.getSelectedItem()));
            int defBoostCount = (int)Double.parseDouble((String)Objects.requireNonNull(defBoosts.getSelectedItem()));


            yourNatureMultiplier = switch(yourNature){
                case "+Attack" -> 1.1;
                case "-Attack" -> 0.9;
                default -> 1;
            };

            oppNatureMultiplier = switch(oppNature){
                case "+Defense" -> 1.1;
                case "-Defense" -> 0.9;
                default -> 1;
            };

            if(move.type.equals("SPECIAL")){
                yourBase = you.baseSpatk;
                oppBase = opp.baseSpdef;
                defenderEVSource = spdefEV;

                yourNatureMultiplier = switch(yourNature){
                    case "+Spatk" -> 1.1;
                    case "-Spatk" -> 0.9;
                    default -> 1;
                };

                oppNatureMultiplier = switch(oppNature){
                    case "+Spdef" -> 1.1;
                    case "-Spdef" -> 0.9;
                    default -> 1;
                };
                atkBoostCount = (int)Double.parseDouble((String) Objects.requireNonNull(specialAttackBoost.getSelectedItem()));
                defBoostCount = (int)Double.parseDouble((String) Objects.requireNonNull(spdefBoosts.getSelectedItem()));
            }

            int oppEV = Integer.parseInt(defenderEVSource.getText());

            final int oppDefenseStat = Miscellaneous.Calculators.statCalculation(oppBase,31,oppEV,yourNatureMultiplier,oppLevel,defBoostCount);
            final int oppHP = Miscellaneous.Calculators.calcHP(oppEV,oppLevel,opp.baseHP);

            int EV = Miscellaneous.Calculators.findLeastAtkEVs(yourBase,oppNatureMultiplier,yourLevel,move,oppDefenseStat,atkBoostCount,oppHP);

            System.out.println("final min ev: "+EV);
        });
    }

    private static String[] arrayListToArray(ArrayList<String> arrayList){
        final int size = arrayList.size();
        final String[] array = new String[size];

        for(int i =0;i<size;i++){array[i] = arrayList.get(i);}
        return array;
    }
}
