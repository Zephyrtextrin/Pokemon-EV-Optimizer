public class Miscellaneous {
    protected static void printStats(int[] stats, String name){
        System.out.println("\n[POKEMON LOADED]: "+name.toUpperCase());
        System.out.printf("[HP]: %d\n[ATTACK]: %d\n[DEFENSE]: %d\n[SP. ATTACK]: %d\n[SP. DEFENSE]: %d\n[SPEED]: %d\n\n",stats[0],stats[1],stats[2],stats[3],stats[4],stats[5]);
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
            case "Exca"->"Excadrill";
            case "Iron Val"->"Iron Valiant";
            default -> input;
        };
    }

    //outputs all possible results to outspeed at certain points (reduces clutter in main method)
    public static void outputAllPossibleResults(int IV, int EV, String nature, int level, int targetStat, int targetBaseStat, String targetName){
        System.out.printf("\n--[SPEED VALUES NEEDED TO OUTSPEED %s NATURE %s]--\n",nature.toUpperCase(),targetName.toUpperCase());



        System.out.println("-[ONE BOOST]-");

        //+1 boost no evs neutral
        System.out.println("[NO EVS, NEUTRAL NATURE]: "+Calculators.findLeastStatBoosted(IV,0,1,level,targetStat,targetBaseStat,1));
        //+1 boost no evs positive
        System.out.println("[NO EVS, POSITIVE NATURE]: "+Calculators.findLeastStatBoosted(IV,0,1.1,level,targetStat,targetBaseStat,1));
        //+1 boost 252 evs neutral
        System.out.println("[252 EVS, NEUTRAL NATURE]: "+Calculators.findLeastStatBoosted(IV,252/4,1,level,targetStat,targetBaseStat,1));
        //+1 boost 252 evs positive
        System.out.println("[252 EVS, POSITIVE NATURE]: "+Calculators.findLeastStatBoosted(IV,252/4,1.1,level,targetStat,targetBaseStat,1));



        System.out.println("\n-TWO BOOSTS]-");

        //+2 boost no evs neutral
        System.out.println("[NO EVS, NEUTRAL NATURE]: "+Calculators.findLeastStatBoosted(IV,0,1,level,targetStat,targetBaseStat,2));
        //+2 boost no evs positive
        System.out.println("[NO EVS, POSITIVE NATURE]: "+Calculators.findLeastStatBoosted(IV,0,1.1,level,targetStat,targetBaseStat,2));
        //+2 boost 252 evs neutral
        System.out.println("[252 EVS, NEUTRAL NATURE]: "+Calculators.findLeastStatBoosted(IV,252/4,1,level,targetStat,targetBaseStat,2));
        //+2 boost 252 evs positive
        System.out.println("[252 EVS, POSITIVE NATURE]: "+Calculators.findLeastStatBoosted(IV,252/4,1.1,level,targetStat,targetBaseStat,2));
    }

    public final static class Calculators{
        //calculation for all stats (excluding HP)
        //more info: https://bulbapedia.bulbagarden.net/wiki/Stat#Generation_III_onward
        //must be cast to double to prevent floating point error but has to be cast back to int because all pokemon calculations *always* round down
        public static int statCalculation(int baseStat, int IV, int EV, double nature, int level){
            return (int)((((double)((2*baseStat+IV+EV)*level) /100)+5)*nature);
        }

        //finds what stat boost you need to be at to be faster than a given stat
        public static int findLeastStatBoosted(int IV, int EV, double nature, int level, int targetStat, int targetBaseStat,int boostCount){
            for(int currentStat = 0; currentStat <targetBaseStat; currentStat++){
                final int stat = (int)(statCalculation(currentStat,IV,EV,nature,level)*getBoostModifier(boostCount));
                if(stat>targetStat){return currentStat;}
            }
            return 0;
        }

        //get stat boost modifier
        private static double getBoostModifier(int boostCount){return (double)(boostCount+2)/2;}

    }
}
