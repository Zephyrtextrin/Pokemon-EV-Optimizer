import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class AttackOptimizerUI extends Database {
    //make the opponent a different panel and u a different panel cause im TIRED of resizing shit we are a layout manager household now
    public static void initUI() throws IOException{
        final String[] WEATHER = Constants.WEATHER_LIST;
        final String[] natDex = arrayListToArray(getNatDexAsArrayList());
        final String[] moveList = arrayListToArray(getMoveListAsArrayList());
        final String[] itemList = arrayListToArray(Database.getItemList());
        String yourName = natDex[0];
        String oppName = natDex[0];

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
        final File yourIMGFile = getSpriteFile(yourName); //the opponentsPokemonIMGFile itself. (just the data the image isnt actually read)
        System.out.println(yourIMGFile);
        final ImageIcon yourIcon = new ImageIcon(ImageIO.read(yourIMGFile)); //ImageIO actually reads the data from the image and then that data is set to an icon
        //the label that actually displays it
        final JLabel youDisplayLabel = new JLabel(yourIcon);
        attackerPanel.add(youDisplayLabel);

        //select what pokemonis attacking
        final JComboBox<String> pokemonSelect = new JComboBox<>(natDex);
        attackerPanel.add(pokemonSelect);

        pokemonSelect.addActionListener(_ ->{
            final String selected = Objects.requireNonNull(pokemonSelect.getSelectedItem()).toString();
            final Pokemon selectedMon = getPokemon(selected);
            try {youDisplayLabel.setIcon(new ImageIcon(ImageIO.read(getSpriteFile(String.valueOf(selectedMon.dexNumber)))));
            }catch(IOException e){
                try{youDisplayLabel.setIcon(new ImageIcon(ImageIO.read(getSpriteFile("0"))));}catch(IOException ex){throw new RuntimeException(ex);}
                throw new RuntimeException(e);
            }
        });

        final JTextField yourLevelSelect = new JTextField("100");
        attackerPanel.add(yourLevelSelect);

        final JComboBox<String> natureSelect = new JComboBox<>(Constants.NATURE);
        attackerPanel.add(natureSelect);

        final JComboBox<String> itemSelect = new JComboBox<>(itemList);
        attackerPanel.add(itemSelect);

        final JComboBox<String> moveSelect = new JComboBox<>(moveList);
        attackerPanel.add(moveSelect);

        /*final JComboBox<String> ability = new JComboBox<>(moveList);
        attackerPanel.add(moveSelect);*/

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
        final File opponentIMGFile = getSpriteFile(oppName); //the opponentsPokemonIMGFile itself. (just the data the image isnt actually read)
        final ImageIcon opponentIcon = new ImageIcon(ImageIO.read(opponentIMGFile)); //ImageIO actually reads the data from the image and then that data is set to an icon
        //the label that actually displays it
        final JLabel opponentDisplayLabel = new JLabel(opponentIcon);
        defenderPanel.add(opponentDisplayLabel);

        //select what pokemonis attacking
        final JComboBox<String> opponentPokemonSelect = new JComboBox<>(natDex);
        defenderPanel.add(opponentPokemonSelect);

        opponentPokemonSelect.addActionListener(_ ->{
            final String selected = Objects.requireNonNull(opponentPokemonSelect.getSelectedItem()).toString();
            final Pokemon selectedMon = getPokemon(selected);

            try{opponentDisplayLabel.setIcon(new ImageIcon(ImageIO.read(getSpriteFile(String.valueOf(selectedMon.dexNumber)))));
            }catch(IOException e){try{opponentDisplayLabel.setIcon(new ImageIcon(ImageIO.read(getSpriteFile("0"))));}catch(IOException ex){throw new RuntimeException(ex);}}
        });

        final JTextField oppLevelSelect = new JTextField("100");
        defenderPanel.add(oppLevelSelect);

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

            final int yourLevel = errorHandler(yourLevelSelect, 1,100);
            final int oppLevel = errorHandler(oppLevelSelect, 1, 100);

            JComboBox<String> boostSourceYou = attackBoost;
            JComboBox<String> boostSourceOpp = defBoosts;

            int atkBoostCount;
            int defBoostCount;

            final String selectedWeather = Objects.requireNonNull(weather.getSelectedItem()).toString();

            final String item = Objects.requireNonNull(itemSelect.getSelectedItem()).toString();

            assert yourNature != null;
            yourNatureMultiplier = switch(yourNature){
                case "+Attack" -> 1.1;
                case "-Attack" -> 0.9;
                default -> 1;
            };

            assert oppNature != null;
            oppNatureMultiplier = switch(oppNature){
                case "+Defense" -> 1.1;
                case "-Defense" -> 0.9;
                default -> 1;
            };

            if(move.moveCategory==Constants.MOVE_CATS.Special){
                yourBase = you.baseSpatk;
                oppBase = opp.baseSpdef;
                defenderEVSource = spdefEV;
                boostSourceYou = specialAttackBoost;
                boostSourceOpp = spdefBoosts;

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


            }

            String youBoost = Objects.requireNonNull(boostSourceYou.getSelectedItem()).toString();
            String oppBoost = Objects.requireNonNull(boostSourceOpp.getSelectedItem()).toString();

            atkBoostCount = (int)Double.parseDouble(String.valueOf(youBoost.charAt(1)));
            defBoostCount = (int)Double.parseDouble(String.valueOf(oppBoost.charAt(1)));


            int oppHP_EV = errorHandler(HP_EV, 0,252);
            final int oppDefense = errorHandler(defenderEVSource, 0,252);

            final int oppDefenseStat = Calculators.statCalculation(oppBase,31, oppDefense,yourNatureMultiplier,oppLevel,defBoostCount);
            final int oppHP = Calculators.calcHP(oppHP_EV,oppLevel,opp.baseHP);

            int min_OHKO_EV = Calculators.findLeastAtkEVs(yourBase,oppNatureMultiplier,yourLevel,move,oppDefenseStat,atkBoostCount,oppHP,you,opp, item, selectedWeather, 0.85);

            if(min_OHKO_EV!=-1){
                System.out.printf("Minimum EVs needed for %s %s Nature %s %s to OHKO %s %s Nature %d HP EV %d (Sp)Defense EV %s with %s: %d\n",youBoost,yourNature,item,you.name,oppBoost,oppNature,oppHP_EV,oppDefense,opp.name,move.name, min_OHKO_EV);
                System.out.println("This calculations assumes the lowest possible roll to prevent matchups from being roll-dependant. (15% damage reduction)");
            }
            else{
                int EV_HighRoll = Calculators.findLeastAtkEVs(yourBase,oppNatureMultiplier,yourLevel,move,oppDefenseStat,atkBoostCount,oppHP,you,opp, item, selectedWeather, 1);
                System.out.println("OHKO not possible with lowest possible roll, regardless of your EV spread. [Attempting to find OHKO range with highest possible roll...]\n");
                if(EV_HighRoll!=-1){
                    System.out.printf("Minimum EVs needed for %s %s Nature %s %s to OHKO %s %s Nature %d HP EV %d (Sp)Defense EV %s with %s: %d\n",youBoost,yourNature,item,you.name,oppBoost,oppNature,oppHP_EV,oppDefense,opp.name,move.name, EV_HighRoll);
                    System.out.println("THIS ABOVE CALCULATION ASSUMES YOU HAVE THE HIGHEST POSSIBLE DAMAGE ROLL, YOU WILL NOT ALWAYS OHKO SO BE AWARE MATCHUPS ARE RNG-DEPENDANT\n");
                }else{System.out.println("OKHO not possible regardless of EV spread or damage roll.\n");}
            }
        });
    }

    private static String[] arrayListToArray(ArrayList<String> arrayList){
        final int size = arrayList.size();
        final String[] array = new String[size];

        for(int i =0;i<size;i++){array[i] = arrayList.get(i);}
        return array;
    }

    private static int errorHandler(JTextField field, int min, int max){
        int temp;
        try{temp = Integer.parseInt(field.getText());}catch(Exception e){return 1;}
        if(temp>max||temp<min){return 1;}

        return temp;
    }

    private static File getSpriteFile(String name){
        int dex = getPokemon(name).dexNumber;
        File file = new File("src/assets/"+dex+".png");
        if(!file.exists()){
            ErrorPrinter.setDetails(file.toString(), false);
            ErrorPrinter.handler("ABN_");
        }
        return file;
    }
}
