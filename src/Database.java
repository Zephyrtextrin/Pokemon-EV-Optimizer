import java.util.*;

//this is very long bc it contains an entry for every pokemon and an entry for every paldea mon
public class Database{
    private static final HashMap<String, Pokemon> natDex = new HashMap<>();
    private static final HashMap<String, Move> moveList = new HashMap<>();
    private static final ArrayList<String> itemList = new ArrayList<>();
    private static final ArrayList<String> abilityList = new ArrayList<>();
    private static final Map<String, Type> typeList = new HashMap<>(); //allows u to sort by type name

    //ughhh repeated code.. u have to do the same thing for every method here cuz the parameters dont match up otherwise-----
    public static String[] getNatDexList(){return HelperMethods.getMapAsList(natDex.keySet());}

    public static String[] getMoveList(){return HelperMethods.getMapAsList(moveList.keySet());}

    public static String[] getItemList(){return HelperMethods.arrayListToArray(itemList);}

    public static String[] getAbilityList(){return HelperMethods.arrayListToArray(abilityList);}

    public static Move getMove(String input){return moveList.get(input);}

    public static Pokemon getPokemon(String input){
        Pokemon result=natDex.get(input);


        if(result==null) {
            Printer.setDetails(input, false);
            Printer.errorHandler(Printer.ERROR_CODE.ABN_DB_MISSINGNO, null);
            return natDex.get("Gallade");
        }

        boolean secondTypeMalformed = result.types.length>1&&result.types[1]==null;
        if(result.types[0]==null||secondTypeMalformed){
            Printer.setDetails(input, false);
            Printer.errorHandler(Printer.ERROR_CODE.ABN_DB_BIRDTYPE, null);
            return natDex.get("Gallade");
        }

        return result;
    }

    //this could technically only be one line but then it would be incredibly unreadable
    public static Type getType(String input){
        Type output = typeList.get(input);

        if(output==null){
            Printer.setDetails(input,false);
            Printer.errorHandler(Printer.ERROR_CODE.ABN_DB_TYPE_DNE, null);
            return typeList.get("Normal");
        }else{return output;}
    }


    public static double getMatchups(Type[] types, String attackType) {
        final Type typeOne = types[0];
        double typeTwoMod = 1;
        if(types.length!=1){typeTwoMod = Type.returnOneMatchup(types[1], attackType);}

        return (Type.returnOneMatchup(typeOne, attackType)*typeTwoMod);
    }

    //THANK U SO MUCH FOR DOING ALL THE TEDIOUS ASS FORMATTING LEXI!!! :HEART EMOJI:
    public static void initialize(){
        Printer.init();
        Type.init();
        Ability.init();
        Items.init();
    }

    static public class Pokemon {
        public int dexNumber;
        public String name;
        public Type[] types;
        public int[] stats;
        

        Pokemon(int dexNumber, String name, Type[] types, int[] stats){
            this.dexNumber = dexNumber;
            this.name = name;
            this.types = types;
            this.stats = stats;


            natDex.put(name, this);
        }

        public int getStat(Constants.Stats stat){
            return switch(stat){
                case HP -> stats[0];
                case Attack -> stats[1];
                case Defense -> stats[2];
                case Spatk -> stats[3];
                case Spdef -> stats[4];
                case Speed -> stats[5];
            };
        }
    }

    static public class Move{
        String name;
        int baseDamage;
        String type;
        Constants.MOVE_CATS moveCategory;

        Move(String name, int baseDamage, String type, String moveCategoryString) {
            this.name = name;
            this.baseDamage = baseDamage;
            this.type = type;
            if(moveCategoryString.equalsIgnoreCase("Physical")){
                this.moveCategory = Constants.MOVE_CATS.Physical;
            }else{
                this.moveCategory = Constants.MOVE_CATS.Special;
            }


            moveList.put(name, this);
        }
    }

    public static class Type {
        String name;
        String[] resistances;
        String[] weaknesses;
        String[] immunities;
        private static final String[] typeNames = new String[]{"Normal", "Fighting", "Flying", "Fire", "Grass", "Water", "Electric", "Ground", "Rock", "Dragon", "Poison", "Bug", "Ghost", "Psychic", "Ice", "Steel", "Dark", "Fairy"};
        //also maybe a section about fringe cases such as typeList.get("Grass") types being immune to spore and typeList.get("Ghost") types unable to be trapped

