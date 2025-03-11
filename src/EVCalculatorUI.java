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

        frame.add(new StatsPanel("Left-Side ",0,1));
        frame.add(new StatsPanel("",0,0));
        frame.add(new StatsPanel("Right-Side ",1,0));

        frame.pack();
    }

    private static CurrentPokemon[] initCurrentPokemon(){
        int[] leftEVs = new int[6];
        int[] leftBoosts = new int[5];
        int[] rightEVs = new int[6];
        int[] rightBoosts = new int[5];

        for(int i=0; i<6;i++){
            String currentStat = Constants.STATS[i];
            leftEVs[i] = Integer.parseInt(HelperMethods.getComponentValue("Left-Side "+currentStat+" EV", true));
            rightEVs[i] = Integer.parseInt(HelperMethods.getComponentValue("Right-Side "+currentStat+" EV", true));

            if(!currentStat.equals("HP")){
                leftBoosts[i-1] = Integer.parseInt(HelperMethods.getComponentValue("Left-Side "+currentStat+" Boost", true));
                rightBoosts[i-1] = Integer.parseInt(HelperMethods.getComponentValue("Right-Side "+currentStat+" Boost", true));
            }
        }

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
        Move moveUsed = subjectMon.getMove();
        int[] EVrolls = {-1,-1,-1}; //0 is lowest roll, 1 is median roll, 2 is highest roll

        if (process.equals("Tank")){
            moveUsed = opponentMon.getMove();
            EVrolls = Calculators.findLeastHPEVs(opponentMon,subjectMon,moveUsed,weather,spread);
        }else if(process.equals("OHKO")){
            EVrolls = Calculators.findLeastAttackEVs(subjectMon,opponentMon,moveUsed,weather,spread);

        }else{ //for outspeeding
            final int[] EV = {Calculators.findLeastSpeedEVs(subjectMon, opponentMon.stats[5], subjectMon.getInt(Constants.Stats.Speed, Constants.Attributes.boost))};
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
                }else{System.out.printf("Minimum EVs needed for %d boosts %s to outspeed %s nature %d EV %s: %d",subjectMon.getInt(Constants.Stats.Speed, Constants.Attributes.boost),subjectMon.base.name,opponentMon.nature.name,opponentMon.getInt(Constants.Stats.Speed,Constants.Attributes.EV),opponentMon.base.name,EV);}

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


        private StatsPanel(String name, int statPanelX, int boostPanelX) throws IOException{
            this.setLayout(new GridBagLayout());
            title = name;
            c.anchor = GridBagConstraints.LINE_START;
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 0.5;

            if(!name.isEmpty()){
                this.createStatsPanel(statPanelX);
                this.makeBoostPanel(boostPanelX);
            }else{this.otherPanel();}
        }

        private void addObject(Component component, String componentName){
            componentName = this.title+componentName;

            c.fill = GridBagConstraints.HORIZONTAL;
            component.setSize(new Dimension(500,500));
            c.gridy = currentY;
            this.add(component, c);
            currentY++;

            if(Constants.DEBUG_UI_MODE){System.out.println("Component ["+componentName+"] has been created.\nClass: "+component.getClass()+"\n");}

            this.repaint();
            this.revalidate();
        }

        public void createStatsPanel(int x) throws IOException{
            currentY = 0;
            c.gridx = x;

            String header = "Pokemon 1";
            if(this.title.equals("Right-Side ")){header="Pokemon 2";}
            final String[] natDex = getNatDexList();
            String pokemonName = natDex[0];

            //text to denote who is attacking
            this.addObject(new JLabel(header),"Header");

            //your pokemon image
            JLabel iconLabel = new JLabel(new ImageIcon(ImageIO.read(HelperMethods.getSpriteFile(pokemonName))));
            this.addObject(iconLabel,"Image");

            //select what pokemonis attacking
            final JComboBox<String> selectedPokemon = new JComboBox<>(natDex);
            this.addObject(selectedPokemon,"Pokemon");
            componentMap.put(this.title+"Pokemon",selectedPokemon);

            //level select
            JTextField level = new JTextField("100");
            this.addObject(level,"Level");
            componentMap.put(this.title+"Level",level);


            //evs
            for(String currentStat:Constants.STATS){
                final String name = currentStat+" EV";
                final JTextField EV = new JTextField(name);
                this.addObject(EV,name);
                componentMap.put(this.title+name,EV);
            }

            for(Constants.Attribute att:Constants.ATTRIBUTES){
                final String name = this.title+att.name;
                final JComboBox<String> field = new JComboBox<>(att.items);
                this.addObject(field,name);
                componentMap.put(name,field);
            }

            selectedPokemon.addActionListener(_ -> {
                final String selected = Objects.requireNonNull(selectedPokemon.getSelectedItem()).toString();
                ImageIcon icon;
                try{icon = new ImageIcon(ImageIO.read(HelperMethods.getSpriteFile(selected)));
                }catch (IOException e){throw new RuntimeException(e);}

                iconLabel.setIcon(icon);
            });
            this.add(Box.createVerticalGlue());
        }


        public void makeBoostPanel(int x){
            c.gridx = x;
            currentY = 5;
            for(String currentStat:Constants.STATS){
                if(!currentStat.equals("HP")) {
                    final String name = this.title + currentStat + " Boost";
                    final JComboBox<String> boost = new JComboBox<>(Constants.BOOSTS);
                    this.addObject(boost,name);
                    componentMap.put(name,boost);

                }
            }
        }

        //other features like field conditions
        public void otherPanel(){
            final String[] WEATHER = Constants.WEATHER_LIST;

            //text to denote what the panel does
            this.addObject(new JLabel("Other"),"Title");

            //filler
            this.add(Box.createVerticalGlue());

            //text to denote what the panel does
            this.addObject(new JLabel("Weather"),"Weather Label");

            //select what weather
            final JComboBox<String> weather = new JComboBox<>(WEATHER);
            this.addObject(weather,"Weather");
            componentMap.put("Weather",weather);

            //select if spread
            final JCheckBox spread = new JCheckBox("Is this a spread move?");
            this.addObject(spread,"Spread");
            componentMap.put("Spread",spread);

            //text to denote what the panel does
            this.addObject(new JLabel("Find Minimum EVs to:"),"minEVs");

            //select what function
            final JComboBox<String> toDo = new JComboBox<>(Constants.CAPABILITY_LIST);
            this.addObject(toDo,"Todo");

            //text to denote what the panel does
            this.addObject(new JLabel("Select which side to find EVS for"),"minEVs");

            //select what function
            final JComboBox<String> target = new JComboBox<>(new String[]{"Pokemon 1 (Left Side)", "Pokemon 2 (Right Side"});
            this.addObject(target,"Target");
            componentMap.put(this.title+"Target",target);


            final JButton run = new JButton("Run");
            this.addObject(run,"Run");


            //gathers all info on both sides and performs preliminary calcs needed for the real damage calc
            //i would make this into a method but tbh the sheer amount of fucking params it would need is SO INSANELY FUCKING COMICAL
            run.addActionListener(_ ->{
                CurrentPokemon[] allData = initCurrentPokemon();
                CurrentPokemon subjectMon = allData[1]; //who is attacking/defending
                CurrentPokemon opponentMon = allData[0];
                if(Constants.DEBUG_CALC_MODE){System.out.println("\n-[AFTER ABILITY MODIFIER CALCULATIONS]-\n[POKEMON]: "+subjectMon.base.name+"\n\nafter pokemon atk: "+subjectMon.stats[1]+"\nafter pokemon def: "+subjectMon.stats[2]+"\nafter pokemon spatk: "+subjectMon.stats[3]+"\nafter pokemon spdef: "+subjectMon.stats[4]+"\nafter pokemon speed: "+subjectMon.stats[5]+"\n----------\n[END].");}

                final String process = Objects.requireNonNull(toDo.getSelectedItem()).toString();

                if(Objects.requireNonNull(target.getSelectedItem()).toString().equals("Pokemon 1 (Left Side)")){
                    subjectMon = allData[0];
                    opponentMon = allData[1];
                }

                if(!Constants.DEBUG_DISABLE_OUTPUT){output(process, subjectMon, opponentMon, Objects.requireNonNull(weather.getSelectedItem()).toString(),spread.isSelected());}
            });
        }
    }

    public static void printAllComponentNames(){for(String i:componentMap.keySet()){System.out.println(i);}}

    //holds data for each pokemon on the UI
    static public class CurrentPokemon{
        private final Pokemon base;
        private final int level;
        private final String item;
        private final Move move;
        private final Nature nature;
        private final int[] stats = new int[6];
        private final int[] boosts;
        private final int[] EVs;
        private final String status;
        private final String ability;

        public CurrentPokemon(String name, int level, String item, Move move, Nature nature, int[] EVs, int[] boosts, String ability, String status){
            base = getPokemon(name);
            this.nature = nature;
            this.level = level;
            this.item = item;
            this.move = move;
            this.EVs = EVs;
            this.boosts = boosts;
            this.status = status;
            this.ability = ability;


            this.stats[0] = Calculators.calcHP(EVs[0],level,base.baseHP);
            this.stats[1] = Calculators.statCalculation(base.baseAttack,31,EVs[1],nature.attack,level,boosts[0]);
            this.stats[2] = Calculators.statCalculation(base.baseDefense,31,EVs[2],nature.defense,level,boosts[1]);
            this.stats[3] = Calculators.statCalculation(base.baseSpatk,31,EVs[3],nature.spatk,level,boosts[2]);
            this.stats[4] = Calculators.statCalculation(base.baseSpdef,31,EVs[4],nature.spdef,level,boosts[3]);
            this.stats[5] = Calculators.statCalculation(base.baseSpeed,31,EVs[5],nature.speed,level,boosts[4]);

            this.abilityStatModifierOpp(HelperMethods.getComponentValue("Weather",true));
        }

        //directly modifies pokemon's stats
        public void abilityStatModifierOpp(String weather){

            switch(ability){
                case "Swift Swim"->{if(weather.equals("Rain")){stats[5]*=2;}}
                case "Chlorophyll"->{if(weather.equals("Sun")){stats[5]*=2;}}
                case "Huge Power"->stats[1]*=2;
                case "Hustle"->stats[1]*=1.5;
                case "Orichalcum Pulse"-> stats[1]*=1.3;
                case "Hadron Engine"-> stats[3]*=1.3;
                case "Solar Power"->{if(weather.equals("Sun")){stats[3]*=1.5;}}
            }
        }

        public int getInt(Constants.Stats stat, Constants.Attributes att){
            int mod = 0;
            if(att==Constants.Attributes.boost){mod = 1;} //used bc the boost array has no value for hp so the indexes r different

            int[] list = switch(att){
                case stats -> this.stats;
                case EV -> this.EVs;
                case boost -> this.boosts;
                default -> new int[]{};
            };

            if(att==Constants.Attributes.level){return this.level;}
            return switch(stat){
                case HP -> list[0];
                case Attack -> list[1-mod];
                case Defense -> list[2-mod];
                case Spatk -> list[3-mod];
                case Spdef -> list[4-mod];
                case Speed -> list[5-mod];
            };
        }

        public Pokemon getBase(){return base;}
        public Nature getNature(){return nature;}
        public Move getMove(){return move;}

        public String getString(Constants.Attributes att) {
            return switch (att) {
                case item -> this.item;
                case status -> this.status;
                case ability -> this.ability;
                default -> "yo wrong one " + att;
            };
        }
    }
}
