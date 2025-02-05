import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AttackOptimizerUI extends Database {
    //make the opponent a different panel and u a different panel cause im TIRED of resizing shit we are a layout manager household now
    static final HashMap<String, Component> componentMap = new HashMap<>();

    public static void initUI() throws IOException {

        final String[] WEATHER = Constants.WEATHER_LIST;

        final int SIZE = 1200;
        final JFrame frame = new JFrame();
        frame.setSize(SIZE, SIZE);

        frame.setLayout(new GridLayout(1, 3));
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
        final JComboBox<String> target = new JComboBox<>(new String[]{"Pokemon 1 (Left Side)", "Pokemon 2 (Right Side"});
        other.add(target);

        final JButton run = new JButton("Run");
        other.add(run);


        //select boosts for right side
        final JPanel rightBoostPanel = new JPanel();
        rightBoostPanel.setLayout(new BoxLayout(rightBoostPanel, BoxLayout.PAGE_AXIS));
        frame.add(rightBoostPanel);
        makeBoostPanel(rightBoostPanel, "Right-Side ");


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


            if(Objects.requireNonNull(target.getSelectedItem()).toString().equals("Pokemon 1 (Left Side)")){
                subjectMon = allData[0];
                opponentMon = allData[1];
            }

            final Move moveUsed = subjectMon.move;

            int min_OHKO_EV = Calculators.findLeastAtkEVs(subjectMon,opponentMon,moveUsed,Objects.requireNonNull(weather.getSelectedItem()).toString(), 0.85);
            int max_OHKO_EV = Calculators.findLeastAtkEVs(subjectMon,opponentMon,moveUsed,Objects.requireNonNull(weather.getSelectedItem()).toString(), 1);

            System.out.println("-[ASSUMING LOWEST POSSIBLE DAMAGE ROLL]-");
            if(min_OHKO_EV!=-1){System.out.printf("Minimum EVs needed for %s Nature %s %s to OHKO %s Nature %d HP EV %d Defense EV %d SpDef EV %s with %s: %d\n",subjectMon.nature.name,subjectMon.item,subjectMon.base.name,opponentMon.nature.name,opponentMon.HP_EV,opponentMon.defEV,opponentMon.spDefEV,opponentMon.base.name,moveUsed.name, min_OHKO_EV);
            }else{System.out.println("NOT POSSIBLE TO OHKO\n");}

            System.out.println("-[ASSUMING HIGHEST POSSIBLE DAMAGE ROLL]-");
            if(max_OHKO_EV!=-1){System.out.printf("Minimum EVs needed for %s Nature %s %s to OHKO %s Nature %d HP EV %d Defense EV %d SpDef EV %s with %s: %d\n",subjectMon.nature.name,subjectMon.item,subjectMon.base.name,opponentMon.nature.name,opponentMon.HP_EV,opponentMon.defEV,opponentMon.spDefEV,opponentMon.base.name,moveUsed.name, max_OHKO_EV);
            }else{System.out.println("NOT POSSIBLE TO OHKO\n");}
        });
    }

    private static CurrentPokemon[] initCurrentPokemon(){
        final int[] leftEVs = new int[]{Integer.parseInt(getComponentValue("Left-Side HP EV")), Integer.parseInt(getComponentValue("Left-Side Atk EV")), Integer.parseInt(getComponentValue("Left-Side Def EV")), Integer.parseInt(getComponentValue("Left-Side SpAtk EV")), Integer.parseInt(getComponentValue("Left-Side SpDef EV")), Integer.parseInt(getComponentValue("Left-Side Speed EV"))};
        final int[] leftBoosts = new int[]{Integer.parseInt(getComponentValue("Left-Side Atk Boost")), Integer.parseInt(getComponentValue("Left-Side Def Boost")), Integer.parseInt(getComponentValue("Left-Side SpAtk Boost")), Integer.parseInt(getComponentValue("Left-Side SpDef Boost")), Integer.parseInt(getComponentValue("Left-Side Speed Boost"))};

        final int[] rightEVs = new int[]{Integer.parseInt(getComponentValue("Right-Side HP EV")), Integer.parseInt(getComponentValue("Right-Side Atk EV")), Integer.parseInt(getComponentValue("Right-Side Def EV")), Integer.parseInt(getComponentValue("Right-Side SpAtk EV")), Integer.parseInt(getComponentValue("Right-Side SpDef EV")), Integer.parseInt(getComponentValue("Right-Side Speed EV"))};
        final int[] rightBoosts = new int[]{Integer.parseInt(getComponentValue("Right-Side Atk Boost")), Integer.parseInt(getComponentValue("Right-Side Def Boost")), Integer.parseInt(getComponentValue("Right-Side SpAtk Boost")), Integer.parseInt(getComponentValue("Right-Side SpDef Boost")), Integer.parseInt(getComponentValue("Right-Side Speed Boost"))};

        CurrentPokemon leftSide = new CurrentPokemon(
                getComponentValue("Left-Side Pokemon"),
                Integer.parseInt(getComponentValue("Left-Side Level")),
                getComponentValue("Left-Side Item"),
                getMove(getComponentValue("Left-Side Move")),
                getNature(getComponentValue("Left-Side Nature")),
                leftEVs, leftBoosts

        );

        CurrentPokemon rightSide = new CurrentPokemon(
                getComponentValue("Right-Side Pokemon"),
                Integer.parseInt(getComponentValue("Right-Side Level")),
                getComponentValue("Right-Side Item"),
                getMove(getComponentValue("Right-Side Move")),
                getNature(getComponentValue("Right-Side Nature")),
                rightEVs, rightBoosts

        );
        return new CurrentPokemon[]{leftSide,rightSide};
    }
    private static void createStatsPanel(JPanel panel, String title) throws IOException {
        String header = "Pokemon 2";
        if(title.equals("Left-Side")){header = "Pokemon 1";}
        final String[] natDex = arrayListToArray(getNatDexAsArrayList());
        final String[] moveList = arrayListToArray(getMoveListAsArrayList());
        final String[] itemList = arrayListToArray(Database.getItemList());
        String pokemonName = natDex[0];

        //text to denote who is attacking
        final JLabel attackerTitleLabel = new JLabel(header);
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
        componentMap.put(title+" Pokemon",pokemonSelect);

        pokemonSelect.addActionListener(_ -> {
            final String selected = Objects.requireNonNull(pokemonSelect.getSelectedItem()).toString();
            ImageIcon icon;
            try {
                icon = new ImageIcon(ImageIO.read(getSpriteFile(selected)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            youDisplayLabel.setIcon(icon);
        });

        final JTextField yourLevelSelect = new JTextField("100");
        panel.add(yourLevelSelect);
        componentMap.put(title+" Level",yourLevelSelect);

        final JTextField HP_EV = new JTextField("HP EV");
        panel.add(HP_EV);
        componentMap.put(title+" HP EV",HP_EV);

        final JTextField atkEV = new JTextField("Attack EV");
        panel.add(atkEV);
        componentMap.put(title+" Atk EV",HP_EV);

        final JTextField defEV = new JTextField("Defense EV");
        panel.add(defEV);
        componentMap.put(title+" Def EV",defEV);

        final JTextField spatkEV = new JTextField("Special Attack EV");
        panel.add(spatkEV);
        componentMap.put(title+" SpAtk EV",spatkEV);

        final JTextField spdefEV = new JTextField("Special Defense EV");
        panel.add(spdefEV);
        componentMap.put(title+" SpDef EV",spdefEV);

        final JTextField speedEV = new JTextField("Speed EV");
        panel.add(speedEV);
        componentMap.put(title+" Speed EV",speedEV);

        final JComboBox<String> natureSelect = new JComboBox<>(Constants.NATURE);
        panel.add(natureSelect);
        componentMap.put(title+" Nature",natureSelect);

        final JComboBox<String> itemSelect = new JComboBox<>(itemList);
        panel.add(itemSelect);
        componentMap.put(title+" Item",itemSelect);

        final JComboBox<String> moveSelect = new JComboBox<>(moveList);
        panel.add(moveSelect);
        componentMap.put(title+" Move",moveSelect);

        /*final JComboBox<String> ability = new JComboBox<>(moveList);
        panel.add(moveSelect);
        componentMap.put(title+" Ability",ability);
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
        componentMap.put(panelTitleAppend+"Atk Boost",attackBoost);

        //text to denote what the panel does
        panel.add(new JLabel("Defense Boosts"));
        //select what defBoost
        final JComboBox<String> defenseBoost = new JComboBox<>(Constants.BOOSTS);
        panel.add(defenseBoost);
        componentMap.put(panelTitleAppend+"Def Boost",defenseBoost);

        //text to denote what the panel does
        panel.add(new JLabel("Special Attack Boosts"));
        //select what spattackBoost
        final JComboBox<String> specialAttackBoost = new JComboBox<>(Constants.BOOSTS);
        panel.add(specialAttackBoost);
        componentMap.put(panelTitleAppend+"SpAtk Boost",specialAttackBoost);

        //text to denote what the panel does
        panel.add(new JLabel("Special Defense Boosts"));
        //select what spdefBoost
        final JComboBox<String> specialDefenseBoost = new JComboBox<>(Constants.BOOSTS);
        panel.add(specialDefenseBoost);
        componentMap.put(panelTitleAppend+"SpDef Boost",specialDefenseBoost);

        //text to denote what the panel does
        panel.add(new JLabel("Speed Boosts"));
        //select what speedBoost
        final JComboBox<String> speedBoost = new JComboBox<>(Constants.BOOSTS);
        panel.add(speedBoost);
        componentMap.put(panelTitleAppend+"Speed Boost",speedBoost);

    }

    private static String[] arrayListToArray(ArrayList<String> arrayList) {
        final int size = arrayList.size();
        final String[] array = new String[size];

        for (int i = 0; i < size; i++) {
            array[i] = arrayList.get(i);
        }
        return array;
    }

    private static String getComponentValue(String name){
        int max = 255;
        if(name.equals("Left-Side Level")||name.equals("Right-Side Level")){max=100;}
        final Component component = componentMap.get(name);
        if(component==null){
            System.out.println(name);
        }

        String tempString;
        if(component.getClass()==JComboBox.class){
            tempString = Objects.requireNonNull(((JComboBox<String>) component).getSelectedItem()).toString();
        }else{
            tempString = ((JTextField) component).getText();
            int toInt = 1;
            try{
                toInt = Integer.parseInt(tempString);
                if(toInt>max||toInt<1){toInt = 1;}
            }catch(Exception _){}
            tempString = Integer.toString(toInt);
        }
        return tempString;
    }

    private static File getSpriteFile(String name) {
        File file = new File("src/assets/0.png");
        if (!Objects.equals(name, "0")) {
            int dex = Database.getPokemon(name).dexNumber;

            try {
                file = new File("src/assets/" + dex + ".png");
            } catch (Exception e) {
                ErrorPrinter.setDetails("src/assets/" + dex + ".png", false);
                ErrorPrinter.handler(ErrorPrinter.ERROR_CODE.ABN_UI_MALFORMED_IMAGE_FILE, e);
            }
        }
        if (!file.exists()) {
            if (Database.getPokemon(name).dexNumber >= 906) {
                System.out.println("Nothing is broken. This is entirely a visual glitch.\nAll Paldea Pokemon don't have sprites available for bulk download.\nAlternate forms such as Regionals and Megas have sprites but it needs me to manually change the file name and the dex number in the codebase so its all still a work in progress.\nstay tuned.");
            } else {
                ErrorPrinter.setDetails(file.toString(), false);
                ErrorPrinter.handler(ErrorPrinter.ERROR_CODE.ABN_UI_MALFORMED_IMAGE_FILE, null);
            }
            file = new File("src/assets/0.png");

        }
        return file;
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
            atkStat = Calculators.statCalculation(base.baseAttack,31,atkEV,nature.baseAttack,level,atkBoost);
            defStat = Calculators.statCalculation(base.baseDefense,31,defEV,nature.baseDefense,level,defBoost);
            spAtkStat = Calculators.statCalculation(base.baseSpatk,31,spAtkEV,nature.baseSpatk,level,spAtkBoost);
            spDefStat = Calculators.statCalculation(base.baseSpdef,31,spDefEV,nature.baseSpdef,level,spDefBoost);
            speedStat = Calculators.statCalculation(base.baseSpeed,31,speedEV,nature.baseSpeed,level,speedBoost);
        }
    }
}