        public Type(String name) {
            this.name = name;
            typeList.put(name, this);
        }


        public static ArrayList[] returnAllMatchups(Type[] defender){
            ArrayList<String> neutral = new ArrayList<>();
            ArrayList<String> superEffective = new ArrayList<>();
            ArrayList<String> notVeryEffective = new ArrayList<>();
            ArrayList<String> immune = new ArrayList<>();

            for(String currentType:typeNames){
                double effectiveness = 1;
            
                for(Type i:defender){effectiveness*=returnOneMatchup(i, currentType);}

                if(effectiveness>1){superEffective.add(currentType);
                }else if(effectiveness<1&&effectiveness!=0){notVeryEffective.add(currentType);
                }else if(effectiveness==0){immune.add(currentType);
                }else{neutral.add(currentType);}
            }

            return new ArrayList[]{neutral,superEffective,notVeryEffective,immune};
        }

        private static double returnOneMatchup(Type defendingType, String attackingType){
            if(defendingType==null){return 1;}

            if(Arrays.asList(defendingType.weaknesses).contains(attackingType)) {return 2;
            }else if(Arrays.asList(defendingType.resistances).contains(attackingType)){return 0.5;
            }try{if(Arrays.asList(defendingType.immunities).contains(attackingType)) {return 0;}}catch(Exception _){}

            return 1;
        }

        public static void init(){
            //type database (inits all types into the array
            for(int i=0; i <=typeNames.length-1; i++){new Type(typeNames[i]);}

            //weaknesses
            typeList.get("Normal").weaknesses = new String[]{"Fighting"};
            typeList.get("Fighting").weaknesses = new String[]{"Flying", "Psychic", "Fairy"};
            typeList.get("Flying").weaknesses = new String[]{"Rock", "Electric", "Ice"};
            typeList.get("Fire").weaknesses = new String[]{"Water", "Ground", "Rock"};
            typeList.get("Grass").weaknesses = new String[]{"Poison", "Flying", "Ice", "Fire", "Bug"};
            typeList.get("Water").weaknesses = new String[]{"Electric", "Grass"};
            typeList.get("Electric").weaknesses = new String[]{"Ground"};
            typeList.get("Ground").weaknesses = new String[]{"Water", "Ice", "Grass"};
            typeList.get("Rock").weaknesses = new String[]{"Fighting", "Water", "Grass", "Ground", "Steel"};
            typeList.get("Dragon").weaknesses = new String[]{"Ice", "Fairy"};
            typeList.get("Poison").weaknesses = new String[]{"Ground", "Psychic"};
            typeList.get("Bug").weaknesses = new String[]{"Flying", "Rock", "Fire"};
            typeList.get("Dark").weaknesses = new String[]{"Fighting", "Fairy", "Bug"};
            typeList.get("Steel").weaknesses = new String[]{"Fighting", "Fire", "Ground"};
            typeList.get("Ghost").weaknesses = new String[]{"Ghost", "Dark"};
            typeList.get("Fairy").weaknesses = new String[]{"Steel", "Poison"};
            typeList.get("Psychic").weaknesses = new String[]{"Dark", "Ghost", "Bug"};
            typeList.get("Ice").weaknesses = new String[]{"Fighting", "Rock", "Fire", "Steel"};

            //resists
            typeList.get("Normal").resistances = new String[]{};
            typeList.get("Fighting").resistances = new String[]{"Rock", "Bug", "Dark"};
            typeList.get("Flying").resistances = new String[]{"Fighting", "Bug", "Grass"};
            typeList.get("Fire").resistances = new String[]{"Bug","Fire","Grass","Steel","Steel","Ice","Fairy"};
            typeList.get("Grass").resistances = new String[]{"Water","Electric","Grass","Ground"};
            typeList.get("Water").resistances = new String[]{"Fire","Water","Ice","Steel"};
            typeList.get("Electric").resistances = new String[]{"Flying","Steel","Electric"};
            typeList.get("Ground").resistances = new String[]{"Poison","Rock"};
            typeList.get("Rock").resistances = new String[]{"Normal","Fire","Poison","Flying"};
            typeList.get("Dragon").resistances = new String[]{"Fire","Water","Grass","Electric"};
            typeList.get("Poison").resistances = new String[]{"Fighting","Poison","Grass","Bug","Fairy"};
            typeList.get("Bug").resistances = new String[]{"Fighting","Ground","Grass"};
            typeList.get("Dark").resistances = new String[]{"Ghost", "Dark"};
            typeList.get("Steel").resistances = new String[]{"Normal","Flying","Rock","Bug","Steel","Grass","Psychic","Ice","Dragon","Fairy"};
            typeList.get("Ghost").resistances = new String[]{"Poison","Bug"};
            typeList.get("Fairy").resistances = new String[]{"Fighting","Bug","Dark"};
            typeList.get("Psychic").resistances = new String[]{"Fighting","Psychic"};
            typeList.get("Ice").resistances = new String[]{"Ice"};

            //immunities
            typeList.get("Normal").immunities = new String[]{"Ghost"};
            typeList.get("Flying").immunities = new String[]{"Ground"};
            typeList.get("Steel").immunities = new String[]{"Poison"};
            typeList.get("Ghost").immunities = new String[]{"Normal", "Fighting"};
            typeList.get("Fairy").immunities = new String[]{"Dragon"};
            }
        }

