import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class AttackOptimizerUI extends Database {
    //make the opponent a different panel and u a different panel cause im TIRED of resizing shit we are a layout manager household now
    public static void initUI() throws IOException {
        final String[] WEATHER = Constants.WEATHER_LIST;

        final int SIZE = 1200;
        final JFrame frame = new JFrame();
        frame.setSize(SIZE,SIZE);

        frame.setLayout(new GridLayout(1,3));
        frame.setVisible(true);

        //gallade is placeholder rn i dont have the files for everyone else


        //left side (the pokemon who is using the move

        final JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
        frame.add(leftPanel);

        createStatsPanel(leftPanel, "Pokemon 1");


        //select boosts for left side
        final JPanel leftBoostPanel = new JPanel();
        leftBoostPanel.setLayout(new BoxLayout(leftBoostPanel, BoxLayout.PAGE_AXIS));
        frame.add(leftBoostPanel);

        leftBoostPanel.add(Box.createVerticalGlue()); //creates filler
        makeBoostPanel(leftBoostPanel);



        //other features like field conditions
        final JPanel other = new JPanel();
        other.setLayout(new BoxLayout(other, BoxLayout.PAGE_AXIS));
        frame.add(other);

        //text to denote what the panel does
        final JLabel otherTitleLabel = new JLabel("Other");
        other.add(otherTitleLabel);

        //text to denote what the panel does
        final JLabel weatherTitleLabel = new JLabel("Weather");
        other.add(weatherTitleLabel);

        //select what weather
        final JComboBox<String> weather = new JComboBox<>(WEATHER);
        other.add(weather);

        //text to denote what the panel does
        final JLabel functionTitleLabel = new JLabel("Find Minimum EVs to:");
        other.add(functionTitleLabel);

        //select what function
        final JComboBox<String> toDo = new JComboBox<>(Constants.CAPABILITY_LIST);
        other.add(toDo);

        //text to denote what the panel does
        final JLabel targetTitleLabel = new JLabel("Select which side is attacking/defending");
        other.add(targetTitleLabel);

        //select what function
        final JComboBox<String> target = new JComboBox<>(new String[]{"Pokemon 1 (Left Side","Pokemon 2 (Right Side"});
        other.add(target);

        final JButton run = new JButton("Run");
        other.add(run);


        //select boosts for right side
        final JPanel rightBoostPanel = new JPanel();
        rightBoostPanel.setLayout(new BoxLayout(rightBoostPanel, BoxLayout.PAGE_AXIS));
        frame.add(rightBoostPanel);

        rightBoostPanel.add(Box.createVerticalGlue()); //creates filler
        makeBoostPanel(rightBoostPanel);



        //right side

        final JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
        frame.add(rightPanel);

        createStatsPanel(rightPanel, "Pokemon 2");

        //gathers all info on both sides and performs preliminary calcs needed for the real damage calc
        //i would make this into a method but tbh the sheer amount of fucking params it would need is SO INSANELY FUCKING COMICAL
        /*run.addActionListener(_ ->{
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
                try{yourBase = you.baseSpatk;}catch(Exception e){
                    ErrorPrinter.setDetails("[REQUESTED POKEMON]: "+you.name+"\n[REQUESTED STAT]: SPECIAL ATTACK", false);
                    ErrorPrinter.handler(ErrorPrinter.ERROR_CODE.ABN_DB_STAT_DNE, e);
                    return;
                }
                try{oppBase = opp.baseSpdef;
                }catch (Exception e){
                    ErrorPrinter.setDetails("[REQUESTED POKEMON]: "+opp.name+"\n[REQUESTED STAT]: SPECIAL DEFENSE", false);
                    ErrorPrinter.handler(ErrorPrinter.ERROR_CODE.ABN_DB_STAT_DNE, e);
                    return;
                }

                defenderEVSource = spdefEV;
                boostSourceYou = specialAttackBoost;
                boostSourceOpp = spdefBoosts;

                yourNatureMultiplier = switch(yourNature){
                    case "+SpAtk" -> 1.1;
                    case "-SpAtk" -> 0.9;
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

            int min_OHKO_EV = Calculators.findLeastAtkEVs(yourBase,yourNatureMultiplier,yourLevel,move,oppDefenseStat,atkBoostCount,oppHP,you,opp, item, selectedWeather, 0.85);

            if(Constants.DEBUG_MODE){System.out.println("your nature: "+yourNatureMultiplier+"\nopp nature: "+oppNatureMultiplier);}

            if(min_OHKO_EV!=-1){
                System.out.printf("\nMinimum EVs needed for %s %s Nature %s %s to OHKO %s %s Nature %d HP EV %d (Sp)Defense EV %s with %s: %d\n",youBoost,yourNature,item,you.name,oppBoost,oppNature,oppHP_EV,oppDefense,opp.name,move.name, min_OHKO_EV);
                System.out.println("This calculations assumes the lowest possible roll to prevent matchups from being roll-dependant. (15% damage reduction)\n");
            }
            else{
                int EV_HighRoll = Calculators.findLeastAtkEVs(yourBase,yourNatureMultiplier,yourLevel,move,oppDefenseStat,atkBoostCount,oppHP,you,opp, item, selectedWeather, 1);
                System.out.println("OHKO not possible with lowest possible roll, regardless of your EV spread. [Attempting to find OHKO range with highest possible roll...]\n");
                if(EV_HighRoll!=-1){
                    System.out.printf("Minimum EVs needed for %s %s Nature %s %s to OHKO %s %s Nature %d HP EV %d (Sp)Defense EV %s with %s: %d\n",youBoost,yourNature,item,you.name,oppBoost,oppNature,oppHP_EV,oppDefense,opp.name,move.name, EV_HighRoll);
                    System.out.println("THIS ABOVE CALCULATION ASSUMES YOU HAVE THE HIGHEST POSSIBLE DAMAGE ROLL, YOU WILL NOT ALWAYS OHKO SO BE AWARE MATCHUPS ARE RNG-DEPENDANT\n");
                }else{System.out.println("OKHO not possible regardless of EV spread or damage roll.\n");}
            }
        });*/
    }

    private static void createStatsPanel(JPanel panel, String title) throws IOException{
        final String[] natDex = arrayListToArray(getNatDexAsArrayList());
        final String[] moveList = arrayListToArray(getMoveListAsArrayList());
        final String[] itemList = arrayListToArray(Database.getItemList());
        String pokemonName = natDex[0];

        //text to denote who is attacking
        final JLabel attackerTitleLabel = new JLabel(title);
        panel.add(attackerTitleLabel);

        //image display for your pokemon
        final File yourIMGFile = getSpriteFile(pokemonName); //the opponentsPokemonIMGFile itself. (just the data the image isnt actually read)
        final ImageIcon yourIcon = new ImageIcon(ImageIO.read(yourIMGFile)); //ImageIO actually reads the data from the image and then that data is set to an icon
        //the label that actually displays it
        final JLabel youDisplayLabel = new JLabel(yourIcon);
        panel.add(youDisplayLabel);

        //select what pokemonis attacking
        final JComboBox<String> pokemonSelect = new JComboBox<>(natDex);
        panel.add(pokemonSelect);

        pokemonSelect.addActionListener(_ ->{
            final String selected = Objects.requireNonNull(pokemonSelect.getSelectedItem()).toString();
            ImageIcon icon;
            try{icon = new ImageIcon(ImageIO.read(getSpriteFile(selected)));
            }catch(IOException e){throw new RuntimeException(e);}

            youDisplayLabel.setIcon(icon);
        });

        final JTextField yourLevelSelect = new JTextField("100");
        panel.add(yourLevelSelect);

        final JTextField leftHP_EV = new JTextField("HP EV");
        panel.add(leftHP_EV);

        final JTextField leftAtkEV = new JTextField("Attack EV");
        panel.add(leftAtkEV);

        final JTextField leftDefEV = new JTextField("Defense EV");
        panel.add(leftDefEV);

        final JTextField leftSpatkEV = new JTextField("Special Attack EV");
        panel.add(leftSpatkEV);

        final JTextField leftSpdefEV = new JTextField("Special Defense EV");
        panel.add(leftSpdefEV);

        final JTextField leftSpeedEV = new JTextField("Speed EV");
        panel.add(leftSpeedEV);

        final JComboBox<String> natureSelect = new JComboBox<>(Constants.NATURE);
        panel.add(natureSelect);

        final JComboBox<String> itemSelect = new JComboBox<>(itemList);
        panel.add(itemSelect);

        final JComboBox<String> moveSelect = new JComboBox<>(moveList);
        panel.add(moveSelect);

        /*final JComboBox<String> ability = new JComboBox<>(moveList);
        panel.add(moveSelect);*/



        panel.revalidate();
    }

    private static void makeBoostPanel(JPanel panel){

        //text to denote what the panel does
        panel.add(new JLabel("Attack Boosts"));
        //select what attackBoost
        final JComboBox<String> attackBoost = new JComboBox<>(Constants.BOOSTS);
        panel.add(attackBoost);

        //text to denote what the panel does
        panel.add(new JLabel("Defense Boosts"));
        //select what defBoost
        final JComboBox<String> defensekBoost = new JComboBox<>(Constants.BOOSTS);
        panel.add(defensekBoost);

        //text to denote what the panel does
        panel.add(new JLabel("Special Attack Boosts"));
        //select what spattackBoost
        final JComboBox<String> specialAttackBoost = new JComboBox<>(Constants.BOOSTS);
        panel.add(specialAttackBoost);

        //text to denote what the panel does
        panel.add(new JLabel("Special Defense Boosts"));
        //select what spdefBoost
        final JComboBox<String> specialDefenseBoost = new JComboBox<>(Constants.BOOSTS);
        panel.add(specialDefenseBoost);

        //text to denote what the panel does
        panel.add(new JLabel("Speed Boosts"));
        //select what speedBoost
        final JComboBox<String> speedBoost = new JComboBox<>(Constants.BOOSTS);
        panel.add(speedBoost);
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
        File file = new File("src/assets/0.png");
        if(!Objects.equals(name, "0")){
            int dex = Database.getPokemon(name).dexNumber;

            try {file = new File("src/assets/" + dex + ".png");
            }catch (Exception e){
                ErrorPrinter.setDetails("src/assets/" + dex + ".png", false);
                ErrorPrinter.handler(ErrorPrinter.ERROR_CODE.ABN_UI_MALFORMED_IMAGE_FILE, e);
            }
        }
        if(!file.exists()){
            if(Database.getPokemon(name).dexNumber>=906){
                System.out.println("Nothing is broken. This is entirely a visual glitch.\nAll Paldea Pokemon don't have sprites available for bulk download.\nAlternate forms such as Regionals and Megas have sprites but it needs me to manually change the file name and the dex number in the codebase so its all still a work in progress.\nstay tuned.");
            }else {
                ErrorPrinter.setDetails(file.toString(), false);
                ErrorPrinter.handler(ErrorPrinter.ERROR_CODE.ABN_UI_MALFORMED_IMAGE_FILE, null);
            }
            file=new File("src/assets/0.png");

        }
        return file;
    }
}
