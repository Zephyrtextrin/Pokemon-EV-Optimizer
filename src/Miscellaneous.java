import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class Miscellaneous extends Pokedex{
    protected static void printStats(Pokemon poke){
        System.out.println("\n[POKEMON LOADED]: "+poke.name.toUpperCase());
        System.out.printf("[HP]: %d\n[ATTACK]: %d\n[DEFENSE]: %d\n[SP. ATTACK]: %d\n[SP. DEFENSE]: %d\n[SPEED]: %d\n\n",poke.baseHP,poke.baseAttack,poke.baseDefense,poke.baseSpatk,poke.baseSpdef,poke.baseSpeed);
    }

    protected static String convertToCamelCase(String input){
        int index = -1; //starts at -1 because indexes start at 0
        final char[] inputArray = input.toCharArray(); //breaks input into a string of characters
        inputArray[0] = charCapital(inputArray[0]);

        for(char i:input.toCharArray()){
            index++;
            if(i==' '&&inputArray[index+1]!=' '){inputArray[index+1] = charCapital(inputArray[index+1]);}
        }
        return String.valueOf(inputArray);
    }

    //sub method used in camel case conversion only
    private static char charCapital(char input){
        String inputToString = Character.toString(input);
        return inputToString.toUpperCase().charAt(0);
    }

    public static double getNature(String input){
        input = input.toUpperCase();

        return switch(input){
            case "POSITIVE" -> 1.1;
            case "NEGATIVE" -> 0.9;
            default -> 1; //for neutral nature and fallback in case of invalid value
        };
    }

    //for all you out there who love the big zapper
    public static String getAlias(String input){
        return switch(input){
            case "The Big Zapper","Big Zapper"->"Zapdos";
            case "The Dust Bowl"->"Ting-Lu";
            case "Ttar"->"Tyranitar";
            case "Chandy"->"Chandelure";
            case "Exbo"->"Typhlosion";
            default -> input;
        };
    }

    //get selected mon
    public static Pokemon selectPokemon(){
        boolean keepGoing = true;
        Pokemon pokemon;
        do {
            String name = Miscellaneous.getAlias(Miscellaneous.convertToCamelCase(InputHelper.getString("Enter a Pokemon name."))); //must convert to camel case bc lists r case sensitive also this is a really long line of code but alles ok cause its tryna catch like 15 different errors
            pokemon = Pokedex.getPokemonStats(getAlias(convertToCamelCase(name)));
            if(pokemon!=null){ //only continues if pokemon is valid
                Miscellaneous.printStats(pokemon); //lists stats
                keepGoing = !InputHelper.getYN("Is this the Pokemon you wanted?");
            }else{System.out.println("[INVALID INPUT!]\nThe Pokedex does not recognize that Pokemon.\n[INPUT POKEMON]: " + name + "\nThis Pokemon either does not exist or cases the code to throw an error. You'll have to type a new Pokemon.\nif you believe this to be a mistake, lmk - Alexander");} //the biggest print of all time

            System.out.println();//makes new line break for formatting. u cant append \n after the keepGoing input because it would mess up the user input and make the formatting even worse

        }while(keepGoing);

        return pokemon;
    }

    //OPTION 1: just download all the fanmade gen6+ sprites and use urls for gen 1-5 (placeholder)
    //OPTION 2: make a github repo of all the images (this is the dream)
    //OPTION 3: just download *all the sprites* (this world increase download times)
    public static URL getImageURL(String name) throws MalformedURLException{
        String link = "https://img.pokemondb.net/sprites/black-white/normal/";
        int index = Pokedex.getIndex(name);
        if(index<=697){
            link +=name.toLowerCase()+".png";
        }
        return new URL(link);
    }

    public static double[] getSpecifications(){
        //find values for base stat calculation
        final int IV = InputHelper.getRangedInt("Enter Speed IV",0,31);
        final int EV = InputHelper.getRangedInt("Enter Speed EV",0,252);
        final String nature = InputHelper.getStringInArray(new String[]{"positive","neutral","negative"},"Enter Nature"); //todo: make it also let u type in natures like adamant
        final double natureMultiplier = Miscellaneous.getNature(nature);
        final int level = InputHelper.getRangedInt("Enter Level",1,100);

        return new double[]{IV,EV,natureMultiplier,level};
    }

/* details[]
0 = IV
1 = EV
2 = nature
3 = level
*/

    public final static class Calculators{
        //calculation for all stats (excluding HP)
        //more info: https://bulbapedia.bulbagarden.net/wiki/Stat#Generation_III_onward
        //must be cast to double to prevent floating point error but has to be cast back to int because all pokemon calculations *always* round down
        public static int statCalculation(int baseStat, int IV, int EV, double nature, int level){
            EV/=4; //divides ev by 4 because ev is divided by 4 in stat calcs
            return (int)((((double)((2*baseStat+IV+EV)*level)/100)+5)*nature);
        }

        //finds what stat boost you need to be at to be faster than a given stat
        public static int findLeastStatBoosted(int IV, int EV, double nature, int level, int targetStat, int boostCount){
            for(int currentStat = 0; currentStat<255; currentStat++){
                final int stat = (int)(statCalculation(currentStat,IV,EV,nature,level)*getBoostModifier(boostCount));
                if(stat>targetStat){return currentStat;}
            }
            return -1;
        }

         //finds what stat boost you need to be at to be faster than a given stat
        public static int findLeastSpeedEVs(int IV, int baseStat, double nature, int level, int targetStat, int boostCount){
            for(int EV = 0; EV<=252; EV++){
                final int stat = (int)(statCalculation(baseStat,IV,EV,nature,level)*getBoostModifier(boostCount));
                if(stat>targetStat){return EV;}
            }
            return -1;
        }

        //finds what stat boost you need to ohko
        public static int findLeastAtkEVs(int baseStat, double nature, int level, Move move, int defenderStat, int boostCount, int oppHP){
            System.out.println("opp hp "+oppHP);
            for(int EV = 0; EV<=252; EV++){
                final int stat = (int)(statCalculation(baseStat,31,EV,nature,level)*getBoostModifier(boostCount));
                final int damage = (int)damageCalc(level,stat,defenderStat,move);
                if(damage>=oppHP){return EV;}
            }
            return -1;
        }

        public static int calcHP(double EV, double level, double baseHP){return (int)((int)(((2*baseHP+31+(EV/4))*level)/100)+level+10);}

        //bro just make a seperate class that has all the evs and ivs and thr base stats atp
        //THIS IS SHITTY UNOPTIMIZED PLACEHOLDER CODE TO GET THE SYSTEM WORKING. ALL THESE ARE PLACEHOLDERS!!
        private static double damageCalc(double attackerLevel, double attackingMonAttack, double targetDefenseStat, Move move){
            attackerLevel = ((2*attackerLevel)/5)+2;


            //rawDamage is the damage calc before any situational modifiers. more info here: https://bulbapedia.bulbagarden.net/wiki/Damage#Generation_V_onward
            double rawDamage = ((attackerLevel*move.baseDamage*(attackingMonAttack/targetDefenseStat))/50)+2;
            rawDamage*=other(new String[]{"Grass", "Ice"}, new String[]{"Fighting", "Fairy"},Pokedex.getMove("Close Combat"));
            System.out.println("raw damage: "+rawDamage);
            return rawDamage;
        }
        //get stat boost modifier
        private static double getBoostModifier(int boostCount){return (double)(boostCount+2)/2;}

    }

    //other factors
    private static double other(String[] oppTypes, String[] yourTypes, Move move){
        double total = 1;
        final Type[] oppTypesArray = {Type.getType(oppTypes[0]),Type.getType(oppTypes[1])};
        total*=Pokedex.Type.getMatchups(oppTypesArray,move.type);
        System.out.println("type "+Pokedex.Type.getMatchups(oppTypesArray,move.type));
        total*=STAB(yourTypes,move);
        System.out.println("STAB "+STAB(yourTypes,move));

        return total;
    }
    private static double STAB(String[] yourTypes, Move move){if(yourTypes[0].equals(move.type)||yourTypes[1].equals(move.type)){return 1.5;}else{return 1;}}
}
