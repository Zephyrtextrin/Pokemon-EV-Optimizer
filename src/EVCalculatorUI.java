import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class EVCalculatorUI extends Database{
    static final HashMap<String, Component> componentMap = new HashMap<>();

    public static void initUI() throws IOException{
        final int SIZE = 1000;
        final JFrame frame = new JFrame();
        frame.setSize(SIZE, SIZE);
        frame.setLayout(new GridLayout(1, 1)); //not good fix later
        frame.setVisible(true);

        frame.add(new StatsPanel("Left-Side "));
        frame.add(new StatsPanel(""));
        frame.add(new StatsPanel("Right-Side "));

    }

    private static CurrentPokemon[] initCurrentPokemon(){
        final int[] leftEVs = new int[]{Integer.parseInt(HelperMethods.getComponentValue("Left-Side HP EV", true)), Integer.parseInt(HelperMethods.getComponentValue("Left-Side Attack EV", true)), Integer.parseInt(HelperMethods.getComponentValue("Left-Side Defense EV", true)), Integer.parseInt(HelperMethods.getComponentValue("Left-Side Special Attack EV", true)), Integer.parseInt(HelperMethods.getComponentValue("Left-Side Special Defense EV", true)), Integer.parseInt(HelperMethods.getComponentValue("Left-Side Speed EV", true))};
        final int[] leftBoosts = new int[]{Integer.parseInt(HelperMethods.getComponentValue("Left-Side Attack Boost", true)), Integer.parseInt(HelperMethods.getComponentValue("Left-Side Defense Boost", true)), Integer.parseInt(HelperMethods.getComponentValue("Left-Side Special Attack Boost", true)), Integer.parseInt(HelperMethods.getComponentValue("Left-Side Special Defense Boost", true)), Integer.parseInt(HelperMethods.getComponentValue("Left-Side Speed Boost", true))};

        final int[] rightEVs = new int[]{Integer.parseInt(HelperMethods.getComponentValue("Right-Side HP EV", true)), Integer.parseInt(HelperMethods.getComponentValue("Right-Side Attack EV", true)), Integer.parseInt(HelperMethods.getComponentValue("Right-Side Defense EV", true)), Integer.parseInt(HelperMethods.getComponentValue("Right-Side Special Attack EV", true)), Integer.parseInt(HelperMethods.getComponentValue("Right-Side Special Defense EV", true)), Integer.parseInt(HelperMethods.getComponentValue("Right-Side Speed EV", true))};
        final int[] rightBoosts = new int[]{Integer.parseInt(HelperMethods.getComponentValue("Right-Side Attack Boost", true)), Integer.parseInt(HelperMethods.getComponentValue("Right-Side Defense Boost", true)), Integer.parseInt(HelperMethods.getComponentValue("Right-Side Special Attack Boost", true)), Integer.parseInt(HelperMethods.getComponentValue("Right-Side Special Defense Boost", true)), Integer.parseInt(HelperMethods.getComponentValue("Right-Side Speed Boost", true))};

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
                leftEVs, leftBoosts,
                HelperMethods.getComponentValue("Left-Side Ability", true),
                HelperMethods.getComponentValue("Left-Side Status", true), true

        );

        CurrentPokemon rightSide = new CurrentPokemon(
                HelperMethods.getComponentValue("Right-Side Pokemon", true),
                Integer.parseInt(HelperMethods.getComponentValue("Right-Side Level", true)),
                HelperMethods.getComponentValue("Right-Side Item", true),
                getMove(HelperMethods.getComponentValue("Right-Side Move", true)),
                getNature(HelperMethods.getComponentValue("Right-Side Nature", true)),
                rightEVs, rightBoosts,
                HelperMethods.getComponentValue("Right-Side Ability", true),
                HelperMethods.getComponentValue("Right-Side Status", true), false

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
            EVrolls = Calculators.findLeastAttackEVs(subjectMon,opponentMon,moveUsed,weather,spread);

        }else{ //for outspeeding
            final int[] EV = {Calculators.findLeastSpeedEVs(subjectMon, opponentMon.speedStat, subjectMon.speedBoost)};
            if(Constants.DEBUG_DAMAGE_MODE){
                System.out.println();
            }
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

    public static class StatsPanel extends JPanel{
        private final String title;
        private int currentY = 0;
        final GridBagConstraints c = new GridBagConstraints();


        private StatsPanel(String name) throws IOException{
            this.setLayout(new GridBagLayout());
            title = name;
            c.anchor = GridBagConstraints.LINE_START;
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 0.5;

            if(!name.isEmpty()){
                this.createStatsPanel();
                this.makeBoostPanel();
            }else{this.otherPanel();}
        }

        public String returnTitle(){return title;}

        /*public void addObject(Component component, String componentName, boolean addToMap, String headerLabel){
            if(!headerLabel.isEmpty()){this.addOneObject(new JLabel(headerLabel),headerLabel,false);}
            this.addOneObject(component,componentName,addToMap);
        }  */

        private void addObject(Component component, String componentName, boolean addToMap){


            c.fill = GridBagConstraints.HORIZONTAL;
            component.setSize(new Dimension(500,500));
            c.gridy = currentY;
            this.add(component, c);
            if(addToMap){componentMap.put(this.title+componentName, this);}
            currentY++;

            this.repaint();
            this.revalidate();
        }

        public void createStatsPanel() throws IOException {
            String header = "Pokemon 1";
            if(this.title.equals("Right-Side ")){header="Pokemon 2";}
            final String[] natDex = getNatDexList();
            final String[] moveList = getMoveList();
            final String[] itemList = getItemList();
            final String[] natureList = getNatureList();
            final String[] abilityList = getAbilityList();
            String pokemonName = natDex[0];

            //text to denote who is attacking
            this.addObject(new JLabel(header),"Header",false);

            //your pokemon image
            JLabel iconLabel = new JLabel(new ImageIcon(ImageIO.read(HelperMethods.getSpriteFile(pokemonName))));
            this.addObject(iconLabel,"Image",true);

            //select what pokemonis attacking
            final JComboBox<String> selectedPokemon = new JComboBox<>(natDex);
            this.addObject(selectedPokemon,"Pokemon",true);

            //level select
            this.addObject(new JTextField("100"),"Level",true);

            //evs
            for(String currentStat:Constants.STATS){
                final String name = currentStat+" EV";
                this.addObject(new JTextField(name),name,true);
            }

            //status conditions
            this.addObject(new JComboBox<>(Constants.STATUS_CONDITION_LIST),"Status",true);

            //nature list
            this.addObject(new JComboBox<>(natureList),"Nature",true);


            //ability
            this.addObject(new JComboBox<>(abilityList),"Ability",true);

            //item
            this.addObject(new JComboBox<>(itemList),"Item",true);

            //move
            this.addObject(new JComboBox<>(moveList),"Move",true);

            selectedPokemon.addActionListener(_ -> {
                final String selected = Objects.requireNonNull(selectedPokemon.getSelectedItem()).toString();
                ImageIcon icon;
                try{icon = new ImageIcon(ImageIO.read(HelperMethods.getSpriteFile(selected)));
                }catch (IOException e){throw new RuntimeException(e);}

                iconLabel.setIcon(icon);
            });
            this.add(Box.createVerticalGlue());
        }


        public void makeBoostPanel() {

            for(String currentStat:Constants.STATS){
                if(!currentStat.equals("HP")) {
                    this.addObject(new JLabel(currentStat + " Boosts"),currentStat + " Boosts",false);

                    final String name = this.title + currentStat + " Boost";
                    final JComboBox<String> boost = new JComboBox<>(Constants.BOOSTS);
                    this.addObject(boost,name,true);

                    if(Constants.DEBUG_UI_MODE){System.out.println("Component [" + name + "] has been created.");}
                }
            }
        }

        //other features like field conditions
        public void otherPanel(){
            final String[] WEATHER = Constants.WEATHER_LIST;

            //text to denote what the panel does
            this.addObject(new JLabel("Other"),"Title",false);

            //filler
            this.add(Box.createVerticalGlue());

            //text to denote what the panel does
            this.addObject(new JLabel("Weather"),"Weather Label",false);

            //select what weather
            final JComboBox<String> weather = new JComboBox<>(WEATHER);
            this.addObject(weather,"Weather",true);

            //select if spread
            final JCheckBox spread = new JCheckBox("Is this a spread move?");
            this.addObject(spread,"Spread",true);

            //text to denote what the panel does
            this.addObject(new JLabel("Find Minimum EVs to:"),"minEVs",false);

            //select what function
            final JComboBox<String> toDo = new JComboBox<>(Constants.CAPABILITY_LIST);
            this.addObject(toDo,"Todo",false);

            //text to denote what the panel does
            this.addObject(new JLabel("Select which side to find EVS for"),"minEVs",false);

            //select what function
            final JComboBox<String> target = new JComboBox<>(new String[]{"Pokemon 1 (Left Side)", "Pokemon 2 (Right Side"});
            this.addObject(target,"Target",false);


            final JButton run = new JButton("Run");
            this.addObject(run,"Run",true);


            //gathers all info on both sides and performs preliminary calcs needed for the real damage calc
            //i would make this into a method but tbh the sheer amount of fucking params it would need is SO INSANELY FUCKING COMICAL
            run.addActionListener(_ ->{
                CurrentPokemon[] allData = initCurrentPokemon();
                CurrentPokemon subjectMon = allData[1]; //who is attacking/defending
                CurrentPokemon opponentMon = allData[0];
                if(Constants.DEBUG_CALC_MODE){System.out.println("\n-[AFTER ABILITY MODIFIER CALCULATIONS]-\n[POKEMON]: "+subjectMon.base.name+"\n\nafter pokemon atk: "+subjectMon.attackStat+"\nafter pokemon def: "+subjectMon.defStat+"\nafter pokemon spatk: "+subjectMon.spAttackStat+"\nafter pokemon spdef: "+subjectMon.spDefStat+"\nafter pokemon speed: "+subjectMon.speedStat+"\n----------\n[END].");}

                final String process = Objects.requireNonNull(toDo.getSelectedItem()).toString();

                if(Objects.requireNonNull(target.getSelectedItem()).toString().equals("Pokemon 1 (Left Side)")){
                    subjectMon = allData[0];
                    opponentMon = allData[1];
                }

                if(!Constants.DEBUG_DISABLE_OUTPUT){output(process, subjectMon, opponentMon, Objects.requireNonNull(weather.getSelectedItem()).toString(),spread.isSelected());}
            });
        }
    }

    //holds data for each pokemon on the UI
    static public class CurrentPokemon{
        public Pokemon base;
        public int level;
        public String item;
        public Move move;
        public Nature nature;
        public int HPStat;
        public int attackStat;
        public int defStat;
        public int spAttackStat;
        public int spDefStat;
        public int speedStat;
        public int attackBoost;
        public int defBoost;
        public int spAttackBoost;
        public int spDefBoost;
        public int speedBoost;
        public int HP_EV;
        public int attackEV;
        public int defEV;
        public int spAttackEV;
        public int spDefEV;
        public int speedEV;
        public String status;
        public String ability;


        public CurrentPokemon(String name, int level, String item, Move move, Nature nature, int[] EVs, int[] boosts, String ability, String status,boolean subject){
            base = getPokemon(name);
            this.nature = nature;
            this.level = level;
            this.item = item;
            this.move = move;
            this.attackBoost = boosts[0];
            this.defBoost = boosts[1];
            this.spAttackBoost = boosts[2];
            this.spDefBoost = boosts[3];
            this.speedBoost = boosts[4];
            this.HP_EV = EVs[0];
            this.attackEV = EVs[1];
            this.defEV = EVs[2];
            this.spAttackEV = EVs[3];
            this.spDefEV = EVs[4];
            this.speedEV = EVs[5];
            this.status = status;
            this.ability = ability;


            HPStat = Calculators.calcHP(HP_EV,level,base.baseHP);
            attackStat = Calculators.statCalculation(base.baseAttack,31,attackEV,nature.attack,level,attackBoost);
            defStat = Calculators.statCalculation(base.baseDefense,31,defEV,nature.defense,level,defBoost);
            spAttackStat = Calculators.statCalculation(base.baseSpatk,31,spAttackEV,nature.spatk,level,spAttackBoost);
            spDefStat = Calculators.statCalculation(base.baseSpdef,31,spDefEV,nature.spdef,level,spDefBoost);
            speedStat = Calculators.statCalculation(base.baseSpeed,31,speedEV,nature.speed,level,speedBoost);

            this.abilityStatModifier(HelperMethods.getComponentValue("Weather",true),subject);
        }

        //directly modifies pokemon's stats
        private void abilityStatModifier(String weather, boolean subject){
            if(Constants.DEBUG_CALC_MODE&&subject){System.out.println("\n-[BEFORE ABILITY MODIFIER CALCULATIONS]-\n[POKEMON]: "+base.name+"\n\nbefore pokemon atk: "+attackStat+"\nbefore pokemon def: "+defStat+"\nbefore pokemon spatk: "+spAttackStat+"\nbefore pokemon spdef: "+spDefStat+"\nbefore pokemon speed: "+speedStat+"\n[ABILITY]: "+ability+"\n----------\n[END].");}

            switch(ability){
                case "Swift Swim"->{if(weather.equals("Rain")){speedStat*=2;}}
                case "Chlorophyll"->{if(weather.equals("Sun")){speedStat*=2;}}
                case "Huge Power"->attackStat*=2;
                case "Hustle"->attackStat*=1.5;
                case "Orichalcum Pulse"-> attackStat*=1.3;
                case "Hadron Engine"-> spAttackStat*=1.3;
                case "Solar Power"->{if(weather.equals("Sun")){spAttackStat*=1.5;}}
            }
            if(Constants.DEBUG_CALC_MODE&&subject){System.out.println("\n-[AFTER ABILITY MODIFIER CALCULATIONS, STILL IN THE SAME METHOD THO]-\n[POKEMON]: "+base.name+"\n\nbefore pokemon atk: "+attackStat+"\nbefore pokemon def: "+defStat+"\nbefore pokemon spatk: "+spAttackStat+"\nbefore pokemon spdef: "+spDefStat+"\nbefore pokemon speed: "+speedStat+"\n----------\n[END].");};
        }

    }

    public static void printAllComponentNames(){for(String i:componentMap.keySet()){System.out.println(i);}}
}
