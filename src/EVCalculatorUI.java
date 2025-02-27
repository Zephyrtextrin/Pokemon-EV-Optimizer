import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class EVCalculatorUI extends Database {
    static final HashMap<String, Component> ComponentMap = new HashMap<>();

    public static void initUI() throws IOException {

        final String[] WEATHER = Constants.WEATHER_LIST;

        final int SIZE = 1000;
        final JFrame frame = new JFrame();
        frame.setSize(SIZE, SIZE);

        frame.setLayout(new GridLayout(1, 1)); //not good fix later
        frame.setVisible(true);


        //left side (the pokemon who is using the move

        final JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
        frame.add(leftPanel);
        createStatsPanel(leftPanel, "Left-Side");

        leftPanel.getName();

        //filler

        //select boosts for left side
        final JPanel leftBoostPanel = new JPanel();
        leftBoostPanel.setLayout(new BoxLayout(leftBoostPanel, BoxLayout.PAGE_AXIS));
        frame.add(leftBoostPanel);
        makeBoostPanel(leftBoostPanel, "Left-Side ");


        //other features like field conditions
        final JPanel other = new JPanel();
        other.setLayout(new BoxLayout(other, BoxLayout.PAGE_AXIS));
        frame.add(other);

        //filler
        other.add(Box.createVerticalGlue());

        //text to denote what the panel does
        other.add(new JLabel("Other"));

        //text to denote what the panel does
        other.add(new JLabel("Weather"));

        //select what weather
        final JComboBox<String> weather = new JComboBox<>(WEATHER);
        other.add(weather);

        final JCheckBox spread = new JCheckBox("Is this a spread move?");
        other.add(spread);

        //text to denote what the panel does
        other.add(new JLabel("Find Minimum EVs to:"));

        //select what function
        final JComboBox<String> toDo = new JComboBox<>(Constants.CAPABILITY_LIST);
        other.add(toDo);

        //text to denote what the panel does
        other.add(new JLabel("Select which side to find EVS for"));

        //select what function
        final JComboBox<String> target = new JComboBox<>(new String[]{"Pokemon 1 (Left Side)", "Pokemon 2 (Right Side"});
        other.add(target);

        final JButton run = new JButton("Run");
        other.add(run);

        //select boosts for right side
        final JPanel rightBoostPanel = new JPanel();
        rightBoostPanel.setLayout(new BoxLayout(rightBoostPanel, BoxLayout.PAGE_AXIS));
        frame.add(rightBoostPanel);
        makeBoostPanel(rightBoostPanel, "Right-Side ");
        other.add(Box.createVerticalGlue());


        //right side

        final JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
        frame.add(rightPanel);
        createStatsPanel(rightPanel, "Right-Side");

        //gathers all info on both sides and performs preliminary calcs needed for the real damage calc
        //i would make this into a method but tbh the sheer amount of fucking params it would need is SO INSANELY FUCKING COMICAL
        run.addActionListener(_ ->{
            CurrentPokemon[] allData = initCurrentPokemon();
            CurrentPokemon subjectMon = allData[1]; //who is attacking/defending
            CurrentPokemon opponentMon = allData[0];
            final String process = Objects.requireNonNull(toDo.getSelectedItem()).toString();

            if(Objects.requireNonNull(target.getSelectedItem()).toString().equals("Pokemon 1 (Left Side)")){
                subjectMon = allData[0];
                opponentMon = allData[1];
            }

            if(!Constants.DEBUG_DISABLE_OUTPUT){output(process, subjectMon, opponentMon, Objects.requireNonNull(weather.getSelectedItem()).toString(),spread.isSelected());}
        });
    }

    private static CurrentPokemon[] initCurrentPokemon(){
        final int[] leftEVs = new int[]{Integer.parseInt(HelperMethods.getComponentValue("Left-Side HP EV", true)), Integer.parseInt(HelperMethods.getComponentValue("Left-Side Atk EV", true)), Integer.parseInt(HelperMethods.getComponentValue("Left-Side Def EV", true)), Integer.parseInt(HelperMethods.getComponentValue("Left-Side SpAtk EV", true)), Integer.parseInt(HelperMethods.getComponentValue("Left-Side SpDef EV", true)), Integer.parseInt(HelperMethods.getComponentValue("Left-Side Speed EV", true))};
        final int[] leftBoosts = new int[]{Integer.parseInt(HelperMethods.getComponentValue("Left-Side Atk Boost", true)), Integer.parseInt(HelperMethods.getComponentValue("Left-Side Def Boost", true)), Integer.parseInt(HelperMethods.getComponentValue("Left-Side SpAtk Boost", true)), Integer.parseInt(HelperMethods.getComponentValue("Left-Side SpDef Boost", true)), Integer.parseInt(HelperMethods.getComponentValue("Left-Side Speed Boost", true))};

        final int[] rightEVs = new int[]{Integer.parseInt(HelperMethods.getComponentValue("Right-Side HP EV", true)), Integer.parseInt(HelperMethods.getComponentValue("Right-Side Atk EV", true)), Integer.parseInt(HelperMethods.getComponentValue("Right-Side Def EV", true)), Integer.parseInt(HelperMethods.getComponentValue("Right-Side SpAtk EV", true)), Integer.parseInt(HelperMethods.getComponentValue("Right-Side SpDef EV", true)), Integer.parseInt(HelperMethods.getComponentValue("Right-Side Speed EV", true))};
        final int[] rightBoosts = new int[]{Integer.parseInt(HelperMethods.getComponentValue("Right-Side Atk Boost", true)), Integer.parseInt(HelperMethods.getComponentValue("Right-Side Def Boost", true)), Integer.parseInt(HelperMethods.getComponentValue("Right-Side SpAtk Boost", true)), Integer.parseInt(HelperMethods.getComponentValue("Right-Side SpDef Boost", true)), Integer.parseInt(HelperMethods.getComponentValue("Right-Side Speed Boost", true))};

        if(Constants.DEBUG_UI_MODE){
            System.out.println("\nLEFT SIDE EVS: "+ Arrays.toString(leftEVs));
            System.out.println("LEFT SIDE BOOSTS: "+ Arrays.toString(leftBoosts));

            System.out.println("RIGHT SIDE EVS: "+ Arrays.toString(rightEVs));
            System.out.println("RIGHT SIDE BOOSTS: "+ Arrays.toString(rightBoosts));

            System.out.println("\n");
        }

        CurrentPokemon leftSide = new CurrentPokemon(
                HelperMethods.getComponentValue("Left-Side Pokemon", true),
                Integer.parseInt(HelperMethods.getComponentValue("Left-Side Level", true)),
                HelperMethods.getComponentValue("Left-Side Item", true),
                getMove(HelperMethods.getComponentValue("Left-Side Move", true)),
                getNature(HelperMethods.getComponentValue("Left-Side Nature", true)),
                leftEVs, leftBoosts

        );

        CurrentPokemon rightSide = new CurrentPokemon(
                HelperMethods.getComponentValue("Right-Side Pokemon", true),
                Integer.parseInt(HelperMethods.getComponentValue("Right-Side Level", true)),
                HelperMethods.getComponentValue("Right-Side Item", true),
                getMove(HelperMethods.getComponentValue("Right-Side Move", true)),
                getNature(HelperMethods.getComponentValue("Right-Side Nature", true)),
                rightEVs, rightBoosts

        );
        return new CurrentPokemon[]{leftSide,rightSide};
    }

    private static void output(String process, CurrentPokemon subjectMon, CurrentPokemon opponentMon, String weather, boolean spread) {
        Move moveUsed = subjectMon.move;
        int[] EVrolls = {-1,-1,-1}; //0 is lowest roll, 1 is median roll, 2 is highest roll

        if (process.equals("Tank")){
            moveUsed = opponentMon.move;
            EVrolls = Calculators.findLeastHPEVs(opponentMon,subjectMon,moveUsed,weather,spread);
        }else if(process.equals("OHKO")){
            EVrolls = Calculators.findLeastAtkEVs(subjectMon,opponentMon,moveUsed,weather,spread);

        }else{ //for outspeeding
            final int[] EV = {Calculators.findLeastSpeedEVs(subjectMon, opponentMon.speedStat, subjectMon.speedBoost)};
            outputClean(process,subjectMon,opponentMon,EV,"");
        }

        if (!process.equals("Outspeed")){outputClean(process,subjectMon,opponentMon,EVrolls,moveUsed.name);}
    }

    private static void outputClean(String process, CurrentPokemon subjectMon, CurrentPokemon opponentMon, int[] EVrolls, String moveUsed){
        if(!moveUsed.isEmpty()){moveUsed="with "+moveUsed;} //this means that a tank/ohko output will display "ev to whatever [pokemonname] with [movename] but an outspeed output will just say "evs to outspeed [pokemonname] since u have to input a move name no matter what but if ur looking to outspeed ur not using a move
        int index = 0;
        for(int EV:EVrolls){
            String message = switch (index) {
                case 0 -> "-[ASSUMING LOWEST POSSIBLE DAMAGE ROLL]-";
                case 1 -> "-[ASSUMING AVERAGE DAMAGE ROLL]-";
                default -> "-[ASSUMING HIGHEST POSSIBLE DAMAGE ROLL]-";
            };
            if(!Objects.equals(process, "Outspeed")){System.out.println(message);}

            if(EV != -1){
                if(!process.equals("Outspeed")) {System.out.printf("Minimum EVs needed for %s to %s %s %s: %d\n\n", subjectMon.base.name, process.toLowerCase(), opponentMon.base.name, moveUsed, EV);
                }else{System.out.printf("Minimum EVs needed for %d boosts %s to outspeed %s nature %d EV %s: %d",subjectMon.speedBoost,subjectMon.base.name,opponentMon.nature.name,opponentMon.speedEV,opponentMon.base.name,EV);}

            }else{System.out.println("NOT POSSIBLE TO " + process.toUpperCase() + "\n");}


            if(index==2){
                if(!process.equals("Outspeed")&&EVrolls[2]!=-1){System.out.println("Please remember that only OHKOing with the highest possible damage roll means there's about a 6% chance you actually ohko, and with an average (median) roll is only 50%- Please do keep in mind this will make matchups RNG-dependant.\nObviously, OHKOing with the lowest possible roll will not have to account for RNG.\n");}

                System.out.println("-----------[END]-----------");
            } //this is appended to the end of highrolls because the highroll typically goes last

            index++;
        }
    }

    private static void createStatsPanel(JPanel panel, String title) throws IOException {
        String header = "Pokemon 2";
        if(title.equals("Left-Side")){header = "Pokemon 1";}
        final String[] natDex = HelperMethods.arrayListToArray(getNatDexAsArrayList());
        final String[] moveList = HelperMethods.arrayListToArray(getMoveListAsArrayList());
        final String[] itemList = HelperMethods.arrayListToArray(Database.getItemList());
        final String[] natureList = HelperMethods.arrayListToArray(getNatureListAsArrayList());
        String pokemonName = natDex[0];

        //set bounds for space filler sizes
        /*
        final Dimension minFillerSize = new Dimension(frame.getWidth()/128, frame.getHeight()/128);
        final Dimension prefFilerSize = new Dimension(frame.getWidth()/64, frame.getHeight()/64);
        final Dimension maxFillerSize = new Dimension(frame.getWidth()/32, frame.getHeight()/32);
        */

        final Dimension maxSize = new Dimension(panel.getWidth(), panel.getHeight()/16);

        //text to denote who is attacking
        panel.add(new JLabel(header));

        //image display for your pokemon
        final JLabel pokemonDisplayLabel = new JLabel(new ImageIcon(ImageIO.read(HelperMethods.getSpriteFile(pokemonName))));
        panel.add(pokemonDisplayLabel);

        //select what pokemonis attacking
        final JComboBox<String> pokemonSelect = new JComboBox<>(natDex);
        panel.add(pokemonSelect);
        ComponentMap.put(title+" Pokemon",pokemonSelect);

        pokemonSelect.addActionListener(_ -> {
            final String selected = Objects.requireNonNull(pokemonSelect.getSelectedItem()).toString();
            ImageIcon icon;
            try {
                icon = new ImageIcon(ImageIO.read(HelperMethods.getSpriteFile(selected)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            pokemonDisplayLabel.setIcon(icon);
        });

        final JTextField levelSelect = new JTextField("100");
        levelSelect.setMaximumSize(maxSize);
        panel.add(levelSelect);
        ComponentMap.put(title+" Level",levelSelect);

        final JTextField HP_EV = new JTextField("HP EV");
        panel.add(HP_EV);
        ComponentMap.put(title+" HP EV",HP_EV);

        final JTextField atkEV = new JTextField("Attack EV");
        panel.add(atkEV);
        ComponentMap.put(title+" Atk EV",atkEV);

        final JTextField defEV = new JTextField("Defense EV");
        panel.add(defEV);
        ComponentMap.put(title+" Def EV",defEV);

        final JTextField spatkEV = new JTextField("Special Attack EV");
        panel.add(spatkEV);
        ComponentMap.put(title+" SpAtk EV",spatkEV);

        final JTextField spdefEV = new JTextField("Special Defense EV");
        panel.add(spdefEV);
        ComponentMap.put(title+" SpDef EV",spdefEV);

        final JTextField speedEV = new JTextField("Speed EV");
        panel.add(speedEV);
        ComponentMap.put(title+" Speed EV",speedEV);

        final JComboBox<String> natureSelect = new JComboBox<>(natureList);
        panel.add(natureSelect);
        ComponentMap.put(title+" Nature",natureSelect);

        final JComboBox<String> itemSelect = new JComboBox<>(itemList);
        panel.add(itemSelect);
        ComponentMap.put(title+" Item",itemSelect);

        final JComboBox<String> moveSelect = new JComboBox<>(moveList);
        panel.add(moveSelect);
        ComponentMap.put(title+" Move",moveSelect);

        /*final JComboBox<String> ability = new JComboBox<>(moveList);
        panel.add(moveSelect);
        ComponentMap.put(title+" Ability",ability);
        */

        panel.revalidate();
    }

    private static void makeBoostPanel(JPanel panel, String panelTitleAppend) {

        panel.add(Box.createVerticalGlue()); //creates filler

        //text to denote what the panel does
        panel.add(new JLabel("Attack Boosts"));
        //select what attackBoost
        final JComboBox<String> attackBoost = new JComboBox<>(Constants.BOOSTS);
        panel.add(attackBoost);
        ComponentMap.put(panelTitleAppend+"Atk Boost",attackBoost);

        //text to denote what the panel does
        panel.add(new JLabel("Defense Boosts"));
        //select what defBoost
        final JComboBox<String> defenseBoost = new JComboBox<>(Constants.BOOSTS);
        panel.add(defenseBoost);
        ComponentMap.put(panelTitleAppend+"Def Boost",defenseBoost);

        //text to denote what the panel does
        panel.add(new JLabel("Special Attack Boosts"));
        //select what spattackBoost
        final JComboBox<String> specialAttackBoost = new JComboBox<>(Constants.BOOSTS);
        panel.add(specialAttackBoost);
        ComponentMap.put(panelTitleAppend+"SpAtk Boost",specialAttackBoost);

        //text to denote what the panel does
        panel.add(new JLabel("Special Defense Boosts"));
        //select what spdefBoost
        final JComboBox<String> specialDefenseBoost = new JComboBox<>(Constants.BOOSTS);
        panel.add(specialDefenseBoost);
        ComponentMap.put(panelTitleAppend+"SpDef Boost",specialDefenseBoost);

        //text to denote what the panel does
        panel.add(new JLabel("Speed Boosts"));
        //select what speedBoost
        final JComboBox<String> speedBoost = new JComboBox<>(Constants.BOOSTS);
        panel.add(speedBoost);
        ComponentMap.put(panelTitleAppend+"Speed Boost",speedBoost);

    }

    //holds data for each pokemon on the UI
    static public class CurrentPokemon{
        public Pokemon base;
        public int level;
        public String item;
        public Move move;
        public Nature nature;
        public int HPStat;
        public int atkStat;
        public int defStat;
        public int spAtkStat;
        public int spDefStat;
        public int speedStat;
        public int atkBoost;
        public int defBoost;
        public int spAtkBoost;
        public int spDefBoost;
        public int speedBoost;
        public int HP_EV;
        public int atkEV;
        public int defEV;
        public int spAtkEV;
        public int spDefEV;
        public int speedEV;


        public CurrentPokemon(String name, int level, String item, Move move, Nature nature, int[] EVs, int[] boosts){
            base = getPokemon(name);
            this.nature = nature;
            this.level = level;
            this.item = item;
            this.move = move;
            this.atkBoost = boosts[0];
            this.defBoost = boosts[1];
            this.spAtkBoost = boosts[2];
            this.spDefBoost = boosts[3];
            this.speedBoost = boosts[4];
            this.HP_EV = EVs[0];
            this.atkEV = EVs[1];
            this.defEV = EVs[2];
            this.spAtkEV = EVs[3];
            this.spDefEV = EVs[4];
            this.speedEV = EVs[5];


            HPStat = Calculators.calcHP(HP_EV,level,base.baseHP);
            atkStat = Calculators.statCalculation(base.baseAttack,31,atkEV,nature.attack,level,atkBoost);
            defStat = Calculators.statCalculation(base.baseDefense,31,defEV,nature.defense,level,defBoost);
            spAtkStat = Calculators.statCalculation(base.baseSpatk,31,spAtkEV,nature.spatk,level,spAtkBoost);
            spDefStat = Calculators.statCalculation(base.baseSpdef,31,spDefEV,nature.spdef,level,spDefBoost);
            speedStat = Calculators.statCalculation(base.baseSpeed,31,speedEV,nature.speed,level,speedBoost);
        }
    }
}