    public static class Items{
        public static double getItemEffect(String input, Constants.MOVE_CATS category){
            if(input.equals("Choice Band")&&category== Constants.MOVE_CATS.Physical){return 1.5;
            }else if(input.equals("Choice Specs")&&category== Constants.MOVE_CATS.Special){return 1.5;
            }else if(input.equals("Life Orb")){return 1.3;}
            return 1;
        }

        private static void init(){
            itemList.add("No Item/Other");
            itemList.add("Choice Band");
            itemList.add("Choice Specs");
            itemList.add("Choice Scarf"); //use when u make outspeed calc
            itemList.add("Life Orb");
            //itemList.add("Booster Energy [doesn't work rn]");
        }
    }

    //ughhh do this shit later
    public static class Ability{
        private static void init(){
            abilityList.add("Other Ability");
            abilityList.add("Swift Swim");
            abilityList.add("Chlorophyll");
            abilityList.add("Huge Power");
            abilityList.add("Thick Fat");
            abilityList.add("Hustle");
            abilityList.add("Guts");
            abilityList.add("Marvel Scale");
            abilityList.add("Pure Power");
            abilityList.add("Heatproof");
            abilityList.add("Dry Skin");
            abilityList.add("Iron Fist");
            abilityList.add("Adaptability");
            abilityList.add("Solar Power");
            abilityList.add("Quick Feet");
            abilityList.add("Technician");
            abilityList.add("Tinted Lens");
            abilityList.add("Filter");
            abilityList.add("Scrappy");
            abilityList.add("Solid Rock");
            abilityList.add("Reckless");
            abilityList.add("Flower Gift");
            abilityList.add("Sheer Force");
            abilityList.add("Sand Rush");
            abilityList.add("Analytic");
            abilityList.add("Sap Sipper");
            abilityList.add("Sand Force");
            abilityList.add("Fur Coat");
            abilityList.add("Strong Jaw");
            abilityList.add("Refrigerate");
            abilityList.add("Mega Launcher");
            abilityList.add("Grass Pelt");
            abilityList.add("Tough Claws");
            abilityList.add("Pixilate");
            abilityList.add("Aerilate");
            abilityList.add("Parental Bond");
            abilityList.add("Dark Aura");
            abilityList.add("Fairy Aura");
            abilityList.add("Delta Stream");
            abilityList.add("Steelworker");
            abilityList.add("Slush Rush");
            abilityList.add("Galvanize");
            abilityList.add("Surge Surfer");
            abilityList.add("Fluffy");
            abilityList.add("Shadow Shield");
            abilityList.add("Prism Armor");
            abilityList.add("Neuroforce");
            abilityList.add("Ice Scales");
            abilityList.add("Steely Spirit");
            abilityList.add("Gorilla Tactics");
            abilityList.add("Transistor");
            abilityList.add("Dragon's Maw");
            abilityList.add("Purifying Salt");
            abilityList.add("Well-Baked Body");
            abilityList.add("Wind Rider");
            abilityList.add("Rocky Payload");
            abilityList.add("Protosynthesis");
            abilityList.add("Quark Drive");
            abilityList.add("Vessel of Ruin");
            abilityList.add("Sword of Ruin");
            abilityList.add("Tablets of Ruin");
            abilityList.add("Beads of Ruin");
            abilityList.add("Orichalcum Pulse");
            abilityList.add("Hadron Engine");
            abilityList.add("Sharpness");
            abilityList.add("Mind's Eye");
        }
    }
}